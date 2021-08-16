package br.com.copernico.docusign.service;


import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.core.model.Session;
import br.com.copernico.docusign.core.model.User;
import org.springframework.stereotype.Service;

@Service
public class AcquisitionService {

    private final Session session;
    private final User user;

    public AcquisitionService(DSConfiguration config, Session session, User user) {
        this.session = session;
        this.user = user;
    }

}
