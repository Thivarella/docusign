package br.com.copernico.docusign.core.model;

import br.com.copernico.docusign.DSConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Locals {
    private DSConfiguration dsConfig;
    private String messages;
}
