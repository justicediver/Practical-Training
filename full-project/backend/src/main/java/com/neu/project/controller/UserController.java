package com.neu.project.controller;

import com.neu.project.entity.User;
import com.neu.project.handler.OnlineUserHandler;
import com.neu.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserMapper userMapper;

    @RequestMapping("/login")
    public int login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return 0;
        }
        if (password.equals(userMapper.findPwByName(username))) {
            return userMapper.findIdByName(username);
        }
        return 0;
    }

    @RequestMapping("/userInfo")
    public User getuser(int id) {
        System.out.println("收到请求" + id);
        return userMapper.findUserById(id);
    }

    @PostMapping("/upUser")
    public int upuser(@RequestBody User user) {
        if (userMapper.findNameExist(user.getId(), user.getUsername()) == 1) {
            return 0;
        }

        userMapper.updateUser(user);
        return 1;
    }

    @PostMapping("/register")
    public int adUser(@RequestBody User user) {
        if (userMapper.findNameExist(user.getId(), user.getUsername()) == 1) {
            return 0;
        }
        userMapper.insertUser(user);
        return 1;
    }

    @GetMapping("/allUser")
    public List<User> getAllUser() {
        System.out.println("get"+userMapper.getAllUser());
        return userMapper.getAllUser();
    }

    @PostMapping("/updateUserWithoutCheck")
    public int updateUserWithoutCheck(@RequestBody User user) {
        try {
            userMapper.updateUserWithoutCheck(user);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @RequestMapping("/checkAdmin")
    public boolean checkAdmin(@RequestParam String username,
                              @RequestParam String password) {
        return "root".equals(username) && "123456".equals(password);
    }
    @GetMapping("/online-users")
    public List<Map<String, Object>> getOnlineUsers() {
        List<Map<String, Object>> onlineUserList = new ArrayList<>();
        Map<Integer, WebSocketSession> onlineUsers = OnlineUserHandler.getOnlineUsers();

        for (Integer userId : onlineUsers.keySet()) {
            User user = userMapper.findUserById(userId);
            if (user != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                onlineUserList.add(userInfo);
            }
        }

        return onlineUserList;
    }
}