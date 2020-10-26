package no.haakon.jotepad.old.gui.menubar;

import no.haakon.jotepad.old.actions.*;
import no.haakon.jotepad.old.actions.buffer.ForrigeBufferAction;
import no.haakon.jotepad.old.actions.buffer.ListBufferAction;
import no.haakon.jotepad.old.actions.buffer.NesteBufferAction;
import no.haakon.jotepad.old.actions.buffer.SlettBufferAction;
import no.haakon.jotepad.old.actions.kommando.EvaluerUttrykkAction;
import no.haakon.jotepad.old.actions.kommando.KjørKommandoAction;
import no.haakon.jotepad.old.actions.prosjekt.ProsjektFilSøkAction;
import no.haakon.jotepad.old.actions.prosjekt.ProsjektTekstSøkAction;
import no.haakon.jotepad.old.actions.prosjekt.SettProsjektMappe;
import no.haakon.jotepad.old.actions.search.*;
import no.haakon.jotepad.old.actions.tabell.NyKolonneAction;
import no.haakon.jotepad.old.actions.tabell.NyRadAction;
import no.haakon.jotepad.old.gui.components.ApplicationFrame;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class JotepadMenubar extends JMenuBar {

    private final ApplicationFrame frame;
    private final DefaultEditorKit editorKit;

    public JotepadMenubar(ApplicationFrame frame) {
        this.frame = frame;
        this.editorKit = new DefaultEditorKit();
        setupFil();
        setupRediger();
        setupSøking();
        setupProsjekt();
        setupBuffere();
        setupTabell();
        setupKommandoer();
    }

    private void setupFil() {
        JMenu fil = new JMenu("Fil");

        fil.add(lagJMenuItem(
                "Ny...",
                new NewFileAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Åpne fil",
                new LoadFileAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Lagre",
                new SaveFileAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Lagre som",
                new SaveAsAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Avslutt",
                new ExitAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)));


        this.add(fil);
    }

    private void setupRediger() {
        JMenu rediger = new JMenu("Rediger");

        rediger.add(lagJMenuItem(
                "Søk",
                new FindTextAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.ALT_DOWN_MASK)));

        rediger.add(lagJMenuItem(
                "Angre",
                new UndoAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK)));

        this.add(rediger);
    }

    private void setupSøking() {
        JMenu søking = new JMenu("Søking");

        søking.add(lagJMenuItem(
                "Sett søketerm [enkelt søk]",
                new SetSearchTermAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));

        søking.add(lagJMenuItem(
                "Vis søketerm",
                new ShowSearchTermAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)));

        søking.add(lagJMenuItem(
                "Finn forrige",
                new FindPreviousAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK)));

        søking.add(lagJMenuItem(
                "Finn neste",
                new FindNextAction(frame),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK)));

        this.add(søking);
    }

    private void setupProsjekt() {
        JMenu prosjekt = new JMenu("Prosjekt");

        prosjekt.add(lagJMenuItem("Sett prosjektmappe",
                                  new SettProsjektMappe(frame),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK)));

        prosjekt.add(lagJMenuItem("Finn fil",
                                  new ProsjektFilSøkAction(frame),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));

        prosjekt.add(lagJMenuItem("Finn Tekst",
                                  new ProsjektTekstSøkAction(frame),
                                  KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK)));

        this.add(prosjekt);
    }

    private void setupBuffere() {
        JMenu buffer = new JMenu("Buffer");

        buffer.add(lagJMenuItem("Neste",
                                new NesteBufferAction(frame),
                                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)));

        buffer.add(lagJMenuItem("Forrige",
                                new ForrigeBufferAction(frame),
                                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)));

        buffer.add(lagJMenuItem("Vis alle",
                                new ListBufferAction(frame),
                                KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)));

        buffer.add(lagJMenuItem("Lukk buffer (Lagrer ikke!)",
                                new SlettBufferAction(frame),
                                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)));

        this.add(buffer);
    }

    private void setupTabell() {
        JMenu tabell = new JMenu("Tabell");
        tabell.add(lagJMenuItem("Ny kolonne",
                                new NyKolonneAction(frame),
                                KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_DOWN_MASK)));
        tabell.add(lagJMenuItem("Ny rad",
                                new NyRadAction(frame),
                                KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));

        this.add(tabell);
    }

    private void setupKommandoer() {
        JMenu kommandoer = new JMenu("Kommandoer");
        kommandoer.add(lagJMenuItem("Kjør...",
                                new KjørKommandoAction(frame),
                                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK)));

        kommandoer.add(lagJMenuItem("Evaluer...",
                                    new EvaluerUttrykkAction(frame),
                                    KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, InputEvent.ALT_DOWN_MASK)));
        this.add(kommandoer);
    }

    private JMenuItem lagJMenuItem(String navn, Action action, KeyStroke hurtigtast) {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setAction(action);
        jMenuItem.setAccelerator(hurtigtast);
        jMenuItem.setText(navn);

        return jMenuItem;
    }
}
