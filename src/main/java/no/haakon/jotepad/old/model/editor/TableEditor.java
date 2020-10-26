package no.haakon.jotepad.old.model.editor;

import no.haakon.jotepad.old.model.buffer.tabell.TabellBuffer;

/**
 * Denne editoren lar deg gjøre endringer på CSV-tabeller.
 * Det er meningen at du skal kunne gjøre de fleste tekstuelle operasjonene som er forbundet med tabeller her.
 * Det vil si:
 * <ul>
 * <li>Legge til og slette rader og kolonner</li>
 * <li>Redigere kolonnenavnene</li>
 * <li>Flytte rundt på koloner og rader</li>
 * <li>Redigere felt (støttet per default)</li>
 * </ul>
 */
public class TableEditor extends Editor {
    private final TabellBuffer buffer;

    public TableEditor(TabellBuffer tabellBuffer) {
        super();
        this.buffer = tabellBuffer;
    }

    @Override
    protected TabellBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void undo() {
        System.err.println("Undo er ikke implementert enda!");
    }

    /**
     * Legger til en ny kolonne, med oppgitt navn. Den blir lagt til på kolonnen som er oppgitt, og eksisterende kolonner blir skubbet et hakk til høyre.
     * @param navnKolonne
     * @param kolonneposisjon
     */
    public void leggTilKolonne(String navnKolonne, int kolonneposisjon) {
        buffer.getModell().settInnKolonne(navnKolonne, kolonneposisjon);
    }
}
