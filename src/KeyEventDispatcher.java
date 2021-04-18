import java.awt.event.KeyEvent;

public class KeyEventDispatcher implements java.awt.KeyEventDispatcher {
    Frame frame;

    KeyEventDispatcher(Frame frame){
        this.frame = frame;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_UP) {
            frame.speed += frame.acceleration * frame.dt / 100;
            if (frame.speed >= frame.maxSpeed) frame.speed = frame.maxSpeed;
        }
        else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_DOWN){
            frame.speed -= frame.acceleration * frame.dt / 10;
            if (frame.speed < 1) frame.speed = 0;
        }
        else {
            frame.speed -= frame.acceleration * frame.dt / 100;
            if (frame.speed < 1) frame.speed = 0;
        }


        // TODO: повороты на малых скоростях
        // TODO: деревья и люди(положение)
        // TODO: графика смертей, скорости и тд
        // TODO: таблица рекордов
        // TODO: время dt
        return false;
    }
}
