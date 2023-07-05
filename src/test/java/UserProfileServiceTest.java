import br.com.openai.medical.aimedicalappointment.repository.UserProfileRepository;
import br.com.openai.medical.aimedicalappointment.repository.model.UserProfile;
import br.com.openai.medical.aimedicalappointment.security.CustomOAuth2User;
import br.com.openai.medical.aimedicalappointment.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByEmail() {
        String userEmail = "test@example.com";
        UserProfile userProfile = new UserProfile();
        when(userProfileRepository.getUserProfileByEmail(userEmail)).thenReturn(userProfile);

        UserProfile result = userProfileService.getUserByEmail(userEmail);

        assertEquals(userProfile, result);
        verify(userProfileRepository, times(1)).getUserProfileByEmail(userEmail);
    }

    @Test
    void processOAuthPostLogin() {

        OAuth2User oauth2UserMock = mock(OAuth2User.class);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oauth2UserMock);

        UserProfile userProfile = createUserProfile(oauth2UserMock);
        when(userProfileRepository.getUserProfileByEmail(any())).thenReturn(userProfile);

        UserProfile userProfileResult = userProfileService.processOAuthPostLogin(customOAuth2User);
        assertEquals(userProfileResult, userProfile);
    }

    @Test
    void processOAuthPostLoginNotFoundUser() {

        OAuth2User oauth2UserMock = mock(OAuth2User.class);
        when(oauth2UserMock.getAttributes()).thenReturn(new HashMap<>());
        when(oauth2UserMock.getAuthorities()).thenReturn(Collections.emptySet());
        when(oauth2UserMock.getAttribute("name")).thenReturn("Test User");
        when(oauth2UserMock.<String>getAttribute("email")).thenReturn("test@example.com");

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oauth2UserMock);
        UserProfile userProfile = createUserProfile(oauth2UserMock);

        when(userProfileRepository.getUserProfileByEmail(any())).thenReturn(null);
        when(userProfileRepository.save(any())).thenReturn(userProfile);

        UserProfile userProfileResult = userProfileService.processOAuthPostLogin(customOAuth2User);
        assertNotNull(userProfileResult);
        assertEquals(userProfileResult.getEmail(),"test@example.com");
        assertEquals(userProfileResult.getName(), "Test User");
    }

    private UserProfile createUserProfile(OAuth2User oAuth2User) {

        UserProfile userProfile = new UserProfile();
        userProfile.setId(UUID.randomUUID());
        userProfile.setEmail("test@example.com");
        userProfile.setName("Test User");
        userProfile.setPicture(oAuth2User.getAttribute("picture"));
        return userProfile;
    }
}