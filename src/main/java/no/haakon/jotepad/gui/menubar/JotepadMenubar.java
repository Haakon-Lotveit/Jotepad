package no.haakon.jotepad.gui.menubar;

import no.haakon.jotepad.actions.*;
import no.haakon.jotepad.actions.search.*;
import no.haakon.jotepad.gui.components.Editor;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class JotepadMenubar extends JMenuBar {

    private final Editor editor;
    private final DefaultEditorKit editorKit;

    public JotepadMenubar(Editor editor) {
        this.editor = editor;
        this.editorKit = new DefaultEditorKit();
        setupFil();
        setupRediger();
        setupSøking();
        setupProsjekt();
    }

    private void setupFil() {
        JMenu fil = new JMenu("Fil");

        fil.add(lagJMenuItem(
                "Ny...",
                new NewFileAction(editor),
                KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Åpne fil",
                new LoadFileAction(editor),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Lagre",
                new SaveFileAction(editor),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Lagre som",
                new SaveAsAction(editor),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));

        fil.add(lagJMenuItem(
                "Avslutt",
                new ExitAction(editor),
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)));


        this.add(fil);
    }

    private void setupRediger() {
        JMenu rediger = new JMenu("Rediger");

        rediger.add(lagJMenuItem(
				 "Søk",
				 new FindTextAction(editor),
        KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));

        rediger.add(lagJMenuItem(
				 "Angre",
				 new UndoAction(editor),
        KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK)));

        this.add(rediger);
    }

    private void setupSøking() {
        JMenu søking = new JMenu("Søking");

        søking.add(lagJMenuItem(
				"Sett søketerm [enkelt søk]",
				new SetSearchTermAction(editor),
				KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)));

	søking.add(lagJMenuItem(
				"Vis søketerm",
				new ShowSearchTermAction(editor),
				KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK | InputEvent.ALT_DOWN_MASK)));

	søking.add(lagJMenuItem(
				"Finn forrige",
				new FindPreviousAction(editor),
				KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK)));

	søking.add(lagJMenuItem(
				"Finn neste",
				new FindNextAction(editor),
				KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK)));

        this.add(søking);
    }

    private void setupProsjekt() {

    }

    private JMenuItem lagJMenuItem(String navn, Action action, KeyStroke hurtigtast) {
        JMenuItem jMenuItem = new JMenuItem();
        jMenuItem.setAction(action);
        jMenuItem.setAccelerator(hurtigtast);
        jMenuItem.setText(navn);

        return jMenuItem;
    }
}
