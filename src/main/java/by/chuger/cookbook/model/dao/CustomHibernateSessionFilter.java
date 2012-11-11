package by.chuger.cookbook.model.dao;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;

/** wrapper for opensession
 *  needs to delete object with FlushMode.COMMIT
 *  default value FlushMode.FALSE
 */
public class CustomHibernateSessionFilter extends OpenSessionInViewFilter {

    @Override
    protected Session getSession(SessionFactory sessionFactory)
                        throws DataAccessResourceFailureException {
        Session session = super.getSession(sessionFactory);
        session.setFlushMode(FlushMode.COMMIT);
        return session;
    }

    @Override
    protected void closeSession(Session session, SessionFactory factory) {
        session.flush();
        super.closeSession(session, factory);
    }
}