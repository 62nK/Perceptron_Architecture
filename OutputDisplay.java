import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class OutputDisplay extends Pane  implements Constants {

    public OutputDisplay(){ }

    public void drawOutput(double[] output){
        getChildren().clear();
        Rectangle rectangleOutput = new Rectangle(20, 0, 250, 100);
        rectangleOutput.setFill(Color.TRANSPARENT);
        rectangleOutput.setStroke(Color.GREY);
        rectangleOutput.setStrokeWidth(1);
        getChildren().add(rectangleOutput);

        String outputText;
        if(output[0]>output[1])
            outputText = "Bright";
        else if(output[0]<output[1])
            outputText = "Dark";
        else
            outputText = "Undetermined";
        Text out = new Text(25,20, "Category: "+outputText);
        out.setFont(Font.font(20));
        getChildren().add(out);
    }
    public void drawAnnOutputs(String output){
        Text out = new Text(0,50, output);
        getChildren().add(out);
    }

}
