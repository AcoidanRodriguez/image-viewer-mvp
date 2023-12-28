package software.ulpgc.imageviewer;

import software.ulpgc.imageviewer.swing.MainFrame;

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
