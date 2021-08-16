package br.com.copernico.docusign.controller;

import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.common.WorkArguments;
import br.com.copernico.docusign.core.common.DocumentType;
import br.com.copernico.docusign.core.controller.eSignature.AbstractEsignatureController;
import br.com.copernico.docusign.core.controller.eSignature.EnvelopeHelpers;
import br.com.copernico.docusign.core.model.Session;
import br.com.copernico.docusign.core.model.User;
import br.com.copernico.docusign.dto.IncomingContractDTO;
import br.com.copernico.docusign.service.AcquisitionService;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/acquisition")
public class AcquisitionController extends AbstractEsignatureController {

    private static final String HTML_DOCUMENT_FILE_NAME_PJ = "templates/Contrato_de_Locacao_e_Adesao_ao_Condominio_Model_CNPJ_.html";
    private static final String HTML_DOCUMENT_FILE_NAME_PF = "templates/Contrato_de_Locacao_e_Adesao_ao_Condominio_Model_PF_.html";
    private static final String HTML_DOCUMENT_NAME = "Contrato de Locação de Quotas e Adesão ao Condomínio";
    private static final int ANCHOR_OFFSET_Y = 15;
    private static final int ANCHOR_OFFSET_X = 0;
    private final AcquisitionService acquisitionService;
    private final Session session;
    private final User user;
    private final EnvelopeHelpers envelopeHelpers;
    @Value("${authorization.code.grant.client.client-id}")
    private String clientId;
    @Value("${authorization.code.grant.client.client-secret}")
    private String secretKey;

    public AcquisitionController(DSConfiguration dsConfiguration, AcquisitionService acquisitionService, Session session, User user, EnvelopeHelpers envelopeHelpers) {
        super(dsConfiguration);
        this.acquisitionService = acquisitionService;
        this.session = session;
        this.user = user;
        this.envelopeHelpers = envelopeHelpers;
    }

