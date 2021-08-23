package br.com.copernico.docusign.controller;

import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.OauthSession;
import br.com.copernico.docusign.common.WorkArguments;
import br.com.copernico.docusign.core.controller.eSignature.AbstractEsignatureController;
import br.com.copernico.docusign.dto.IncomingContractDTO;
import br.com.copernico.docusign.service.AcquisitionService;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.model.EnvelopeDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/acquisition")
public class AcquisitionController extends AbstractEsignatureController {

    private final AcquisitionService acquisitionService;
    @Value("${authorization.code.grant.client.client-id}")
    private String clientId;
    @Value("${authorization.code.grant.client.client-secret}")
    private String secretKey;

    public AcquisitionController(DSConfiguration dsConfiguration, AcquisitionService acquisitionService) {
        super(dsConfiguration);
        this.acquisitionService = acquisitionService;
    }

    @Override
    protected Object doWork(IncomingContractDTO incomingContractDTO, ModelMap model, HttpServletResponse response) throws Exception {
        OauthSession oauthSession = OauthSession.getInstance();

        EnvelopesApi envelopesApi = AbstractEsignatureController.createEnvelopesApi(config.getBasePath(), oauthSession.getAccessToken());

        WorkArguments args = acquisitionService.convertDTOToWorkArguments(incomingContractDTO);

        EnvelopeDefinition envelope = acquisitionService.makeEnvelope(args);

        return envelopesApi.createEnvelope(config.getApiAccountId(), envelope);
    }
}
