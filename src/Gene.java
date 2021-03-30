import java.util.Random;

public class Gene implements Comparable<Gene>{
    int nextBlocks;
    int futurePositions;
    double ddt;
    double score;

    Gene(){
        this.nextBlocks = new Random().nextInt(15) + 1;
        this.futurePositions = new Random().nextInt(200) + 1;
        this.ddt = new Random().nextInt(5000) * 1.0 / 100;
        this.score = new Random().nextDouble();
    }

    @Override
    public int compareTo(Gene o) {
        return Double.compare(this.score, o.score);
    }
}
