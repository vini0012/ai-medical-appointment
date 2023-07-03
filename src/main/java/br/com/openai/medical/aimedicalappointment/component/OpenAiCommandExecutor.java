package br.com.openai.medical.aimedicalappointment.component;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatFunctionCall;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Observed
public class OpenAiCommandExecutor {

  private static final Logger LOG = LoggerFactory.getLogger(OpenAiCommandExecutor.class);
  private static final String CIRCUIT_BREAKER_INSTANCE_NAME = "aiMedicalAppointment";
  private static final String FALLBACK_METHOD = "fallback";
  @Resource(name = "chatHistory")
  List<ChatMessage> messages;
  private final OpenAiService openAiService;
  private final FunctionExecutor functionExecutor;

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
      LOG.info("Trying to execute {}...", functionCall.getName());
      Optional<ChatMessage> message = functionExecutor.executeAndConvertToMessageSafely(functionCall);
      if (message.isPresent()) {
        LOG.info("Executed {}.", functionCall.getName());
        messages.add(message.get());
      } else {
        LOG.info("Something went wrong with the execution of {}...", functionCall.getName());
      }
    }

    responseMessage = getResponseMessage(chatCompletionRequest);
    messages.add(responseMessage);

    return responseMessage.getContent();
  }

  @CircuitBreaker(name = CIRCUIT_BREAKER_INSTANCE_NAME, fallbackMethod = FALLBACK_METHOD)
  private ChatMessage getResponseMessage(ChatCompletionRequest chatCompletionRequest) {
    return openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
  }

  protected ChatMessage fallback(ChatCompletionRequest chatCompletionRequest, Throwable t) {
    LOG.error("Inside circuit breaker fallback, cause", t);
    return new ChatMessage("System", "Erro ao chamar API - Circuit Breaker Open");
  }
}
