package br.com.copernico.docusign.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenAuthenticationService {

    static final String HEADER_STRING = "Authorization";
    @Value("${docusign.server.api.token}")
    public String apiToken;

    public TokenAuthenticationService() {
    }

    public Boolean getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        return apiToken.equals(token);
    }
}