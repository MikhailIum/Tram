import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Frame extends JFrame implements MouseWheelListener {

    int speed = 10;

    double scaleSpeed = 0.003;
    int x0;
    int y0;
    RailBlock railBlock;
    Tram tram;
    MapBlock mapBlock;
    double currentScale;
    double targetScale;
    long prevTime;
    long dt;
    ArrayList<Person> people;
    ArrayList<Person> peopleToBeRemoved = new ArrayList<>();


    Frame(){
        this.setSize(1000, 1000);
        this.setTitle("Tram");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocation(500, 50);
        this.setVisible(true);

        currentScale = targetScale = 2;
        mapBlock = new MapBlock();
        x0 = 500;
        y0 = (int) (mapBlock.height - this.getHeight() * currentScale); // 1000
        railBlock = new RailBlock((int) (x0 + this.getWidth() / 2 * currentScale - 50), (int) (y0 + this.getHeight() * currentScale - 100)); // x = 1450, y = 2900
        tram = new Tram(railBlock);
        prevTime = System.currentTimeMillis();

        people = new ArrayList<>();


        addMouseWheelListener(this);
        createBufferStrategy(2);
    }

    @Override
    public void paint(Graphics g) {
        BufferStrategy bufferStrategy = getBufferStrategy();        // Обращаемся к стратегии буферизации
        if (bufferStrategy == null) {                               // Если она еще не создана
            createBufferStrategy(2);                                // то создаем ее
            bufferStrategy = getBufferStrategy();                   // и опять обращаемся к уже наверняка созданной стратегии
        }
        g = bufferStrategy.getDrawGraphics();                       // Достаем текущую графику (текущий буфер)

        super.paint(g);

        if(currentScale > targetScale + 0.05)
            currentScale -= scaleSpeed;
        else if (currentScale < targetScale - 0.05)
            currentScale += scaleSpeed;

        for (int m = 0; m < mapBlock.height; m += 60)
            for(int n = 0; n < mapBlock.width; n += 60) {
                if ((m % 120 == 0 && n % 120 != 0) || (m % 120 != 0 && n % 120 == 0))
                    g.setColor(new Color(161, 208, 73));
                else g.setColor(new Color(169, 214, 81));

//                g.fillRect((int) ((m * 60 - x0) / currentScale), (int) ((n * 60 - y0) / currentScale),
//                        ((int) (((m+1) * 60 - x0) / currentScale)-(int) ((m * 60 - x0) / currentScale)),
//                        ((int) ( ((n+1) * 60 - y0) / currentScale)-(int) ((n * 60 - y0) / currentScale)));
                g.fillRect((int) ((n - x0) / currentScale), (int) ((m - y0) / currentScale), 60, 60);
            }

        // рельса
        g.setColor(Color.black);
        g.drawLine((int) ((railBlock.x - x0) / currentScale), (int) ((railBlock.y - y0) / currentScale), (int) ((railBlock.x - x0) / currentScale), (int) ((railBlock.y - y0 + railBlock.height) / currentScale));
        g.drawLine((int) ((railBlock.x - x0 + railBlock.width) / currentScale), (int) ((railBlock.y - y0) / currentScale), (int) ((railBlock.x - x0 + railBlock.width) / currentScale), (int) ((railBlock.y - y0 + railBlock.height) / currentScale));


//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // трамвайка
        g.setColor(Color.cyan);
        g.fillRect((int) ((tram.x - x0) / currentScale),(int) ((tram.y - y0) / currentScale),(int) (tram.width / currentScale), (int) (tram.height / currentScale));

        dt = System.currentTimeMillis() - prevTime;

        tram.y -= speed;

        if (tram.y < y0 + getHeight() / 2 * currentScale - tram.height / 2)
            y0 -= speed;
        x0 = (int) (tram.x - this.getWidth() / 2 * currentScale + railBlock.width / 2);

        // Людишки(бедолаги)
        synchronized (people) {
            for (Person person: people){
                person.checkCollision(this);

                g.fillOval((int) ((person.x - x0) / currentScale), (int) ((person.y - y0) / currentScale), (int) (person.width / currentScale), (int) (person.height / currentScale));
                person.x += Math.cos(Math.toRadians(person.angleDeg)) * person.speed;
                person.y += Math.sin(Math.toRadians(person.angleDeg)) * person.speed;

                if (person.x < 400 || person.x > 400 + 2000 * 2.7 || person.y < 0 || person.y > railBlock.height){
                    peopleToBeRemoved.add(person);
                }

            }
        }
        people.removeAll(peopleToBeRemoved);






        g.dispose();                // Освободить все временные ресурсы графики (после этого в нее уже нельзя рисовать)
        bufferStrategy.show();      // Сказать буферизирующей стратегии отрисовать новый буфер (т.е. поменять показываемый и обновляемый буферы местами)

        prevTime = System.currentTimeMillis();

        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
            if (targetScale <= 2.7) targetScale += e.getWheelRotation() * e.getScrollAmount() / 100.0;
            else targetScale = 2.7;
            if (targetScale >= 1.3) targetScale += e.getWheelRotation() * e.getScrollAmount() / 100.0;
            else targetScale = 1.3;
            targetScale = Math.round(targetScale * 100.0) / 100.0;
        }
    }
}
