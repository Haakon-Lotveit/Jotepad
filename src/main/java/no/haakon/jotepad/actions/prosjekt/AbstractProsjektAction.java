package no.haakon.jotepad.actions.prosjekt;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class AbstractProsjektAction extends AbstractJotepadAction {

    public static final String INDEX_ROOT = "FILINDEX";
    public static final String INDEKSERT_MAPPE = "MAPPE";
    public static final String NØKKEL_INDEKSERT_MAPPE = INDEX_ROOT + "_" + INDEKSERT_MAPPE;
    public static final String NØKKEL_INDEKSERTE_FILER = INDEX_ROOT + "_" + "FILER";
    public static final String NØKKEL_INDEKSERT_INNHOLD = INDEX_ROOT + "_" + "INNHOLD";

    /**
     * This constructor is protected because Streams are not meant to be used as arguments in general.
     * However, I consider this to be fine, as a Stream is a fine lowest-common-demoninator: You can have an array, collection or a singular object represented in a stream.
     * This makes things easier.
     * <p>
     * This makes inheriting from the abstract class a bit simpler, since you can specify all sorts of constructors yourself, depending on need.
     *
     * @param commandRoot The root of the command. Typically something like "NEW_FILE", or "SAVE",etc.
     *                    The actual command will have a UUID (v4) appended to it to make it unique.
     *                    That is, the first piece of the id is for information, the second piece is for uniqueification.
     *                    You can manually mess this up, but automatically, not. How necessary the uniqueification is, I don't know. I added it for fun.
     *                    This is a personal project, so I'm allowed to be a bit silly.
     * @param editor      The Editor that this action is supposed to be bound to. Note that it might <i>also</i>
     *                    be bound to the menubar. But that's more about the frame having actions injected.
     * @param shortcuts   The keyboard shortcuts you want to use. Note that there is ZERO attempt at having these be
     *                    unique. It's up to the programmer to make sure that they're unique, and that should not be a
     */
    protected AbstractProsjektAction(String commandRoot, Editor editor, Stream<KeyStroke> shortcuts) {
        super(commandRoot, editor, shortcuts);
    }

    public void oppdaterFilIndeks() {
        String sti = editor.getValue(NØKKEL_INDEKSERT_MAPPE);
        if (null == sti || sti.isEmpty()) {
            System.err.println("ingen mappe satt, ingen indeksering");
            return;
        }

        final Map<File, String> lastetInnholdForSøk = new HashMap<>();
        final Set<File> filerForÅpning = new HashSet<>();

        final Consumer<File> leggTilFil = fil -> {
            lastFilForSøk(fil, lastetInnholdForSøk);
            filerForÅpning.add(fil);
        };

        fileStuff(leggTilFil, new File(sti));

        editor.setTypedObject(Set.class, NØKKEL_INDEKSERTE_FILER, filerForÅpning);
        editor.setTypedObject(Map.class, NØKKEL_INDEKSERT_INNHOLD, lastetInnholdForSøk);
    }

    public void lastFilForSøk(File fil, Map<File, String> lastedeFiler) {
        try (FileInputStream fis = new FileInputStream(fil)) {
            String filInnhold = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
            lastedeFiler.put(fil, filInnhold);
        } catch (FileNotFoundException fnfe) {
            System.err.printf("Systemet kunne ikke finne fil '%s', fil hoppet over.");
        } catch (IOException ioe) {
            System.err.printf("Feil under lesing av fil '%s'. Fil hoppet over.");
        }
    }

    /**
     * Dette er en rekursiv metode for å følge et tre (filstrukturen i moderne operativsystem) og jakte ned alle filene under en mappe.
     *
     * @param behandleFil
     * @param currentFolder
     */
    public void fileStuff(Consumer<File> behandleFil, File currentFolder) {
        for (File f : currentFolder.listFiles()) {
            if (f.isDirectory()) {
                fileStuff(behandleFil, f);
            } else if (f.isFile()) {
                behandleFil.accept(f);
            } else {
                System.err.printf("Fil '%s' er hverken mappe eller fil. Hopper over den.", f.getAbsolutePath());
            }
        }
    }

    protected boolean mappeHarBlittSatt() {
        if(editor.getValue(NØKKEL_INDEKSERT_MAPPE) == null) {
            System.err.println("Ingen mappe indeksert, kan ikke gjøre noe enda.");
            editor.popupError("Ingen mappe satt", "Ingen mappe satt som prosjektmappe. Sett denne først.");
            return false;
        }
        return true;
    }

    // Vi kan garantere at denne holder, gitt at andre aksesser til editor respekterer grensene som er satt.
    @SuppressWarnings("unchecked")
    protected Set<File> getSøkbareFiler() {
        return (Set<File>) editor.getTypedObject(Set.class, NØKKEL_INDEKSERTE_FILER);
    }

    // Vi kan garantere at også denne casten holder, gitt at andre aksesser til editor respekterer grensene som er satt.
    @SuppressWarnings("unchecked")
    protected Map<File, String> getIndeksertInnhold() {
        return (Map<File, String>) editor.getTypedObject(Map.class, NØKKEL_INDEKSERT_INNHOLD);
    }
}
