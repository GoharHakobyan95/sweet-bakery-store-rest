package am.itspace.sweetbakerystorerest.service;

import am.itspace.sweetbakerystorecommon.entity.Address;
import am.itspace.sweetbakerystorecommon.entity.City;
import am.itspace.sweetbakerystorecommon.entity.Role;
import am.itspace.sweetbakerystorecommon.entity.User;
import am.itspace.sweetbakerystorecommon.repository.UserRepository;
import am.itspace.sweetbakerystorecommon.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1)
                .name("John")
                .surname("Smith")
                .role(Role.USER)
                .createAt(new Date())
                .isActive(true)
                .email("John@mail.ru")
                .password("john888")
                .phone("77777777")
                .verifyToken("token")
                .address(new Address(5, "165 apt.", new City(1, "Yerevan")))
                .build();
    }

    @Test
    void shouldDeleteById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        assertNotNull(userService.findById(1));
        assertEquals(user.getId(), 1);
        userService.deleteById(1);
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(any())).thenReturn(user);
        userService.save(user);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Optional<User> userById = userService.findById(1);
        assertTrue(userById.isPresent());
        assertEquals("John", userById.get().getName());
        assertEquals(1, userById.get().getId());
        verify(userRepository, times(1)).save(any());
    }


    @Test
    void shouldFindByIdAndRole() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        userService.findByIdAndRole(1, Role.USER);
        assertNotNull(user.getRole());
        assertEquals(user.getRole(), Role.USER);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getEmailByAdminRole() {
        when(userRepository.findByRole(any())).thenReturn(Optional.of(user));
        //save user with Admin Role
        User admin = User.builder()
                .id(2)
                .name("John")
                .surname("Smith")
                .role(Role.ADMIN)
                .createAt(new Date())
                .isActive(true)
                .email("admin@mail.ru")
                .password("Admin555$")
                .phone("88888888")
                .verifyToken("%token555$")
                .address(new Address(2, "5 building", new City(1, "Yerevan")))
                .build();
        userService.save(admin);
        when(userRepository.findByRole(any())).thenReturn(Optional.of(admin));
        Optional<User> byIdAndRole = userService.getAdminEmail();
        assertEquals(byIdAndRole.get().getRole(), Role.ADMIN);
        assertEquals(byIdAndRole.get().getId(), 2);
    }

    @Test
    void getUserByEmail() {
        when(userRepository.getByEmail(anyString())).thenReturn(Optional.of(user));
        User byEmail = userService.getByEmail("John@mail.ru");
        assertNotNull(byEmail);
        assertEquals("John", byEmail.getName());
    }
}