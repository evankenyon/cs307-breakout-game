import javafx.scene.shape.Circle;
import java.util.Random;

public class Ball {

    private Circle circ;
    private double speedX;
    private double speedY;
    private double angle;
    private Random rand;

    public Ball(double centerX, double centerY, double radius) {
        circ = new Circle(centerX, centerY, radius);
        rand = new Random();
        angle = rand.nextDouble() * 2 * Math.PI;
        speedX = 100*Math.cos(angle);
        speedY = 100*Math.sin(angle);
    }

    public Circle getCirc() {
        return circ;
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

    public double getCenterX() {
        return circ.getCenterX();
    }

    public void setCenterX(double centerY) {
        circ.setCenterX(centerY);
    }

    public void setCenterY(double centerY) {
        circ.setCenterY(centerY);
    }

    public double getCenterY() {
        return circ.getCenterY();
    }

}
