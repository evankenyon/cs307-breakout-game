package breakout;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Purpose:
 * Assumptions:
 * Dependencies:
 * Example:
 * Other details:
 *
 * @author Evan Kenyon
 */
public class Bricks extends Group {

    // Integer Property code for score in this class was borrowed from
    // https://stackoverflow.com/questions/56016866/how-do-i-output-updating-values-for-my-scoreboard
    private Random rand;
    private List<Integer> blockedX;
    private List<Integer> blockedY;
    private int brickWidth;
    private int brickHeight;
    private int sceneWidth;
    private int sceneHeight;
    private IntegerProperty score;
    private double blockedRowOrColFreq;

    /**
     * Purpose: Construct a Bricks object which has the number of bricks (Rectangles) of size brickWidth x brickHeight
     * that fit into a sceneWidth x sceneHeight/2 area, with roughly blockRowOrColFreq of the rows/columns empty
     * Assumptions:
     * @param sceneWidth scene width, used so that bricks aren't generated outside of scene
     * @param sceneHeight scene height, used so that bricks aren't generated outside of scene
     * @param brickWidth width of each brick
     * @param brickHeight height of each brick
     * @param blockedRowOrColFreq frequency of a row or column being empty
     */
    public Bricks(int sceneWidth, int sceneHeight, int brickWidth, int brickHeight, double blockedRowOrColFreq) {
        rand = new Random();
        blockedX = new ArrayList<>();
        blockedY = new ArrayList<>();
        score = new SimpleIntegerProperty(0);
        this.brickWidth = brickWidth;
        this.brickHeight = brickHeight;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.blockedRowOrColFreq = blockedRowOrColFreq;
        addBlockedCoordinates();
        generateBricks();
    }

    private void addBlockedCoordinates() {
        addBlockedXCoordinates();
        addBlockedYCoordinates();
    }

    private void addBlockedXCoordinates() {
        for(int x = 0; x <= sceneWidth; x += brickWidth + 1) {
            if(rand.nextDouble() <= blockedRowOrColFreq) {
                blockedX.add(x);
            }
        }
    }

    private void addBlockedYCoordinates() {
        for(int y = 0; y <= sceneHeight/2; y += brickHeight + 1) {
            if(rand.nextDouble() <= blockedRowOrColFreq) {
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

    /**
     * Purpose: See if a shape is intersecting with a brick
     * Assumptions: shape is in the same scene as a brick, shape could conceivably intersect with a brick
     * @param shape the shape which is checked to see if its intersecting with a brick
     * @return
     */
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

    /**
     * Purpose: Check if any bricks haven't been hit by the ball yet
     * Assumptions: none
     * @return true if all bricks have been hit by the ball, false otherwise
     */
    public boolean isBrickRemaining() {
        return getChildren().size() != 0;
    }

    /**
     * Purpose: Return the number of bricks already hit by the ball
     * Assumptions: none
     * @return the number of bricks already hit by the ball
     */
    public IntegerProperty getScore() {
        return score;
    }
}
