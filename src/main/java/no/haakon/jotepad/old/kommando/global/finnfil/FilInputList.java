package no.haakon.jotepad.old.kommando.global.finnfil;

import no.haakon.jotepad.old.gui.components.ApplicationFrame;
import no.haakon.jotepad.old.gui.components.inputlister.SimpleNarrowingInputList;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilInputList extends SimpleNarrowingInputList<File> {
    private static final ListModel<ListRepresentation<File>> tomModell = new DefaultListModel<>();

    public static FilInputList forFolder(File folder, ApplicationFrame frame) {
        Collection<File> filer = folder.isDirectory()?
                Arrays.asList(folder.listFiles()).stream().map(File::getAbsoluteFile).collect(Collectors.toList()):
                Collections.singletonList(folder);

        FilInputList fil = new FilInputList("Velg fil", filer, frame);
        String startTekst = folder.getAbsolutePath() + (folder.isDirectory()? File.separator : "");
        fil.søkefelt.setText(startTekst);
        return fil;
    }

    private FilInputList(String tittel, Collection<File> elementerTilListen, ApplicationFrame frame) {
        super(tittel, elementerTilListen, frame);
    }

    @Override
    protected void gjørValg() {
        if(getValgtElement().isPresent()) {
            File fil = getValgtElement().get();
            if(fil.isDirectory()) {
                søkefelt.setText(fil.getAbsolutePath() + File.separator);
                søkefelt.requestFocusInWindow();
            } else {
                frame.åpneFil(fil, StandardCharsets.UTF_8);
                this.lukkVindu();
            }
        } else {
            // hvis det ikke er noe valgt element, er listen tom.
            // Da må vi se om det er en eksisterende fil, eksisterende mappe, eller noe vi kan opprette.
            String filsti = søkefelt.getText();
            File valgtFil = new File(filsti);
            // Det er en fil som allerede finnes. Hvis det er en mappe, gjør vi ingenting, hvis det er en fil, åpner vi.
            if(valgtFil.exists()) {
                if(valgtFil.isDirectory()) {
                    // Vi antar at brukeren ikke vil åpne en hel mappe.
                    return;
                } else {
                    // Men hvis det er en vanlig fil kan vi jo åpne den som brukeren ber om.
                    frame.logg("Åpner fil " + valgtFil.getAbsolutePath());
                    frame.åpneFil(valgtFil, StandardCharsets.UTF_8);
                    return;
                }
            }
            File mappe = valgtFil.getParentFile();
            if(!mappe.exists()) {
                boolean opprett = frame.synligBuffer().guiHelpers().jaNeiPopup("Opprett ny mappe?",
                        "Mappen " + valgtFil.getParentFile().getAbsolutePath() + " finnes ikke. Opprett mappen (samt evt. mapper over den igjen)?").valgteJa();
                if(opprett && mappe.mkdirs()) {
                    frame.logg("Opprettet mappen " + mappe.getAbsolutePath());
                } else {
                    frame.logg("Kunne ikke opprette mappe " + mappe.getAbsolutePath());
                }
            }
            try {
                if (valgtFil.createNewFile()) {
                    frame.logg("Opprettet ny fil " + valgtFil.getAbsolutePath());
                    frame.åpneFil(valgtFil, StandardCharsets.UTF_8);
                } else {
                    frame.logg("Fikk ikke til å opprette ny fil.");
                }
            } catch (IOException e) {
                frame.loggFormattert("Feilmelding under opprettelse av ny fil (%s): %s", valgtFil.getAbsolutePath(), e.getMessage());
            }
            lukkVindu();
        }
    }

    @Override
    protected String stringRepresentasjon(File element) {
        return element.getAbsolutePath();
    }

    @Override
    protected void oppdaterUtvalg(String tekst) {
        if(tekst == null || tekst.isBlank()) {
            this.nåværendeUtvalg.setModel(tomModell);
            return;
        }
        File filFraInput = new File(tekst).getAbsoluteFile();

        if(filFraInput.exists() && filFraInput.isDirectory()) {
            DefaultListModel<ListRepresentation<File>> nyModell = new DefaultListModel<>();
            File[] filerIMappen = Optional.ofNullable(filFraInput.listFiles()).orElse(new File[0]);
            nyModell.addAll(Arrays.stream(filerIMappen)
                    .map(File::getAbsoluteFile)
                    .map(this::listRepresentation)
                    .sorted()
                    .collect(Collectors.toList()));
            this.nåværendeUtvalg.setModel(nyModell);
            return;
        }

        // Ellers, om vi har kommet så langt, så er dette ikke en eksisterende mappe.
        // Da vil vi ha alle filer i mappen som begynner med det samme som denne filen gjør.
        if(tekst.length() < oppdateringGrenseverdi()) {
            return; //
        }
        Pattern match = Pattern.compile(tekst, Pattern.LITERAL | Pattern.CASE_INSENSITIVE);
        DefaultListModel<ListRepresentation<File>> nyModell = new DefaultListModel<>();
        nyModell.addAll(Optional.ofNullable(filFraInput.getParentFile().listFiles())
                .map(Arrays::stream).orElse(Stream.empty())
                .map(File::getAbsoluteFile)
                .filter(fileMatches(match))
                .map(this::listRepresentation)
                .sorted()
                .collect(Collectors.toList()));
        this.nåværendeUtvalg.setModel(nyModell);
    }

    private Predicate<File> fileMatches(Pattern pattern) {
        return (File f) -> {
            Matcher matcher = pattern.matcher(f.getAbsolutePath());
            return matcher.find() && matcher.toMatchResult().start() == 0;
        };
    }

    @Override
    protected int oppdateringGrenseverdi() {
        return 0;
    }
}
