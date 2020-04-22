package no.haakon.jotepad.actions.search;

import no.haakon.jotepad.model.buffer.AbstractTekstBuffer;

import java.util.OptionalInt;

public class SimpleSearcher implements Searcher {

    private final AbstractTekstBuffer buffer;

    public SimpleSearcher(AbstractTekstBuffer editor) {
        this.buffer = editor;
    }

    @Override
    public void searchForward(String søkEtter) {
        final int posisjon = buffer.getEditor().getMarkørPosisjon();
        final String tekst = buffer.getEditor().getTekst();
        final int fra = tekst.indexOf(søkEtter, posisjon);
        if(fra < 0) {
            finnerIkkePopup();
            buffer.getComponent().setCaretPosition(posisjon);
            return;
        }
        final int til = fra + søkEtter.length();

        buffer.getComponent().setSelectionStart(fra);
        buffer.getComponent().setSelectionEnd(til);
    }

    @Override
    public void searchBackwards(String term) {
        final int posisjon = buffer.getEditor().getMarkørPosisjon();

        // Hvis en tekstbit er valgt, så søker vi FØR den valgte tekstbiten.
        int effektivPosisjon = posisjon - this.buffer.getEditor().getValgTekst().length();

        final String tekst = buffer.getEditor().getTekst().substring(0, effektivPosisjon);
        final int fra = tekst.lastIndexOf(term);
        final int til = fra + term.length();

        if(fra < 0) {
            finnerIkkePopup();
            buffer.getComponent().setCaretPosition(effektivPosisjon);
            return;
        }

        buffer.getComponent().setSelectionStart(fra);
        buffer.getComponent().setSelectionEnd(til);

    }


    private void finnerIkkePopup() {
        buffer.guiHelpers().popupInfo("Finner ikke", "Kan ikke finne teksten din");
    }

}
