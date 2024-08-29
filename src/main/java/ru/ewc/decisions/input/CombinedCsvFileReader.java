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

import java.net.URI;
import java.util.List;
import ru.ewc.decisions.api.ComputationContext;
import ru.ewc.decisions.api.DecisionTables;
import ru.ewc.decisions.api.Locator;
import ru.ewc.decisions.core.DecisionTable;

/**
 * I am the specific implementation of the {@link ContentsReader} for the combined CSV format. My
 * main responsibility is to manage all the file-related operations and transform the file lines
 * into a {@link DecisionTable}.
 *
 * @since 0.8.0
 */
public final class CombinedCsvFileReader implements ContentsReader {
    /**
     * The folder with source data files.
     */
    private final SourceFilesFolder folder;

    /**
     * The symbol that separates CSV-record fields.
     */
    private final String delimiter;

    /**
     * Ctor.
     *
     * @param dir The path to the source data files.
     * @param extension The extension of the source data files.
     * @param delimiter The symbol that separates CSV-record fields.
     */
    public CombinedCsvFileReader(final URI dir, final String extension, final String delimiter) {
        this.folder = new SourceFilesFolder(dir, extension);
        this.delimiter = delimiter;
    }

    /**
     * Reads all the source data and returns the {@link Locator}s collection, that can be used to
     * initialize the {@link ComputationContext}.
     *
     * @return A collection of {@link Locator}s , representing the contents of
     *  {@link DecisionTable}s data sources.
     */
    public DecisionTables allTables() {
        return new DecisionTables(this.readAll().stream()
            .map(RawContent::asDecisionTable)
            .toList()
        );
    }

    @Override
    public List<RawContent> readAll() {
        return this.folder.files()
            .stream()
            .map(
                file -> {
                    final SourceLines lines = SourceLines.fromLinesWithDelimiter(
                        file.asStrings(),
                        this.delimiter
                    );
                    return new RawContent(lines, file.nameWithoutExtension());
                }
            ).toList();
    }
}
