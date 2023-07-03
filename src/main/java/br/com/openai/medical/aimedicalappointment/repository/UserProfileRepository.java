package br.com.openai.medical.aimedicalappointment.repository;

import br.com.openai.medical.aimedicalappointment.repository.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserProfileRepository extends MongoRepository<UserProfile, UUID> {
  public UserProfile getUserProfileByEmail(String userName);
}
