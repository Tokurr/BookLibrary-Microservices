package com.library_book.auth_service.service;

import com.library_book.auth_service.client.AdminClient;
import com.library_book.auth_service.model.AdminDetailsResponse;
import com.library_book.auth_service.model.LoginRequest;
import com.library_book.auth_service.model.TokenResponse;
import com.library_book.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {



    private final AdminClient adminClient;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final BlackListService blacklist;

    public TokenResponse login(LoginRequest req) {
        AdminDetailsResponse admin = adminClient.getByUsername(req.username());
        if (admin == null) {
            throw new RuntimeException("User not found");
        }
        if (!encoder.matches(req.password(), admin.passwordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        String accessToken  = jwtUtil.generateAccessToken(admin.username(), admin.roles());
        String refreshToken = refreshTokenService.create(admin.username());




        return new TokenResponse(accessToken,refreshToken);
    }


    public TokenResponse refresh(String username, String refreshToken)
    {
        if(!refreshTokenService.verify(username,refreshToken))
        {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccesToken = jwtUtil.generateAccessToken(username,adminClient.getByUsername(username).roles());

        return new TokenResponse(newAccesToken,refreshToken);
    }

    public void logout(String token, String username) {
        Date exp = jwtUtil.getExpiration(token);
        long ttl = exp.getTime() - System.currentTimeMillis();
        blacklist.addToBlackList(token, ttl);
        refreshTokenService.revoke(username);
    }

}