package org.apereo.cas.configuration.model.support.surrogate;

import org.apereo.cas.configuration.support.RequiresModule;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * This is {@link SurrogateAuthenticationTicketGrantingTicketProperties}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
@RequiresModule(name = "cas-server-support-surrogate-webflow")
@Getter
@Setter
@Accessors(chain = true)
public class SurrogateAuthenticationTicketGrantingTicketProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = 2077366413438267330L;

    /**
     * Timeout in seconds to kill the surrogate session and consider tickets expired.
     */
    private long timeToKillInSeconds = 1_800;
}
