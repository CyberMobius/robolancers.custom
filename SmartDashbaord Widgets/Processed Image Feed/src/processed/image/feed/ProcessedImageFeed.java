/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processed.image.feed;

import edu.wpi.first.smartdashboard.gui.DashboardFrame;
import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/**
 *
 * @author Eric
 */
public class ProcessedImageFeed extends StaticWidget {

    public static final String NAME = "Processed Image Feed";

    public class ImgThread extends Thread {

        Runnable draw = new Runnable() {

            public void run() {
                DashboardFrame.getInstance().getPanel().repaint(getBounds());
            }
        };

        @Override
        public void run() {
            while (true) {
                try {
                    URL url = new URL("ftp://10.3.21.2/ni-rt/system/filteredImage.png");
                    drawnImage = ImageIO.read(url);
//                	SwingUtilities.invokeLater(draw);
                } catch (IOException e) {
                }
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProcessedImageFeed.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private BufferedImage drawnImage;
    ImgThread imgThread = new ImgThread();

    @Override
    public void init() {
        drawnImage = null;
        setPreferredSize(new Dimension(100, 100));
        imgThread.start();
        revalidate();
        DashboardFrame.getInstance().getPanel().repaint(getBounds());
    }

    @Override
    public void propertyChanged(Property prprt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    boolean resized = false;

    @Override
    protected void paintComponent(Graphics g) {
        if (drawnImage != null) {
            if (!resized) {
                setPreferredSize(new Dimension(drawnImage.getWidth(), drawnImage.getHeight()));
                revalidate();
            }
            int width = getBounds().width;
            int height = getBounds().height;
            double scale = Math.min((double) width / (double) drawnImage.getWidth(), (double) height / (double) drawnImage.getHeight());
            g.drawImage(drawnImage, (int) (width - (scale * drawnImage.getWidth())) / 2, (int) (height - (scale * drawnImage.getHeight())) / 2,
                    (int) ((width + scale * drawnImage.getWidth()) / 2), (int) (height + scale * drawnImage.getHeight()) / 2,
                    0, 0, drawnImage.getWidth(), drawnImage.getHeight(), null);
        } else {
            g.setColor(Color.PINK);
            g.fillRect(0, 0, getBounds().width, getBounds().height);
            g.setColor(Color.BLACK);
            g.drawString("Image is null", 10, 10);
        }
    }
}