    public static EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {

        Tabs signerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/signature1/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X)
        );
        Tabs secondSignerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/signature2/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X)
        );
        Tabs thirdSignerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/signature3/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X)
        );
        Tabs fourthSignerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/signature4/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X)
        );
        Tabs fifthSignerTabs = EnvelopeHelpers.createSignerTabs(
                EnvelopeHelpers.createSignHere("/signature5/", ANCHOR_OFFSET_Y, ANCHOR_OFFSET_X)
        );

        Signer signer = new Signer();
        signer.setEmail(args.getSignerEmail());
        signer.setName(args.getSignerName());
        signer.setRecipientId("1");
        signer.setRoutingOrder("1");
        signer.setTabs(signerTabs);

        Signer secondSigner = new Signer();
        secondSigner.setEmail(args.getSignerEmail());
        secondSigner.setName(args.getSignerName());
        secondSigner.setRecipientId("2");
        secondSigner.setRoutingOrder("2");
        secondSigner.setTabs(secondSignerTabs);

        Signer thirdSigner = new Signer();
        thirdSigner.setEmail(args.getSignerEmail());
        thirdSigner.setName(args.getSignerName());
        thirdSigner.setRecipientId("3");
        thirdSigner.setRoutingOrder("3");
        thirdSigner.setTabs(thirdSignerTabs);

        Signer fouthSigner = new Signer();
        fouthSigner.setEmail(args.getSignerEmail());
        fouthSigner.setName(args.getSignerName());
        fouthSigner.setRecipientId("4");
        fouthSigner.setRoutingOrder("4");
        fouthSigner.setTabs(fourthSignerTabs);

        Signer fifthSigner = new Signer();
        fifthSigner.setEmail(args.getSignerEmail());
        fifthSigner.setName(args.getSignerName());
        fifthSigner.setRecipientId("5");
        fifthSigner.setRoutingOrder("5");
        fifthSigner.setTabs(fifthSignerTabs);

        // create a cc recipient to receive a copy of the documents, identified by name and email
        CarbonCopy cc = new CarbonCopy();
        cc.setEmail(args.getSignerEmail());
        cc.setName(args.getSignerName());
        cc.setRecipientId("6");
        cc.setRoutingOrder("6");


        byte[] htmlDocument = EnvelopeHelpers.createHtmlFromTemplateFile("J".equals(args.getTipoPessoa()) ?
                HTML_DOCUMENT_FILE_NAME_PJ : HTML_DOCUMENT_FILE_NAME_PF, "args", args);
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Please sign this document set");
        envelope.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocument(htmlDocument, HTML_DOCUMENT_NAME,
                        DocumentType.HTML.getDefaultFileExtention(), "1")));
        envelope.setRecipients(new Recipients());
        envelope.getRecipients().setCarbonCopies(Collections.singletonList(cc));
        envelope.getRecipients().setSigners(Arrays.asList(signer, secondSigner, thirdSigner, fouthSigner, fifthSigner));
        envelope.setStatus("sent");

        return envelope;
    }

    @Override
    protected Object doWork(IncomingContractDTO incomingContractDTO, ModelMap model, HttpServletResponse response) throws Exception {
        EnvelopesApi envelopesApi = AbstractEsignatureController.createEnvelopesApi(session.getBasePath(), user.getAccessToken());

        WorkArguments args = convertDTOToWorkArguments(incomingContractDTO);

        EnvelopeDefinition envelope = makeEnvelope(args);
        EnvelopeSummary results = envelopesApi.createEnvelope(session.getAccountId(), envelope);

        session.setEnvelopeId(results.getEnvelopeId());


        return "OK";
    }

    private WorkArguments convertDTOToWorkArguments(IncomingContractDTO incomingContractDTO) {
        LocalDate localDate = LocalDate.now();
        String name = "J".equals(incomingContractDTO.getTipoPessoa())
                ? incomingContractDTO.getNomeRepresentanteLegal()
                : incomingContractDTO.getName();
        String job = "J".equals(incomingContractDTO.getTipoPessoa())
                ? incomingContractDTO.getOcupacaoRepresentanteLegal()
                : incomingContractDTO.getJob();
        String buyerAddress = "J".equals(incomingContractDTO.getTipoPessoa())
                ? incomingContractDTO
                .getEnderecoRepresentanteLegal()
                .concat(" - ")
                .concat(incomingContractDTO.getComplementoRepresentanteLegal())
                .concat(" - ")
                .concat(incomingContractDTO.getCepRepresentanteLegal())
                : incomingContractDTO
                .getAddress()
                .concat(" - ")
                .concat(incomingContractDTO.getComplement())
                .concat(" - ")
                .concat(incomingContractDTO.getZipCode());

        String estadoCivil = "J".equals(incomingContractDTO.getTipoPessoa())
                ? incomingContractDTO
                .getEstadoCivilRepresentanteLegal()
                : incomingContractDTO.getEstadoCivil();

        WorkArguments workArguments = new WorkArguments();
        workArguments.setSignerEmail(incomingContractDTO.getEmail());
        workArguments.setSignerName(name);
        workArguments.setBuyerName(name);
        workArguments.setBuyerAddress(buyerAddress);
        workArguments.setBuyerOccupation(job);
        workArguments.setContractDay(String.valueOf(localDate.getDayOfMonth()));
        workArguments.setContractMonth(String.valueOf(localDate.getMonth()));
        workArguments.setContractYear(String.valueOf(localDate.getYear()));
        workArguments.setBuyerAddress(buyerAddress);

        if ("J".equals(incomingContractDTO.getTipoPessoa())) {
            String companyAddress = incomingContractDTO
                    .getAddress()
                    .concat(" - ")
                    .concat(incomingContractDTO.getComplement())
                    .concat(" - ")
                    .concat(incomingContractDTO.getZipCode());

            workArguments.setCnpj(incomingContractDTO.getCnpj());
            workArguments.setCompanyAddres(companyAddress);
            workArguments.setCompanyCity(incomingContractDTO.getCity());
            workArguments.setCompanyState(incomingContractDTO.getState());
            workArguments.setCompanyZipCode(incomingContractDTO.getZipCode());
            workArguments.setCompanyName(incomingContractDTO.getName());
            workArguments.setConsumptionUnit(companyAddress);
            workArguments.setLegalRepAddress(buyerAddress);
            workArguments.setLegalRepCity(incomingContractDTO.getCidadeRepresentanteLegal());
            workArguments.setLegalRepCPF(incomingContractDTO.getCpfRepresentanteLegal());
            workArguments.setLegalRepCivilState(estadoCivil);
            workArguments.setLegalRepOccupation(job);
            workArguments.setLegalRepName(incomingContractDTO.getNomeRepresentanteLegal());
            workArguments.setLegalRepRG(incomingContractDTO.getRgRepresentanteLegal());
            workArguments.setLegalRepState(incomingContractDTO.getEstadoRepresentanteLegal());
            workArguments.setLegalRepZipCode(incomingContractDTO.getCepRepresentanteLegal());
        } else {
            workArguments.setCivilState(estadoCivil);
            workArguments.setRg(incomingContractDTO.getRg());
            workArguments.setCpf(incomingContractDTO.getCpf());
            workArguments.setCity(incomingContractDTO.getCity());
            workArguments.setState(incomingContractDTO.getState());
            workArguments.setZipCode(incomingContractDTO.getZipCode());
            workArguments.setConsumptionUnit(buyerAddress);
        }

        workArguments.setTipoPessoa(incomingContractDTO.getTipoPessoa());
        workArguments.setQuoteAmount(calculateQuotes(incomingContractDTO));

        return workArguments;
    }

    private String calculateQuotes(IncomingContractDTO incomingContractDTO) {
        Float quotes = 0F;
        quotes += Float.parseFloat(incomingContractDTO.getMonthOne());
        quotes += Float.parseFloat(incomingContractDTO.getMonthTwo());
        quotes += Float.parseFloat(incomingContractDTO.getMonthThree());
        quotes += Float.parseFloat(incomingContractDTO.getMonthFour());
        quotes += Float.parseFloat(incomingContractDTO.getMonthFive());
        quotes += Float.parseFloat(incomingContractDTO.getMonthSix());
        quotes += Float.parseFloat(incomingContractDTO.getMonthSeven());
        quotes += Float.parseFloat(incomingContractDTO.getMonthEight());
        quotes += Float.parseFloat(incomingContractDTO.getMonthNine());
        quotes += Float.parseFloat(incomingContractDTO.getMonthTen());
        quotes += Float.parseFloat(incomingContractDTO.getMonthEleven());
        quotes += Float.parseFloat(incomingContractDTO.getMonthTwelve());

        return String.valueOf(quotes / 6699490 * 120);
    }
}
