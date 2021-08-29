package breakout;

import javafx.scene.shape.Circle;
import java.util.Random;

public class Ball extends Circle {

    private double speedX;
    private double speedY;
    private double angle;
    private Random rand;
    private double originalCenterX;
    private double originalCenterY;

    public Ball(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        originalCenterX = centerX;
        originalCenterY = centerY;
        rand = new Random();
        randomizeSpeed();
    }


    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
        angle = Math.atan2(speedY, this.speedX);
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
        angle = Math.atan2(this.speedY, speedX);
    }

    public double getAngle() {
        return angle;
    }
    public void setAngle(double angle) {
        this.angle = angle;
        setSpeed();
    }

    public void resetPosition() {
        setCenterY(originalCenterY);
        setCenterX(originalCenterX);
        randomizeSpeed();
    }

    private void randomizeAngle() {
        angle = rand.nextDouble() * 2 * Math.PI;
    }

    private void setSpeed() {
        speedX = 300*Math.cos(angle);
        speedY = 300*Math.sin(angle);
    }

    private void randomizeSpeed() {
        randomizeAngle();
        setSpeed();
    }


}
