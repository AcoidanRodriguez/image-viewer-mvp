package software.ulpgc.imageviewer.swing;

import software.ulpgc.imageviewer.FileImageLoader;
import software.ulpgc.imageviewer.ImagePresenter;

import java.io.File;

public class Main {
    private static final String root = "C:/Users/acer/Pictures/Capturas";

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        ImagePresenter presenter = new ImagePresenter(frame.getImageDisplay());
        presenter.show(new FileImageLoader(new File(root)).load());
        frame.setVisible(true);
    }

}
