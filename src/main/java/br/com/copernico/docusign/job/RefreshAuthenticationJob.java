package br.com.copernico.docusign.job;

import br.com.copernico.docusign.OauthSession;
import br.com.copernico.docusign.core.utils.FileUtils;
import br.com.copernico.docusign.dto.RefreshTokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

@Component
@EnableScheduling
public class RefreshAuthenticationJob {

    private static final String GRANT_TYPE = "refresh_token";

    private final String clientId;
    private final String clientSecret;
    private final String oAuthPath;


    public RefreshAuthenticationJob(@Value("${authorization.code.grant.client.client-id}") String clientId,
                                    @Value("${authorization.code.grant.client.client-secret}") String clientSecret,
                                    @Value("${authorization.code.grant.client.access-token-uri}") String oAuthPath) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oAuthPath = oAuthPath;
    }

    @Scheduled(fixedDelay = 21600000, initialDelay = 2000)
    private void refreshToken() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String basicAuth = clientId.concat(":").concat(clientSecret);
        String basicAuthEndoded = Base64.getEncoder().encodeToString(basicAuth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(basicAuthEndoded);

        HttpEntity httpEntity = new HttpEntity(headers);

        String uri = UriComponentsBuilder.fromHttpUrl(oAuthPath)
                .queryParam("grant_type", GRANT_TYPE)
                .queryParam(GRANT_TYPE, OauthSession.getInstance().getRefreshToken())
                .toUriString();

        ResponseEntity<RefreshTokenDTO> responseEntity = restTemplate.postForEntity(uri, httpEntity, RefreshTokenDTO.class);
        OauthSession.reInitData(responseEntity.getBody().getAccessToken(),
                responseEntity.getBody().getTokenType(),
                responseEntity.getBody().getRefreshToken(),
                responseEntity.getBody().getExpiresIn());
        FileUtils.saveNewCredentials(OauthSession.getInstance());
    }
}
