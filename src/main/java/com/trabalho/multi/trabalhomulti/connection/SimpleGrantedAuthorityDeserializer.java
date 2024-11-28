package com.trabalho.multi.trabalhomulti.connection;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class SimpleGrantedAuthorityDeserializer extends JsonDeserializer<Collection<? extends GrantedAuthority>> {

    public SimpleGrantedAuthorityDeserializer() {
    }

    @Override
    public Collection<? extends GrantedAuthority> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        while (p.nextToken() != null) {
            String authority = p.getText();
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        return authorities;
    }

}
