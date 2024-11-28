package com.trabalho.multi.trabalhomulti.connection;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public interface CustomUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsernameAndProviderId(String username, UUID providerId) throws UsernameNotFoundException;
}