import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class KeyEventDispatcher implements java.awt.KeyEventDispatcher {
    Frame frame;

    KeyEventDispatcher(Frame frame){
        this.frame = frame;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (!frame.menu.gameOver) {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_UP) {
                frame.speed += frame.acceleration * frame.dt / 100;
                if (frame.speed >= frame.maxSpeed) frame.speed = frame.maxSpeed;
            } else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_DOWN) {
                frame.speed -= frame.acceleration * frame.dt / 10;
                if (frame.speed < 1) frame.speed = 0;
            } else if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_UP){
                frame.speed -= 0.1;
            }
        } else {
            if (e.getID() == KeyEvent.KEY_TYPED){
                    frame.isRestarted = true;
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }


        // TODO: деревья и люди(положение)
        // TODO: zoom
        return false;
    }
}
