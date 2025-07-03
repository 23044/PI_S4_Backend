package com.example.backend.jwtModule.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.Models.Users;
// import com.example.backend.Repositories.UsersRepository;
import com.example.backend.Repositories.UserRepository;


@Service
public class MyUserDetailsService implements UserDetailsService{

    private final UserRepository usersRepository;

    public MyUserDetailsService(UserRepository usersRepository){
        this.usersRepository = usersRepository ;

    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(email).
        orElseThrow(() -> new UsernameNotFoundException("email n`a pas ete trouver "));
        return org.springframework.security.core.userdetails.User
        .withUsername(user.getEmail())
        .password(user.getPassword())
        .roles(user.getRole().name())
        .build();
    }

    
}
