package com.krishna.scan.service;

import com.krishna.scan.dto.AuthResponse;
import com.krishna.scan.dto.LoginRequest;
import com.krishna.scan.dto.RegisterRequest;
import com.krishna.scan.entity.Role;
import com.krishna.scan.entity.User;
import com.krishna.scan.repository.UserRepository;
import com.krishna.scan.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User Already Present");
        }

        User user = User.builder()
                        .name(request.getName())
                                .email(request.getEmail())
                                        .password(passwordEncoder.encode(request.getPassword()))
                                                .role(Role.ROLE_CUSTOMER)
                                                        .build();
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        String email = authenticate.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token,user.getEmail());
    }
}
