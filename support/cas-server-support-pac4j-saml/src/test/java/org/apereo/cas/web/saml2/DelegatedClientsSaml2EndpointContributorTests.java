package org.apereo.cas.web.saml2;

import org.apereo.cas.pac4j.client.DelegatedIdentityProviders;
import org.apereo.cas.support.pac4j.authentication.clients.DelegatedClientsEndpointContributor;
import org.apereo.cas.test.CasTestExtension;
import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pac4j.core.client.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link DelegatedClientsSaml2EndpointContributorTests}.
 *
 * @author Misagh Moayyed
 * @since 7.1.0
 */
@Tag("Delegation")
@SpringBootTest(classes = BaseSaml2DelegatedAuthenticationTests.SharedTestConfiguration.class)
@ExtendWith(CasTestExtension.class)
class DelegatedClientsSaml2EndpointContributorTests {
    @Autowired
    @Qualifier("delegatedClientsSaml2EndpointContributor")
    private DelegatedClientsEndpointContributor delegatedClientsSaml2EndpointContributor;

    @Autowired
    @Qualifier(DelegatedIdentityProviders.BEAN_NAME)
    private DelegatedIdentityProviders identityProviders;


    @Test
    void verifyOperation() throws Exception {
        val saml2Client = (BaseClient) identityProviders.findClient("SAML2Client").orElseThrow();
        assertTrue(delegatedClientsSaml2EndpointContributor.supports(saml2Client));
        val results = delegatedClientsSaml2EndpointContributor.contribute(saml2Client);
        assertFalse(results.isEmpty());
        assertTrue(results.containsKey("serviceProviderEntityId"));
        assertTrue(results.containsKey("identityProviderEntityId"));
        assertTrue(results.containsKey("identityProviderMetadata"));
    }
}
