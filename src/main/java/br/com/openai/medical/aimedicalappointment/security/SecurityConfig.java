package br.com.openai.medical.aimedicalappointment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(
  securedEnabled = true,
  jsr250Enabled = true)
public class SecurityConfig {
  private final CustomOAuth2UserService oAuth2UserService;
  private final OAuth2SuccessHandler successHandler;

  public SecurityConfig(CustomOAuth2UserService oAuth2UserService, OAuth2SuccessHandler successHandler) {
    this.oAuth2UserService = oAuth2UserService;
    this.successHandler = successHandler;
  }

  private static AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configureDefaultRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry defaultRequests) {
    return defaultRequests
      .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
      .anyRequest().authenticated();
  }

  @Bean
  SecurityFilterChain configure(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(SecurityConfig::configureDefaultRequests)
      .oauth2Login(this::configureOauth2)
      .build();
  }

  private OAuth2LoginConfigurer<HttpSecurity> configureOauth2(OAuth2LoginConfigurer<HttpSecurity> oauth2login) {
    return oauth2login
      .userInfoEndpoint(userInfoEndpoint ->
        userInfoEndpoint
          .userService(oAuth2UserService)
      ).successHandler(successHandler);
  }
}
