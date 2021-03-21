import java.io.File;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        int bestScore = 0;

        while (true) {
            int nextBlocks = new Random().nextInt(10) + 1;
            int futurePositions = new Random().nextInt(10) + 1;
            double ddt = new Random().nextDouble() + 0.1;

            int score = runSimulation(nextBlocks, futurePositions, ddt);
            if (score > bestScore){
                bestScore = score;
                System.out.println("Improved! score = " + score + "(nextBlocks = " + nextBlocks + "; futurePositions = " + futurePositions + "; ddt = " + ddt +")");
            }
        }
    }

        public static int runSimulation(int nextBlocks, int futurePositions, double ddt){
            Frame frame = new Frame(nextBlocks, futurePositions, ddt);
            while (frame.isShowing()){
                frame.repaint();
            }
            return frame.points;
        }

}

