package br.com.copernico.docusign.service;


import br.com.copernico.docusign.common.WorkArguments;
import br.com.copernico.docusign.core.common.DocumentType;
import br.com.copernico.docusign.core.controller.eSignature.EnvelopeHelpers;
import br.com.copernico.docusign.core.utils.CsvGenerator;
import br.com.copernico.docusign.dto.IncomingContractDTO;
import com.docusign.esign.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AcquisitionService {
    private static final String HTML_DOCUMENT_FILE_NAME_PJ = "templates/Contrato_de_Locacao_e_Adesao_ao_Condominio_Model_CNPJ.html";
    private static final String HTML_DOCUMENT_FILE_NAME_PF = "templates/Contrato_de_Locacao_e_Adesao_ao_Condominio_Model_PF.html";
    private static final String HTML_DOCUMENT_NAME = "Contrato de Adesão a Usina Solar Fotovoltaica e Outras Avenças";
    private static final int ANCHOR_OFFSET_Y = 6;
    private static final int ANCHOR_OFFSET_X = 1;
    @Value("${second.signer.name}")
    private String secondSignerName;
    @Value("${second.signer.email}")
    private String secondSignerEmail;
    @Value("${third.signer.name}")
    private String thirdSignerName;
    @Value("${third.signer.email}")
    private String thirdSignerEmail;
    @Value("${fourth.signer.name}")
    private String fourthSignerName;
    @Value("${fourth.signer.email}")
    private String fourthSignerEmail;
    @Value("${fifth.signer.name}")
    private String fifthSignerName;
    @Value("${fifth.signer.email}")
    private String fifthSignerEmail;

    @Value("${first.carbon.copy.name}")
    private String firstCcName;
    @Value("${first.carbon.copy.email}")
    private String firstCcEmail;

    @Value("${second.carbon.copy.name}")
    private String secondCcName;
    @Value("${second.carbon.copy.email}")
    private String secondCcEmail;

    public WorkArguments convertDTOToWorkArguments(IncomingContractDTO incomingContractDTO) throws IOException {
        LocalDate localDate = LocalDate.now();
        String name = "J".equals(incomingContractDTO.getTipoPessoa())
                ? incomingContractDTO.getNomeRepresentanteLegal()
                : incomingContractDTO.getName();
        String job = "J".equals(incomingContractDTO.getTipoPessoa())
                ? incomingContractDTO.getOcupacaoRepresentanteLegal()
                : incomingContractDTO.getJob();
        String buyerAddress = "J".equals(incomingContractDTO.getTipoPessoa()) ?
                Objects.nonNull(incomingContractDTO.getComplement()) && !StringUtils.isEmpty(incomingContractDTO.getComplementoRepresentanteLegal().trim())
                        ? incomingContractDTO.getEnderecoRepresentanteLegal().concat(" - ").concat(incomingContractDTO.getComplementoRepresentanteLegal())
                        : incomingContractDTO.getEnderecoRepresentanteLegal()
                : Objects.nonNull(incomingContractDTO.getComplement()) && !StringUtils.isEmpty(incomingContractDTO.getComplement().trim())
                ? incomingContractDTO.getAddress().concat(" - ").concat(incomingContractDTO.getComplement())
                : incomingContractDTO.getAddress();

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
        workArguments.setContractMonth(convertToBrazilianMonth(localDate.getMonthValue()));
        workArguments.setContractYear(String.valueOf(localDate.getYear()));
        workArguments.setBuyerAddress(buyerAddress);

        if ("J".equals(incomingContractDTO.getTipoPessoa())) {
            String companyAddress = Objects.nonNull(incomingContractDTO.getComplement())
                    && !StringUtils.isEmpty(incomingContractDTO.getComplement().trim()) ?
                    incomingContractDTO.getAddress().concat(" - ").concat(incomingContractDTO.getComplement()) :
                    incomingContractDTO.getAddress();

            workArguments.setCnpj(incomingContractDTO.getCnpj());
            workArguments.setCompanyAddres(companyAddress);
            workArguments.setCompanyCity(incomingContractDTO.getCity());
            workArguments.setCompanyState(incomingContractDTO.getState());
            workArguments.setCompanyZipCode(incomingContractDTO.getZipCode());
            workArguments.setCompanyName(incomingContractDTO.getSocialReason());
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
        workArguments.setQuoteAmount(incomingContractDTO.calculateQuotes());

        CsvGenerator.createCsv(new WorkArguments(), Stream.of(workArguments).map(WorkArguments::toString).collect(Collectors.toList()));
        return workArguments;
    }

    private String convertToBrazilianMonth(int monthValue) {
        switch (monthValue) {
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
            default:
                return "";
        }
    }

    public EnvelopeDefinition makeEnvelope(WorkArguments args) throws IOException {

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
        signer.setRecipientId("2");
        signer.setRoutingOrder("2");
        signer.setTabs(signerTabs);

        Signer secondSigner = new Signer();
        secondSigner.setEmail(secondSignerEmail);
        secondSigner.setName(secondSignerName);
        secondSigner.setRecipientId("3");
        secondSigner.setRoutingOrder("3");
        secondSigner.setTabs(secondSignerTabs);

        Signer thirdSigner = new Signer();
        thirdSigner.setEmail(thirdSignerEmail);
        thirdSigner.setName(thirdSignerName);
        thirdSigner.setRecipientId("1");
        thirdSigner.setRoutingOrder("1");
        thirdSigner.setTabs(thirdSignerTabs);

        Signer fourthSigner = new Signer();
        fourthSigner.setEmail(fourthSignerEmail);
        fourthSigner.setName(fourthSignerName);
        fourthSigner.setRecipientId("4");
        fourthSigner.setRoutingOrder("4");
        fourthSigner.setTabs(fourthSignerTabs);

        Signer fifthSigner = new Signer();
        fifthSigner.setEmail(fifthSignerEmail);
        fifthSigner.setName(fifthSignerName);
        fifthSigner.setRecipientId("5");
        fifthSigner.setRoutingOrder("5");
        fifthSigner.setTabs(fifthSignerTabs);

        CarbonCopy firstCc = new CarbonCopy();
        firstCc.setEmail(firstCcEmail);
        firstCc.setName(firstCcName);
        firstCc.setRecipientId("6");
        firstCc.setRoutingOrder("6");

        CarbonCopy secondCc = new CarbonCopy();
        secondCc.setEmail(secondCcEmail);
        secondCc.setName(secondCcName);
        secondCc.setRecipientId("7");
        secondCc.setRoutingOrder("7");

        byte[] htmlDocument = EnvelopeHelpers.createHtmlFromTemplateFile("J".equals(args.getTipoPessoa()) ?
                HTML_DOCUMENT_FILE_NAME_PJ : HTML_DOCUMENT_FILE_NAME_PF, "args", args);
        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("Bem vindo! Assine seu contrato com a Copérnico");
        envelope.setDocuments(Arrays.asList(
                EnvelopeHelpers.createDocument(htmlDocument, HTML_DOCUMENT_NAME,
                        DocumentType.HTML.getDefaultFileExtention(), "1")));
        envelope.setRecipients(new Recipients());
        envelope.getRecipients().setCarbonCopies(Arrays.asList(firstCc, secondCc));
        envelope.getRecipients().setSigners(Arrays.asList(signer, secondSigner, thirdSigner, fourthSigner, fifthSigner));
        envelope.setStatus("sent");

        return envelope;
    }
}
