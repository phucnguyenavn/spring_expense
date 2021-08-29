package personal.nphuc96.money_tracker.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import personal.nphuc96.money_tracker.security.user.SecurityUser;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.expired-time}")
    private Integer EXPIRED_TIME;

   /* @Value("${jwt.refresh-expired-time}")
    private final Integer REFRESH_EXPIRED_TIME;*/

    @Value("${jwt.signing.key}")
    private String signature;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(signature);
    }

    private Date expiredTime(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        log.info("Current time : {}", calendar.getTime());
        calendar.add(Calendar.HOUR, hours);
        log.info("Expired time : {}", calendar.getTime());
        return calendar.getTime();
    }

    public String createToken(SecurityUser user) {
        try {
            return JWT.create()
                    .withIssuer("Auth0 - Money Tracker Application")
                    .withExpiresAt(expiredTime(EXPIRED_TIME))
                    .withSubject(user.getUsername())
                    .withIssuedAt(new Date())
                    .withClaim("Roles", Collections.singletonList(user.getAuthorities()))
                    .sign(getAlgorithm());
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Can not create new token", exception.getCause());
        }
    }

    private DecodedJWT decodedJWT(String token) {
        JWTVerifier verifier = JWT
                .require(getAlgorithm())
                .acceptLeeway(1)
                .build();
        return verifier.verify(token);
    }

    public String getUsernameFromToken(String token) {
        try {
            return decodedJWT(token).getSubject();
        } catch (JWTDecodeException exception) {
            throw new JWTDecodeException("Invalid token");
        }
    }

}