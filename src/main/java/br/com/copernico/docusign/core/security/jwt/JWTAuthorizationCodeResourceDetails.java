package br.com.copernico.docusign.core.security.jwt;

import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.core.exception.LauncherException;
import com.docusign.esign.client.ApiClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.client.token.grant.redirect.AbstractRedirectResourceDetails;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

@Getter
@Setter
public class JWTAuthorizationCodeResourceDetails extends AbstractRedirectResourceDetails {

    private final ApiClient apiClient = new ApiClient();
    private String privateKeyPath;
    private String impersonatedUserGuid;
    private String baseUrl;
    @Autowired
    private DSConfiguration dsConfiguration;
    private FileSystemResource rsaPrivateKey;

    public byte[] getRsaBytes() throws LauncherException {
        if (rsaPrivateKey == null) {
            rsaPrivateKey = new FileSystemResource(privateKeyPath);
        }
        try {
            return FileCopyUtils.copyToByteArray(rsaPrivateKey.getInputStream());
        } catch (IOException e) {
            throw new LauncherException("Error reading rsa file. Check configuration");
        }
    }

    public ApiClient getApiClient() {
        return apiClient;
    }
}
