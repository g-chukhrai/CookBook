package by.chuger.cookbook.model.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_authority", catalog = "cookbook")
public class UserAuthority implements GrantedAuthority, Serializable {

    public static final Integer ROLE_USER = 1;
    public static final Integer ROLE_ADMIN = 2;
    private Integer id;
    private String authority;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_authority_id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    @Column(name="authority", length = 50, nullable = false, unique = true)
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
