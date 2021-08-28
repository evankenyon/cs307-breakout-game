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
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void resetPosition() {
        setCenterY(originalCenterY);
        setCenterX(originalCenterX);
        randomizeSpeed();
    }

    private void randomizeSpeed() {
        angle = rand.nextDouble() * 2 * Math.PI;
        speedX = 300*Math.cos(angle);
        speedY = 300*Math.sin(angle);
    }

}
