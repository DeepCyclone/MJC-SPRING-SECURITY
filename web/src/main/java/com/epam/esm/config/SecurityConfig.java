package com.epam.esm.config;

import com.epam.esm.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class SecurityConfig
    extends WebSecurityConfigurerAdapter implements ApplicationContextAware {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(GET, "/api/v1/**").permitAll()
                .antMatchers(POST, "/api/v1/users").permitAll()
                .antMatchers(POST, "/orders").fullyAuthenticated()
                .antMatchers(GET, "/tags/**", "/users/**").fullyAuthenticated()
                .anyRequest().hasRole(Role.ADMIN);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder.jdbcAuthentication().
                dataSource(this.dataSource).
                usersByUsernameQuery("SELECT u_name,u_password,u_enabled FROM certificatessystem.user WHERE u_name = ?").
                authoritiesByUsernameQuery("SELECT u_name,r_name FROM user JOIN role on r_id = u_r_id WHERE u_name = ?").
                passwordEncoder(passwordEncoder);
    }
}