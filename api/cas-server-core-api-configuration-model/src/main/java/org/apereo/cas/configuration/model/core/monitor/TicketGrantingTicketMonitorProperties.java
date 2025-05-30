package org.apereo.cas.configuration.model.core.monitor;

import org.apereo.cas.configuration.support.RequiresModule;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * This is {@link TicketGrantingTicketMonitorProperties}.
 *
 * @author Misagh Moayyed
 * @since 6.4.0
 */
@RequiresModule(name = "cas-server-core-monitor", automated = true)
@Getter
@Setter
@Accessors(chain = true)
public class TicketGrantingTicketMonitorProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -2756454350350278724L;

    /**
     * Warning options for monitoring TGT production.
     */
    @NestedConfigurationProperty
    private MonitorWarningProperties warn = new MonitorWarningProperties(10000);
}

