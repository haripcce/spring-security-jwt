package com.auth0.samples.authapi.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.auth0.samples.authapi.security.SecurityConstants.*;

@RestController
@RequestMapping("/users")
public class UserController {

	private ApplicationUserRepository applicationUserRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserController(ApplicationUserRepository applicationUserRepository,
						  BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.applicationUserRepository = applicationUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PostMapping(value = "/sign-up",produces = "application/json")
	public ResponseEntity<?> signUp(@RequestBody ApplicationUser user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		ApplicationUser createdUser = null;
		try {
			createdUser = applicationUserRepository.save(user);
			String token = Jwts.builder()
					.setSubject(user.getEmail())
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
					.compact();

			String responseContent = new String("{\"user\":{\"email\":\"" + user.getEmail() + "\"" +
					",\"token\":\"" + token + "\"}" +
					"}");
			return new ResponseEntity<String>(responseContent, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{\"errors\":{\"email\" : \"This email is already taken\"}}", HttpStatus.BAD_REQUEST);
		}

	}
}
