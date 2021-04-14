import com.sun.xml.internal.bind.v2.TODO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Frame extends JFrame implements MouseWheelListener {

    double speed = 17;
    //TODO: double speed = 0;
    double acceleration = 2; // m / sec ^ 2


    Random rand = new Random(24728);
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
    boolean graphicsOn = true;
    int nextBlocks;
    int futurePositions;
    double ddt;
    int timeOfOnePopulation;
    int maxSpeed;
    Background background = new Background();
    ArrayList<Tree> trees = new ArrayList<>();
    ArrayList<Tree> treesToRemove = new ArrayList<>();
    ArrayList<BufferedImage> personImage = new ArrayList<>();
    double personImagePosition = 0;




    Frame(int nextBlocks, int futurePositions, double ddt, int timeOfOnePopulation, int maxSpeed) throws IOException {
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
        y0 = 0; // 0 m
        railBuilder = new RailBuilder((int) (x0 + toMeters(this.getWidth()) / 2 - RailBlock.width / 2), (int) (y0 + this.getHeight() / currentScale - 5), Direction.UP); // x = 98 m, y = 195 m
        tram = new Tram(railBuilder.rail);
        //prevTime = System.currentTimeMillis();

        people = new ArrayList<>();
        people.add(new Person(this));
        deadPeople = new ArrayList<>();

        for (int i = 0; i < 22; i++){
            personImage.add(ImageIO.read(new File("res/zombie.png")).getSubimage(125 * i, 0, 125, 125));
        }


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

        Graphics2D g2d = (Graphics2D) g;

        if (tram.y < toMeters(getHeight()) / 2.0)
            y0 = tram.y - this.getHeight() / 2.0 / currentScale;
        x0 = (tram.x - toMeters(this.getWidth()) / 2.0 + RailBlock.width / 2.0);


        Random  r = new Random(3452);
        while (railBuilder.rail.getLast().y > y0 - 2 * RailBlock.length)
        {
            int isRotate = r.nextInt(100);
            if (isRotate > 5)
                railBuilder.move();
            else railBuilder.rotate(new Random().nextBoolean()); // 23435
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
             if (!railBlock.isRotate) {
                 if (railBlock.direction == Direction.UP) {
                     g.setColor(new Color(0xB97A57));
                     for (int leng = 0; leng < RailBlock.length; leng ++)
                     {
                         g.fillRect(toPixels(railBlock.x - x0) - 5, toPixels(railBlock.y - leng - y0) - 8, toPixels(RailBlock.length) + 11, 4);
                     }

                     g.setColor(new Color(0x808080));
                     g.fillRect(toPixels(railBlock.x - x0) - 3, toPixels(railBlock.y - y0 - RailBlock.length), 7, toPixels(RailBlock.length));
                     g.fillRect(toPixels(railBlock.x - x0 + RailBlock.width) - 3, toPixels(railBlock.y - y0 - RailBlock.length), 7, toPixels(RailBlock.length));

                     g.setColor(new Color(0xFFC0C0C0, true));
                     g.fillRect(toPixels(railBlock.x - x0) - 1, toPixels(railBlock.y - y0 - RailBlock.length), 3, toPixels(RailBlock.length));
                     g.fillRect(toPixels(railBlock.x - x0 + RailBlock.width) - 1, toPixels(railBlock.y - y0 - RailBlock.length), 3, toPixels(RailBlock.length));

                 } else if (railBlock.direction == Direction.RIGHT) {

                     g.setColor(new Color(0xB97A57));
                     for (int leng = 0; leng < RailBlock.length; leng ++)
                     {
                         g.fillRect(toPixels(railBlock.x - leng - x0) + 25, toPixels(railBlock.y - y0) - 5, 4, toPixels(RailBlock.length) + 11);
                     }

                     g.setColor(new Color(0x808080));
                     g.fillRect(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0) - 3, toPixels(RailBlock.length), 7);
                     g.fillRect(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0 + RailBlock.length) - 3, toPixels(RailBlock.length), 7);
//
                     g.setColor(new Color(0xFFC0C0C0, true));
                     g.fillRect(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0) - 1, toPixels(RailBlock.length), 3);
                     g.fillRect(toPixels(railBlock.x - x0), toPixels(railBlock.y - y0 + RailBlock.length) - 1, toPixels(RailBlock.length), 3);

                 } else {
                     g.setColor(new Color(0xB97A57));
                     for (int leng = 0; leng < RailBlock.length; leng++)
                     {
                         g.fillRect(toPixels(railBlock.x - leng - x0) - 10, toPixels(railBlock.y - y0 - RailBlock.length) - 5, 4, toPixels(RailBlock.length) + 11);
                     }

                     g.setColor(new Color(0x808080));
                     g.fillRect(toPixels(railBlock.x - x0) - toPixels(RailBlock.length), toPixels(railBlock.y - y0) - 3, toPixels(RailBlock.length), 7);
                     g.fillRect(toPixels(railBlock.x - x0) - toPixels(RailBlock.length), toPixels(railBlock.y - y0 - RailBlock.length) - 3, toPixels(RailBlock.length), 7);
//
                     g.setColor(new Color(0xFFC0C0C0, true));
                     g.fillRect(toPixels(railBlock.x - x0) - toPixels(RailBlock.length), toPixels(railBlock.y - y0) - 1, toPixels(RailBlock.length), 3);
                     g.fillRect(toPixels(railBlock.x - x0) - toPixels(RailBlock.length), toPixels(railBlock.y - y0 - RailBlock.length) - 1, toPixels(RailBlock.length), 3);
                 }
             } else {
                 int changeX = 1;
                 int changeY = 1;
                 if (railBlock.direction == Direction.LEFT){
                     changeX = -1;
                 } else if (railBlock.direction == Direction.UP && railBlock != railBuilder.rail.getFirst()) {
                     if (railBuilder.rail.get(railBuilder.rail.indexOf(railBlock) - 1).direction == Direction.RIGHT) {
                        changeX = -1;
                        changeY = -1;
                     }
                     else{
                         changeY = -1;
                     }
                 }
                 for (int alpha = 0; alpha <= 90; alpha += 30){
                     g.setColor(new Color(0xB97A57));
                     g2d.setStroke(new BasicStroke(4));

                     int xStart = toPixels(railBlock.xCenter - x0) - changeX * 9;
                     int yStart = toPixels(railBlock.yCenter - y0) - changeY * 10;
                     g.drawLine(xStart,yStart ,(int) (xStart - changeX * Math.cos(Math.toRadians(alpha)) * (toPixels(RailBlock.length) + 11)), (int) (yStart - changeY * Math.sin(Math.toRadians(alpha)) * (toPixels(RailBlock.length) + 11)));
                     g2d.setStroke(new BasicStroke(1));
                 }

                 g.setColor(new Color(0x808080));
                 g2d.setStroke(new BasicStroke(6));

                 g.drawArc(toPixels(railBlock.xCenter - RailBlock.width - RailBuilder.R - x0), toPixels(railBlock.yCenter - RailBlock.width - RailBuilder.R - y0), toPixels(RailBlock.width + RailBuilder.R) * 2, toPixels(RailBlock.width + RailBuilder.R) * 2, railBlock.ang1, railBlock.ang2);
                 g.drawArc(toPixels(railBlock.xCenter - RailBuilder.R - x0), toPixels(railBlock.yCenter - RailBuilder.R - y0), toPixels(RailBuilder.R) * 2, toPixels(RailBuilder.R) * 2, railBlock.ang1, railBlock.ang2);

                 g.setColor(new Color(0xFFC0C0C0, true));
                 g2d.setStroke(new BasicStroke(3));
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
//                    if (graphicsOn) g.drawImage(personImage.get((int) personImagePosition), (int) ((person.x - x0) * currentScale), (int) ((person.y - y0) * currentScale),(int) (person.width * currentScale), (int) (person.height * currentScale), null );
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

        if (!trees.isEmpty()){
            for (Tree tree: trees) {
                background.addBackground(g, this, tree);
                if (tree.x < x0 - 50 || tree.x > x0 + mapBlock.width + 50 || tree.y < y0 - 50 || tree.y > y0 + mapBlock.height){
                    treesToRemove.add(tree);
                }
            }
        }
        trees.removeAll(treesToRemove);


        double minDiff = 100000;
        Person closestPerson = null;
        try {
            closestPerson = new Person(1000, 1000, 90);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Person person : people) {
                double diff = Math.abs((person.x + person.width / 2.0) - (tram.x + tram.width / 2.0)) +
                        Math.abs((person.y + person.height / 2.0) - (tram.y + tram.height / 2.0));
                if (diff < minDiff) {
                    minDiff = diff;
                    try {
                        closestPerson = new Person(person.x, person.y, person.angleDeg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

        if (DT % 200 == 0) {
            people.add(new Person(this));
            trees.add(new Tree(this));
        }
        if (DT > timeOfOnePopulation && !graphicsOn) {
//            System.out.println("nextBlocks = " + nextBlocks + "; futurePositions = " + futurePositions + "; ddt = " + ddt + ";  points = " + points);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

        personImagePosition += 0.1;
        if (personImagePosition + 1 > personImage.size() * 1.0)
            personImagePosition = 0;


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
