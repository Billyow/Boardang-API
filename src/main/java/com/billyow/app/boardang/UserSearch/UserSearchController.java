package com.billyow.app.boardang.UserSearch;
import com.billyow.app.boardang.user.DTO.UserDTO;
import com.billyow.app.boardang.user.mapper.UserMapperImpl;
import com.billyow.app.boardang.user.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserSearchController {
    private UserServiceImpl userService;
    private UserMapperImpl userMapper;
    @GetMapping("/{email}")
    public UserDTO searchForUser(@PathVariable String email){
        return userMapper.toUserDTOResponse(userService.findByEmail(email));
    }

    @GetMapping("/cache/{email}")
    public UserDTO searchForUserCached(@PathVariable String email){
        return userMapper.toUserDTOResponse(userService.CfindByEmail(email));
    }
}
