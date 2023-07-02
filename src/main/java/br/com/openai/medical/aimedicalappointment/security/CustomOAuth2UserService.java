package br.com.openai.medical.aimedicalappointment.security;

import br.com.openai.medical.aimedicalappointment.repository.model.UserProfile;
import br.com.openai.medical.aimedicalappointment.service.UserProfileService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserProfileService userProfileService;

  public CustomOAuth2UserService(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    CustomOAuth2User oAuthUser = new CustomOAuth2User(super.loadUser(userRequest));

    UserProfile profile = userProfileService.processOAuthPostLogin(oAuthUser);
    if (nonNull(profile)) {
      profile.getRoles().forEach(role -> oAuthUser.getAuthorities().add(new SimpleGrantedAuthority(role.name())));
    }
    return oAuthUser;
  }
}
