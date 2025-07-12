package com.neu.project.controller;

import com.neu.project.entity.User;
import com.neu.project.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        // 准备测试数据
        String username = "testUser";
        String password = "testPass";

        // 模拟UserMapper行为
        when(userMapper.findPwByName(username)).thenReturn(password);
        when(userMapper.findIdByName(username)).thenReturn(1);

        // 调用方法
        int result = userController.login(username, password);

        // 验证结果
        assertEquals(1, result);
        verify(userMapper).findPwByName(username);
        verify(userMapper).findIdByName(username);
    }

    @Test
    void login_Failure_WrongPassword() {
        String username = "testUser";
        String password = "wrongPass";

        when(userMapper.findPwByName(username)).thenReturn("correctPass");

        int result = userController.login(username, password);

        assertEquals(0, result);
    }

    @Test
    void login_Failure_EmptyInput() {
        assertEquals(0, userController.login("", ""));
        assertEquals(0, userController.login("user", ""));
        assertEquals(0, userController.login("", "pass"));
    }

    @Test
    void getuser_Success() {
        int userId = 1;
        User expectedUser = new User(userId, "pass", "user");

        when(userMapper.findUserById(userId)).thenReturn(expectedUser);

        User result = userController.getuser(userId);

        assertEquals(expectedUser, result);
        verify(userMapper).findUserById(userId);
    }

    @Test
    void upuser_Success() {
        User user = new User(1, "newPass", "newUser");

        when(userMapper.findNameExist(user.getId(), user.getUsername())).thenReturn(0);

        int result = userController.upuser(user);

        assertEquals(1, result);
        verify(userMapper).findNameExist(user.getId(), user.getUsername());
        verify(userMapper).updateUser(user);
    }

    @Test
    void upuser_Failure_UsernameExists() {
        User user = new User(1, "pass", "existingUser");

        when(userMapper.findNameExist(user.getId(), user.getUsername())).thenReturn(1);

        int result = userController.upuser(user);

        assertEquals(0, result);
        verify(userMapper).findNameExist(user.getId(), user.getUsername());
        verify(userMapper, never()).updateUser(user);
    }

    @Test
    void adUser_Success() {
        User newUser = new User(0, "pass", "newUser");

        when(userMapper.findNameExist(newUser.getId(), newUser.getUsername())).thenReturn(0);

        int result = userController.adUser(newUser);

        assertEquals(1, result);
        verify(userMapper).findNameExist(newUser.getId(), newUser.getUsername());
        verify(userMapper).insertUser(newUser);
    }

    @Test
    void adUser_Failure_UsernameExists() {
        User existingUser = new User(0, "pass", "existingUser");

        when(userMapper.findNameExist(existingUser.getId(), existingUser.getUsername())).thenReturn(1);

        int result = userController.adUser(existingUser);

        assertEquals(0, result);
        verify(userMapper).findNameExist(existingUser.getId(), existingUser.getUsername());
        verify(userMapper, never()).insertUser(existingUser);
    }

    @Test
    void getAllUser_Success() {
        List<User> expectedUsers = Arrays.asList(
                new User(1, "pass1", "user1"),
                new User(2, "pass2", "user2")
        );

        when(userMapper.getAllUser()).thenReturn(expectedUsers);

        List<User> result = userController.getAllUser();

        assertEquals(expectedUsers, result);
        verify(userMapper).getAllUser();
    }

    @Test
    void updateUserWithoutCheck_Success() {
        User user = new User(1, "newPass", "newUser");

        int result = userController.updateUserWithoutCheck(user);

        assertEquals(1, result);
        verify(userMapper).updateUserWithoutCheck(user);
    }

    @Test
    void updateUserWithoutCheck_Failure() {
        User user = new User(1, "newPass", "newUser");

        doThrow(new RuntimeException()).when(userMapper).updateUserWithoutCheck(user);

        int result = userController.updateUserWithoutCheck(user);

        assertEquals(0, result);
    }

    @Test
    void checkAdmin_Success() {
        assertTrue(userController.checkAdmin("root", "123456"));
    }

    @Test
    void checkAdmin_Failure_WrongCredentials() {
        assertFalse(userController.checkAdmin("wrong", "wrong"));
        assertFalse(userController.checkAdmin("root", "wrong"));
        assertFalse(userController.checkAdmin("wrong", "123456"));
    }
}