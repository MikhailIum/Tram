import java.util.Random;
import java.util.function.DoubleConsumer;

public class Gene implements Comparable<Gene>{
    int nextBlocks;
    int futurePositions;
    double ddt;
    double score;

    Gene(){
        this.nextBlocks = new Random().nextInt(10);
        this.futurePositions = new Random().nextInt();
        this.ddt = new Random().nextDouble();;
    }

    @Override
    public int compareTo(Gene o) {
        return Double.compare(o.score, this.score);
    }
}
