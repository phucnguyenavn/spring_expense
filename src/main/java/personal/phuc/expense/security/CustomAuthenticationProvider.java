package personal.phuc.expense.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import personal.phuc.expense.security.user.CustomUserDetailsService;
import personal.phuc.expense.security.user.SecurityUser;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        SecurityUser securityUser = (SecurityUser) customUserDetailsService.loadUserByUsername(email);
        if (passwordEncoder.matches(password, securityUser.getPassword())) {
            if (!securityUser.isEnabled()) {
                return new UsernamePasswordAuthenticationToken(securityUser, password);
            }
            return new UsernamePasswordAuthenticationToken(securityUser, password, securityUser.getAuthorities());
        }
        throw new BadCredentialsException("Something went wrong");
    }

    @Override
    public boolean supports(Class<?> type) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(type);
    }

}
