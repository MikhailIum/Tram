import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        while(true) {
            Frame frame = new Frame(0, 0, 0, 0, 17);

            KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            manager.addKeyEventDispatcher(new KeyEventDispatcher(frame));

            while (!frame.isClosed()) frame.repaint();

            if (!frame.isReallyAgainButtonWasPressedButNotTheCloseCrossWasPressed) {
                break;
            }
        }
    }

}

