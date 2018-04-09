package com.auth0.samples.authapi.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
			return new ResponseEntity<ApplicationUser>(createdUser, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("{\"errors\":{\"email\" : \"This email is already taken\"}}", HttpStatus.BAD_REQUEST);
		}

	}
}
