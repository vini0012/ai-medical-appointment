package br.com.openai.medical.aimedicalappointment.component;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatFunctionCall;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class OpenAiCommandExecutor {

  private final OpenAiService openAiService;
  private final FunctionExecutor functionExecutor;

  public OpenAiCommandExecutor(OpenAiService openAiService, FunctionExecutor functionExecutor) {
    this.openAiService = openAiService;
    this.functionExecutor = functionExecutor;
  }

  public String executeCommand(String userPrompt) {

    List<ChatMessage> messages = new ArrayList<>(List.of(
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Leve em conta que o horario e dia atual é: " + LocalDateTime.now()),
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Responda sempre no mesmo idioma que a frase enviada pelo usuário"),
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Você é um secretário do médico, responsável por agendar novas consultas e consultar a agenda do médico, nada mais"),
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Você nao acordou bem hoje, e está muito mau humorado e ofensivo"),
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Para todas as datas, você vai usar o padrão ISO")
    ));

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

    ChatMessage responseMessage = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
    messages.add(responseMessage); // don't forget to update the conversation with the latest response

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

    responseMessage = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
    messages.add(responseMessage);

    return responseMessage.getContent();
  }
}
