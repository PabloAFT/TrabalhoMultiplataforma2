package com.trabalho.multi.trabalhomulti.connection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;

import java.io.IOException;
import java.util.Collection;

// @Configuration
public class JacksonConfiguration {


    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("configuring object mapper");

        SimpleModule module = new SimpleModule("SimpleGrantedAuthorityModule");
        module.addSerializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthoritySerializer());
        module.addDeserializer(SimpleGrantedAuthority.class, new SimpleGrantedAuthorityDeserializer());
        module.addDeserializer(Collection.class, new com.trabalho.multi.trabalhomulti.connection.SimpleGrantedAuthorityDeserializer() );
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        // objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
        // objectMapper.addMixIn(getClass(), getClass())
        objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
        objectMapper.addMixInAnnotations(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);

        objectMapper.registerModule(module);
        System.out.println(objectMapper.findMixInClassFor(SimpleGrantedAuthority.class));
        
    

        return objectMapper;
    }

    public static class SimpleGrantedAuthoritySerializer extends JsonSerializer<SimpleGrantedAuthority> {
        @Override
        public void serialize(SimpleGrantedAuthority value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
            gen.writeString(value.getAuthority());
        }
    }

    public static class SimpleGrantedAuthorityDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {
        @Override
        public SimpleGrantedAuthority deserialize(JsonParser p, com.fasterxml.jackson.databind.DeserializationContext ctxt) throws IOException {
            System.out.println("Deserializing");
            return new SimpleGrantedAuthority(p.getValueAsString());
        }
    }
}
