package isel.pg3.music.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;

import static java.awt.Component.LEFT_ALIGNMENT;

public class Utils {

    // Methods utils
    public static Icon getIconFromResource(String iconRes)  {
        try {
            Arrays.asList( 1);
            InputStream playImageStream =
                ClassLoader.getSystemResourceAsStream(iconRes);
            BufferedImage img = ImageIO.read(playImageStream);
            Image butImg = img.getScaledInstance( 25, 25,  java.awt.Image.SCALE_SMOOTH ) ;
            return new ImageIcon( butImg );
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static JButton getButton( Icon icon, ActionListener action) {
        JButton b = new JButton( icon );
        b.addActionListener( action );
        return b;
    }


    public static JPanel getBox(String label, JTextField tf, JButton but) {
        JPanel textBox = new JPanel();
        textBox.setAlignmentX(LEFT_ALIGNMENT);
        tf.setEditable( false );
        textBox.add(new JLabel(label));
        textBox.add(tf);
        textBox.add(but);
        return textBox;
    }


    public static File chooseFile(String rootFolder) {
        //Creating FileChooser for choosing the music mp3 file
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File(rootFolder));
        fileChooser.setDialogTitle("Select Mp3");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Mp3 files", "mp3"));

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();

        }
        return null;
    }
}
