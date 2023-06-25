package br.com.openai.medical.aimedicalappointment.repository;

import br.com.openai.medical.aimedicalappointment.repository.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfile, UUID> {
  public UserProfile getUserProfileByEmail(String userName);
}
