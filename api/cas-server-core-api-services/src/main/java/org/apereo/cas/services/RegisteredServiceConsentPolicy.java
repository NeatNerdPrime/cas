package org.apereo.cas.services;

import org.apereo.cas.configuration.support.TriStateBoolean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.core.Ordered;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This is {@link RegisteredServiceConsentPolicy}.
 *
 * @author Misagh Moayyed
 * @since 5.2.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface RegisteredServiceConsentPolicy extends Serializable, Ordered {

    /**
     * Indicate whether consent is enabled.
     *
     * @return true/false/undefined
     */
    default TriStateBoolean getStatus() {
        return TriStateBoolean.UNDEFINED;
    }

    /**
     * Gets excluded services from this policy.
     * Useful when the policy is applied to a service definition
     * that is considered an aggregate of many other applications.
     *
     * @return the excluded services
     */
    default Set<String> getExcludedServices() {
        return null;
    }

    /**
     * Gets excluded attributes.
     * Excludes the set of specified attributes from consent.
     *
     * @return the excluded attributes
     */
    default Set<String> getExcludedAttributes() {
        return new LinkedHashSet<>();
    }

    /**
     * Gets include-only attributes.
     * If specified, consent should only be applied to the listed attributes
     * and not everything the attribute release policy may indicate.
     *
     * @return the include-only attributes. If the return collection is null or empty,
     * attribute release policy is consulted to determine all of included attributes.
     */
    default Set<String> getIncludeOnlyAttributes() {
        return new LinkedHashSet<>();
    }

    @Override
    default int getOrder() {
        return 0;
    }

    /**
     * Size.
     *
     * @return the int
     */
    @JsonIgnore
    default int size() {
        return 0;
    }

    @JsonIgnore
    default List<RegisteredServiceConsentPolicy> getPolicies() {
        return List.of(this);
    }
}
