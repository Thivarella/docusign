package br.com.copernico.docusign.core.controller.eSignature;

import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.core.controller.AbstractController;
import com.docusign.esign.api.AccountsApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;


/**
 * Abstract base class for all eSignature controllers.
 */
@Controller
public abstract class AbstractEsignatureController extends AbstractController {

    protected static final String MODEL_ENVELOPE_OK = "envelopeOk";

    protected AbstractEsignatureController(DSConfiguration config) {
        super(config);
    }

    /**
     * Creates new instance of the eSignature API client.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link ApiClient}
     */
    protected static ApiClient createApiClient(String basePath, String userAccessToken) {
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader(HttpHeaders.AUTHORIZATION, BEARER_AUTHENTICATION + userAccessToken);
        return apiClient;
    }

    /**
     * Creates a new instance of the eSignature EnvelopesApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link EnvelopesApi}
     */
    public static EnvelopesApi createEnvelopesApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new EnvelopesApi(apiClient);
    }

    /**
     * Creates a new instance of the eSignature AccountsApi. This method
     * creates an instance of the ApiClient class silently.
     *
     * @param basePath        URL to eSignature REST API
     * @param userAccessToken user's access token
     * @return an instance of the {@link AccountsApi}
     */
    protected static AccountsApi createAccountsApi(String basePath, String userAccessToken) {
        ApiClient apiClient = createApiClient(basePath, userAccessToken);
        return new AccountsApi(apiClient);
    }
}
