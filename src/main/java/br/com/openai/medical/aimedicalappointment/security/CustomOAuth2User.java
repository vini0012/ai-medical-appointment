package br.com.openai.medical.aimedicalappointment.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomOAuth2User implements OAuth2User {
  private final OAuth2User oauth2User;
  private final Set<GrantedAuthority> authoritySet;

  public CustomOAuth2User(OAuth2User oauth2User) {
    this.oauth2User = oauth2User;
    this.authoritySet = new HashSet<>(oauth2User.getAuthorities());
  }

  @Override
  public Map<String, Object> getAttributes() {
    return oauth2User.getAttributes();
  }

  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return this.authoritySet;
  }

  @Override
  public String getName() {
    return oauth2User.getAttribute("name");
  }

  public String getEmail() {
    return oauth2User.<String>getAttribute("email");
  }
}
