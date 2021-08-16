package br.com.copernico.docusign.controller;

import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.common.WorkArguments;
import br.com.copernico.docusign.core.controller.eSignature.AbstractEsignatureController;
import br.com.copernico.docusign.core.model.Session;
import br.com.copernico.docusign.core.model.User;
import br.com.copernico.docusign.dto.IncomingContractDTO;
import br.com.copernico.docusign.service.AcquisitionService;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/acquisition")
public class AcquisitionController extends AbstractEsignatureController {

    private final AcquisitionService acquisitionService;
    private final Session session;
    private final User user;
    @Value("${authorization.code.grant.client.client-id}")
    private String clientId;
    @Value("${authorization.code.grant.client.client-secret}")
    private String secretKey;

    public AcquisitionController(DSConfiguration dsConfiguration, AcquisitionService acquisitionService, Session session, User user) {
        super(dsConfiguration);
        this.acquisitionService = acquisitionService;
        this.session = session;
        this.user = user;
    }

    @Override
    protected Object doWork(IncomingContractDTO incomingContractDTO, ModelMap model, HttpServletResponse response) throws Exception {
        EnvelopesApi envelopesApi = AbstractEsignatureController.createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        WorkArguments args = acquisitionService.convertDTOToWorkArguments(incomingContractDTO);

        EnvelopeDefinition envelope = acquisitionService.makeEnvelope(args);
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        session.setEnvelopeId(results.getEnvelopeId());


        return "OK";
    }
}
