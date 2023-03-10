package com.spring.ideafestivalbackend.domain.email.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "verify", timeToLive = 60 * 5)
public class EmailEntity {
    @Id
    private String email;

    @Length(max=6)
    private String randomValue;
    private Boolean verification;

    private Boolean authentication;
    @ColumnDefault("1")
    private Integer requestCount = 0;

    public void updateVerification(Boolean verification){
        this.verification = verification;
    }

    public void updateRandomValue(String uuid){ this.randomValue = uuid; }

    public void increaseRequestCount(){ this.requestCount += 1; }



}