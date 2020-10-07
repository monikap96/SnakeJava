package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static sample.Consts.*;

public class DrawingGameField {
    Label label;
    Consts v;

    public void drawGameField(GraphicsContext gc, Label label, int score) {
        this.label = label;
        label.setLayoutX(SIZE);
        label.setLayoutY(HEIGHT * SIZE);
        label.setFont(Font.font(SIZE));
        label.setText("Score: " + score);

        gc.setFill(Color.GREY);
        gc.setLineWidth(5);
        gc.setStroke(Color.BLACK);
        gc.fillRect(0, 0, WIDTH * SIZE, HEIGHT * SIZE);
        gc.strokeRect(0, 0, WIDTH * SIZE, HEIGHT * SIZE);
    }
}
