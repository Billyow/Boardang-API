package com.billyow.app.boardang.user.controller;
import com.billyow.app.boardang.user.DTO.UserDTO;
import com.billyow.app.boardang.user.mapper.UserMapper;
import com.billyow.app.boardang.user.model.User;
import com.billyow.app.boardang.user.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {
    private final IUserService userService;
    private final UserMapper userMapper;
    public UserController(IUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    @GetMapping("/get/{id}")
    public UserDTO getUserById(@PathVariable long id) {
        return userMapper.toUserDTOResponse(userService.findById(id).orElse(null));
    }
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser() {
        var user = new User();
        user.setName("jorgito2");
        user.setEmail("jorgito2@gmail.com");
        user.setPassword("123456");
        return ResponseEntity.ok(userMapper.toUserDTOResponse(userService.save(user)));
    }
}
