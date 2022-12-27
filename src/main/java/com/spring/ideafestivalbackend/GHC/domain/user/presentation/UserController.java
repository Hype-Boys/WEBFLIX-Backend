package com.spring.ideafestivalbackend.GHC.domain.user.presentation;
import com.spring.ideafestivalbackend.GHC.domain.user.presentation.dto.UserDto;
import com.spring.ideafestivalbackend.GHC.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<Void> signup(@RequestBody UserDto userDto){
        userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}