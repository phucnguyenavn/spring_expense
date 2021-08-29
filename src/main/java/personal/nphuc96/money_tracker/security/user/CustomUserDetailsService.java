package personal.nphuc96.money_tracker.security.user;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import personal.nphuc96.money_tracker.dao.AppUserDAO;
import personal.nphuc96.money_tracker.entity.app_user.AppUser;

import java.util.Optional;

@Component
@Data
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserDAO appUserDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AppUser> temp = appUserDAO.findByEmail(email);
        if (temp.isEmpty()) {
            throw new UsernameNotFoundException("Invalid email");
        }
        return new SecurityUser(temp.get());
    }
}