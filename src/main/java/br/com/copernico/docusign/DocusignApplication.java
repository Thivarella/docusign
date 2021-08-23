package br.com.copernico.docusign;

import br.com.copernico.docusign.core.utils.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocusignApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DocusignApplication.class, args);
        FileUtils.loadCredentials();
    }
}
