package no.haakon.jotepad.old.gui.components.inputlister;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.kommando.Kommando;
import no.haakon.jotepad.old.model.buffer.Buffer;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Kommandoliste extends RegexNarrowingInputList<Kommando> {

    public Kommandoliste(Collection<Kommando> elementerTilListen, ApplicationFrame frame) {
        super("Kjør kommando", elementerTilListen, frame);
    }

    @Override
    protected void gjørValg() {
        Buffer buffer = this.frame.synligBuffer();
        Optional<Kommando> valgtElement = getValgtElement();
        if(valgtElement.isEmpty()) {
            System.out.println("Ingenting valgt");
        }
        this.lukkVindu();
        valgtElement.ifPresent(kommando -> kommando.kjør(buffer, kommando.lesArgumenter(buffer)));
    }

    @Override
    protected Optional<Pattern> mønsterFra(String tekst) {
        try {
            return Optional.of(Pattern.compile(tekst.replace("-", ".*-.*")));
        }
        catch(PatternSyntaxException pse) {
            System.err.println("Kan ikke matche mot mønster '" + tekst + "'");
            return Optional.empty();
        }
    }

    @Override
    protected boolean matchMotMønster(Pattern mønster, Kommando element) {
        return mønster.matcher(element.navn()).find();
    }

    @Override
    protected String stringRepresentasjon(Kommando element) {
        return element.navn();
    }

    @Override
    protected int oppdateringGrenseverdi() {
        return 0;
    }
}
