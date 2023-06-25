package br.com.openai.medical.aimedicalappointment.api;

import br.com.openai.medical.aimedicalappointment.component.OpenAiCommandExecutor;
import br.com.openai.medical.aimedicalappointment.security.CustomOAuth2User;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class AssistantApi {

  private final OpenAiCommandExecutor executeCommand;

  public AssistantApi(OpenAiCommandExecutor executeCommand) {
    this.executeCommand = executeCommand;
  }

  @PostMapping
  @RolesAllowed("USER")
  String chatRequest(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody String userPrompt) {
    return executeCommand.executeCommand(userPrompt);
  }

  @PostMapping("/config")
  @RolesAllowed("ADMIN")
  String configRequest(@AuthenticationPrincipal CustomOAuth2User user) {
    return "config";
  }
}
