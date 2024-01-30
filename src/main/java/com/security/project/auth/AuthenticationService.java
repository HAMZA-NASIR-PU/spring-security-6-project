package com.security.project.auth;

import com.security.project.config.JwtService;
import com.security.project.generics.GenericResponse;
import com.security.project.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.project.user.User;
import com.security.project.user.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	public GenericResponse<AuthenticationResponse> register(RegisterRequest request) {
		GenericResponse<AuthenticationResponse> response = new GenericResponse<>();
		Optional<User> existingUser = repository.findByEmail(request.getEmail());
		//if email already exist.
		if(existingUser.isPresent()){
			response.setMessage("Email already exist...");
			response.setData(null);
			return response;
		}
		var user = User.builder()
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.build();
		repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setToken(jwtToken);
		response.setMessage("User registered successfully...");
		response.setData(authenticationResponse);
		return response;
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		//Now user is authenticated otherwise an exception will thrown
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

}
