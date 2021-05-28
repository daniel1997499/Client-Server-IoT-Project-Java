package com.example.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*
     * 1 Allow in memory authentication with a user named “user”
     * 2 Allow in memory authentication with an administrative user named “admin”
     * 3 Ignore any request that starts with “/resources/”. This is similar to configuring http@security=none when using the XML namespace configuration.
     * 4 Allow anyone (including unauthenticated users) to access to the URLs “/signup” and “/about”
     * 5 Allow anyone (including unauthenticated users) to access to the URLs “/login” and “/login?error”. The permitAll() in this case means, allow access to any URL that formLogin() uses.
     * 6 Any URL that starts with “/admin/” must be an administrative user. For our example, that would be the user “admin”.
     * 7 All remaining URLs require that the user be successfully authenticated
     * 8 Setup form based authentication using the Java configuration defaults. Authentication is performed when a POST is submitted to the URL “/login” with the parameters “username” and “password”.
     * 9 Explicitly state the login page, which means the developer is required to render the login page when GET /login is requested.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user")  // #1
                .password("{noop}1q2w3e4r5t6y")
                .roles("USER")
                .and()
                .withUser("admin") // #2
                .password("{noop}q1w2e3r4t5y6")
                .roles("ADMIN","USER");
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web
//                .ignoring()
//                .antMatchers("/resources/static"); // #3
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/authenticate", "/sensordata").permitAll() // #4
                .antMatchers("/h2-console/**").hasRole("ADMIN") // #6
                .anyRequest().authenticated() // 7
                .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
                .and().headers().frameOptions().sameOrigin()//allow use of frame to same origin urls
                .and()
                .formLogin()  // #8
//                .loginPage("/login") // #9
                .permitAll();
    }
}