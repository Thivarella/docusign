package br.com.copernico.docusign.config;

import br.com.copernico.docusign.model.DocuSign;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DocuSignConfig {
    @Bean("accessTokenService")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DocuSign getAccountService() {
        return new DocuSign();
    }
}
