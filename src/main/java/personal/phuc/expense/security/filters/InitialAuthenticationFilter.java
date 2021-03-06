package personal.phuc.expense.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import personal.phuc.expense.dto.JwtToken;
import personal.phuc.expense.exception.model.ExceptionResponse;
import personal.phuc.expense.security.user.SecurityUser;
import personal.phuc.expense.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;


public class InitialAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager manager;
    private final JwtUtil jwtUtil;

    public InitialAuthenticationFilter(AuthenticationManager manager, JwtUtil jwtUtil) {
        this.manager = manager;
        this.jwtUtil = jwtUtil;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        return checkAuthentication(manager.authenticate(authentication), response);

    }

    private void writeJsonResponse(HttpServletResponse response, Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(response.getOutputStream(), object);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        SecurityUser user = (SecurityUser) auth.getPrincipal();
        String token = jwtUtil.createToken(user, request);
        writeJsonResponse(response, new JwtToken(token, user.getAppUser().getId(), jwtUtil.getTokenExpiration(token)));

    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    private Authentication checkAuthentication(Authentication authentication, HttpServletResponse response) throws IOException {
        if (!authentication.isAuthenticated()) {
            response.setStatus(400);
            writeJsonResponse(response, new ExceptionResponse("Invalid User", "400", LocalDateTime.now()));

        }
        return authentication;
    }
}
