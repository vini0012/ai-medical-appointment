package br.com.openai.medical.aimedicalappointment.config;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SessionScopeConfig {

  @Bean
  @SessionScope
  public List<ChatMessage> chatHistory() {
    return new ArrayList<>(List.of(
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Leve em conta que o horario e dia atual é: " + LocalDateTime.now()),
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Responda sempre no mesmo idioma que a frase enviada pelo usuário"),
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Você é um secretário do médico, responsável por agendar novas consultas e consultar a agenda do médico"),
      new ChatMessage(ChatMessageRole.SYSTEM.value(), "Para todas as datas, você vai usar o padrão ISO")
    ));
  }

}
