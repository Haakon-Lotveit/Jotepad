package no.haakon.jotepad.gui.components;

import no.haakon.jotepad.actions.search.RegexSearcher;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.lang.Integer.max;

/**
 * Lar det opprette og behandle et interaktivt søk.
 */
public class ProsjektFinnFilVindu {

    private final Collection<File> filer;
    private final Editor assosiertEditor;
    private JList<File> lovligeFiler;
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
                oppdaterFiler(tekst);
            } catch (BadLocationException ble) {
                System.err.println("Kunne ikke hente ut teksten fra søkefeltet, noe som aldri burde skje.");
            }
        }
    };

    // håndterer at vi klikker på listen.
    private final MouseListener håndterLister = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() > 1) {
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

    public ProsjektFinnFilVindu(Collection<File> filerIProsjektet, Editor assosiertEditor) {
        this.filer = filerIProsjektet;
        this.assosiertEditor = assosiertEditor;
        init(assosiertEditor.getParentFrame());
    }

    private void velgFil() {
        if (lovligeFiler.getModel().getSize() == 0) {
            // Ingen filer å velge, så ikke gjør noe som helst!
            return;
        }
        switch (lovligeFiler.getSelectedIndex()) {
            // Ingenting valgt, velg første fil.
            case -1:
                lovligeFiler.setSelectedIndex(0);
                // Merk at det ikke er en break, så ting hopper bare videre.
                // Dette er bare jeg som syntes det var gøy, og jeg ville ikke gjort dette på jobb, så å si.
            default:
                assosiertEditor.loadFile(lovligeFiler.getSelectedValue(), StandardCharsets.UTF_8);
                vindu.dispose();
        }
    }

    public void oppdaterFiler(String tekst) {
        Pattern mønster;
        try {
            mønster = Pattern.compile(tekst);
        } catch(PatternSyntaxException ignored) {
            System.err.println("Syntaksfeil i regex, oppdaterer ikke liste for tekst '" + tekst + "'");
            return;
        }
        Predicate<File> matcherMønster = fil -> tekst.isEmpty() || mønster.matcher(fil.getName()).find();

        DefaultListModel<File> nyListModel = new DefaultListModel<>();
        filer.stream()
                .filter(matcherMønster)
                .sorted()
                .forEach(nyListModel::addElement);

        lovligeFiler.setModel(nyListModel);
    }

    protected void init(JFrame hovedVindu) {
        vindu = new JFrame("Finn fil i prosjekt");
        JPanel panel = new JPanel();
        vindu.add(panel);
        BoxLayout manager = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(manager);

        JTextField søkefelt = new JTextField(40);


        DefaultListModel<File> fileListModel = new DefaultListModel<>();
        fileListModel.addAll(filer);
        lovligeFiler = new JList<>(fileListModel);
        JScrollPane filSkroller = new JScrollPane();
        filSkroller.setViewportView(lovligeFiler);
        lovligeFiler.setLayoutOrientation(JList.VERTICAL);


        // setter felles bredde
        final int commonWidth = max(søkefelt.getPreferredSize().width, lovligeFiler.getPreferredSize().width);
        Dimension søkefeltStørrelse = søkefelt.getPreferredSize();
        søkefeltStørrelse.width = commonWidth;
        søkefelt.setMinimumSize(søkefeltStørrelse);

        Dimension lovligeFilerStørrelse = lovligeFiler.getPreferredSize();
        lovligeFilerStørrelse.width = commonWidth;
        lovligeFiler.setMinimumSize(lovligeFilerStørrelse);
        lovligeFiler.setFixedCellWidth(commonWidth);

        panel.add(søkefelt);
        panel.add(filSkroller);

        panel.setSize(new Dimension(commonWidth, søkefeltStørrelse.height + lovligeFilerStørrelse.height));
        vindu.pack();
        vindu.setVisible(true);
        vindu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        søkefelt.getDocument().addDocumentListener(reagerPåEndringer);
        søkefelt.addActionListener(reagerPåEnter);
        lovligeFiler.addMouseListener(håndterLister);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        lovligeFiler.getInputMap().put(enter, "ENTER");
        lovligeFiler.getActionMap().put("ENTER", new AbstractAction() {
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
                if(lovligeFiler.getSelectedIndex() < 0) {
                    lovligeFiler.setSelectedIndex(0);
                }
                lovligeFiler.requestFocus();
            }
        });


        // putter dette vinduet i sentrum av det andre vinduet.
        int xPosisjon = ((hovedVindu.getWidth() / 2) - (vindu.getWidth() / 2)) + hovedVindu.getLocationOnScreen().x; // Velkommen til gamle dager, da dette her med get og set var litt sånn nytt og vakkert...
        int yPosisjon = ((hovedVindu.getHeight() / 2) - (vindu.getHeight() / 2)) + hovedVindu.getLocationOnScreen().y;

        vindu.setLocation(xPosisjon, yPosisjon);
    }


}
