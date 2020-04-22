package no.haakon.jotepad.actions.prosjekt;

import no.haakon.jotepad.actions.AbstractJotepadAction;
import no.haakon.jotepad.gui.components.ApplicationFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AbstractProsjektAction extends AbstractJotepadAction {

    public static final String INDEX_ROOT = "FILINDEX";
    public static final String INDEKSERT_MAPPE = "MAPPE";
    public static final String NØKKEL_INDEKSERT_MAPPE = INDEX_ROOT + "_" + INDEKSERT_MAPPE;
    public static final String NØKKEL_INDEKSERTE_FILER = INDEX_ROOT + "_" + "FILER";
    public static final String NØKKEL_INDEKSERT_INNHOLD = INDEX_ROOT + "_" + "INNHOLD";


    protected AbstractProsjektAction(String commandRoot, ApplicationFrame frame) {
        super(commandRoot, frame);
    }

    public void oppdaterFilIndeks() {
        String sti = frame.getValue(NØKKEL_INDEKSERT_MAPPE);
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

        frame.setTypedObject(Set.class, NØKKEL_INDEKSERTE_FILER, filerForÅpning);
        frame.setTypedObject(Map.class, NØKKEL_INDEKSERT_INNHOLD, lastetInnholdForSøk);
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
        if(frame.getValue(NØKKEL_INDEKSERT_MAPPE) == null) {
            System.err.println("Ingen mappe indeksert, kan ikke gjøre noe enda.");
            frame.synligBuffer().guiHelpers().popupError("Ingen mappe satt", "Ingen mappe satt som prosjektmappe. Sett denne først.");
            return false;
        }
        return true;
    }

    // Vi kan garantere at denne holder, gitt at andre aksesser til editor respekterer grensene som er satt.
    @SuppressWarnings("unchecked")
    protected Set<File> getSøkbareFiler() {
        return (Set<File>) frame.getTypedObject(Set.class, NØKKEL_INDEKSERTE_FILER);
    }

    // Vi kan garantere at også denne casten holder, gitt at andre aksesser til editor respekterer grensene som er satt.
    @SuppressWarnings("unchecked")
    protected Map<File, String> getIndeksertInnhold() {
        return (Map<File, String>) frame.getTypedObject(Map.class, NØKKEL_INDEKSERT_INNHOLD);
    }
}
