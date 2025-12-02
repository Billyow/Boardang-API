package com.billyow.app.boardang.user.controller;
import com.billyow.app.boardang.user.DTO.RegisterRequest;
import com.billyow.app.boardang.user.DTO.UserDTO;
import com.billyow.app.boardang.user.mapper.UserMapper;
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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("the User: "+registerRequest.getName()+" registered successfully");
    }

}
