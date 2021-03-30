import com.sun.xml.internal.bind.v2.TODO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Frame extends JFrame implements MouseWheelListener {

    double speed = 17;
    //TODO: double speed = 0;
    double acceleration = 2; // m / sec ^ 2


    double scaleSpeed = 0.03;
    double x0;
    double y0;
    Tram tram;
    MapBlock mapBlock;
    double currentScale;
    double targetScale;
    long dt;
    ArrayList<Person> people;
    ArrayList<Person> deadPeople;
    ArrayList<Person> peopleToBeRemoved = new ArrayList<>();
    RailBuilder railBuilder;
    int DT = 0;
    int points = 0;
    boolean graphicsOn = false;
    int nextBlocks;
    int futurePositions;
    double ddt;
    int timeOfOnePopulation;
    int maxSpeed;



    Frame(int nextBlocks, int futurePositions, double ddt, int timeOfOnePopulation, int maxSpeed){
        this.setSize(1000, 1000);
        this.setTitle("Tram");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocation(500, 50);
        this.setVisible(true);

        if (graphicsOn) {
            this.nextBlocks = 7;
            this.futurePositions = 200;
            this.ddt = 0.5;
        }
        else {
            this.nextBlocks = nextBlocks;
            this.futurePositions = futurePositions;
            this.ddt = ddt;
        }

        this.timeOfOnePopulation = timeOfOnePopulation;
        this.maxSpeed = maxSpeed;


        currentScale = targetScale = 20;
        mapBlock = new MapBlock(toMeters(getHeight()), toMeters(getWidth())); // 50, 50
        x0 = 0; // 0 m
        //y0 = mapBlock.height - toMeters(this.getHeight()); // 50 m
        y0 = 0; // 0 m
        railBuilder = new RailBuilder((int) (x0 + toMeters(this.getWidth()) / 2 - RailBlock.width / 2), (int) (y0 + this.getHeight() / currentScale - 5), Direction.UP); // x = 98 m, y = 195 m
        tram = new Tram(railBuilder.rail);
        //prevTime = System.currentTimeMillis();

        people = new ArrayList<>();
        people.add(new Person(this));
        deadPeople = new ArrayList<>();

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


        if (tram.y < toMeters(getHeight()) / 2.0)
            y0 = tram.y - this.getHeight() / 2.0 / currentScale;
        x0 = (tram.x - toMeters(this.getWidth()) / 2.0 + RailBlock.width / 2.0);


        Random  r = new Random();
        while (railBuilder.rail.getLast().y > y0 - 2 * RailBlock.length)
        {
            int isRotate = r.nextInt(100);
            if (isRotate > 5)
                railBuilder.move();
            else railBuilder.rotate(new Random().nextBoolean());
        }

        while (railBuilder.rail.get(1).y > y0 + toMeters(getHeight())){
            railBuilder.deleteFirst();
        }



        if(currentScale > targetScale + scaleSpeed)
            currentScale -= scaleSpeed;
        else if (currentScale < targetScale - scaleSpeed)
            currentScale += scaleSpeed;


        int cellSize = 100;

        for (int m = -cellSize; m < getHeight() + cellSize; m += cellSize)
            for(int n = -cellSize; n < getWidth() + cellSize; n += cellSize) {


                double xStart = - ((toPixelsD(x0)) % cellSize) + n;
                double yStart = - ((toPixelsD(y0)) % cellSize) + m;

                if (graphicsOn) {
                    g.setColor(new Color(161, 208, 73));
                    g.fillRect((int) xStart, (int) yStart, cellSize, cellSize);
                    g.setColor(new Color(169, 214, 81));
                    g.fillRect((int) xStart + cellSize / 20, (int) yStart + cellSize / 20, cellSize * 18 / 20, cellSize * 18 / 20);
                }
            }

        // рельса
        if (graphicsOn){
         for (RailBlock railBlock: railBuilder.rail) {
             g.setColor(Color.black);
             if (!railBlock.isRotate) {
                 if (railBlock.direction == Direction.UP) {
                     g.drawLine(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0 - RailBlock.length), toPixels(railBlock.x - x0), toPixels(railBlock.y - y0));
                     g.drawLine(toPixels(railBlock.x - x0 + RailBlock.width) - 1, toPixels(railBlock.y - y0 - RailBlock.length), toPixels(railBlock.x - x0 + RailBlock.width) - 1, toPixels(railBlock.y - y0));
                 } else if (railBlock.direction == Direction.DOWN) {
                     g.drawLine(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0), toPixels(railBlock.x - x0), toPixels(railBlock.y - y0 + RailBlock.length));
                     g.drawLine(toPixels(railBlock.x - x0 - RailBlock.width) - 1, toPixels(railBlock.y - y0), toPixels(railBlock.x - x0 - RailBlock.width) - 1, toPixels(railBlock.y - y0 + RailBlock.length));
                 } else if (railBlock.direction == Direction.RIGHT) {
                     g.drawLine(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0), toPixels(railBlock.x - x0 + RailBlock.length), toPixels(railBlock.y - y0));
                     g.drawLine(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0 + RailBlock.width) - 1, toPixels(railBlock.x - x0 + RailBlock.length), toPixels(railBlock.y - y0 + RailBlock.width) - 1);
                 } else {
                     g.drawLine(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0), toPixels(railBlock.x - x0 - RailBlock.length), toPixels(railBlock.y - y0));
                     g.drawLine(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0 - RailBlock.width) - 1, toPixels(railBlock.x - x0 - RailBlock.length), toPixels(railBlock.y - y0 - RailBlock.width) - 1);
                 }
             } else {
                 g.drawArc(toPixels(railBlock.xCenter - RailBlock.width - RailBuilder.R - x0), toPixels(railBlock.yCenter - RailBlock.width - RailBuilder.R - y0), toPixels(RailBlock.width + RailBuilder.R) * 2, toPixels(RailBlock.width + RailBuilder.R) * 2, railBlock.ang1, railBlock.ang2);
                 g.drawArc(toPixels(railBlock.xCenter - RailBuilder.R - x0), toPixels(railBlock.yCenter - RailBuilder.R - y0), toPixels(RailBuilder.R) * 2, toPixels(RailBuilder.R) * 2, railBlock.ang1, railBlock.ang2);
             }
         }
         }

        // Людишки(бедолаги)
            for (Person person: people){
                person.checkCollision(this);

                if (graphicsOn) g.setColor(person.color);

                if (person.color == Color.orange) {
                    person.x += dt * 1.0 / 1000 * Math.cos(Math.toRadians(person.angleDeg)) * person.speed + person.acceleration * dt * dt / 1000 / 1000 / 2;
                    person.y += dt * 1.0 / 1000 * Math.sin(Math.toRadians(person.angleDeg)) * person.speed + person.acceleration * dt * dt / 1000 / 1000 / 2;
                    if (graphicsOn) g.fillOval((int) ((person.x - x0) * currentScale), (int) ((person.y - y0) * currentScale), (int) (person.width * currentScale), (int) (person.height * currentScale));
                }


                if (person.x < x0 - 50 || person.x > x0 + mapBlock.width + 50 || person.y < y0 - 50 || person.y > y0 + mapBlock.height){
                    peopleToBeRemoved.add(person);
                }

            }
            for (Person person: deadPeople){
                if (graphicsOn) {
                    g.setColor(person.color);
                    g.fillOval((int) ((person.x - x0) * currentScale), (int) ((person.y - y0) * currentScale), (int) (person.width * currentScale), (int) (person.height * currentScale) + 5);
                }

            }
        people.removeAll(peopleToBeRemoved);


        // трамвайка
        if (graphicsOn) g.setColor(Color.cyan);
            double startX = ((tram.x - x0) * currentScale);
            double startY = ((tram.y - y0) * currentScale);
            double widthX = (tram.width * currentScale);
            double heightY = (tram.height * currentScale);
            int x1 = (int) startX;
            int y1 = (int) startY;
            int x2 = (int) (startX + widthX);
            int y2 = (int) (startY + heightY);
