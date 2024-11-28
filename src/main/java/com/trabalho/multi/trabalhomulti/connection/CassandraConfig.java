package com.trabalho.multi.trabalhomulti.connection;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
public class CassandraConfig {

    // @Bean
    // public DriverConfigLoaderBuilderCustomizer defaultProfile() {
    // return loaderbuilder -> loaderbuilder

    // .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT,
    // Duration.ofSeconds(1)).build();

    // }

    @WritingConverter
    public class SimpleGrantedAuthorityWriteConverter implements Converter<SimpleGrantedAuthority, String> {

        @Override
        public String convert(SimpleGrantedAuthority source) {
            return source.getAuthority();
        }
    }

    @ReadingConverter
    public class SimpleGrantedAuthorityReadConverter implements Converter<String, SimpleGrantedAuthority> {

        @Override
        public SimpleGrantedAuthority convert(String source) {
            return new SimpleGrantedAuthority(source);
        }
    }

    @ReadingConverter
    public class GrantedAuthorityReadConverter implements Converter<String, GrantedAuthority> {

        @Override
        public SimpleGrantedAuthority convert(String source) {
            return new SimpleGrantedAuthority(source);
        }
    }

    static protected String getKeyspaceName() {
        return "trab_multi";
    }

    static public String getSecret() {
        return "Jn.baXjvgkGQ.0mqUGElZAzTgcR9ZBu7j.8RPB52BkrE3nSkSwrbiPiRxXs,+3muoOqMqJY6NZhZZBtIU9+vbI5qrJz50zprF4AZ5jXEf9bKpKZeTm1RggmJDMqP0A5A";
    }

    // public String getContactPoints() {
    // return "";
    // }
    static public String getUsername() {
        return "cemPkGmBCJFfqRKGsNscdYyk";
    }

    static public String getToken() {
        return "AstraCS:cemPkGmBCJFfqRKGsNscdYyk:240a8289c355a94066a64669aa832f322a976fc3b1bc7effc01abba598306eba";
    }

    static public String getSecureConnectPath() {
        return "src/main/resources/secure-connect-trabalho-multi.zip";
    }

    public InputStream getSecureConnect() {
        System.out.println("Loading secure connect");
        return getClass().getClassLoader().getResourceAsStream("secure-connect-trabalho-multi.zip");
    }
}
