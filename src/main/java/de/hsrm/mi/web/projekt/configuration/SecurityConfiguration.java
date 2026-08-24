package de.hsrm.mi.web.projekt.configuration;

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChainApp(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(toH2Console()).permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
            .requestMatchers("/api/**", "/stompbroker/**").permitAll()
            .requestMatchers("/admin/benutzer", "/admin/benutzer/**").hasRole("ADMINISTRATOR")
            .requestMatchers("/admin/anzeige", "/admin/anzeige/**").authenticated()
            .anyRequest().authenticated()
        )
        .formLogin(folo -> folo
            .permitAll()
            .defaultSuccessUrl("/admin/anzeige", true)
        )
        .logout(out -> out
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/**")
            .ignoringRequestMatchers(toH2Console())
            .ignoringRequestMatchers("/admin/benutzer/*/hx/feld/*")
        )
        .headers(hdrs -> hdrs
            .frameOptions(fo -> fo.sameOrigin())
        )
        .build();
    }

}
