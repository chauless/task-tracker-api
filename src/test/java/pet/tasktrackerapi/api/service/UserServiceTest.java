package pet.tasktrackerapi.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pet.tasktrackerapi.api.dto.UserDto;
import pet.tasktrackerapi.api.model.User;
import pet.tasktrackerapi.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    private UserService userService;


    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        userService = new UserService(userRepository, modelMapper);
    }

    @Test
    void testGetUserInfo() {

        Long testId = 1L;
        String testName = "test";

        User user = User.builder()
                .id(testId)
                .username(testName)
                .build();

        UserDto userDto = new UserDto();
        userDto.setId(testId);
        userDto.setUsername(testName);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        UserDto result = userService.getUserInfo(user);

        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void testGetUserInfoWithInvalidUser() {

        Long testId = 1L;
        String testName = "test";

        User user = User.builder()
                .id(testId)
                .username(testName)
                .build();

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserInfo(user));
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void testGetUserEntity() {
        //given
        Long testId = 1L;
        User testUser = new User();

        //when
        when(userRepository.findById(testId)).thenReturn(Optional.of(testUser));
        userService.getUserEntity(testId);

        //then
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).findById(argumentCaptor.capture());

        Long actual = argumentCaptor.getValue();
        assertThat(actual).isEqualTo(testId);

    }
}