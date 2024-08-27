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

package ru.ewc.decisions.input;

import java.util.List;
import ru.ewc.decisions.core.BaseLocators;
import ru.ewc.decisions.core.DecisionTable;

/**
 * I am the specific implementation of the {@link DecisionReader} for the combined CSV format. My
 * main responsibility is to manage all the file-related operations and transform the file lines
 * into a {@link DecisionTable}.
 *
 * @since 0.8.0
 */
public final class CombinedCsvFileReader implements DecisionReader {
    /**
     * The symbol that separates CSV-record fields.
     */
    private final String delimiter;

    public CombinedCsvFileReader(final String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public BaseLocators allTables() {
        return null;
    }

    String[][] conditionsFrom(final List<String> lines) {
        return this.toArray(lines.stream()
            .filter(line -> line.startsWith("CND"))
            .map(line -> line.substring(4))
            .toList()
        );
    }

    private String[][] toArray(final List<String> lines) {
        final String[][] result;
        if (lines.isEmpty()) {
            result = new String[0][0];
        } else {
            final int width = lines.get(0).split(this.delimiter).length;
            result = new String[lines.size()][width];
            for (int idx = 0; idx < lines.size(); idx = idx + 1) {
                result[idx] = lines.get(idx).split(this.delimiter);
            }
        }
        return result;
    }
}
