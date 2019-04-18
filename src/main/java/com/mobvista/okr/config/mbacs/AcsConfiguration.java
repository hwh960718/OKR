package com.mobvista.okr.config.mbacs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class AcsConfiguration {

    private final Logger log = LoggerFactory.getLogger(AcsConfiguration.class);

    /**
     * acs配置
     */
    private final AcsConfiguration.Acs acs = new AcsConfiguration.Acs();

    public static class Acs {

        private String code = "UTF-8";

        private String accountDomain;

        private String clientId;

        private String clientSecret;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getAccountDomain() {
            return accountDomain;
        }

        public void setAccountDomain(String accountDomain) {
            this.accountDomain = accountDomain;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }

    public Acs getAcs() {
        return acs;
    }
}
