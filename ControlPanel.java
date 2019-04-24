import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ControlPanel extends VBox implements Constants {

    TextField textFieldProblemSize, textFieldLayerCount, textFieldHiddenLayer, textFieldOutputLayer, textFieldFileName;

    public ControlPanel(){
        draw();
    }

    private void draw(){
        // Problem size
        HBox hbproblemSize = new HBox();
        Text textProblemSize = new Text(0,30, "Problem Size: ");
        textFieldProblemSize = new TextField();
        textFieldProblemSize.setMaxWidth(40);
        textFieldProblemSize.setFont(Font.font(9));
        textFieldProblemSize.setText(""+PROBLEM_SIZE);
        hbproblemSize.getChildren().addAll(textProblemSize, textFieldProblemSize);


        // Problem size
        HBox hBoxFileName = new HBox();
        Text textFileName = new Text(0,30, "File Name: ");
        textFieldFileName = new TextField();
        textFieldFileName.setMinWidth(140);
        textFieldFileName.setFont(Font.font(9));
        textFieldFileName.setText(PATH+FILENAME);
        hBoxFileName.getChildren().addAll(textFileName, textFieldFileName);

        getChildren().addAll(hbproblemSize, hBoxFileName);
    }

}
