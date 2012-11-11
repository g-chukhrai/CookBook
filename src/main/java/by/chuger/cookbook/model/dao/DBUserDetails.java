package by.chuger.cookbook.model.dao;

import by.chuger.cookbook.model.domain.UserAccount;
import by.chuger.cookbook.model.domain.UserDetailsHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DBUserDetails implements UserDetailsService {

    private Facade facade;

    public DBUserDetails(Facade facade) {
        this.facade = facade;
    }

    /**
     * Получает данные пользователя из базы данных
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        UserDetailsHelper helper = null;
        if (username != null) {
            UserAccount userAccountByName = facade.getUserAccountByName(username);
            if (userAccountByName != null) {
                helper = new UserDetailsHelper(userAccountByName);
            }
        }
        return helper;
    }
}
