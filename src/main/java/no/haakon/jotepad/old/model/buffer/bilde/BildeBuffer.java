package no.haakon.jotepad.old.model.buffer.bilde;

import no.haakon.jotepad.old.model.GuiHelpers;
import no.haakon.jotepad.old.model.buffer.Buffer;
import no.haakon.jotepad.old.model.editor.Editor;
import no.haakon.jotepad.old.model.editor.NoOpEditor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BildeBuffer extends Buffer {
    static {
        ImageIO.scanForPlugins();
    }

    private final BufferedImage bufferedImage;
    private final NoOpEditor editor;
    private final GuiHelpers guiHelpers;
    private final JLabel komponenten;

    public BildeBuffer(File bildefil, Map<String, Object> argumenter) {
        super(bildefil, argumenter);
        try {
            bufferedImage = ImageIO.read(fil);
        } catch (IOException e) {
            throw new IllegalStateException("Kunne ikke lese bildefil " + fil.getAbsolutePath(), e);
        }

        this.editor = new NoOpEditor(this);
        this.guiHelpers = new GuiHelpers(frame);

        this.komponenten = new JLabel();
        komponenten.setIcon(new ImageIcon(bufferedImage));
        init();
    }

    public void init() {

    }

    @Override
    public Optional<File> getFil() {
        return Optional.of(fil);
    }

    @Override
    public String getTypeNavn() {
        return "Bilde";
    }

    @Override
    public void die() {
        bufferedImage.flush();
    }

    @Override
    public JComponent getComponent() {
        return komponenten;
    }

    @Override
    public Editor getEditor() {
        return editor;
    }

    @Override
    public byte[] innholdSomBytes() {
        String etternavn = fil.getName();
        etternavn = etternavn.substring(etternavn.lastIndexOf('.'));

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, etternavn, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Kunne ikke lese ut data fra bildet");
            return new byte[0];
        }

    }

    @Override
    public GuiHelpers guiHelpers() {
        return guiHelpers;
    }

    public static Collection<Pattern> støttedeFiler() {
        return Arrays.stream(ImageIO.getReaderFormatNames())
                .map(filename -> String.format("\\.%s$", filename))
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }

}
