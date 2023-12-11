package com.Mexxar.Test.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
public class JwtService {
    private static final String SECRET_KEY = "dJ6abSnBOE0oRe60YjPDOjSrDIjbBuXfySTmcFVDlkl8UUXNKB686lt2Zvav+H0FWl/aYEAWYjTXRdUla7KYmQEAVzmgh9j/04phaALbPnlapIERKhLTe7TjIuiUNBBO3JVz8hTa+zUE7TT0JgJ9drukivvtuiMHM+j/bVRzMsUhx20XDAicF5v2kH6YvL8Tcu9FrOB782j/5+1VTtpG7WzIlhrCfrlYSH2fJ83PbYcS4Y8D9f66Dw6SBIdlFEFqpUhMehAI4uZCqoCX+lX9OrG6E9tweWFxkO6T82QUWT0lY5NNs1qJSoFD7xQZWKIVJOgbdHm/zdI9V7gS8c+/1ph61yAnCX8upnkXmPKhcMc=";
    private final JwtParser jwtParser;

    //Constructor to pass the secretkey
    public JwtService(){
        this.jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);
    }
    //Extract username(email) in to the Token
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }
    //Claim the Token
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }
    //Extract all claims from token
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //Generate a Token
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    //Check if Token is Valid
    public boolean isTokenValid (String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    //Check if Token Expiration date has passed
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    //Get Expiration date from Token
    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
    //Generate the Token (use email from userDetails)
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }
    private Claims extractAllClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }
    //Used to Decode the Token
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
