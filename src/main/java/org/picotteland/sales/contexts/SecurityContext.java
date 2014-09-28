package org.picotteland.sales.contexts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

/**
 * Created by gdepuille on 27/09/14.
 */
@Slf4j
@Configuration
@EnableWebMvcSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated();

        http.httpBasic().realmName("ARMOIRE DE NILS");
    }

    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true)
    protected static class AuthContext extends GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("admin").password("c1b0rd3l").roles("ADMIN", "USER").and()
                    .withUser("user").password("armoire-nils").roles("USER").and();

        }
    }
}
