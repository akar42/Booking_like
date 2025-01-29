package com.example.hotelApp.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequest {
	private String login;
	private String password;
	private String name;
}
