import java.awt.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Frame frame;
        while(true) {
            frame = new Frame(0, 0, 0, 0, 17);

            KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            manager.addKeyEventDispatcher(new KeyEventDispatcher(frame));

            while (!frame.isClosed) {
                frame.repaint();
            }
        }
    }

}

