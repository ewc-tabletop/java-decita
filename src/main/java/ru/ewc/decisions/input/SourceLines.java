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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record SourceLines(Map<String, List<String>> lines, String delimiter) {
    static SourceLines fromLinesWithDelimiter(final List<String> lines, final String delimiter) {
        return new SourceLines(
            Stream
                .of("CND", "OUT", "ASG")
                .collect(Collectors.toMap(s -> s, s -> filteredList(s, lines))),
            delimiter
        );
    }

    public String[][] asArrayOf(final String key) {
        return this.toArray(this.lines.getOrDefault(key, List.of()));
    }

    private static List<String> filteredList(final String key, final List<String> source) {
        return source.stream()
            .filter(line -> line.startsWith(key))
            .map(line -> line.substring(key.length() + 1))
            .toList();
    }

    private String[][] toArray(final List<String> source) {
        final String[][] result;
        if (source.isEmpty()) {
            result = new String[0][0];
        } else {
            final int width = source.get(0).split(this.delimiter).length;
            result = new String[source.size()][width];
            for (int idx = 0; idx < source.size(); idx = idx + 1) {
                result[idx] = source.get(idx).split(this.delimiter);
            }
        }
        return result;
    }
}