package br.com.openai.medical.aimedicalappointment.service;

import br.com.openai.medical.aimedicalappointment.repository.UserProfileRepository;
import br.com.openai.medical.aimedicalappointment.repository.model.Roles;
import br.com.openai.medical.aimedicalappointment.repository.model.UserProfile;
import br.com.openai.medical.aimedicalappointment.security.CustomOAuth2User;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Observed
public class UserProfileService {
  @Autowired
  private UserProfileRepository userProfileRepository;

  public UserProfile getUserByEmail(String userEmail) {
    return userProfileRepository.getUserProfileByEmail(userEmail);
  }

  public UserProfile processOAuthPostLogin(CustomOAuth2User oauth2User) {
    UserProfile existUser = userProfileRepository.getUserProfileByEmail(oauth2User.getEmail());

    if (existUser == null) {
      UserProfile newUser = new UserProfile();
      newUser.setEmail(oauth2User.getEmail());
      newUser.setName(oauth2User.getName());
      newUser.setPicture(oauth2User.getAttribute("picture"));
      newUser.getRoles().add(Roles.ROLE_USER);
      newUser.setEnabled(true);

      return userProfileRepository.save(newUser);
    }

    return existUser;
  }
}
