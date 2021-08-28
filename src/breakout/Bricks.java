package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Bricks extends Group {

//    private int size;
    private Random rand;
    private ArrayList<Integer> blockedX;
    private ArrayList<Integer> blockedY;

    public Bricks(int width, int height, double frequency) {
        rand = new Random();
        blockedX = new ArrayList<Integer>();
        blockedY = new ArrayList<Integer>();
        for(int x = 0; x <= 800; x += width + 1) {
            if(rand.nextDouble() <= frequency) {
                blockedX.add(x);
            }
        }
        for(int y = 0; y <= 400; y += height + 1) {
            if(rand.nextDouble() <= frequency) {
                blockedY.add(y);
            }
        }

        for(int x = 0; x <= 800; x += width + 1) {
            for(int y = 0; y <= 400; y += height + 1) {
                if(! (blockedX.contains(x) || blockedY.contains(y))) {
                    getChildren().add(new Rectangle(x, y, width, height));
                }
            }
        }
    }
}
