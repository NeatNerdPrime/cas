package org.apereo.cas.gauth.config;

import org.apereo.cas.gauth.BaseGoogleAuthenticatorTests;
import org.apereo.cas.test.CasTestExtension;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import lombok.Getter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link GoogleAuthenticatorConfigurationTests}.
 *
 * @author Misagh Moayyed
 * @since 6.2.0
 */
@SpringBootTest(classes = BaseGoogleAuthenticatorTests.SharedTestConfiguration.class)
@Getter
@Tag("MFAProvider")
@ExtendWith(CasTestExtension.class)
class GoogleAuthenticatorConfigurationTests {
    @Autowired
    @Qualifier("googleAuthenticatorInstance")
    private IGoogleAuthenticator googleAuthenticatorInstance;

    @Test
    void verifyOperation() throws Throwable {
        assertNotNull(googleAuthenticatorInstance);
    }
}
