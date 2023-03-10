package com.spring.ideafestivalbackend.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refreshToken")
public class RefreshToken {
    @Id
    private String email;

    @Indexed
    private String token;

    @TimeToLive
    private Long expirdAt;

    public void exchangeRefreshToken(String token){
        this.token = token;
    }
}
