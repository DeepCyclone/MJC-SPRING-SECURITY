package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig
    extends WebSecurityConfigurerAdapter implements ApplicationContextAware {

    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager userDetailsManager;

    @Autowired
    public SecurityConfig(DataSource dataSource, PasswordEncoder passwordEncoder, UserDetailsManager userDetailsManager) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().
                and().formLogin().permitAll();
//        http.authorizeRequests().anyRequest();
//                antMatchers(HttpMethod.POST,"/").hasAuthority().
//                anyRequest().permitAll().
//                csrf().disable();
//                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);   //TODO STATELESS OR NEVER
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception{
        builder.jdbcAuthentication().
                dataSource(this.dataSource).
                usersByUsernameQuery("SELECT u_name,u_password,u_enabled from certificatessystem.users where u_name = ?");
        builder.
                userDetailsService(userDetailsManager).
                passwordEncoder(passwordEncoder);
    }
}