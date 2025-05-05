package kg.nurtelecom.coffee_sale.repository;

import kg.nurtelecom.coffee_sale.entity.RefreshToken;
import kg.nurtelecom.coffee_sale.entity.UserEntity;
import kg.nurtelecom.coffee_sale.payload.respone.JwtResponse;
import kg.nurtelecom.coffee_sale.security.filter.JwtUtil;
import kg.nurtelecom.coffee_sale.service.RefreshTokenService;
import kg.nurtelecom.coffee_sale.service.jpa.postgres.UserServiceJPA;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepository implements RefreshTokenService {

    private final JdbcClient jdbcClient;
    private final JwtUtil jwtUtil;
    private final UserServiceJPA userServiceJPA;

    public RefreshTokenRepository(JdbcClient jdbcClient, JwtUtil jwtUtil, UserServiceJPA userServiceJPA) {
        this.jdbcClient = jdbcClient;
        this.jwtUtil = jwtUtil;
        this.userServiceJPA = userServiceJPA;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        String sql = "SELECT * FROM refresh_token WHERE token = ?";

        return jdbcClient.sql(sql)
                .param(token)
                .query(RefreshToken.class)
                .optional();
    }

    @Override
    public boolean delete(String token) {
        String sql = "DELETE FROM refresh_token WHERE token = ?";

        int deleted = jdbcClient.sql(sql)
                .param(token)
                .update();
        return deleted > 0;
    }

    @Override
    public boolean verifyExpiryDate(String token) {
        Optional<RefreshToken> optionalRefreshToken = findByToken(token);
        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get();
            if (refreshToken.getExpiryDate().isBefore(OffsetDateTime.now(ZoneOffset.UTC))) {
                delete(token);
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public RefreshToken createRefreshToken(String username) {
//        deleteExpiredTokens(username);

        long tokensCount = countUserTokens(username);

        if (tokensCount > 9) {
            deleteFirstToken(username);
        }

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID(),
                UUID.randomUUID().toString(),
                OffsetDateTime.now(ZoneOffset.UTC).plus(Duration.ofDays(3)),
                username
        );
        save(refreshToken);

        return refreshToken;
    }

    @Override
    public Optional<JwtResponse> getTokens(String token) {
        Optional<RefreshToken> optionalRefreshToken = findByToken(token);

        if (optionalRefreshToken.isPresent() && verifyExpiryDate(token)) {
            RefreshToken refreshToken = optionalRefreshToken.get();
            String username = refreshToken.getUsername();
            String newRefreshToken = updateRefreshToken(refreshToken.getId());
            refreshToken.setToken(newRefreshToken);

            deleteExpiredTokens(username);

            Optional<UserEntity> optionalUser = Optional.ofNullable(userServiceJPA.findByUsername(username));

            if (optionalUser.isPresent()) {
                UserEntity user = optionalUser.get();

                String jwt = jwtUtil.createAccessToken(
                        username,
                        List.of(user.getAuthorities().toString()),
                        24 * 60 * 60 * 1000);
                JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken.getToken());

                return Optional.of(jwtResponse);
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public void logout() {
        String sql = "DELETE FROM refresh_token WHERE username = ?";
        String username = getCurrentUsername();

        jdbcClient.sql(sql)
                .param(username)
                .update();
    }

    private String updateRefreshToken(UUID uuid) {
        String sql = "UPDATE refresh_token SET token = ? WHERE id = ?";

        String newToken = UUID.randomUUID().toString();

        jdbcClient.sql(sql)
                .params(newToken, uuid)
                .update();
        return newToken;
    }

    private void save(RefreshToken refreshToken) {
        String sql = "INSERT INTO refresh_token(id, token, expiry_date, username) VALUES (?, ?, ?, ?)";

        int inserted = jdbcClient.sql(sql)
                .params(
                        List.of(
                                refreshToken.getId(),
                                refreshToken.getToken(),
                                refreshToken.getExpiryDate(),
                                refreshToken.getUsername()
                        )
                )
                .update();
    }

    private List<RefreshToken> getUsersTokens(String username) {
        String sql = "SELECT * FROM refresh_token WHERE username = ?";

        return jdbcClient.sql(sql)
                .param(username)
                .query(RefreshToken.class)
                .list();
    }

    private void deleteExpiredTokens(String username) {
        String sql = "DELETE FROM refresh_token WHERE username = ? AND expiry_date < CURRENT_TIMESTAMP";

        jdbcClient.sql(sql)
                .param(username)
                .update();
    }


    private void deleteFirstToken(String username) {
        String sql = """
            DELETE FROM refresh_token 
            WHERE id = (
                SELECT id FROM refresh_token 
                WHERE username = ? 
                ORDER BY expiry_date ASC 
                LIMIT 1
            )
        """;

        jdbcClient.sql(sql)
                .param(username)
                .update();
    }

    private long countUserTokens(String username) {
        String sql = "SELECT COUNT(*) FROM refresh_token WHERE username = ?";
        return jdbcClient.sql(sql)
                .param(username)
                .query(Long.class)
                .single();
    }

    private String getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            return username;
        }
        return authentication != null ? authentication.getPrincipal().toString() : null;
    }
}
