package org.apereo.cas;

import org.apereo.cas.authentication.CoreAuthenticationTestUtils;
import org.apereo.cas.authentication.attribute.PrincipalAttributeRepositoryFetcher;
import org.apereo.services.persondir.IPersonAttributeDao;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link PrincipalAttributeRepositoryFetcherCascadeTests}.
 *
 * @author Misagh Moayyed
 * @since 6.2.0
 */
@Tag("Attributes")
class PrincipalAttributeRepositoryFetcherTests {
    @SpringBootTest(classes = BasePrincipalAttributeRepositoryTests.SharedTestConfiguration.class,
        properties = {
            "cas.authn.attribute-repository.json[0].location=classpath:/json-attribute-repository.json",
            "cas.authn.attribute-repository.json[0].order=1",
            "cas.authn.attribute-repository.json[0].id=JSON",

            "cas.authn.attribute-repository.groovy[0].location=classpath:/GroovyAttributeRepository.groovy",
            "cas.authn.attribute-repository.groovy[0].order=2",
            "cas.authn.attribute-repository.groovy[0].id=GROOVY",

            "cas.authn.attribute-repository.core.require-all-repository-sources=true"
        })
    static class BaseTests {
        @Autowired
        @Qualifier("aggregatingAttributeRepository")
        protected IPersonAttributeDao aggregatingAttributeRepository;
    }

    @Nested
    @SuppressWarnings("ClassCanBeStatic")
    class DefaultTests extends BaseTests {
        @Test
        void verifyOperation() {
            val attributes = PrincipalAttributeRepositoryFetcher.builder()
                .attributeRepository(aggregatingAttributeRepository)
                .principalId("casuser-whatever")
                .currentPrincipal(CoreAuthenticationTestUtils.getPrincipal("current-cas"))
                .build()
                .retrieve();
            assertNotNull(attributes);
            assertTrue(attributes.isEmpty());
        }
    }


    @TestPropertySource(properties = "cas.person-directory.active-attribute-repository-ids=")
    @Nested
    @SuppressWarnings("ClassCanBeStatic")
    class NoActiveRepositoryTests extends BaseTests {
        @Test
        void verifyOperation() {
            val attributes = PrincipalAttributeRepositoryFetcher.builder()
                .attributeRepository(aggregatingAttributeRepository)
                .principalId("friabili")
                .build()
                .retrieve();
            assertNotNull(attributes);
            assertTrue(attributes.isEmpty());
        }
    }

    @TestPropertySource(properties = "cas.person-directory.active-attribute-repository-ids=GROOVY,JSON")
    @Nested
    @SuppressWarnings("ClassCanBeStatic")
    class SelectiveRepositoryTests extends BaseTests {
        @Test
        void verifyOperation() {
            val attributes = PrincipalAttributeRepositoryFetcher.builder()
                .attributeRepository(aggregatingAttributeRepository)
                .principalId("friabili")
                .activeAttributeRepositoryIdentifiers(Set.of("GROOVY"))
                .build()
                .retrieve();
            assertNotNull(attributes);
            assertTrue(attributes.containsKey("groovyNewName"));
        }
    }

}
