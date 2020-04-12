package no.haakon.jotepad.gui.components;

import no.haakon.jotepad.actions.prosjekt.Treff;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

import static java.lang.Integer.max;

public class ProsjektSøkTekstVindu {

    private static final String vindustittel = "Søkeresultater";
    private final Editor assosiertEditor;
    private final Map<File, String> innhold;
    private JList<Treff> treffListe;
    private JFrame vindu;

    // Lys, Kamera, ACTION!
    private final ActionListener reagerPåEnter = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            velgFil();
        }
    };

    private final DocumentListener reagerPåEndringer = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            react(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            react(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            react(e);
        }

        private void react(DocumentEvent e) {
            try {
                String tekst = e.getDocument().getText(0, e.getDocument().getLength());
                oppdaterTreff(tekst);
            } catch (BadLocationException ble) {
                System.err.println("Kunne ikke hente ut teksten fra søkefeltet, noe som aldri burde skje.");
            }
        }
    };

    // håndterer at vi klikker på listen.
    private final MouseListener håndterLister = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() > 1) {
                velgFil();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            return;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            return;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            return;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            return;
        }
    };

    // TODO: Kopiert over fra PFFV.java, må fikses senere.
    public ProsjektSøkTekstVindu(Map<File, String> innhold, Editor assosiertEditor) {
        this.innhold = innhold;
        this.assosiertEditor = assosiertEditor;
        init(assosiertEditor.getParentFrame());
    }

    private void velgFil() {
        if (treffListe.getModel().getSize() == 0) {
            // Ingen filer å velge, så ikke gjør noe som helst!
            return;
        }
        switch (treffListe.getSelectedIndex()) {
            // Ingenting valgt, velg første fil.
            case -1:
                treffListe.setSelectedIndex(0);
                // Merk at det ikke er en break, så ting hopper bare videre.
                // Dette er bare jeg som syntes det var gøy, og jeg ville ikke gjort dette på jobb, så å si.
            default:
                assosiertEditor.loadFile(treffListe.getSelectedValue().getFil(), StandardCharsets.UTF_8);
                lukkVindu();
        }
    }

    private void lukkVindu() {
        vindu.dispose();
    }

    public void oppdaterTreff(String tekst) {
        if(tekst.length() < 3) {
            // dette er for knotete å søke etter!
            treffListe.setModel(new DefaultListModel<>());
            vindu.setTitle(vindustittel);
            return;
        }

        Pattern søkekriterier;
        try {
            søkekriterier = Pattern.compile(tekst);
        } catch (PatternSyntaxException pse) {
            return; // Vi kan ikke gjøre noe hvis vi ikke har et søkekriterium.
        }

        DefaultListModel<Treff> treffModel = new DefaultListModel<>();

        System.out.println("Treff:");
        innhold.entrySet().stream()
                .flatMap(entry -> finnTreff(entry, søkekriterier))
                .forEach(treffModel::addElement);

        treffListe.setModel(treffModel);
        vindu.setTitle(String.format("%s: %d treff", vindustittel, treffModel.getSize()));
    }

    private Stream<Treff> finnTreff(Map.Entry<File, String> entry, Pattern søkekriterier) {
        Matcher matcher = søkekriterier.matcher(entry.getValue());
        return matcher.results().map(matchResult -> fraMatchResult(matchResult, entry)).filter(Objects::nonNull);
    }

    private Treff fraMatchResult(MatchResult mr, Map.Entry<File, String> oppslag) {
        try {
            return new Treff(mr, oppslag.getKey(), oppslag.getValue());
        } catch(RuntimeException re) {
            return null;
        }
    }

    protected void init(JFrame hovedVindu) {
        vindu = new JFrame("Finn fil i prosjekt");
        JPanel panel = new JPanel();
        vindu.add(panel);
        BoxLayout manager = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(manager);

        JTextField søkefelt = new JTextField(40);

        DefaultListModel<Treff> fileListModel = new DefaultListModel<>();
        treffListe = new JList<>(fileListModel);
        JScrollPane filSkroller = new JScrollPane();
        filSkroller.setViewportView(treffListe);
        treffListe.setLayoutOrientation(JList.VERTICAL);

        // setter felles bredde
        final int commonWidth = max(søkefelt.getPreferredSize().width, treffListe.getPreferredSize().width);
        Dimension søkefeltStørrelse = søkefelt.getPreferredSize();
        søkefeltStørrelse.width = commonWidth;
        søkefelt.setMinimumSize(søkefeltStørrelse);

        Dimension lovligeFilerStørrelse = treffListe.getPreferredSize();
        lovligeFilerStørrelse.width = commonWidth;
        treffListe.setMinimumSize(lovligeFilerStørrelse);
        treffListe.setFixedCellWidth(commonWidth);

        panel.add(søkefelt);
        panel.add(filSkroller);

        panel.setSize(new Dimension(commonWidth, søkefeltStørrelse.height + lovligeFilerStørrelse.height));
        vindu.pack();
        vindu.setVisible(true);
        vindu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        søkefelt.getDocument().addDocumentListener(reagerPåEndringer);
        søkefelt.addActionListener(reagerPåEnter);
        treffListe.addMouseListener(håndterLister);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        treffListe.getInputMap().put(enter, "ENTER");
        treffListe.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                velgFil();
            }
        });

        søkefelt.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "FOKUS_LISTE");
        søkefelt.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "FOKUS_LISTE");
        søkefelt.getActionMap().put("FOKUS_LISTE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Gir fokus til listen");
                if (treffListe.getSelectedIndex() < 0) {
                    treffListe.setSelectedIndex(0);
                }
                treffListe.requestFocus();
            }
        });


        // putter dette vinduet i sentrum av det andre vinduet.
        int xPosisjon = ((hovedVindu.getWidth() / 2) - (vindu.getWidth() / 2)) + hovedVindu.getLocationOnScreen().x; // Velkommen til gamle dager, da dette her med get og set var litt sånn nytt og vakkert...
        int yPosisjon = ((hovedVindu.getHeight() / 2) - (vindu.getHeight() / 2)) + hovedVindu.getLocationOnScreen().y;

        vindu.setLocation(xPosisjon, yPosisjon);
    }


}
