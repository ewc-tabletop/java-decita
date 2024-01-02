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

import java.util.HashMap;
import java.util.Map;

/**
 * I am the container for all the things, required for TruthTable evaluation. My main responsibility
 * is to provide the set of {@link Locator}s in order to find all the required
 * {@link StateFragment}s.
 *
 * @since 0.1
 */
public class ComputationContext {
    /**
     * The set of all the available {@link Locator}s.
     */
    private final Map<String, Locator> locators;

    /**
     * Ctor.
     */
    public ComputationContext() {
        this.locators = new HashMap<>();
    }

    /**
     * Returns a concrete {@link Locator} if it's found in an instance storage.
     *
     * @param locator The String identifier of the required {@link Locator}.
     * @return The instance of {@link Locator}.
     * @throws DecitaException If the specified {@link Locator} is missing.
     */
    public Locator locatorFor(final String locator) throws DecitaException {
        if (this.locators.containsKey(locator)) {
            return this.locators.get(locator);
        }
        throw new DecitaException(
            String.format("Locator '%s' not found in computation context", locator)
        );
    }

    /**
     * Registers a new {@link Locator} with this {@link ComputationContext}.
     *
     * @param id The {@link Locator}'s identifier.
     * @param locator The {@link Locator}'s instance.
     */
    public void registerLocator(final String id, final Locator locator) {
        this.locators.put(id, locator);
    }
    // @todo #4 Refactor the Locator registration process, probably it can be done with fluent API
    // or something like that. Current mechanism is too cumbersome.
}