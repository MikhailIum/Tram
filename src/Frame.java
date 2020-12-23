import javafx.beans.binding.MapBinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.math.BigDecimal;
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
        x0 = 1500;
        y0 = (int) (mapBlock.height - this.getHeight() * currentScale);
        railBlock = new RailBlock(x0 + getWidth() / 2, 6000);
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

        for (int m = 0; m < 100; m++)
            for(int n = 0; n < 100; n++) {
                if ((m % 2 == 0 && n % 2 != 0) || (m % 2 != 0 && n % 2 == 0))
                    g.setColor(new Color(161, 208, 73));
                else g.setColor(new Color(169, 214, 81));

                g.fillRect((int) ((m * 60 - x0) / currentScale), (int) ((n * 60 - y0) / currentScale),
                        ((int) (((m+1) * 60 - x0) / currentScale)-(int) ((m * 60 - x0) / currentScale)),
                        ((int) ( ((n+1) * 60 - y0) / currentScale)-(int) ((n * 60 - y0) / currentScale)));
            }

        // рельса
        g.setColor(Color.black);
        g.drawRect((int) ((railBlock.x - x0) / currentScale),(int) ((railBlock.y - y0) / currentScale),(int) (railBlock.width / currentScale), (int)(railBlock.height / currentScale));
        g.drawLine(railBlock.x - x0, (int) ((railBlock.y - y0) / currentScale), railBlock.x - x0, (int) ((railBlock.y - y0) / currentScale) + railBlock.height);
        System.out.println((railBlock.x - x0) / currentScale);
        System.out.println(railBlock.x - x0);
        System.out.println((int) ((railBlock.y - y0) / currentScale));
        System.out.println(y0 + this.getHeight());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // трамвайка
        g.setColor(Color.cyan);
        g.fillRect((int) ((tram.x - x0) / currentScale),(int) ((tram.y - y0) / currentScale),(int) (tram.width / currentScale), (int) (tram.height / currentScale));

        dt = System.currentTimeMillis() - prevTime;

        tram.y -= speed;
        if (tram.y <= - tram.height){
            tram.y = railBlock.height;
            y0 = (int) (railBlock.height - this.getHeight() * currentScale);
        }
        else if (y0 > railBlock.y && tram.y < railBlock.height - this.getHeight() / 2 * currentScale)
            y0 = (int) (tram.y - this.getHeight() / 2 * currentScale);
        else if (tram.y > railBlock.height / 2) y0 = (int) (railBlock.height - this.getHeight() * currentScale);

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
