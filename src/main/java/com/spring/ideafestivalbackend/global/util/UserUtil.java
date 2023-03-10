package com.spring.ideafestivalbackend.global.util;

import com.spring.ideafestivalbackend.domain.user.entity.User;
import com.spring.ideafestivalbackend.domain.user.exception.UserNotFoundException;
import com.spring.ideafestivalbackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserUtil {
    private final UserRepository userRepository;

    public User currentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByEmail(email).orElseThrow(()->new UserNotFoundException("유저가 없습니다."));
    }
}
