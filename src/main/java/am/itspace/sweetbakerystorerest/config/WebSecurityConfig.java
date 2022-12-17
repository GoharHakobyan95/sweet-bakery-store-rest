package am.itspace.sweetbakerystorerest.config;

import am.itspace.sweetbakerystorecommon.entity.Role;
import am.itspace.sweetbakerystorecommon.security.UserDetailServiceImpl;
import am.itspace.sweetbakerystorerest.security.JwtAuthenticationEntryPoint;
import am.itspace.sweetbakerystorerest.security.JwtAuthenticationTokenFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationTokenFilter filter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .authorizeRequests()
                .antMatchers( "/api/categories/**").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET,  "/api/categories","/api/categories/{id}").hasAuthority(Role.USER.name())
                .antMatchers( "/api/products/**").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/products/{id}", "/api/products").hasAuthority(Role.USER.name())
                .antMatchers("/api/addresses/**").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/api/addresses").hasAuthority(Role.USER.name() )
                .antMatchers( "/api/city/**").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/payment").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/payment/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/payment").hasAuthority(Role.USER.name())
                .antMatchers(HttpMethod.PUT, "/api/payment/{id}").hasAuthority(Role.USER.name())
                .antMatchers(HttpMethod.POST, "/api/order").hasAuthority(Role.USER.name())
                .antMatchers(HttpMethod.GET, "/api/order/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/api/order}").hasAuthority(Role.ADMIN.name())
                .anyRequest().permitAll();
        // Custom JWT based security filter
        http
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        // disable page caching
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

}
