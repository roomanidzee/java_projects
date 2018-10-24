package com.romanidze.asyncappdbstorage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Filter authenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(@Qualifier("restAuthenticationFilter") Filter authenticationFilter,
                          AuthenticationProvider authenticationProvider,
                          @Qualifier("restUserDetailsService") UserDetailsService userDetailsService,
                          AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationFilter = authenticationFilter;
        this.authenticationProvider = authenticationProvider;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(this.userDetailsService);
        auth.authenticationProvider(this.authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{

        http.addFilterBefore(this.authenticationFilter, BasicAuthenticationFilter.class)
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
              .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)
            .and()
              .authorizeRequests()
              .antMatchers("/users/**").permitAll()
              .antMatchers("/users/get_users").hasAnyAuthority("ADMIN", "ANONYMOUS")
              .anyRequest().authenticated()
            .and()
              .csrf()
              .disable()
              .httpBasic()
              .disable()
              .formLogin()
              .disable();

    }

}
