package no.haakon.jotepad.model.buffer.tabell;

import com.opencsv.*;

import java.io.File;
import java.io.Reader;
import java.io.Writer;

public class CsvIoInnstillinger {
    private final char escapeChar;
    private final String lineEnd;
    private final int linesSkipped;
    private final char quoteChar;
    private final char separator;
    private final boolean strictQuotes;

    public char escapeChar() {
        return escapeChar;
    }

    public String lineEnd() {
        return lineEnd;
    }

    public int linesSkipped() {
        return linesSkipped;
    }

    public char quoteChar() {
        return quoteChar;
    }

    public char separator() {
        return separator;
    }

    public boolean strictQuotes() {
        return strictQuotes;
    }

    // Ikke bruk denne. Bruk builderen.
    private CsvIoInnstillinger(char escapeChar, String lineEnd, int linesSkipped, char quoteChar, char separator, boolean strictQuotes) {
        this.escapeChar = escapeChar;
        this.lineEnd = lineEnd;
        this.linesSkipped = linesSkipped;
        this.quoteChar = quoteChar;
        this.separator = separator;
        this.strictQuotes = strictQuotes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CSVReader lagReader(Reader reader) {
        return new CSVReaderBuilder(reader)
                .withCSVParser(new CSVParserBuilder()
                                       .withEscapeChar(escapeChar())
                                       .withQuoteChar(quoteChar())
                                       .withSeparator(separator())
                                       .withStrictQuotes(strictQuotes())
                                       .build())
                .withSkipLines(linesSkipped())
                .build();
    }

    public ICSVWriter lagWriter(Writer writer) {
        return new CSVWriterBuilder(writer)
                .withEscapeChar(escapeChar())
                .withLineEnd(lineEnd())
                .withQuoteChar(quoteChar())
                .withSeparator(separator())
                .build();
    }

    public static class Builder {
        private Builder() {
        }

        // Vi prøver å ha rimelige standarder her.
        private char escapeChar = '\'';
        private String lineEnd = "\r\n"; // Windows sine linjeskift er standard i RFC-4180
        private int linesSkipped = 0;
        private char quoteChar = '"';
        private char separator = ',';
        private boolean strictQuotes = false;

        public Builder escapeChar(char escapeChar) {
            this.escapeChar = escapeChar;
            return this;
        }

        public Builder lineEnd(String lineEnd) {
            this.lineEnd = lineEnd;
            return this;
        }

        public Builder linesSkipped(int linesSkipped) {
            this.linesSkipped = linesSkipped;
            return this;
        }

        public Builder quoteChar(char quoteChar) {
            this.quoteChar = quoteChar;
            return this;
        }

        public Builder separator(char separator) {
            this.separator = separator;
            return this;
        }

        public Builder strictQuotes(boolean strictQuotes) {
            this.strictQuotes = strictQuotes;
            return this;
        }

        public CsvIoInnstillinger build() {
            return new CsvIoInnstillinger(escapeChar, lineEnd, linesSkipped, quoteChar, separator, strictQuotes);
        }
    }

}
