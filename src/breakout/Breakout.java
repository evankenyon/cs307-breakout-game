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
import javafx.scene.paint.Paint;
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
 * Assumptions:
 * Dependencies: This class depends on several classes from the JavaFX platform.
 * Example:
 * Other details:
 *
 * @author Evan Kenyon
 */
public class Breakout extends Application {

    // size, frames per second, second delay, background, highlight, offset amount, mover, paddle color, and paddle color
    // borrowed from example_animation in course gitlab
    public static final String TITLE = "Breakout Game";
    public static final int SIZE = 800;
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final int OFFSET_PADDLE_AMOUNT = 50;
    public static final int OFFSET_BALL_AMOUNT = OFFSET_PADDLE_AMOUNT + 100;
    public static final int PADDLE_SPEED = 25;
    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 20;
    public static final int BRICK_SIZE = 25;
    public static final double BALL_RADIUS = BRICK_SIZE / 2.5;
    public static final double DISPLAY_X_POS = 50;
    public static final double SCORE_DISPLAY_Y_POS = 600;
    public static final double END_MESSAGE_X_POS = 50;
    public static final double END_MESSAGE_Y_POS = 50;
    public static final int FONT_SIZE = 30;
    public static final String FONT_TYPE = "Verdana";

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


//    Borrowed from example_animation in course gitlab

    /**
     * Purpose: Start the game by calling helper methods which set up all the scenes,
     * the primary stage, and the timeline
     * Assumptions:
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
        Label scoreDisplay = setupDynamicDataDisplay("Score: ", bricks.getScore().asString(), SCORE_DISPLAY_Y_POS);
        Label livesDisplay = setupDynamicDataDisplay("Lives: ", lives.asString(), SCORE_DISPLAY_Y_POS + 50);
        delayIntersectionFrames = 2;
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(paddle, ball, bricks, scoreDisplay, livesDisplay);
        primaryRoot = root;
        return setupScene(root);
    }

    private Scene setupTextScene(String message) {
        // Text construction was borrowed from https://docs.oracle.com/javafx/2/text/jfxpub-text.htm
        Text text = new Text(END_MESSAGE_X_POS, END_MESSAGE_Y_POS, message);
        text.setFont(Font.font (FONT_TYPE, FONT_SIZE));
        // All of the below was borrowed from example_animation in course gitlab
        Group root = new Group(text);
        return setupScene(root);
    }

    private Scene setupScene(Group root) {
        Scene scene = new Scene(root, SIZE, SIZE, BACKGROUND);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    // Label setup code was borrowed from https://stackoverflow.com/questions/56016866/how-do-i-output-updating-values-for-my-scoreboard
    private Label setupDynamicDataDisplay(String text, StringBinding data, double yVal) {
        Label display = new Label();
        display.setFont(new Font(FONT_TYPE,FONT_SIZE));
        // Found concat method and SimpleStringProperty from
        // https://docs.oracle.com/javase/8/javafx/api/javafx/beans/property/SimpleStringProperty.html
        display.textProperty().bind(new SimpleStringProperty(text).concat(data));
        display.setLayoutX(DISPLAY_X_POS);
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
        switch (code) {
            case RIGHT -> paddle.setX(paddle.getX() + PADDLE_SPEED);
            case LEFT -> paddle.setX(paddle.getX() - PADDLE_SPEED);
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
        if(ball.getCenterX() - BALL_RADIUS <= 0) {
            ball.setCenterX(BALL_RADIUS);
            ball.reverseXVelocity();
        } else if (ball.getCenterX() + BALL_RADIUS >= mainScene.getWidth()) {
            ball.setCenterX(mainScene.getWidth() - BALL_RADIUS);
            ball.reverseXVelocity();
        } else if (ball.getCenterY() - BALL_RADIUS <= 0) {
            ball.setCenterY(BALL_RADIUS);
            ball.reverseYVelocity();
        } else if (ball.getCenterY() >= mainScene.getHeight()) {
            ball.resetPosition();
            handleLifeDecrement();
        }
    }

    private void handleLifeDecrement() {
        lives.set(lives.get() -1);
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
        } else if (paddle.getX() + PADDLE_WIDTH >= mainScene.getWidth()) {
            paddle.setX(mainScene.getWidth() - PADDLE_WIDTH);
        }
    }

    private void handleBallIntersectingPaddle() {
        if(isIntersecting(paddle, ball) && delayIntersectionFrames == 2) {
            ball.reverseYVelocity();
            ball.setAngle(ball.getAngle() + Math.toRadians(0.5 * ((paddle.getBoundsInParent().getMinX() + paddle.getWidth()/2) - ball.getCenterX())));
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
        primaryStage.setTitle(TITLE);
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
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
        animation.play();
    }

    private void setupMainSceneNodes() {
        // Rectangle constructor parameters from example_animation in course gitlab
        paddle = new Rectangle(SIZE / 2 - PADDLE_WIDTH / 2, SIZE - OFFSET_PADDLE_AMOUNT, PADDLE_WIDTH, PADDLE_HEIGHT);
        ball = new Ball(SIZE/2, SIZE - OFFSET_BALL_AMOUNT, BALL_RADIUS);
        bricks = new Bricks(SIZE, SIZE, BRICK_SIZE, BRICK_SIZE, 0.1);
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
