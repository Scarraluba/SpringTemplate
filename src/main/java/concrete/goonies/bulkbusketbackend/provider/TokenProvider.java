package concrete.goonies.bulkbusketbackend.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import concrete.goonies.bulkbusketbackend.services.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/27 09:58
 * Author: Scarra Luba
 */

@Component
public class TokenProvider {

    private static final String GET_ARRAYS_LLC = "GET_ARRAYS_LLC";
    private static final long ACCESS_TOKEN_EXPIRATIOM_TIME = 1_800_000;
    private static final String USER_MANAGEMENT_SERVICE = "USER_MANAGEMENT_SERVICE";
    private static final String AUTHORITIES = "AUTHORITIES";
    private static final long REFRESH_TOKEN_EXPIRATIOM_TIME = 432_000_000;
    @Value("${jwt.secrete}")
    private String secrete;

    public String createAccessToken(UserPrincipal userPrincipal) {

        String[] claims = getClaimsFromUser(userPrincipal);

        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(USER_MANAGEMENT_SERVICE)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername()).withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(currentTimeMillis() + ACCESS_TOKEN_EXPIRATIOM_TIME))
                .sign(Algorithm.HMAC512(secrete.getBytes()));
    }

    public String createRefreshToken(UserPrincipal userPrincipal) {

        String[] claims = getClaimsFromUser(userPrincipal);

        return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(USER_MANAGEMENT_SERVICE)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withExpiresAt(new Date(currentTimeMillis() + REFRESH_TOKEN_EXPIRATIOM_TIME))
                .sign(Algorithm.HMAC512(secrete.getBytes()));
    }

    public String getSubject(String token, HttpServletRequest request) {
        JWTVerifier verifier = getJWTVerifier();
        try {
            return getJWTVerifier().verify(token).getSubject();
        } catch (TokenExpiredException ex) {
          //  throw new JWTVerificationException("Token can not be verified");
            request.setAttribute("expiredMessage",ex.getMessage());
        } catch (InvalidClaimException ex) {
           // throw new JWTVerificationException("Token can not be verified");
            request.setAttribute("invalid Claim",ex.getMessage());
        }catch (Exception exception){
            throw exception;
        }
        return token;
    }

    public Authentication getAuthentication(String email, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    public boolean isTokenValid(String email, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(email) && !isTokenExpired(verifier, token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    public List<GrantedAuthority> grantedAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);

        return stream(claims).map(SimpleGrantedAuthority::new).collect(toList());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;

        try {
            Algorithm algorithm = Algorithm.HMAC512(secrete);
            verifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException("Token can not be verified");
        }
        return verifier;

    }

    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }
}
