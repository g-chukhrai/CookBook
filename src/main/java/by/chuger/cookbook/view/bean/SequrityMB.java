package by.chuger.cookbook.view.bean;

import by.chuger.cookbook.model.dao.Facade;
import by.chuger.cookbook.model.domain.UserAccount;
import by.chuger.cookbook.model.domain.UserAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import resources.Text;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Named("sequrityMB")
@Scope("request")
public class SequrityMB implements Serializable {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Facade facade;

    private UserAccount userAccount = new UserAccount();
    private static final String LOGIN_COMPONENT_ID = "login";
    private UserDetails userDetails;

    public SequrityMB() {
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * Создает нового пользователя в базе данных
     */
    public String doRegister() throws IOException {
        String username = userAccount.getUsername();
        UserAccount userAccountByName = facade.getUserAccountByName(username);
        if (userAccountByName != null) {
            Text.addError(LOGIN_COMPONENT_ID, "menu.all");
            return null;
        }
        UserAuthority userRole = facade.getUserAuthorityById(UserAuthority.ROLE_USER);
        userAccount.setUserAuthority(userRole);
        userAccount.setPassword(passwordEncoder.encodePassword(userAccount.getPassword(), null));
        facade.createUser(userAccount);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/CookBook");

        return null;
    }

    /**
     * Обрабатывает запрос на аутентификацию.
     */
    public String doLogin() throws IOException, ServletException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        RequestDispatcher dispatcher =
                ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }

    /**
     * Выполняет завершение сеанса работы пользователя с системой.
     */
    public String doLogout() throws IOException, ServletException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        RequestDispatcher dispatcher =
                ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_logout");
        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }

    public UserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
