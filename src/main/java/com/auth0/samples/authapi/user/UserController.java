package com.auth0.samples.authapi.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			String token = Jwts.builder()
					.setSubject(user.getEmail())
					.claim("confirmed", false)
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
					.compact();
			createdUser = applicationUserRepository.save(user);


			String responseContent = new String("{\"user\":" +
					"{" +
					"\"email\":\"" + user.getEmail() + "\"" +
					",\"token\":\"" + token + "\"" +
					 "}" +
					"}");
			return new ResponseEntity<String>(responseContent, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{\"errors\":{\"email\" : \"This email is already taken\"}}", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping(value = "/confirm", produces = "application/json")
	public ResponseEntity<?> confirm(@RequestBody ConfirmationToken token) {

		ApplicationUser createdUser = null;

		if (token.getToken() != null) {
			// parse the token.
			String user = Jwts.parser()
					.setSigningKey(SECRET.getBytes())
					.parseClaimsJws(token.getToken())
					.getBody()
					.getSubject();
			createdUser = applicationUserRepository.findByEmail(user);
			createdUser.setConfirmed(true);
			createdUser = applicationUserRepository.save(createdUser);

			String newToken = Jwts.builder()
					.setSubject(createdUser.getEmail())
					.claim("confirmed", createdUser.isConfirmed())
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
					.compact();

			String responseContent = new String("{\"user\":{\"email\":\"" + user + "\"" +
					",\"token\":\"" + newToken + "\"" +
					"}" +
					"}");
			return new ResponseEntity<String>(responseContent, HttpStatus.CREATED);

		}
		return new ResponseEntity<String>("{\"errors\":{\"token\" : \"Token validation failed\"}}", HttpStatus.BAD_REQUEST);

	}

	@PostMapping(value = "/reset_password_request", produces = "application/json")
	public ResponseEntity<?> resetPasswordRequest(@RequestBody ApplicationUser user) {
		ApplicationUser createdUser = null;
		createdUser = applicationUserRepository.findByEmail(user.getEmail());
		if (createdUser != null) {
			return new ResponseEntity<String>("{}", HttpStatus.OK);
		}
		return new ResponseEntity<String>("{\"errors\":{\"global\" : \"User does not exist\"}}", HttpStatus.BAD_REQUEST);

	}

	@PostMapping(value = "/validate_token", produces = "application/json")
	public ResponseEntity<?> validateToken(@RequestBody ConfirmationToken token) {

		ApplicationUser createdUser = null;

		if (token.getToken() != null) {
			// parse the token.
			String user = Jwts.parser()
					.setSigningKey(SECRET.getBytes())
					.parseClaimsJws(token.getToken())
					.getBody()
					.getSubject();
			createdUser = applicationUserRepository.findByEmail(user);
			if (createdUser != null) {
				String responseContent = new String("{}");
				return new ResponseEntity<String>(responseContent, HttpStatus.OK);
			}


		}
		return new ResponseEntity<String>("{\"errors\":{\"global\" : \"Token validation failed\"}}", HttpStatus.UNAUTHORIZED);

	}

	@PostMapping(value = "/reset_password", produces = "application/json")
	public ResponseEntity<?> resetPassword(@RequestBody ConfirmationToken token) {

		ApplicationUser createdUser = null;

		if (token.getToken() != null) {
			// parse the token.
			String user = Jwts.parser()
					.setSigningKey(SECRET.getBytes())
					.parseClaimsJws(token.getToken())
					.getBody()
					.getSubject();
			createdUser = applicationUserRepository.findByEmail(user);

			if (createdUser != null) {
				createdUser.setPassword(bCryptPasswordEncoder.encode(token.getPassword()));
				applicationUserRepository.save(createdUser);

				String responseContent = new String("{}");
				return new ResponseEntity<String>(responseContent, HttpStatus.OK);
			}


		}
		return new ResponseEntity<String>("{\"errors\":{\"global\" : \"Invalid token..\"}}", HttpStatus.UNAUTHORIZED);

	}
}
