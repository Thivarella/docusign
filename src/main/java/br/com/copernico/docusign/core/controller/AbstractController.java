package br.com.copernico.docusign.core.controller;

import br.com.copernico.docusign.DSConfiguration;
import br.com.copernico.docusign.common.WorkArguments;
import br.com.copernico.docusign.dto.IncomingContractDTO;
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
    protected final DSConfiguration config;

    public AbstractController(DSConfiguration config) {
        this.config = config;
    }

    @PostMapping
    public Object create(@RequestBody IncomingContractDTO incomingContractDTO, ModelMap model, HttpServletResponse response) {
        try {
            return doWork(incomingContractDTO, model, response);
        } catch (Exception exception) {
            return exception.getMessage();
        }
    }

    protected abstract Object doWork(IncomingContractDTO incomingContractDTO, ModelMap model,
                                     HttpServletResponse response) throws Exception;

}
