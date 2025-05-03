package kg.nurtelecom.coffee_sale.security.config;

import kg.nurtelecom.coffee_sale.security.filter.CustomAuthenticationFilter;
import kg.nurtelecom.coffee_sale.security.filter.CustomAuthorizationFilter;
import kg.nurtelecom.coffee_sale.security.jwt.JWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWT jwt;

    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWT jwt) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwt = jwt;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationConfiguration, jwt);
        CustomAuthorizationFilter customAuthorizationFilter = new CustomAuthorizationFilter(jwt);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/h2-ui/**"));
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/css/**", "/images/**").permitAll()
                .requestMatchers("/", "/login", "/register", "/logout").permitAll()
                .anyRequest().authenticated()
        );

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll());

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll());

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "pbkdf2";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}