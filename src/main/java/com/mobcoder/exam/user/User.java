package com.mobcoder.exam.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobcoder.exam.base.AuditModel;
import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity(name = "user")
@Table(name = "user")
public class User extends AuditModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    @Email
    @Size(max = 200)
    @Column(name = "username", unique = true)
    public String username;

    @NotNull
    @Size(max = 8, min = 6)
    @Column(name = "password")
    public String password;

    public boolean isAdmin;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> f = new ArrayList<>();
        f.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "USER";
            }
        });
        return f;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
