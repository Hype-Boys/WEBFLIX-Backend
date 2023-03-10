package com.spring.ideafestivalbackend.domain.user.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotBlank(message = "이메일[필수]")
    private String email;

    @NotBlank(message = "비밀번호[필수]")
    private String password;

}
