package personal.nphuc96.money_tracker.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import personal.nphuc96.money_tracker.security.user.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
@Log4j2
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final UserManagementConfig userManagementConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        log.info("Username : {} , password : {}", email, password);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userManagementConfig.passwordEncoder().matches(password, userDetails.getPassword())) {
            log.info("Found user, return UsernamePasswordAuthenticationToken object");
            return new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
        }
        throw new BadCredentialsException("Something went wrong");
    }

    @Override
    public boolean supports(Class<?> type) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(type);
    }
}