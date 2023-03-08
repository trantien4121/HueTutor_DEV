package com.trantien.huetutor.payloads;

public class LoginResponse {
    public String accessToken;
    private String tokenType = "Bearer";

    public LoginResponse(String accessToken){
        this.accessToken = accessToken;
    }
}
