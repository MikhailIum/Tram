import java.util.Random;

public class Tree {

    double x;
    double y;

    Tree(Frame frame) {
            x = new Random().nextInt(frame.mapBlock.width + 100) - 50 + frame.x0;

            if (x + frame.toMeters(frame.background.treeImage.getWidth()) >= frame.x0 && x <= frame.x0 + frame.getWidth())
                y = new Random().nextInt(frame.mapBlock.height) - 60 + frame.y0;
            else y = new Random().nextInt(frame.mapBlock.height + 50) - 50 + frame.y0;
    }

}
