package com.reservation.application.domain.entity;

import com.reservation.common.base.NumericJpaIdentifier;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class AppUser extends NumericJpaIdentifier {

    @EqualsAndHashCode.Include
    private String name;

    @EqualsAndHashCode.Include
    private String username;

    @EqualsAndHashCode.Include
    private String email;

    private String login;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<AppRole> roles;

    @Builder
    public AppUser(Long id, String name, String username, String email, String login, String password) {
        super(id);
        this.name = name;
        this.username = username;
        this.email = email;
        this.login = login;
        this.password = password;
    }
}
