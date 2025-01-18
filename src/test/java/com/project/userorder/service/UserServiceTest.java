package com.project.userorder.service;

import com.project.userorder.entity.User;
import com.project.userorder.exception.ResourceNotFoundException;
import com.project.userorder.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        User user = new User(1L, "Lmn", "lmn@gmail.com", "9123456785", "Address");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(
            new User(1L, "Lmn", "lmn@gmail.com", "9123456785", "Address"),
            new User(2L, "Lmnl", "lmnl@gmail.com", "9123456782", "Address2")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserById_UserFound() {
        User user = new User(1L, "Lmn", "lmn@gmail.com", "9123456785", "Address");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }

    @Test 
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        assertEquals("User does not exist", thrown.getMessage());
    }

    @Test
    void testDeleteUser_UserFound() {
        User user = new User(1L, "Lmn", "lmn@gmail.com", "9123456785", "Address");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }
}
