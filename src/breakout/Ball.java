package breakout;

import javafx.scene.shape.Circle;
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
     * Assumptions:
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
     * Assumptions: xVelocity is not null
     * @return the current x velocity
     */
    public double getXVelocity() {
        return isMoving ? xVelocity : 0;
    }

    /**
     * Purpose: Set this object's xVelocity
     * Assumptions: the xVelocity argument provided is not null
     * @param xVelocity the value that this object's xVelocity is set to
     */
    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
        angle = Math.atan2(-yVelocity, this.xVelocity);
    }

    /**
     * Purpose: Return the current y velocity
     * Assumptions: yVelocity is not null
     * @return the current y velocity
     */
    public double getYVelocity() {
        return isMoving ? yVelocity : 0;
    }

    /**
     * Purpose: Set this object's yVelocity
     * Assumptions: the yVelocity argument provided is not null
     * @param yVelocity the value that this object's yVelocity is set to
     */
    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
        angle = Math.atan2(-this.yVelocity, xVelocity);
    }

    /**
     * Purpose: Invert this object's yVelocity
     * Assumptions: yVelocity is not null
     */
    public void reverseYVelocity() {
        setYVelocity(-yVelocity);
    }

    /**
     * Purpose: Invert this object's xVelocity
     * Assumptions: xVelocity is not null
     */
    public void reverseXVelocity() {
        setXVelocity(-xVelocity);
    }

    /**
     * Purpose: Get this object's angle
     * Assumptions: angle is not null
     * @return this object's angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Purpose: Set this object's angle
     * Assumptions: the angle argument provided is not null
     * @param angle the value to set this object's angle to
     */
    public void setAngle(double angle) {
        this.angle = angle;
        setSpeed();
    }

    /**
     * Purpose: Reset the ball to its original position and set a new random starting speed
     * Assumptions:
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
