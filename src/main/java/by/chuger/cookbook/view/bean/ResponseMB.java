package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.Feedback;
import by.chuger.cookbook.model.domain.UserAccount;
import by.chuger.cookbook.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.util.Calendar;

@Component
@Named("responseMB")
@Scope("request")
public class ResponseMB {

    @Autowired
    private Facade facade;
    private String email;
    private String response;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void sendResponse(ActionEvent e) {
        if (MessageUtils.isNotEmpty(response) ) {
            Feedback feedback = new Feedback();
            feedback.setResponse(response);
            feedback.setDateAdded(Calendar.getInstance().getTime());
            if (MessageUtils.isNotEmpty(email)) {
                feedback.setEmail(email);
            } else {
                feedback.setUserAccount(getUserAccount());
            }
            facade.createFeedback(feedback);
        }
    }

    private UserAccount getUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!validateRecipe(principal)) {
            return null;
        }
        UserDetails userDetails = (UserDetails) principal;
        return facade.getUserAccountByName(userDetails.getUsername());
    }

    private boolean validateRecipe(final Object principal) {
        boolean isValid = true;
        if (principal instanceof String) {
            isValid = false;
            MessageUtils.showMessage("Authentification problem", "You mus be logged in to rate recipe.");
        } else if (principal instanceof UserDetails == false) {
            isValid = false;
            MessageUtils.showMessage("Authentification problem", "Something wrong.");
        }
        return isValid;
    }
}
