import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class InputImagePane extends Pane implements Constants {

    private double[] input;
    private int size;

    public InputImagePane(int size){
        this.size = size;
        input = new double[size];
        randomGenerator();
    }

    public double[] getInput() {
        return input;
    }

    public void draw(){
        clear();
        drawSquares();
    }
    private void drawSquares(){
        Rectangle[] squares = new Rectangle[size];
        for(int y=0; y<(int)Math.sqrt(size); y++)
            for(int x=0; x<(int)Math.sqrt(size); x++){
                squares[y*(int)Math.sqrt(size)+x] = new Rectangle(20+x*SQUARE_SIZE, y*SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                squares[y*(int)Math.sqrt(size)+x].setFill(input[y*(int)Math.sqrt(size)+x]==WHITE_PIXEL? Color.WHITE:Color.BLACK);
                squares[y*(int)Math.sqrt(size)+x].setStroke(Color.GREY);
            }
        getChildren().addAll(squares);
    }
    public void randomGenerator(){
        for(int index=0; index<size; index++)
            input[index] = Math.random()>=0.5?WHITE_PIXEL:BLACK_PIXEL;
    }
    private void clear(){
        getChildren().clear();
    }

}