package br.com.copernico.docusign.core.controller;

import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.common.WorkArguments;
import br.com.copernico.docusign.core.model.Session;
import br.com.copernico.docusign.dto.IncomingContractDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;


/**
 * Abstract base class for all controllers. It handles all requests which are
 * registered by the derived classes and delegates an example specific action
 * back to the appropriate controllers. If a user had not been authorized, it
 * redirects him onto an authentication page. Derived classes must override an
 * abstract method {@link #doWork(WorkArguments, ModelMap, HttpServletResponse)}
 * to do something. This method is called from the POST request. Optionally you
 * can override method {@link #onInitModel(WorkArguments, ModelMap)} to add
 * example specific attributes into a page.
 */
@Controller
public abstract class AbstractController {

    protected static final String BEARER_AUTHENTICATION = "Bearer ";
    protected static final String DONE_EXAMPLE_PAGE = "pages/example_done";
    protected static final String DONE_EXAMPLE_PAGE_COMPARE = "pages/example_done_compare";
    protected static final String ERROR_PAGE = "error";
    private static final String REDIRECT_PREFIX = "redirect:";
    protected static final String REDIRECT_AUTHENTICATION_PAGE = REDIRECT_PREFIX + "/ds/mustAuthenticate";
    protected final DSConfiguration config;
    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;
    @Autowired
    private Session session;

    public AbstractController(DSConfiguration config) {
        this.config = config;
    }

    @PostMapping
    public Object create(@RequestBody IncomingContractDTO incomingContractDTO, ModelMap model, HttpServletResponse response) {
        if (isTokenExpired()) {
            return REDIRECT_AUTHENTICATION_PAGE;
        }

        try {
            return doWork(incomingContractDTO, model, response);
        } catch (Exception exception) {
            return exception.getMessage();
        }
    }

    protected abstract Object doWork(IncomingContractDTO incomingContractDTO, ModelMap model,
                                     HttpServletResponse response) throws Exception;

    private boolean isTokenExpired() {
        OAuth2AccessToken accessToken = oAuth2ClientContext.getAccessToken();
        boolean tokenExpired = accessToken != null && accessToken.isExpired();
        session.setRefreshToken(tokenExpired);
        return tokenExpired;
    }
}