//            g.fillRect(x1, y1, x2 - x1, y2 - y1);
            // TODO: вернуть трамвай
            if (graphicsOn) g.fillOval(x1, y1, 40, 40);

//        dt = System.currentTimeMillis() - prevTime;

        dt = 4;
        DT += 4;


        if (!tram.currentRailBlock.isRotate) {
                tram.x += tram.currentRailBlock.direction.dx * (speed * dt / 1000 + acceleration * dt / 1000 * dt / 1000 / 2);
                tram.y += tram.currentRailBlock.direction.dy * (speed * dt / 1000 + acceleration * dt / 1000 * dt / 1000 / 2);

            if ((tram.currentRailBlock.direction == Direction.UP && tram.y + 1 < tram.currentRailBlock.yCenter - RailBlock.length) ||
                    (tram.currentRailBlock.direction == Direction.RIGHT && tram.x + 1 > tram.currentRailBlock.x + RailBlock.length) ||
                    (tram.currentRailBlock.direction == Direction.LEFT && tram.x + 1 < tram.currentRailBlock.x - RailBlock.length)) {
                tram.currentRailBlock = tram.rail.get(tram.rail.indexOf(tram.currentRailBlock) + 1);
                points += 2;
            }
        }
        else {
            if (tram.currentRailBlock.direction == Direction.UP) {

                double a = tram.rail.get(tram.rail.indexOf(tram.currentRailBlock) - 1).direction.dx * (tram.x + 1) - tram.rail.get(tram.rail.indexOf(tram.currentRailBlock) - 1).direction.dx * tram.currentRailBlock.xCenter;
                double b = (tram.y + 1) - tram.currentRailBlock.yCenter;
                tram.ang = Math.atan(a / b);


                tram.y -= Math.abs(Math.sin(tram.ang) * (speed * dt / 1000 + acceleration * dt / 1000 * dt / 1000 / 2));
                tram.x += tram.rail.get(tram.rail.indexOf(tram.currentRailBlock) - 1).direction.dx * Math.cos(tram.ang) * (speed * dt / 1000 + acceleration * dt / 1000 * dt / 1000 / 2);

                if (tram.y + 1 < tram.currentRailBlock.yCenter) {
                    tram.currentRailBlock = tram.rail.get(tram.rail.indexOf(tram.currentRailBlock) + 1);
                    points += 2;
                }

            } else {
                double a = tram.currentRailBlock.direction.dx * tram.currentRailBlock.xCenter - tram.currentRailBlock.direction.dx * (tram.x + 1);
                double b = tram.currentRailBlock.y - (tram.y + 1);
                tram.ang = Math.atan(b / a);


                tram.y -= Math.cos(tram.ang) * (speed * dt / 1000 + acceleration * dt / 1000 * dt / 1000 / 2);
                tram.x += tram.currentRailBlock.direction.dx * Math.abs(Math.sin(tram.ang) * (speed * dt / 1000 + acceleration * dt / 1000 * dt / 1000 / 2));

                if (tram.currentRailBlock.direction == Direction.RIGHT && tram.x > tram.currentRailBlock.x + RailBlock.length || tram.currentRailBlock.direction == Direction.LEFT && tram.x < tram.currentRailBlock.x - RailBlock.length) {
                    tram.currentRailBlock = tram.rail.get(tram.rail.indexOf(tram.currentRailBlock) + 1);
                    points += 2;
                }
            }
        }

        double minDiff = 100000;
            Person closestPerson = new Person(1000, 1000, 90);
            for (Person person : people) {
                double diff = Math.abs((person.x + person.width / 2.0) - (tram.x + tram.width / 2.0)) +
                        Math.abs((person.y + person.height / 2.0) - (tram.y + tram.height / 2.0));
                if (diff < minDiff) {
                    minDiff = diff;
                    closestPerson = new Person(person.x, person.y, person.angleDeg);
                }

            RailBlock nextBlockInfo;
            RailBlock nextBlock;
            boolean danger = false;
            for (int personPosition = 0; personPosition < futurePositions; personPosition++) {
                for (int i = 0; i <= nextBlocks; i++) {
                    nextBlockInfo = railBuilder.rail.get(tram.rail.indexOf(tram.currentRailBlock) + i);
                    nextBlock = new RailBlock(nextBlockInfo.x, nextBlockInfo.y, nextBlockInfo.direction, nextBlockInfo.ang1, nextBlockInfo.ang2, nextBlockInfo.isRotate, nextBlockInfo.xCenter, nextBlockInfo.yCenter);

                    int nextBlockWidth = RailBlock.width;
                    if (nextBlock.isRotate){
                        nextBlockWidth++;
                        if (nextBlock.direction == Direction.RIGHT){
                            nextBlock.y -= nextBlockWidth;
                        } else if (nextBlock.direction == Direction.LEFT){
                            nextBlock.x += RailBlock.width;
                        } else if (nextBlock.direction == Direction.UP){
                            if (nextBlock.x + nextBlockWidth == nextBlock.xCenter) {
                                nextBlock.x -= nextBlockWidth;
                            } else{
                                nextBlock.y += RailBlock.width;
                            }
                        }

                    }

                    if ((nextBlock.direction == Direction.UP && closestPerson.x + closestPerson.width >= nextBlock.x - 2 && closestPerson.x <= nextBlock.x + nextBlockWidth + 2 && closestPerson.y <= nextBlock.y && closestPerson.y + closestPerson.height >= nextBlock.y - nextBlockWidth)
                    || (nextBlock.direction == Direction.RIGHT && closestPerson.x + closestPerson.width >= nextBlock.x && closestPerson.x <= nextBlock.x + nextBlockWidth && closestPerson.y <= nextBlock.y + nextBlockWidth + 2 && closestPerson.y + closestPerson.height >= nextBlock.y - 2)
                    || (nextBlock.direction == Direction.LEFT && closestPerson.x + closestPerson.width >= nextBlock.x - nextBlockWidth && closestPerson.x <= nextBlock.x && closestPerson.y <= nextBlock.y + 2 && closestPerson.y + closestPerson.height >= nextBlock.y - nextBlockWidth - 2)){
                        danger = true;
                    }

                    closestPerson.x += ddt / 1000 * Math.cos(Math.toRadians(closestPerson.angleDeg)) * closestPerson.speed + closestPerson.acceleration * ddt * ddt / 1000 / 1000 / 2;
                    closestPerson.y += ddt / 1000 * Math.sin(Math.toRadians(closestPerson.angleDeg)) * closestPerson.speed + closestPerson.acceleration * ddt * ddt / 1000 / 1000 / 2;
                }
            }

            if (danger) speed -= acceleration * dt / 1000;
            else if (speed <= maxSpeed) speed += acceleration * dt / 1000;
            if (speed < 0) speed = 0;
        }

        if (DT % 200 == 0) people.add(new Person(this));
        if (DT > timeOfOnePopulation && !graphicsOn) {
//            System.out.println("nextBlocks = " + nextBlocks + "; futurePositions = " + futurePositions + "; ddt = " + ddt + ";  points = " + points);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        g.dispose();                // Освободить все временные ресурсы графики (после этого в нее уже нельзя рисовать)
        bufferStrategy.show();      // Сказать буферизирующей стратегии отрисовать новый буфер (т.е. поменять показываемый и обновляемый буферы местами)

        //prevTime = System.currentTimeMillis();

        repaint();
    }

    int toPixels(double xMeters){
        return (int) (xMeters * currentScale);
    }

    int toMeters(int xPixels){
        return (int) (xPixels / currentScale);
    }

    double toPixelsD(double xMeters){
        return (xMeters * currentScale);
    }

    double toMetersD(double xPixels){
        return (xPixels / currentScale);
    }



    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
            if (targetScale <= 30) targetScale -= e.getWheelRotation() * e.getScrollAmount() / 100.0;
            else targetScale = 30;
            if (targetScale >= 10) targetScale -= e.getWheelRotation() * e.getScrollAmount() / 100.0;
            else targetScale = 10;
            targetScale = Math.round(targetScale * 100.0) / 100.0;
        }
    }
}
