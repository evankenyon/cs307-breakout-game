package breakout;

import javafx.scene.shape.Circle;
import java.util.Random;

/**
 * Purpose: Create a ball in the Breakout class that is used to break bricks by bouncing it off of a paddle.
 * Assumptions: JavaFX installed on device, values of vars do not push past practical limits
 * (e.g. centerX and centerY values that would cause the ball to spawn off-screen)
 * Dependencies: This class depends on several classes from the JavaFX platform and the Random class.
 * Example: Construct a ball object with a reasonable radius and center point to be used in the Breakout class, which
 * takes care of intersections with the borders of the scene (bounce off all except the bottom), bricks (destroys them),
 * and the paddle (bounces off of it).
 * Other details: note that the positive y direction is actually towards the bottom of the scene when manipulating the angle.
 *
 * @author Evan Kenyon
 */
public class Ball extends Circle {

    private double xVelocity;
    private double yVelocity;
    private double angle;
    private Random rand;
    private double originalCenterX;
    private double originalCenterY;
    private boolean isMoving;

    /**
     * Purpose: Construct a ball object which has the given dimensions desired
     *  and call a helper method to give the ball an initial, random speed
     * Assumptions: centerX, centerY, and radius are set such that the ball spawns within the scene
     * @param centerX the x value for the center of the ball
     * @param centerY the y value for the center of the ball
     * @param radius the radius of the ball
     */
    public Ball(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        originalCenterX = centerX;
        originalCenterY = centerY;
        isMoving = false;
        rand = new Random();
        randomizeSpeed();
    }

    /**
     * Purpose: Return the current x velocity
     * Assumptions: none
     * @return the current x velocity
     */
    public double getXVelocity() {
        return isMoving ? xVelocity : 0;
    }

    /**
     * Purpose: Set this object's xVelocity
     * Assumptions: current angle value properly reflects actual angle of this ball's movement,
     * the provided xVelocity argument is not null
     * @param xVelocity the value that this object's xVelocity is set to
     */
    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
        angle = Math.atan2(-yVelocity, this.xVelocity);
    }

    /**
     * Purpose: Return the current y velocity
     * Assumptions: none
     * @return the current y velocity
     */
    public double getYVelocity() {
        return isMoving ? yVelocity : 0;
    }

    /**
     * Purpose: Set this object's yVelocity
     * Assumptions: current angle value properly reflects actual angle of this ball's movement,
     * the provided yVelocity argument is not null
     * @param yVelocity the value that this object's yVelocity is set to
     */
    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
        angle = Math.atan2(-this.yVelocity, xVelocity);
    }

    /**
     * Purpose: Invert this object's yVelocity
     * Assumptions: none
     */
    public void reverseYVelocity() {
        setYVelocity(-yVelocity);
    }

    /**
     * Purpose: Invert this object's xVelocity
     * Assumptions: none
     */
    public void reverseXVelocity() {
        setXVelocity(-xVelocity);
    }

    /**
     * Purpose: Get this object's angle
     * Assumptions: none
     * @return this object's angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Purpose: Set this object's angle
     * Assumptions: the provided angle argument is not null
     * @param angle the value to set this object's angle to
     */
    public void setAngle(double angle) {
        this.angle = angle;
        setSpeed();
    }

    /**
     * Purpose: Reset the ball to its original position and set a new random starting speed
     * Assumptions: none
     */
    public void resetPosition() {
        setCenterY(originalCenterY);
        setCenterX(originalCenterX);
        randomizeSpeed();
    }

    private void randomizeAngle() {
        // Set the range to pi/6 through pi instead of 0 through pi to make
        // sure that the angle isn't so small that the ball is barely moving vertically
        angle = (rand.nextDouble() * (5*Math.PI)/6) + Math.PI/6;
    }

    private void setSpeed() {
        xVelocity = 300*Math.cos(angle);
        yVelocity = -300*Math.sin(angle);
    }

    private void randomizeSpeed() {
        randomizeAngle();
        setSpeed();
    }

    /**
     * Purpose: Set whether this ball is currently moving
     * Assumptions: the isMoving argument provided is not null
     * @param isMoving the boolean to set this object's isMoving variable to
     *                 (which determines if the ball is currently moving or not)
     */
    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
}
