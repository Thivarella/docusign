package br.com.copernico.docusign.model;

import br.com.copernico.docusign.service.DocuSignService;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Data
public class DocuSign {

    private String integrationKey;
    private String secretKey;
    private String accessToken;

    public String getSecretKey() {
        if (Objects.isNull(this.accessToken))
            this.accessToken = DocuSignService.getAccessToken();
        return secretKey;
    }
}
