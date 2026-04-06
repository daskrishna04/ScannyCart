package com.krishna.scan.service;

import com.krishna.scan.entity.User;
import com.krishna.scan.exception.CartNotFoundException;
import com.krishna.scan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("user not found"));
    }

    public User findById(int id){
        return userRepository.findById(id).orElseThrow(()-> new CartNotFoundException("cart not found"));
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
}
