package software.ulpgc.imageviewer;

import software.ulpgc.imageviewer.swing.ImageDisplay;
import software.ulpgc.imageviewer.swing.ImageDisplay.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePresenter {
    private final ImageDisplay display;
    private Image image;

    public ImagePresenter(ImageDisplay display) {
        this.display = display;
        this.display.on((Shift) this::shift);
        this.display.on((Released) this::released);
    }

    private void shift(int offset) {
        display.clear();
        display.paint(image.name(), offset,load(image.name()));
        if (offset > 0)
            display.paint(image.prev().name(), offset - display.getWidth(),load(image.prev().name()));
        else
            display.paint(image.next().name(), display.getWidth() + offset,load(image.next().name()));

    }

    private void released(int offset) {
        display.clear();
        if (Math.abs(offset) >= display.getWidth() / 2)
            image = offset > 0 ? image.prev() : image.next();
        repaint();
    }

    public void show(Image image) {
        this.image = image;
        repaint();
    }

    private void repaint() {
        this.display.clear();
        this.display.paint(image.name(), 0, load(image.name()));
    }

    private BufferedImage load(String name) {
        try {
            return ImageIO.read(new File(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
