package de.hsrm.mi.web.projekt.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

@Service
public class BenutzerUserDetailsService implements UserDetailsService{
    private final PasswordEncoder passwordEncoder;
    private final BenutzerService benutzerService;

    public BenutzerUserDetailsService(PasswordEncoder passwordEncoder, BenutzerService benutzerService) {
        this.passwordEncoder = passwordEncoder;
        this.benutzerService = benutzerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            return User.withUsername("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMINISTRATOR")
                    .build();
        }
        else{
            Benutzer benutzer = benutzerService.findAllBenutzer().stream()
                .filter(b -> b.getLoginName().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Benutzer '" + username + "' nicht gefunden."));

            return User.withUsername(benutzer.getLoginName())
                .password(benutzer.getPasswort()) 
                .roles(benutzer.getRolle())
                .build();
        }
    }
}
