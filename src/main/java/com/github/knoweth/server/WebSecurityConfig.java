package com.github.knoweth.server;

import com.github.knoweth.server.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/api/users/register").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().and()
                .logout().permitAll();
//            http
//                    .csrf().disable()
//                    .exceptionHandling()
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/api/foos").authenticated()
//                    .antMatchers("/api/admin/**").hasRole("ADMIN")
//                    .and()
//                    .formLogin()
//                    .successHandler(mySuccessHandler)
//                    .failureHandler(myFailureHandler)
//                    .and()
//                    .logout();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}