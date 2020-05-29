package no.haakon.jotepad.kommando;

import no.haakon.jotepad.model.buffer.Buffer;
import no.haakon.jotepad.model.buffer.tabell.TabellBuffer;

import java.util.LinkedHashMap;

public class NyRadEtter implements Kommando {

    public static String NAVN = "ny-rad-etter";

    public String navn() {
        return NAVN;
    }
    public void kjør(Buffer buffer, LinkedHashMap<String, Object> args) {
        if(!(buffer instanceof TabellBuffer)) {
            System.err.println("Nåværende buffer er ikke en tabellbuffer.");
            return;
        }

        final TabellBuffer tabellBuffer = (TabellBuffer) buffer;
        final int valgtRad = tabellBuffer.getComponent().getSelectedRow();

        if(valgtRad < 0) {
            System.err.println("Ingen rad valgt");
            return;
        }

        tabellBuffer.getModell().settInnRad(valgtRad+1);

    }

    @Override
    public LinkedHashMap<String, Object> lesArgumenter(Buffer buffer) {
        return new LinkedHashMap<>();
    }
}
