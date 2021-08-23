package br.com.copernico.docusign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomingContractDTO {

    @JsonProperty("cep")
    private String zipCode;

    @JsonProperty("cidade")
    private String city;

    private String cnpj;

    @JsonProperty("complemento")
    private String complement;

    private String rg;

    @JsonProperty("ocupacao")
    private String job;

    private String cpf;

    private String email;

    @JsonProperty("endereco")
    private String address;

    @JsonProperty("estado")
    private String state;

    @JsonProperty("nome")
    private String name;

    private String estadoCivil;

    @JsonProperty("razaoSocial")
    private String socialReason;

    @JsonProperty("telefone")
    private String phoneNumber;

    @JsonProperty("mes1")
    private String monthOne;

    @JsonProperty("mes2")
    private String monthTwo;

    @JsonProperty("mes3")
    private String monthThree;

    @JsonProperty("mes4")
    private String monthFour;

    @JsonProperty("mes5")
    private String monthFive;

    @JsonProperty("mes6")
    private String monthSix;

    @JsonProperty("mes7")
    private String monthSeven;

    @JsonProperty("mes8")
    private String monthEight;

    @JsonProperty("mes9")
    private String monthNine;

    @JsonProperty("mes10")
    private String monthTen;

    @JsonProperty("mes11")
    private String monthEleven;

    @JsonProperty("mes12")
    private String monthTwelve;

    @JsonProperty("mesAtual")
    private String currentMonth;

    @JsonProperty("tipoConexao")
    private String connectionType;

    private String tipoPessoa;
    private String estadoCivilRepresentanteLegal;
    private String cepRepresentanteLegal;
    private String cidadeRepresentanteLegal;
    private String complementoRepresentanteLegal;
    private String cpfRepresentanteLegal;
    private String enderecoRepresentanteLegal;
    private String estadoRepresentanteLegal;
    private String nomeRepresentanteLegal;
    private String ocupacaoRepresentanteLegal;
    private String rgRepresentanteLegal;

    private Boolean checkIfMonthValueAreNull() {
        return Objects.nonNull(this.monthOne) &&
                Objects.nonNull(this.monthTwo) &&
                Objects.nonNull(this.monthThree) &&
                Objects.nonNull(this.monthFour) &&
                Objects.nonNull(this.monthFive) &&
                Objects.nonNull(this.monthSix) &&
                Objects.nonNull(this.monthSeven) &&
                Objects.nonNull(this.monthEight) &&
                Objects.nonNull(this.monthNine) &&
                Objects.nonNull(this.monthTen) &&
                Objects.nonNull(this.monthEleven) &&
                Objects.nonNull(this.monthTwelve);
    }

    public String calculateQuotes() {
        DecimalFormat df = new DecimalFormat("#.######");

        float quotes = 0F;
        if (Boolean.FALSE.equals(this.checkIfMonthValueAreNull()))
            return df.format(quotes);
        quotes = Float.parseFloat(this.getMonthOne())
                + Float.parseFloat(this.getMonthTwo())
                + Float.parseFloat(this.getMonthThree())
                + Float.parseFloat(this.getMonthFour())
                + Float.parseFloat(this.getMonthFive())
                + Float.parseFloat(this.getMonthSix())
                + Float.parseFloat(this.getMonthSeven())
                + Float.parseFloat(this.getMonthEight())
                + Float.parseFloat(this.getMonthNine())
                + Float.parseFloat(this.getMonthTen())
                + Float.parseFloat(this.getMonthEleven())
                + Float.parseFloat(this.getMonthTwelve());

        return String.valueOf(df.format(((quotes / 12) / 6699490 * 12) * 100));
    }
}
