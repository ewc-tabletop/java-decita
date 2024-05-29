/*
 * MIT License
 *
 * Copyright (c) 2024 Eugene Terekhov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.ewc.decisions.api;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import ru.ewc.decisions.core.InMemoryLocator;
import ru.ewc.decisions.core.RequestLocator;
import ru.ewc.decisions.input.PlainTextDecisionReader;
import ru.ewc.state.StoredState;

/**
 * Full scenario tests.
 *
 * @since 0.1
 */
final class EndToEndTest {
    /**
     * Constant for the table outcome field.
     */
    public static final String OUTCOME = "outcome";

    /**
     * Constant for the empty incoming request data.
     */
    public static final RequestLocator EMPTY_REQ = new RequestLocator(InMemoryLocator.empty());

    @Test
    void shouldComputeTheWholeTable() throws DecitaException {
        final DecitaFacade facade = EndToEndTest.facadeWithState(
            new StoredState(
                Map.of(
                    "data", new InMemoryLocator(Map.of("is-stored", "true")),
                    "market", new InMemoryLocator(Map.of("shop", 2)),
                    "currentPlayer", new InMemoryLocator(Map.of("name", "Eugene"))
                )
            )
        );
        MatcherAssert.assertThat(
            "The table is computed correctly",
            facade.decisionFor("sample-table", EndToEndTest.EMPTY_REQ),
            Matchers.allOf(
                Matchers.hasEntry(Matchers.equalTo(EndToEndTest.OUTCOME), Matchers.equalTo("true")),
                Matchers.hasEntry(Matchers.equalTo("text"), Matchers.equalTo("hello world"))
            )
        );
    }

    @Test
    void shouldComputeTheWholeTableWithElseRule() throws DecitaException {
        final DecitaFacade facade = EndToEndTest.facadeWithState(
            new StoredState(
                Map.of(
                    "data", new InMemoryLocator(Map.of("is-stored", false)),
                    "market", new InMemoryLocator(Map.of("shop", 3)),
                    "currentPlayer", new InMemoryLocator(Map.of("name", "Eugene"))
                )
            )
        );
        MatcherAssert.assertThat(
            "The table is computed correctly",
            facade.decisionFor("sample-table", EndToEndTest.EMPTY_REQ),
            Matchers.allOf(
                Matchers.hasEntry(Matchers.equalTo(EndToEndTest.OUTCOME), Matchers.equalTo("else")),
                Matchers.hasEntry(Matchers.equalTo("text"), Matchers.equalTo("no rule satisfied"))
            )
        );
    }

    @Test
    void shouldThrowIfSeveralRulesResolveToTrue() {
        final DecitaFacade facade = EndToEndTest.facadeWithState(
            new StoredState(
                Map.of(
                    "data", new InMemoryLocator(Map.of("value", 1))
                )
            )
        );
        Assertions
            .assertThatThrownBy(() -> facade.decisionFor("multiple-rules", EndToEndTest.EMPTY_REQ))
            .isInstanceOf(DecitaException.class)
            .hasMessageContaining("Multiple rules are satisfied");
    }

    private static URI tablesDirectory() {
        return Path.of(
            Paths.get("").toAbsolutePath().toString(),
            "src/test/resources/tables"
        ).toUri();
    }

    private static DecitaFacade facadeWithState(final StoredState state) {
        return new DecitaFacade(
            new PlainTextDecisionReader(tablesDirectory(), ".csv", ";")::allTables,
            state
        );
    }

}