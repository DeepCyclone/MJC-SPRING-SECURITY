package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;

@Configuration
public class UserManagement {

    private final DataSource dataSource;

    @Autowired
    public UserManagement(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public UserDetailsManager userDetailsManager(){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(this.dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT u_name,u_password,u_enabled from certificatessystem.users where u_name = ?");
        System.out.println(jdbcUserDetailsManager.userExists("Flexus"));
//        jdbcUserDetailsManager.createUser(User.builder().username("Flexus").password("123").authorities(Collections.emptyList()).build());
        return jdbcUserDetailsManager;
    }
}
