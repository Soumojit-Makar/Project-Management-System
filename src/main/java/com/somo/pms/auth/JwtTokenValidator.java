package com.somo.pms.auth;
import com.somo.pms.utils.AppConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Value("${jwt.secrete.key}")
    private String SECRET_KEY;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(AppConstant.AUTH_HEADER);
        if (authHeader!=null) {
           String token = authHeader.substring(7);
           try{
               SecretKey key= Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
               Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
               String email=String.valueOf(claims.get("email"));
               String authorities=String.valueOf(claims.get("authorities"));
               List<GrantedAuthority> authoritiesList= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
               Authentication authentication = new UsernamePasswordAuthenticationToken(email, null,authoritiesList);
               SecurityContextHolder.getContext().setAuthentication(authentication);
           }
           catch(Exception e){
                throw new BadCredentialsException("invalid token...");
           }
        }
        filterChain.doFilter(request, response);
    }
}
