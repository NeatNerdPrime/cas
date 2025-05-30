package org.apereo.cas.shell.commands.saml;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.support.saml.OpenSamlConfigBean;
import org.apereo.cas.support.saml.idp.metadata.generator.FileSystemSamlIdPMetadataGenerator;
import org.apereo.cas.support.saml.idp.metadata.generator.SamlIdPMetadataGeneratorConfigurationContext;
import org.apereo.cas.support.saml.idp.metadata.locator.FileSystemSamlIdPMetadataLocator;
import org.apereo.cas.support.saml.idp.metadata.writer.DefaultSamlIdPCertificateAndKeyWriter;
import org.apereo.cas.util.crypto.CipherExecutor;
import org.apereo.cas.util.function.FunctionUtils;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * This is {@link GenerateSamlIdPMetadataCommand}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@ShellCommandGroup("SAML")
@ShellComponent
@Slf4j
public class GenerateSamlIdPMetadataCommand {
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    @Qualifier("shibboleth.VelocityEngine")
    private VelocityEngine velocityEngineFactoryBean;

    @Autowired
    @Qualifier(OpenSamlConfigBean.DEFAULT_BEAN_NAME)
    private OpenSamlConfigBean openSamlConfigBean;

    /**
     * Generate saml2 idp metadata at the specified location.
     *
     * @param metadataLocation the metadata location
     * @param entityId         the entity id
     * @param serverPrefix     the server prefix
     * @param scope            the scope
     * @param force            force generation of metadata
     * @param subjectAltNames  additional subject alternative names for cert (besides entity id)
     * @throws Throwable the throwable
     */
    @ShellMethod(key = "generate-idp-metadata", value = "Generate SAML2 IdP Metadata")
    public void generate(
        @ShellOption(value = {"metadataLocation", "--metadataLocation"},
            help = "Directory location to hold metadata and relevant keys/certificates",
            defaultValue = "/etc/cas/saml") final String metadataLocation,
        @ShellOption(value = {"entityId", "--entityId"},
            help = "Entity ID to use for the generated metadata",
            defaultValue = "cas.example.org") final String entityId,
        @ShellOption(value = {"hostName", "--hostName"},
            help = "CAS server prefix to be used at the IdP host name when generating metadata",
            defaultValue = "https://cas.example.org/cas") final String serverPrefix,
        @ShellOption(value = {"scope", "--scope"},
            help = "Scope to use when generating metadata",
            defaultValue = "example.org") final String scope,
        @ShellOption(value = {"force", "--force"},
            help = "Force metadata generation (XML only, not certs), overwriting anything at the specified location") final boolean force,
        @ShellOption(value = {"subjectAltNames", "--subjectAltNames"},
            help = "Comma separated list of other subject alternative names for the certificate (besides entityId)",
            defaultValue = StringUtils.EMPTY) final String subjectAltNames) throws Throwable {

        val locator = new FileSystemSamlIdPMetadataLocator(CipherExecutor.noOpOfStringToString(),
            new File(metadataLocation),
            Caffeine.newBuilder().initialCapacity(1).maximumSize(1).build(),
            applicationContext);
        val writer = new DefaultSamlIdPCertificateAndKeyWriter(entityId);
        if (StringUtils.isNotBlank(subjectAltNames)) {
            writer.setUriSubjectAltNames(List.of(StringUtils.split(subjectAltNames, ",")));
        }

        val generateMetadata = FunctionUtils.doIf(locator.exists(Optional.empty()),
            () -> Boolean.TRUE,
            () -> {
                LOGGER.warn("Metadata artifacts are available at the specified location [{}]", metadataLocation);
                return force;
            }).get();

        if (generateMetadata) {
            val props = new CasConfigurationProperties();
            props.getAuthn().getSamlIdp().getCore().setEntityId(entityId);
            props.getServer().setScope(scope);
            props.getServer().setPrefix(serverPrefix);
            
            val context = SamlIdPMetadataGeneratorConfigurationContext.builder()
                .samlIdPMetadataLocator(locator)
                .samlIdPCertificateAndKeyWriter(writer)
                .applicationContext(applicationContext)
                .casProperties(props)
                .metadataCipherExecutor(CipherExecutor.noOpOfStringToString())
                .openSamlConfigBean(openSamlConfigBean)
                .velocityEngine(velocityEngineFactoryBean)
                .build();

            val generator = new FileSystemSamlIdPMetadataGenerator(context);
            generator.initialize();
            generator.generate(Optional.empty());
            LOGGER.info("Generated metadata is available at [{}]", locator.resolveMetadata(Optional.empty()));
        } else {
            LOGGER.info("No metadata was generated; it might already exist at the specified path");
        }
    }
}
