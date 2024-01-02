/*
 * MIT License
 *
 * Copyright (c) 2023-2024 Eugene Terekhov
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

package ru.ewc.decita;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The tests for {@link ComputationContext}.
 *
 * @since 0.1
 */
class ComputationContextTest {
    @Test
    void shouldInstantiateWithLocators() throws DecitaException {
        final ComputationContext target = contextWithConstantLocator();
        final Locator actual = target.locatorFor("constant");
        MatcherAssert.assertThat(
            actual,
            Matchers.isA(ConstantLocator.class)
        );
    }

    @Test
    void shouldFindAFragment() throws DecitaException {
        final ComputationContext context = contextWithConstantLocator();
        final StateFragment actual = new Coordinate("constant", "true").fragmentFrom(context);
        MatcherAssert.assertThat(
            actual.asBoolean(),
            Matchers.is(true)
        );
    }

    @Test
    void shouldThrowIfLocatorIsNotFound() {
        final ComputationContext target = contextWithConstantLocator();
        Assertions.assertThrows(DecitaException.class, () -> target.locatorFor("non-existing"));
    }

    private static ComputationContext contextWithConstantLocator() {
        final ComputationContext target = new ComputationContext();
        new ConstantLocator().registerWith(target);
        return target;
    }
}