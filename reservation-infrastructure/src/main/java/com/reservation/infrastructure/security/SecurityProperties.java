package com.reservation.infrastructure.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("reservation-api.security")
@Data
public class SecurityProperties {

    private Jwt jwt;

    private Login login;

    private String authorizationErrorHeader;

    @Data
    public static class Jwt {

        private String secret;

        private String prefixHeader;

        private String authoritiesClaim;

        private Token accessToken;

        private Token refreshToken;

        public String getPrefixHeader() {
            return prefixHeader + " ";
        }

        @Data
        public static class Token {

            private Integer expiry;

            private String header;
        }
    }

    @Data
    public static class Login {

        private String url;

        private String usernameParameter;

        private String passwordParameter;
    }
}
