package no.haakon.jotepad.old.model.buffer.tekst;

import java.io.IOException;
import java.io.OutputStream;

public class LoggBufferOutputStream extends OutputStream {
    private static final char newline = '\n';
    private static final int bitmask = 0x00_00_00_FF; // Hvis du ander denne bitmasken fjerner du alt annet enn de første 8 bitsene.
    StringBuilder buffer = new StringBuilder();
    LoggEditor editor;

    public LoggBufferOutputStream(LoggBuffer loggBuffer) {
        this(loggBuffer.getEditor());
    }

    public LoggBufferOutputStream(LoggEditor editor) {
        this.editor = editor;
    }

    @Override
    public void write(int b) throws IOException {
        final char tegnet = (char) (b & bitmask); // null ut alt untatt siste byte, cast resultatet til char.

        if(tegnet == newline) {
            editor.melding(buffer.toString());
            buffer = new StringBuilder();
            // Vi er ferdig med å akkumulere, og skal skrive ut.
        } else {
            buffer.append(tegnet);
        }

    }


}
