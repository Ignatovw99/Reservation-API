package com.reservation.application.domain.entity;

import com.reservation.common.base.NumericJpaIdentifier;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUser extends NumericJpaIdentifier {

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–]).{8,35}$";

    private String name;

    private String username;

    private String email;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<AppRole> roles;

    @Builder
    public AppUser(Long id, String name, String username, String email, String password) {
        super(id);
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getLogin() {
        return Objects.isNull(username)
                ? email
                : username;
    }
}
