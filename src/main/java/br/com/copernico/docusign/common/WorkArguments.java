package br.com.copernico.docusign.common;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Data
public class WorkArguments {

    private String signerName;
    private String signerEmail;
    private String companyName;
    private String companyCity;
    private String companyState;
    private String city;
    private String state;
    private String companyAddres;
    private String companyZipCode;
    private String zipCode;
    private String cnpj;
    private String rg;
    private String cpf;
    private String legalRepName;
    private String legalRepCivilState;
    private String legalRepOccupation;
    private String legalRepRG;
    private String legalRepCPF;
    private String legalRepCity;
    private String legalRepState;
    private String legalRepAddress;
    private String legalRepZipCode;
    private String quoteAmount;
    private String consumptionUnit;
    private String buyerName;
    private String buyerAddress;
    private String buyerOccupation;
    private String contractDay;
    private String contractMonth;
    private String contractYear;
    private String civilState;
    private String tipoPessoa;
}
