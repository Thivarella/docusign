package br.com.copernico.docusign.service;


import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.core.model.Session;
import br.com.copernico.docusign.core.model.User;
import org.springframework.stereotype.Service;

@Service
public class AcquisitionService {
    private static final String HTML_DOCUMENT_FILE_NAME = "templates/Contrato_de_Locacao_e_Adesao_ao_Condominio_Model_CNPJ_.html";
    private static final String HTML_DOCUMENT_NAME = "Contrato de Locação de Quotas e Adesão ao Condomínio";
    private static final int ANCHOR_OFFSET_Y = 15;
    private static final int ANCHOR_OFFSET_X = 0;

    private final Session session;
    private final User user;

    public AcquisitionService(DSConfiguration config, Session session, User user) {
        this.session = session;
        this.user = user;
    }

}
