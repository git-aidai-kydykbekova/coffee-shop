package kg.nurtelecom.coffee_sale.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JWT {
    private static final String JWT_HEADER_ALGORITHM = "HS384";
    private static final String JWT_HEADER_TYPE = "JWT";
    private static final String HMAC_SHA384 = "HmacSHA384";

    @Value("${spring.jwt.secret_key}")
    private String secret;

    public String createJWT(String username, List<String> roles) throws Exception {
        String jwtHeader = generateHeader();
        String jwtPayload = generatePayload(username, roles);
        String signature = generateSignature(jwtHeader, jwtPayload);

        return jwtHeader + "." + jwtPayload + "." + signature;
    }

    public Map<String, Object> verifyToken(String token) throws Exception {
        validateToken(token);

        String[] parts = token.split("\\.");
        String jwtHeader = parts[0];
        String jwtPayload = parts[1];
        String receivedSignature = parts[2];

        validateSignature(jwtHeader, jwtPayload, receivedSignature);

        return decodePayload(jwtPayload);
    }

    private String generateHeader() throws Exception {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", JWT_HEADER_ALGORITHM);
        header.put("typ", JWT_HEADER_TYPE);
        return encodeMapToBase64(header);
    }

    private String generatePayload(String username, List<String> roles) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("sub", username);
        payload.put("exp", System.currentTimeMillis() / 1000 + 600);
        payload.put("iat", System.currentTimeMillis() / 1000);
        payload.put("roles", roles);
        return encodeMapToBase64(payload);
    }

    private String generateSignature(String jwtHeader, String jwtPayload) throws Exception {
        String data = jwtHeader + "." + jwtPayload;
        return hmacSha384(data, secret);
    }

    private void validateToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }
    }

    private void validateSignature(String jwtHeader, String jwtPayload, String receivedSignature) throws Exception {
        String expectedSignature = generateSignature(jwtHeader, jwtPayload);
        if (!expectedSignature.equals(receivedSignature)) {
            throw new SecurityException("Invalid JWT signature");
        }
    }

    private Map<String, Object> decodePayload(String jwtPayload) throws Exception {
        String payloadJson = new String(Base64.getUrlDecoder().decode(jwtPayload), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(payloadJson, Map.class);
    }

    private String encodeMapToBase64(Map<String, Object> map) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(map);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    private String hmacSha384(String data, String secret) throws Exception {
        Mac hmac = Mac.getInstance(HMAC_SHA384);
        hmac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA384));
        byte[] hmacBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
    }
}