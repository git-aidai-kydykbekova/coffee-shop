package kg.nurtelecom.coffee_sale.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.nurtelecom.coffee_sale.entity.RefreshToken;
import kg.nurtelecom.coffee_sale.service.RefreshTokenService;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public CustomAuthenticationFilter(AuthenticationConfiguration authenticationConfiguration, RefreshTokenService refreshTokenService, JwtUtil jwtUtil) throws Exception{
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);

    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        String access_token = jwtUtil.createAccessToken(
                user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                24 * 60 * 60 * 1000);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        String refreshTokenString = refreshToken.getToken();

        Map<String, String> token = new HashMap<>();
        token.put("accessToken", access_token);
        token.put("token", refreshTokenString);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }

    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        String access_token = "Unsuccessful Authentication";
        Map<String, String> token = new HashMap<>();
        token.put("accessToken", access_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), token);
    }
}
