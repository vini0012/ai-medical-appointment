package br.com.openai.medical.aimedicalappointment.repository.model;

import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserProfile {
  @Id
  private UUID id = UUID.randomUUID();

  private String email;

  private boolean enabled;

  private String name;

  private String picture;

  private Set<Roles> roles = new HashSet<>();

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Roles> getRoles() {
    return roles;
  }

  public void setRoles(Set<Roles> roles) {
    this.roles = roles;
  }
}
