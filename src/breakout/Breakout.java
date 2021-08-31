package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Purpose: The purpose of this class is to setup a breakout game, and to update the different
 * member objects and variables as necessary throughout the game (i.e. at each frame and at each
 * key input)
 * Assumptions: JavaFX installed on device, values of vars do not push past practical limits
 * (e.g. SCENE_SIZE isn't set to a value larger than the device screen itself).
 * Dependencies: This class depends on several classes from the JavaFX platform, the Ball claass, and the Bricks class.
 * Example: Use this class to set up and manage objects involved in a breakout style video game.
 * Specifically, running this class will do just that.
 * Other details: Due to JavaFX syntax, a main method is not needed for running this class. The start
 * method acts as its replacement.
 *
 * @author Evan Kenyon
 */
public class Breakout extends Application {

    private final int SCENE_SIZE = 800;
    private final int FONT_SIZE = 30;
    private final String FONT_TYPE = "Verdana";

    private Scene mainScene;
    private Scene gameOverScene;
    private Scene winScene;
    private Rectangle paddle;
    private Ball ball;
    private Bricks bricks;
    private int delayIntersectionFrames;
    private IntegerProperty lives;
    Stage primaryStage;
    Group primaryRoot;


    /**
     * Purpose: Start the game by calling helper methods which set up all the scenes,
     * the primary stage, and the timeline
     * Assumptions: JavaFX is installed on the device, set up scene methods are not changed in a way that
     * would remove any key objects (ex. the bricks) from the game or in a way that would return an invalid/null
     * Scene object, timeline method is not changed in such a way that would return an invalid/null timeline
     * Attributions: The setup for this method was borrowed from example_animation in course gitlab
     * @param stage the stage in which different scenes reside throughout the game
     */
    @Override
    public void start (Stage stage) {
        setupAllScenes();
        setupPrimaryStage(stage);
        setupTimeline();
    }

    private Scene setupGame() {
        setupMainSceneNodes();
        double scoreDisplayYPos = 600;
        Label scoreDisplay = setupDynamicDataDisplay("Score: ", bricks.getScore().asString(), scoreDisplayYPos);
        Label livesDisplay = setupDynamicDataDisplay("Lives: ", lives.asString(), scoreDisplayYPos + 50);
        delayIntersectionFrames = 2;
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(paddle, ball, bricks, scoreDisplay, livesDisplay);
        primaryRoot = root;
        return setupScene(root);
    }

    private Scene setupTextScene(String message) {
        // Text construction was borrowed from https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
        Text text = new Text(50, 50, message);
        text.setFont(Font.font (FONT_TYPE, FONT_SIZE));
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(text);
        return setupScene(root);
    }

    private Scene setupScene(Group root) {
        Scene scene = new Scene(root, SCENE_SIZE, SCENE_SIZE, Color.AZURE);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    // Label setup code was borrowed from https://stackoverflow.com/questions/56016866/how-do-i-output-updating-values-for-my-scoreboard
    private Label setupDynamicDataDisplay(String text, StringBinding data, double yVal) {
        Label display = new Label();
        display.setFont(new Font(FONT_TYPE, FONT_SIZE));
        // Found concat method and SimpleStringProperty from
        // https://docs.oracle.com/javase/8/javafx/api/javafx/beans/property/SimpleStringProperty.html
        display.textProperty().bind(new SimpleStringProperty(text).concat(data));
        display.setLayoutX(50);
        display.setLayoutY(yVal);
        return display;
    }

    private void step (double elapsedTime) {
        handleDelayingIntersectionFrames();
        handleIntersections();
        updateBallPosition(elapsedTime);
        handleNoBricksRemaining();
    }

    // Borrowed from example_animation in course gitlab
    private void handleKeyInput (KeyCode code) {
        int paddleSpeed = 25;
        switch (code) {
            case RIGHT -> paddle.setX(paddle.getX() + paddleSpeed);
            case LEFT -> paddle.setX(paddle.getX() - paddleSpeed);
            case SPACE -> ball.setIsMoving(true);
        }
    }

    // Borrowed from example_animation in course gitlab
    private boolean isIntersecting (Shape a, Shape b) {
        return b.getBoundsInParent().intersects(a.getBoundsInParent());
    }

    // Borrowed idea for checking if side collision from
    // https://stackoverflow.com/questions/8866389/java-gaming-collision-detection-side-collision-with-rectangles
    private boolean isSideCollision(Node stationaryNode, Ball ball) {
        return stationaryNode.getBoundsInParent().getMinY() < ball.getCenterY() && stationaryNode.getBoundsInParent().getMaxY() > ball.getCenterY();
    }

    private void handleBallIntersectingBounds() {
        if(ball.getCenterX() - ball.getRadius() <= 0) {
            ball.setCenterX(ball.getRadius());
            ball.reverseXVelocity();
        } else if (ball.getCenterX() + ball.getRadius() >= mainScene.getWidth()) {
            ball.setCenterX(mainScene.getWidth() - ball.getRadius());
            ball.reverseXVelocity();
        } else if (ball.getCenterY() - ball.getRadius() <= 0) {
            ball.setCenterY(ball.getRadius());
            ball.reverseYVelocity();
        } else if (ball.getCenterY() >= mainScene.getHeight()) {
            ball.resetPosition();
            handleLifeDecrement();
        }
    }

    private void handleLifeDecrement() {
        lives.set(lives.get() -1);
        ball.setIsMoving(false);
        if(lives.get() == 0) {
            primaryStage.setScene(gameOverScene);
        }
    }

    private void updateBallPosition(double elapsedTime) {
        ball.setCenterX(ball.getCenterX() + ball.getXVelocity() * elapsedTime);
        ball.setCenterY(ball.getCenterY() + ball.getYVelocity() * elapsedTime);
    }

    private void handlePaddleIntersectingBounds() {
        if(paddle.getX() <= 0) {
            paddle.setX(0);
        } else if (paddle.getX() + paddle.getWidth() >= mainScene.getWidth()) {
            paddle.setX(mainScene.getWidth() - paddle.getWidth());
        }
    }

    private void handleBallIntersectingPaddle() {
        if (isIntersecting(paddle, ball) ) {
            ball.reverseYVelocity();
            if (isSideCollision(paddle, ball)) {
                ball.reverseXVelocity();
            } else if (delayIntersectionFrames == 2){
                ball.setAngle(ball.getAngle() + Math.toRadians(0.5 * ((paddle.getBoundsInParent().getMinX() + paddle.getWidth() / 2) - ball.getCenterX())));
            }
        }
    }

    private void handleBallIntersectingBrick() {
        Node intersectedBrick = bricks.getBrickIntersecting(ball);
        if(intersectedBrick != null) {
            primaryRoot.getChildren().remove(intersectedBrick);
            mainScene.setRoot(primaryRoot);
            if(isSideCollision(intersectedBrick, ball)) {
                ball.reverseXVelocity();
            } else {
                ball.reverseYVelocity();
            }
        }
    }

    private void handleNoBricksRemaining() {
        if(!bricks.isBrickRemaining()) {
            primaryStage.setScene(winScene);
        }
    }

    private void setupPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setScene(mainScene);
        // size, frames per second, second delay, background, highlight, offset amount, mover, paddle color, and paddle color
        // borrowed from example_animation in course gitlab
        String title = "Breakout Game";
        primaryStage.setTitle(title);
        primaryStage.show();
    }

    private void setupAllScenes() {
        mainScene = setupGame();
        gameOverScene = setupTextScene("Game Over");
        winScene = setupTextScene("You win!");
    }

    private void setupTimeline() {
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        double secondDelay = 1.0 / 60;
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(secondDelay), e -> step(secondDelay)));
        animation.play();
    }

    private void setupMainSceneNodes() {
        // Rectangle constructor parameters from example_animation in course gitlab
        int offsetPaddleAmount = 50;
        int paddleWidth = 100;
        paddle = new Rectangle(SCENE_SIZE / 2 - paddleWidth / 2, SCENE_SIZE - offsetPaddleAmount, paddleWidth, 20);
        int offsetBallAmount = offsetPaddleAmount + 100;
        int brickSize = 40;
        double ballRadius = brickSize/2.5;
        ball = new Ball(SCENE_SIZE /2, SCENE_SIZE - offsetBallAmount, ballRadius);
        bricks = new Bricks(SCENE_SIZE, SCENE_SIZE, brickSize, brickSize, 0.1);
        lives = new SimpleIntegerProperty(3);
    }

    private void handleDelayingIntersectionFrames() {
        delayIntersectionFrames--;
        if(delayIntersectionFrames == 0) {
            delayIntersectionFrames = 2;
        }
    }

    private void handleIntersections() {
        handlePaddleIntersectingBounds();
        handleBallIntersectingBounds();
        handleBallIntersectingPaddle();
        handleBallIntersectingBrick();
    }

}
