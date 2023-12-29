package software.ulpgc.imageviewer.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.BLACK;
import static java.awt.image.AffineTransformOp.TYPE_BICUBIC;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private Shift shift = Shift.Null;
    private Released released = Released.Null;
    private int initShift;
    private List<Paint> paints = new ArrayList<>();

    public SwingImageDisplay() {
        this.addMouseListener(mouseListener());
        this.addMouseMotionListener(mouseMotionListener());
    }

    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                initShift = e.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) { released.offset(e.getX() - initShift);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) { }
        };
    }

    private MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                shift.offset(e.getX() - initShift);
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        };
    }

    @Override
    public void paint(String id, int offset, BufferedImage bitmap) {
        paints.add(new Paint(id, offset, bitmap));
        repaint();
    }

    @Override
    public void clear() {paints.clear();}
    @Override
    public void paint(Graphics g) {
        for (Paint paint : paints) {
            g.setColor(BLACK);
            g.drawImage(getScaledImageWithBackground(paint),paint.offset,0,null);
        }
    }

    private BufferedImage getScaledImageWithBackground(Paint paint) {
        BufferedImage image = scale(paint.bitmap());
        return addBackground(image);
    }

    private BufferedImage addBackground(BufferedImage image) {
        BufferedImage imageWithBackground = new BufferedImage(getWidth(),getHeight(), image.getType());
        Graphics2D graphics2D = imageWithBackground.createGraphics();
        graphics2D.setColor(BLACK);
        graphics2D.fillRect(0,0,getWidth(),getHeight());
        graphics2D.drawImage(image,getXCoordinate(image), getYCoordinate(image),null);
        graphics2D.dispose();
        return imageWithBackground;
    }

    private  int getYCoordinate(BufferedImage bitmap) {
        return this.getHeight() < bitmap.getHeight() ?
                0 : (this.getHeight()-bitmap.getHeight())/2 ;
    }

    private int getXCoordinate(BufferedImage bitmap) {
        return this.getWidth() < bitmap.getWidth() ?
                0 : ((this.getWidth()-bitmap.getWidth())/2);
    }

    private BufferedImage scale(BufferedImage bitmap) {
        if (this.getWidth() < bitmap.getWidth() && this.getHeight() < bitmap.getHeight()){
            return resizeAll(bitmap,new BufferedImage(this.getWidth(), this.getHeight(), bitmap.getType()));
        }
        else if (this.getWidth() >= bitmap.getWidth() && this.getHeight() < bitmap.getHeight()) {
            return resizeHeight(bitmap,new BufferedImage(bitmap.getWidth(), this.getHeight(), bitmap.getType()));
        } else if (this.getWidth() < bitmap.getWidth() && this.getHeight() >= bitmap.getHeight()) {
            return resizeWidth(bitmap,new BufferedImage(this.getWidth(), bitmap.getHeight(), bitmap.getType()));
        }
        return bitmap;
    }

    private BufferedImage resizeWidth(BufferedImage bitmap, BufferedImage newBitmap) {
        new AffineTransformOp(AffineTransform.getScaleInstance(
                (float)this.getWidth()/(float) bitmap.getWidth(),
                1),TYPE_BICUBIC)
                .filter(bitmap,newBitmap);
        return newBitmap;
    }

    private BufferedImage resizeHeight(BufferedImage bitmap, BufferedImage newBitmap) {
        new AffineTransformOp(AffineTransform.getScaleInstance(
                1,
                (float)this.getHeight()/(float) bitmap.getHeight()),TYPE_BICUBIC)
                .filter(bitmap,newBitmap);
        return newBitmap;
    }

    private BufferedImage resizeAll(BufferedImage bitmap, BufferedImage newBitmap ) {
        new AffineTransformOp(AffineTransform.getScaleInstance(
                (float)this.getWidth()/(float)bitmap.getWidth(),
                (float)this.getHeight()/(float)bitmap.getHeight()), TYPE_BICUBIC)
                .filter(bitmap,newBitmap);
        return newBitmap;
    }

    @Override
    public void on(Shift shift) {
        this.shift = shift != null ? shift : Shift.Null;
    }

    @Override
    public void on(Released released) {
        this.released = released != null ? released : Released.Null;
    }

    private record Paint (String id ,int offset, BufferedImage bitmap){}
}
