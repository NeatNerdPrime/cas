package org.apereo.cas.ticket.refreshtoken;

import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.ticket.ExpirationPolicy;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.apereo.cas.ticket.code.OAuth20DefaultCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An OAuth refresh token implementation.
 *
 * @author Jerome Leleu
 * @since 5.0.0
 */
@Getter
@NoArgsConstructor
public class OAuth20DefaultRefreshToken extends OAuth20DefaultCode implements OAuth20RefreshToken {

    private static final long serialVersionUID = -3544459978950667758L;

    /**
     * The ticket ids which are tied to this ticket.
     */
    private Set<String> accessTokens = new HashSet<>(0);

    public OAuth20DefaultRefreshToken(final String id, final Service service,
                                      final Authentication authentication,
                                      final ExpirationPolicy expirationPolicy,
                                      final TicketGrantingTicket ticketGrantingTicket,
                                      final Collection<String> scopes,
                                      final String codeChallenge,
                                      final String codeChallengeMethod,
                                      final String clientId,
                                      final String accessToken,
                                      final Map<String, Map<String, Object>> requestClaims) {
        super(id, service, authentication, expirationPolicy,
            ticketGrantingTicket, scopes,
            codeChallenge, codeChallengeMethod, clientId, requestClaims);
        this.accessTokens.add(accessToken);
    }

    public OAuth20DefaultRefreshToken(final String id, final Service service,
                                      final Authentication authentication,
                                      final ExpirationPolicy expirationPolicy,
                                      final TicketGrantingTicket ticketGrantingTicket,
                                      final Collection<String> scopes,
                                      final String clientId,
                                      final String accessToken,
                                      final Map<String, Map<String, Object>> requestClaims) {
        this(id, service, authentication, expirationPolicy,
            ticketGrantingTicket, scopes, null, null, clientId, accessToken, requestClaims);
    }

    @Override
    public String getPrefix() {
        return OAuth20RefreshToken.PREFIX;
    }
}
