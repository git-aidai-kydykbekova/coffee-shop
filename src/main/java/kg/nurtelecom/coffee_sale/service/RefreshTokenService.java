package kg.nurtelecom.coffee_sale.service;


import kg.nurtelecom.coffee_sale.entity.RefreshToken;
import kg.nurtelecom.coffee_sale.payload.respone.JwtResponse;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    boolean delete(String token);

    boolean verifyExpiryDate(String token);

    RefreshToken createRefreshToken(String username);

    Optional<JwtResponse> getTokens(String token);

    void logout();
}