public class Main {
    public static void main(String[] args) throws InterruptedException {
        Frame frame = new Frame();
        while(true){
            synchronized (frame.people) {
                frame.people.add(new Person(frame));
            }
            Thread.sleep(10);
        }
    }
}
