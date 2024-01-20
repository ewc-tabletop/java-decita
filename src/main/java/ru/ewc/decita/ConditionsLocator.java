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
 * I am the {@link Locator} responsible for storing and retrieving all the {@link Condition}s known
 * to the engine.
 *
 * @since 0.1
 */
public final class ConditionsLocator implements Locator {
    // @todo #31 Remove the ability to locate Conditions from DecisionTables.

    /**
     * Collection of stored {@link Condition}s.
     */
    private final Map<String, Condition> conditions = new HashMap<>();

    @Override
    public String fragmentBy(final String fragment, final ComputationContext context)
        throws DecitaException {
        return String.valueOf(this.conditions.get(fragment).evaluate(context));
    }

    @Override
    public void registerWith(final ComputationContext context) {
        context.registerLocator(Locator.CONDITIONS, this);
    }

    /**
     * Stores a new {@link Condition} within this instance.
     *
     * @param name Name of the {@link Condition}.
     * @param condition The {@link Condition} itself.
     * @return Itself, in order to use fluent API.
     */
    public ConditionsLocator with(final String name, final SingleCondition condition) {
        this.conditions.put(name, condition);
        return this;
    }
}
