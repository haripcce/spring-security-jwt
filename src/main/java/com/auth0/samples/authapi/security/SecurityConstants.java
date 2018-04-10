package com.auth0.samples.authapi.security;

public class SecurityConstants {
	public static final String SECRET = "SecretKeyToGenJWTs";
	public static final long EXPIRATION_TIME = 864_000_000; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
	public static final String CONFIRMATION_URL = "/users/confirm";
	public static final String REST_PASSWORD_URL = "/users/reset_password_request";
	public static final String VALIDATE_TOKEN_URL = "/users/validate_token";
	public static final String RESETPASS_UPDATE_URL = "/users/reset_password";
}
