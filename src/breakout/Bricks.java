package breakout;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.Random;

public class Bricks extends Group {

    // Integer Property code for score in this class was borrowed from
    // https://stackoverflow.com/questions/56016866/how-do-i-output-updating-values-for-my-scoreboard
    private Random rand;
    private ArrayList<Integer> blockedX;
    private ArrayList<Integer> blockedY;
    private int brickWidth;
    private int brickHeight;
    private int sceneWidth;
    private int sceneHeight;
    private IntegerProperty score;
    private double frequency;

    public Bricks(int sceneWidth, int sceneHeight, int brickWidth, int brickHeight, double frequency) {
        rand = new Random();
        blockedX = new ArrayList<Integer>();
        blockedY = new ArrayList<Integer>();

        score = new SimpleIntegerProperty(0);
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
                    Rectangle newBrick = new Rectangle(x, y, brickWidth, brickHeight);
                    // Made range for colors 0.1 to 1.0 instead of generic 0.0 to 1.0 so that bricks wouldn't be hard to see
                    // Based off of random color generator found at
                    // https://stackoverflow.com/questions/35715283/set-text-to-random-color-opacity-javafx/35715848
                    newBrick.setFill(Color.color((Math.random() * 0.9) + 0.1, (Math.random() * 0.9) + 0.1, (Math.random() * 0.9) + 0.1));
                    getChildren().add(newBrick);
                }
            }
        }
    }

    public Node getBrickIntersecting(Shape shape) {
        for(Node brick : getChildren()) {
            if(shape.getBoundsInParent().intersects(brick.getBoundsInParent())) {
                Node oldBrick = brick;
                getChildren().remove(brick);
                score.set(score.get() + 1);
                return oldBrick;
            }
        }
        return null;
    }

    public boolean isBrickRemaining() {
        return getChildren().size() != 0;
    }

    public IntegerProperty getScore() {
        return score;
    }
}
