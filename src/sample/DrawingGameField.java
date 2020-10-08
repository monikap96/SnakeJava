package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static sample.Consts.*;

public class DrawingGameField {

    public void clearGameField(GraphicsContext gc) {
        gc.setFill(Color.GREY);
        gc.setLineWidth(5);
        gc.setStroke(Color.BLACK);
        gc.fillRect(0, 0, WIDTH * SIZE, HEIGHT * SIZE);
        gc.strokeRect(0, 0, WIDTH * SIZE, HEIGHT * SIZE);
    }

    public void reloadScoreLabel(Label label, int score) {
        label.setLayoutX(SIZE);
        label.setLayoutY(HEIGHT * SIZE);
        label.setFont(Font.font(SIZE));
        label.setText("Score: " + score);
    }

    public void drawApple(GraphicsContext gc, int appleX, int appleY) {
        gc.setFill(Color.RED);
        gc.fillRect(appleX, appleY, SIZE, SIZE);
        gc.strokeRect(appleX, appleY, SIZE, SIZE);
    }
}
