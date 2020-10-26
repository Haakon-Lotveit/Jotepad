package no.haakon.jotepad.old.model.editor;

import no.haakon.jotepad.old.model.GuiHelpers;
import no.haakon.jotepad.old.model.buffer.Buffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * En editor er en klasse som tilbyr forskjellige kommandoer som kan utføres på et dokument det er assosiert med.
 * Forskjellige editorer tilbyr forskjellige sett med kommandoer. En editor for en tekstfil vil la deg søke gjennom tekst. en editor for et bilde vil ikke kunne gjøre dette.
 * Hele denne klassen er ikke ferdig designet enda.
 */
public abstract class Editor {

    protected abstract Buffer getBuffer();

    public void lagreSom(File fil) {
        Buffer buffer = getBuffer();
        GuiHelpers guiHelpers = buffer.guiHelpers();

        if (!fil.canWrite()) {
            guiHelpers.logg("Har ikke skriverettigheter til " + fil.getAbsolutePath());
            String feilmelding = String.format("Har ikke skriverettigheter til fil %s. Kan ikke lagre.", fil.getAbsolutePath());
            guiHelpers.popupError("Kan ikke lagre", feilmelding);
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(fil)) {
            try {
                fos.write(buffer.innholdSomBytes());
            } catch (IOException ioe) {
                guiHelpers.logg("Kan ikke skrive data:\n" + ioe);
                guiHelpers.popupError("Feil under lagring", "Kan ikke skrive data.");
            }
        } catch (FileNotFoundException fnfe) {
            guiHelpers.logg("Finner ikke filen.");
            guiHelpers.popupError("Finner ikke filen", String.format("Fil %s finnes ikke", fil.getAbsolutePath()));
        } catch (IOException ioe) {
            guiHelpers.logg("En generisk io-feil oppstod:\n" + ioe);
            guiHelpers.popupError("Feil under lagring", "En ukjent feil oppstod under lagring");
        }
    }
    public abstract void undo();

    public void lagre() {
        Optional<File> sjekkFil = getBuffer().getFil();
        if(sjekkFil.isEmpty()) {
            getBuffer().guiHelpers().logg("Ingen fil å lagre.");
            return;
        }
        getBuffer().getFil().ifPresent(this::lagreSom);
    };
}
