package software.ulpgc.imageviewer.swing;

import javax.swing.*;
import java.awt.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class MainFrame extends JFrame {
    private final static String DefaultPath = "C:/Users/" + System.getProperty("user.name") + "/Pictures/";
    private ImageDisplay imageDisplay;

    public MainFrame()  {
        this.setTitle("Image Viewer");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(createImageDisplay());
    }

    public ImageDisplay getImageDisplay() {
        return imageDisplay;
    }

    public String getPath(){
        String path = JOptionPane.showInputDialog("Ingrese la ruta donde están las imágenes: ");
        return !path.equals("") && Files.exists(FileSystems.getDefault().getPath(path)) ?
                path :
                getDefaultPath(path);
    }

    private  String getDefaultPath(String path) {
        JOptionPane.showMessageDialog(null,"La ruta " + path +
                " no existe, se usará " + DefaultPath);
        return DefaultPath;
    }

    private Component createImageDisplay() {
        SwingImageDisplay display = new SwingImageDisplay();
        this.imageDisplay = display;
        return display;
    }

}
