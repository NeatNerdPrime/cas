package org.apereo.cas.nativex;

import lombok.val;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;

/**
 * This is {@link CasGroovyRuntimeHintsTests}.
 *
 * @author Misagh Moayyed
 * @since 7.1.0
 */
@Tag("Native")
public class CasGroovyRuntimeHintsTests {
    @Test
    void verifyHints() throws Throwable {
        val hints = new RuntimeHints();
        new CasGroovyRuntimeHints().registerHints(hints, getClass().getClassLoader());
    }
}
