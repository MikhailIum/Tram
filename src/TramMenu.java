import com.sun.javafx.robot.impl.FXRobotHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class TramMenu {

    int points = 0;
    int numberOfScoreDigits;
    boolean gameOver = false;
    String bestResultString;
    int numberOfBestScoreDigits;
    JPanel panel = new JPanel();
    int restartImageX = 0;
    int restartImageY = 0;
    BufferedImage restartImage = ImageIO.read(new File("res/resetBtnImage.png"));


    TramMenu(Frame frame) throws IOException {
        frame.getContentPane().add(panel);
    }

    public void drawMenu(Frame frame, Graphics g) throws IOException {
        BufferedImage resetImage = ImageIO.read(new File("res/resetImage.png"));
        JButton resetBtn = new JButton();


        if (!gameOver) frame.remove(resetBtn);

        // create the font
        Font customFont = Font.getFont("TimesRoman");
            try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/Samson.ttf")).deriveFont(Font.PLAIN, 100);
        } catch (FontFormatException | IOException e) {
            System.out.println(e);
        }


        // скорость
        g.setFont(customFont);
        g.setColor(new Color(0x39A4AD));
        g.drawString(String.format("%.2f", frame.speed), frame.getWidth() - 210, frame.getHeight() - 50);


        //счёт
        int x = points;
            while (x > 0) {
            numberOfScoreDigits++;
            x /= 10;
        }
        String pointsString = String.valueOf(points);
            if (!gameOver) {
                g.setFont(customFont);
                g.setColor(new Color(0xFFFFFF));
                g.drawString(pointsString, frame.getWidth() - 100 - 20 * (numberOfScoreDigits - 1), 100);
        }
        numberOfScoreDigits = 0;


            if(gameOver){
            // менюшка
            g.drawImage(resetImage, frame.getWidth() / 2 - 75,frame.getHeight() / 2 - 105, null);
            g.setFont(customFont.deriveFont(Font.PLAIN, 50));
            g.setColor(new Color(0xF97757));
            g.drawString("Game over", frame.getWidth() / 2 - 100, frame.getWidth() / 2 - 170);

            // очки
            x = points;
            if (x == 0) numberOfScoreDigits++;
            while (x > 0) {
                numberOfScoreDigits++;
                x /= 10;
            }
            g.setFont(customFont.deriveFont(Font.PLAIN, 70));
            g.setColor(new Color(0xFFFFFF));
            g.drawString(pointsString, frame.getWidth() / 2 - 10 * (numberOfScoreDigits - 1), frame.getWidth() / 2); // 305
            numberOfScoreDigits = 0;

            restartImageX = frame.getWidth() / 2 - 95;
            restartImageY = frame.getHeight() / 2 - 75 + resetImage.getHeight();
            g.drawImage(restartImage, restartImageX,restartImageY, null);



            // кнопка рестарта
//            ImageIcon resetBtnImage = new ImageIcon("res/resetBtnImage.png");

//            resetBtn.setSize(resetBtnImage.getIconWidth(), resetBtnImage.getIconHeight());
//            resetBtn.setBorder(null);
//            resetBtn.setLocation(135,450);
//            resetBtn.setIcon(resetBtnImage);

//            panel.add(resetBtn);

            try {
                bestScore();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            g.drawString(bestResultString, frame.getWidth() / 2 - 20 * (numberOfBestScoreDigits - 1), frame.getWidth() / 2 + 85);

            resetBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        restart(frame);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });
        }
}

    public void restart(Frame frame) throws IOException {
        frame.isRestarted = true;
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    public void bestScore() throws FileNotFoundException {
        String fileString;
        int bestResult = 0;
        FileReader fileReader = new FileReader("leaderboard.txt");
        Scanner scanner = new Scanner(fileReader);
        bestResultString = "";


        if (scanner.hasNext()) {
            fileString = scanner.nextLine();
            for (int i = 0; i < fileString.length(); i++) {
                if (fileString.charAt(i) >= '0' && fileString.charAt(i) <= '9') {
                    bestResult = bestResult * 10 + fileString.charAt(i) - 48;
                    bestResultString = Integer.toString(bestResult);
                }
            }
        }


        if (points > bestResult) {
            try {
                FileWriter fileWriter = new FileWriter("leaderboard.txt");
                fileWriter.write(Integer.toString(this.points));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bestResultString = Integer.toString(points);
        }


        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        numberOfBestScoreDigits = bestResultString.length();
    }

    public void points(){
        points++;
    }

    public void deadPerson(){
        points -= 100;
        if (points < 0) points = 0;
    }

}
