package no.haakon.jotepad.model.buffer;

import no.haakon.jotepad.gui.components.ApplicationFrame;
import no.haakon.jotepad.gui.components.BufferSkroller;
import no.haakon.jotepad.model.editor.Editor;
import no.haakon.jotepad.model.GuiHelpers;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * En buffer er en klasse som holder på ting som skal redigeres.
 * Den er en kontroller mellom ApplicationFrame og modellen som ligger under.
 * Det er flere ting som en buffer må ha:
 * <ul>
 *     <li>En editor, en klasse som tilbyr operasjoner mot dokumentet</li>
 *     <li>Et dokument som redigeres, som oftest forbundet med en fil</li>
 *     <li>Filen (om noen) som er forbundet med dokumentet</li>
 *     <li>En JScrollPane som viser dokumentet, som kan skrolle</li>
 *     <li>En GuiHelpers som tilbyr abstraksjoner for enkle ting som input og outputbokser</li>
 * </ul>
 *
 * Merk at selv om det i en periode vil være mulig å hente ut dokumentet, editoren, mm. er det ikke gitt at dette skal være mulig i lengden.
 * Selv om buffere må tilby en del basisfunksjonalitet (via editorer), vil den funksjonaliteten som tilbys nødvendigvis variere, avhengig av hvilken type dokument som er opprettet.
 *
 * Denne abstrakte klassen fungerer også som en fabrikk for buffere, og må vite hvordan den oppretter buffere. Buffere i seg selv er noe mer dynamisk enn det Java er vant med.
 * Det er OK, men det fordrer at implementasjoner sjekker nøye før de sier at ting er OK.
 */
public abstract class Buffer {

    private final String unikId = UUID.randomUUID().toString();

    private final Map<String, String> state;
    private final Map<String, Map<Class<?>, Object>> objekter;

    public static final String APPLICATION_FRAME = "APPFRAME";

    public abstract Optional<File> getFil();
    public abstract String getTypeNavn();
    public abstract String getBufferNavn();
    public abstract void die();
    public abstract JComponent getComponent();
    public abstract Editor getEditor();
    public abstract void save();
    public abstract GuiHelpers guiHelpers(); // TODO: Burde kanskje opprettes her.

    protected Buffer() {
        state = new HashMap<>();
        objekter = new HashMap<>();
    }


    public void setVerdi(String nøkkel, String verdi) {
        if(nøkkel == null || verdi == null) {
            System.err.println("Hverken nøkkel eller verdi kan være null. Ingenting vil bli satt.");
            return;
        }
        state.put(nøkkel, verdi);
    }

    public String getVerdi(String nøkkel) {
        return state.getOrDefault(nøkkel, "");
    }

    public <T> void setObjekt(String nøkkel, Class<T> type, T verdi) {
        Map<Class<?>, Object> klassemap = objekter.getOrDefault(nøkkel, new HashMap<>());
        klassemap.put(type, verdi);
        objekter.put(nøkkel, klassemap);
    }

    @SuppressWarnings("unchecked") // Vi kan garantere dette gitt at vi bare setter objekter via setObjekt.
    public <T> T getObjekt(String nøkkel, Class<T> type) {
        Object verdi = objekter.getOrDefault(nøkkel, new HashMap<>())
                .get(type);

        return (T) verdi;
    }

    /**
     * Dette er en unik ID som blir gitt hver eneste buffer.
     * Denne verdien blir brukt til bokføring av ymse komponenter mm. og kan derfor ikke overskrives.
     * @return en UUID unik for en enkelt buffer.
     */
    public final String getUnikId() {
        return unikId;
    }

    /**
     * Hjelpemetode: Vil konvertere verdier om de kan til oppgitt type.
     */
    protected <T> Optional<T> konverter(Object arg, Class<T> type) {
        if(arg == null) {
            return Optional.empty();
        }

        if(type.isAssignableFrom(arg.getClass())) {
            return Optional.of(type.cast(arg));
        }

        return Optional.empty();
    }

    public JScrollPane getSkroller() {
        return new BufferSkroller(this);
    }
}
