package br.com.openai.medical.aimedicalappointment.component;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatFunctionCall;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class OpenAiCommandExecutor {
  @Resource(name = "chatHistory")
  List<ChatMessage> messages;
  private final OpenAiService openAiService;
  private final FunctionExecutor functionExecutor;

  private final RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
    .handle(SocketTimeoutException.class)
    .onRetry(event -> {
      System.out.println("Timeout Occurred, retrying...");
    })
    .withMaxRetries(3)
    .build();

  public OpenAiCommandExecutor(OpenAiService openAiService, FunctionExecutor functionExecutor) {
    this.openAiService = openAiService;
    this.functionExecutor = functionExecutor;
  }

  public String executeCommand(String userPrompt) {
    ChatMessage firstMsg = new ChatMessage(ChatMessageRole.USER.value(), userPrompt);
    messages.add(firstMsg);

    ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
      .builder()
      .model("gpt-3.5-turbo-0613")
      .messages(messages)
      .functions(functionExecutor.getFunctions())
      .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
      .n(1)
      .maxTokens(100)
      .logitBias(new HashMap<>())
      .build();

    ChatMessage responseMessage = getResponseMessage(chatCompletionRequest);
    messages.add(responseMessage);

    ChatFunctionCall functionCall = responseMessage.getFunctionCall();
    if (functionCall != null) {
      System.out.println("Trying to execute " + functionCall.getName() + "...");
      Optional<ChatMessage> message = functionExecutor.executeAndConvertToMessageSafely(functionCall);
      if (message.isPresent()) {
        System.out.println("Executed " + functionCall.getName() + ".");
        messages.add(message.get());
      } else {
        System.out.println("Something went wrong with the execution of " + functionCall.getName() + "...");
      }
    }

    responseMessage = getResponseMessage(chatCompletionRequest);
    messages.add(responseMessage);

    return responseMessage.getContent();
  }

  private ChatMessage getResponseMessage(ChatCompletionRequest chatCompletionRequest) {
    return Failsafe
      .with(retryPolicy)
      .get(() -> openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage());
  }
}
