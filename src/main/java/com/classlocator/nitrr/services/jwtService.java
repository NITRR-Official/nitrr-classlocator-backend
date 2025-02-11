package com.classlocator.nitrr.services;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class jwtService {

    private String secretKey = "";

    /**  
     * Retrieves the secret key used for signing JWT tokens.  
     * Decodes the Base64-encoded key and returns it as a SecretKey.  
     *  
     * @return SecretKey - The decoded secret key for HMAC SHA-256 signing.  
     */
    private SecretKey getKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    /**  
     * Extracts a specific claim from the given JWT token using a claim resolver function.  
     *  
     * @param token String - The JWT token from which the claim is extracted.  
     * @param claimResolver Function<Claims, T> - A function that extracts the required claim.  
     * @param <T> - The type of the extracted claim.  
     * @return T - The extracted claim value.  
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**  
     * Extracts all claims (payload) from the given JWT token.  
     *  
     * @param token String - The JWT token from which claims are extracted.  
     * @return Claims - The extracted claims from the token.  
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**  
     * Checks if the given JWT token has expired.  
     *  
     * @param token String - The JWT token to be checked for expiration.  
     * @return boolean - True if the token is expired, otherwise false.  
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**  
     * Extracts the expiration date from the given JWT token.  
     *  
     * @param token String - The JWT token from which the expiration date is extracted.  
     * @return Date - The expiration date of the token.  
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**  
     * Constructor for jwtService that generates a secret key  
     * for signing JWT tokens using HMAC SHA-256 encryption.  
     *  
     * Initializes `secretKey` with a Base64-encoded secret key.  
     */
    public jwtService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sKey = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            System.out.print(e.toString()); //To be added in log file
        }
    }

    /**  
     * Generates a JWT token with user details and an expiration time of 30 days.  
     *  
     * @param rollno String - The roll number (subject) of the user.  
     * @param name String - The name of the user.  
     * @param department String - The department of the user.  
     * @param role String - The role assigned to the user (e.g., ADMIN, SUPER_ADMIN).  
     * @return String - The generated JWT token.  
     */
    public String generateToken(String rollno, String name, String department, String role) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("role", role);
        claims.put("name", name);
        claims.put("department", department);
        return Jwts.builder().claims().add(claims)
                .subject(rollno).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 30 * 1000))
                .and()
                .signWith(getKey())
                .compact();
    }

    /**  
     * Extracts the roll number (subject) from the given JWT token.  
     *  
     * @param token String - The JWT token from which the roll number is extracted.  
     * @return String - The extracted roll number (subject) from the token.  
     */
    public String extractRoll(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**  
     * Validates the given JWT token by checking if the extracted roll number  
     * matches the username from UserDetails and ensures the token is not expired.  
     *  
     * @param token String - The JWT token to be validated.  
     * @param userDetails UserDetails - The user details containing the expected username.  
     * @return boolean - True if the token is valid, otherwise false.  
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractRoll(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
