package com.reservation.web.model;

import com.reservation.common.base.NumericIdentifier;
import lombok.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserApi extends NumericIdentifier {

    private String name;

    private String username;

    private String email;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private String password;

    @Builder
    public UserApi(Long id, String name, String username, String email, String password) {
        super(id);
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
