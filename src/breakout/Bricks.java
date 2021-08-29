package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.Random;

public class Bricks extends Group {

//    private int size;
    private Random rand;
    private ArrayList<Integer> blockedX;
    private ArrayList<Integer> blockedY;
    private int brickWidth;
    private int brickHeight;
    private int sceneWidth;
    private int sceneHeight;
    private int score;
    private double frequency;

    public Bricks(int sceneWidth, int sceneHeight, int brickWidth, int brickHeight, double frequency) {
        rand = new Random();
        blockedX = new ArrayList<Integer>();
        blockedY = new ArrayList<Integer>();
        score = 0;
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.frequency = frequency;
        addBlockedCoordinates();
        generateBricks();
    }

    private void addBlockedCoordinates() {
        addBlockedXCoordinates();
        addBlockedYCoordinates();
    }

    private void addBlockedXCoordinates() {
        for(int x = 0; x <= sceneWidth; x += brickWidth + 1) {
            if(rand.nextDouble() <= frequency) {
                blockedX.add(x);
            }
        }
    }

    private void addBlockedYCoordinates() {
        for(int y = 0; y <= sceneHeight/2; y += brickHeight + 1) {
            if(rand.nextDouble() <= frequency) {
                blockedY.add(y);
            }
        }
    }

    private void generateBricks() {
        for(int x = 0; x <= sceneWidth; x += brickWidth + 1) {
            for(int y = 0; y <= sceneHeight/2; y += brickHeight + 1) {
                if(! (blockedX.contains(x) || blockedY.contains(y))) {
                    getChildren().add(new Rectangle(x, y, brickWidth, brickHeight));
                }
            }
        }
    }

    public Node getBrickIntersecting(Shape shape) {
        for(Node brick : getChildren()) {
            if(shape.getBoundsInParent().intersects(brick.getBoundsInParent())) {
                Node oldBrick = brick;
                getChildren().remove(brick);
                score++;
                return oldBrick;
            }
        }
        return null;
    }

    public boolean isBrickRemaining() {
        return getChildren().size() != 0;
    }
}
