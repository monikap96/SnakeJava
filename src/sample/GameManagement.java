package sample;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sample.Consts.*;
import static sample.Consts.HEIGHT;

public class GameManagement {

    int speed = 2;
    int score = 0;
    List<Rectangle> snake = new ArrayList<>();
    int appleX, appleY;
    boolean isEatenApple = false;
    boolean isGameOver = false;
    boolean justDigestedApple = false;
    List<Rectangle> eatenApples = new ArrayList<>();
    Label label;
    Direction actualDirection = Direction.RIGHT;
    Direction newDirection = actualDirection;
    GraphicsContext gc;
    long tmp = 0;
    Stage primaryStage;

    public GameManagement(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SNAKE");
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH * SIZE, HEIGHT * SIZE + 50);
        Canvas canvas = new Canvas(WIDTH * SIZE, HEIGHT * SIZE);
        primaryStage.setResizable(false);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        label = new Label();
        root.getChildren().add(label);
        primaryStage.setScene(scene);
        scene.setOnKeyPressed(e -> checkWhichKeyWasPressed(e));
        primaryStage.show();
    }

    private void checkWhichKeyWasPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP:
                if (actualDirection != Direction.DOWN) {
                    newDirection = Direction.UP;
                }
                break;
            case DOWN:
                if (actualDirection != Direction.UP) {
                    newDirection = Direction.DOWN;
                }
                break;
            case LEFT:
                if (actualDirection != Direction.RIGHT) {
                    newDirection = Direction.LEFT;
                }
                break;
            case RIGHT:
                if (actualDirection != Direction.LEFT) {
                    newDirection = Direction.RIGHT;
                }
                break;
        }
    }

    public void goSnakeGo() {
        actualDirection = newDirection;
        switch (newDirection) {
            case UP:
                setNewPosition(0, -SIZE);
                break;
            case DOWN:
                setNewPosition(0, SIZE);
                break;
            case LEFT:
                setNewPosition(-SIZE, 0);
                break;
            case RIGHT:
                setNewPosition(SIZE, 0);
                break;
        }
        drawObjectsOnGameField(gc);
    }

    private void checkIfIsGameOver() {
        int headX = (int) snake.get(0).getX();
        int headY = (int) snake.get(0).getY();
        if (headX < 0 || headY < 0 || headX > WIDTH * SIZE - SIZE || headY > WIDTH * SIZE - SIZE) {
            isGameOver = true;
        } else {
            for (int i = 1; i < snake.size(); i++) {
                if (headX == snake.get(i).getX() && headY == snake.get(i).getY()) {
                    isGameOver = true;
                    break;
                }
            }
        }
    }

    public void newGame() {
        tmp = 0;
        eatenApples.clear();
        isEatenApple = false;
        justDigestedApple = false;
        snake.clear();
        speed = 2;
        score = 0;
        actualDirection = Direction.RIGHT;
        newDirection = actualDirection;

        snake.add(new Rectangle(5 * SIZE, 5 * SIZE, SIZE, SIZE));
        snake.add(new Rectangle(4 * SIZE, 5 * SIZE, SIZE, SIZE));
        snake.add(new Rectangle(3 * SIZE, 5 * SIZE, SIZE, SIZE));
        generateApplePosition();
        drawObjectsOnGameField(gc);
    }

    private void generateApplePosition() {
        appleX = new Random().nextInt(WIDTH) * SIZE;
        appleY = new Random().nextInt(WIDTH) * SIZE;
        isEatenApple = false;
    }

    private void setNewPosition(int moveX, int moveY) {
        int oldHeadX = (int) snake.get(0).getX();
        int oldHeadY = (int) snake.get(0).getY();

        snake.get(0).setX(oldHeadX + moveX);
        snake.get(0).setY(oldHeadY + moveY);
        checkIfIsGameOver();
        checkIfSnakeAteApple(moveX, moveY, oldHeadX, oldHeadY);

        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
        }
        snake.get(1).setX(oldHeadX);
        snake.get(1).setY(oldHeadY);
    }

    private void checkIfSnakeAteApple(int moveX, int moveY, int oldHeadX, int oldHeadY) {
        if (oldHeadX + moveX == appleX && oldHeadY + moveY == appleY) {
            isEatenApple = true;
            eatenApples.add(new Rectangle(appleX, appleY, SIZE, SIZE));
        }

        if (isEatenApple) {
            score++;
            generateApplePosition();
            if (speed < 10) {
                speed++;
            }
        }
    }

    public void drawObjectsOnGameField(GraphicsContext gc) {
        if (!isGameOver) {
            DrawingGameField drawingGameField = new DrawingGameField();
            drawingGameField.clearGameField(gc);
            drawingGameField.reloadScoreLabel(label,score);
            gc.setLineWidth(1);
            drawingGameField.drawApple(gc, appleX,appleY);

            Snake snake = new Snake();
            snake.drawSnake(gc, this.snake,eatenApples);
            checkIfElementIsReadyToAddIntoSnake(snake.isJustDigestedApple());
        }
    }

    private void checkIfElementIsReadyToAddIntoSnake(boolean isJustDigestedApple) {
        justDigestedApple = isJustDigestedApple;
        if (justDigestedApple) {
            snake.add(new Rectangle(eatenApples.get(0).getX(), eatenApples.get(0).getY(), SIZE, SIZE));
            eatenApples.remove(0);
            justDigestedApple = false;
        }
    }
}
