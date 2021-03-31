import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Background {

    BufferedImage treeImage;

    Background() throws IOException {
        treeImage = ImageIO.read(new File("res/green_tea.png"));
    }

    public void addBackground(Graphics g, Frame frame, Tree tree){
        g.drawImage(treeImage, frame.toPixels(tree.x - frame.x0), frame.toPixels(tree.y - frame.y0), null);
    }
}
