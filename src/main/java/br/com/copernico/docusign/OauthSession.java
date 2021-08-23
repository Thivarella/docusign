package br.com.copernico.docusign;

import lombok.Data;

import java.util.Objects;

@Data
public final class OauthSession {
    private static OauthSession instance;
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String expiresIn;

    private OauthSession(String accessToken, String tokenType, String refreshToken, String expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public static OauthSession getInstance(String accessToken, String tokenType, String refreshToken, String expiresIn) {
        if (Objects.isNull(instance)) {
            instance = new OauthSession(accessToken, tokenType, refreshToken, expiresIn);
        }
        return instance;
    }

    public static OauthSession getInstance() {
        return instance;
    }

    public static void reInitData(String accessToken, String tokenType, String refreshToken, String expiresIn) {
        instance = new OauthSession(accessToken, tokenType, refreshToken, expiresIn);
    }

}
