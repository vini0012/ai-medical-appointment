package br.com.openai.medical.aimedicalappointment.security;

import br.com.openai.medical.aimedicalappointment.service.UserProfileService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final UserProfileService userProfileService;

  public OAuth2SuccessHandler(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
    CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
