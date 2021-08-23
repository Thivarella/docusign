package br.com.copernico.docusign;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class DSConfiguration {
    @Value("${DS_API_ACCOUNT_ID}")
    private String apiAccountId;

    @Value("${DS_API_NAME}")
    private String apiName;

    @Value("${DS_BASE_PATH}")
    private String basePath;
}
