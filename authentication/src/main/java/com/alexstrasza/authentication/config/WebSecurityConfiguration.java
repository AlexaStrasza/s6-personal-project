package com.alexstrasza.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Import(Encoders.class)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

//    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
//            new AntPathRequestMatcher("/users/login/**"),
//            new AntPathRequestMatcher("/users/sign-up/**"),
//            new AntPathRequestMatcher("/oauth/token/**"),
//            new AntPathRequestMatcher("/error/**")
//    );
//
//    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Override
//    public void configure(final WebSecurity web)
//    {
//        web.ignoring().requestMatchers(PUBLIC_URLS);
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(STATELESS)
                .and().authorizeRequests().antMatchers("/oauth/token","/h2-console/**","/users/sign-up","/users/login")
                .permitAll().anyRequest().authenticated();
//        http
//                .sessionManagement()
//                .sessionCreationPolicy(STATELESS)
//                // this entry point handles when you request a protected page and you are not yet
//                // authenticated
//                .and()
//                .authorizeRequests()
//                .requestMatchers(PROTECTED_URLS)
//                .authenticated()
//                .and()
//                .csrf().disable()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .logout().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
