package com.spring.ideafestivalbackend.domain.user.exception;

import com.spring.ideafestivalbackend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;
    //
    public UserNotFoundException(String message){
        super(message);
        this.errorCode = ErrorCode.USER_NOT_FOUD;
    }
}
