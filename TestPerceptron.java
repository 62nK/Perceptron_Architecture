
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import perceptron.InputTargetPair;
import perceptron.Perceptron;

import java.util.ArrayList;

public class TestPerceptron extends Application implements Constants {

    // Variables
    int problemSize = PROBLEM_SIZE;
    double learningRate = LEARNING_RATE;

    int epochs = EPOCHS;
    double treshold = TRESHOLD;
    double momentum = MOMENTUM;

    String filename = PATH+FILENAME;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Objects
        Perceptron perceptron;
        BorderPane borderPane = new BorderPane();
        ControlPanel controlPanel = new ControlPanel();
        HBox hBox = new HBox(5);
        InputImagePane inputImagePane = new InputImagePane(problemSize);
        OutputDisplay outputDisplay = new OutputDisplay();

        // Input
        ArrayList<InputTargetPair> trainingSet = new ArrayList<>();
//        {
//            trainingSet.add(new InputTargetPair(new double[]{-10, -10, -10, -10}, new double[]{0, 1}));
//            trainingSet.add(new InputTargetPair(new double[]{-10, -10, -10, 10}, new double[]{0, 1}));
//            trainingSet.add(new InputTargetPair(new double[]{-10, -10, 10, -10}, new double[]{0, 1}));
//            trainingSet.add(new InputTargetPair(new double[]{-10, -10, 10, 10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{-10, 10, -10, -10}, new double[]{0, 1}));
//            trainingSet.add(new InputTargetPair(new double[]{-10, 10, -10, 10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{-10, 10, 10, -10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{-10, 10, 10, 10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{10, -10, -10, -10}, new double[]{0, 1}));
//            trainingSet.add(new InputTargetPair(new double[]{10, -10, -10, 10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{10, -10, 10, -10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{10, -10, 10, 10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{10, 10, -10, -10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{10, 10, -10, 10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{10, 10, 10, -10}, new double[]{1, 0}));
//            trainingSet.add(new InputTargetPair(new double[]{10, 10, 10, 10}, new double[]{1, 0}));
//        }
        for(int index=0; index<PROBLEM_SIZE*PROBLEM_SIZE*PROBLEM_SIZE; index++){
            double[] input = new double[PROBLEM_SIZE];
            randomGenerator(input);
            InputTargetPair inputTargetPair = new InputTargetPair(input, targetGenerator(input));
            trainingSet.add(inputTargetPair);
        }
        ArrayList<InputTargetPair> testSet = new ArrayList<>();
//        {
//            testSet.add(new InputTargetPair(new double[]{10, 10, -10, -10}, new double[]{1, 0}));
//            testSet.add(new InputTargetPair(new double[]{-10, 10, 10, -10}, new double[]{1, 0}));
//            testSet.add(new InputTargetPair(new double[]{10, 10, 10, 10}, new double[]{1, 0}));
//            testSet.add(new InputTargetPair(new double[]{-10, -10, -10, -10}, new double[]{0, 1}));
//            testSet.add(new InputTargetPair(new double[]{-10, -10, 10, -10}, new double[]{0, 1}));
//            testSet.add(new InputTargetPair(new double[]{10, 10, 10, 10}, new double[]{1, 0}));
//
//        }
        for(InputTargetPair itp: trainingSet) testSet.add(itp);

        ArrayList<double[]> inputs = new ArrayList<>();
        for(InputTargetPair itp: trainingSet) inputs.add(itp.getInput());

        perceptron = new Perceptron(problemSize, treshold);

        // Main Pane
        hBox.getChildren().add(inputImagePane);
        hBox.getChildren().add(outputDisplay);
        hBox.setSpacing(10);
        borderPane.setPadding(new Insets(20));
        borderPane.setCenter(hBox);
        borderPane.setLeft(controlPanel);

        Button nextState = new Button("Next Example");
        Button train = new Button("Train");
        Button test = new Button("Test");
        Button restore = new Button("Read");
        Button save = new Button("Write");
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.TOP_CENTER);
        buttons.setSpacing(5);
        buttons.getChildren().addAll(restore, save, train, test, nextState);
        nextState.setMinHeight(100);
        borderPane.setRight(buttons);
        nextState.setOnMouseClicked(event -> {
            inputImagePane.randomGenerator();
            inputImagePane.draw();
            categorize(perceptron, inputImagePane.getInput());
            outputDisplay.drawOutput(perceptron.getAxons());
            int[] w = whitePixelsCounter(inputImagePane.getInput());
            System.out.println("whites:"+w[0]+" blacks:"+w[1]);
        });
        train.setOnMouseClicked(event -> {
            train(perceptron, trainingSet, epochs, learningRate, momentum);
        });
        test.setOnMouseClicked(event -> {
            test(perceptron, testSet);
        });
        restore.setOnMouseClicked(event -> {
            read(perceptron, filename);
        });
        save.setOnMouseClicked(event -> {
            write(perceptron, filename);
        });

        // Output
        if(DEBUG) print(perceptron);

        // Put the pane on the scene and the scene on the stage
        Scene scene = new Scene(borderPane, WIDTH, Constants.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Perceptron Dark/Bright detection - Andrea Pinardi");
        primaryStage.show();
    }

    public static void write(Perceptron perceptron, String filename){
        perceptron.writePerceptronToFile(filename);
        if(DEBUG) System.out.println(perceptron.toString("Perceptron written to file \""+filename+"\""));
    }
    public static void print(Perceptron perceptron){
        System.out.println(perceptron.toString("Perceptron"));
    }
    public static void train(Perceptron perceptron, ArrayList<InputTargetPair> trainingSet, int epochs, double learningRate, double alpha){
        perceptron.train(epochs, learningRate, trainingSet, alpha);
        if(DEBUG) System.out.println("Training set error("+trainingSet.size()+"):"+perceptron.getNetworkError());
    }
    public static void categorize(Perceptron perceptron, ArrayList<double[]> inputs){
        for(double[] input: inputs){
            perceptron.feedForward(input);
            double[] output = perceptron.getAxons();
            if(DEBUG) {
                System.out.println(perceptron.outputToString());
                if (output[0] > output[1])
                    System.out.println("Bright");
                else
                    System.out.println("Dark");
            }
        }
    }

    private static void read(Perceptron perceptron, String filename){
        perceptron.toNeurons(Perceptron.readPerceptronFromFile(filename));
        if(DEBUG) System.out.println(perceptron.toString("Perceptron read from file \""+filename+"\""));
    }

    private static void test(Perceptron perceptron, ArrayList<InputTargetPair> testSet){
        perceptron.test(testSet);
        if(DEBUG) System.out.println("Test set error("+testSet.size()+"):"+perceptron.getNetworkError());
    }
    private static void categorize(Perceptron perceptron, double[] input) {
        perceptron.feedForward(input);
        double[] output = perceptron.getAxons();
        if (DEBUG) {
            System.out.println(perceptron.outputToString());
            if (output[0] > output[1])
                System.out.println("Bright");
            else
                System.out.println("Dark");
        }
    }
    private static void randomGenerator(double[] array){
        for(int index=0; index<array.length; index++)
            array[index] = Math.random()>=0.5?WHITE_PIXEL:BLACK_PIXEL;
    }
    private static double[] targetGenerator(double[] input){
        double[] target = new double[2];
        int whitePixelsCounter = 0;
        for (int index=0; index<input.length; index++)
            whitePixelsCounter +=input[index]==WHITE_PIXEL?+1:-1;
        target[0] = whitePixelsCounter>=0?1:0;
        target[1] = whitePixelsCounter>=0?0:1;
        return target;
    }
    private static int[] whitePixelsCounter(double[] input){
        int[] target = new int[2];
        int whitePixelsCounter = 0;
        for (int index=0; index<input.length; index++)
            whitePixelsCounter +=input[index]==WHITE_PIXEL?+1:0;
        target[0] = whitePixelsCounter;
        target[1] = input.length-whitePixelsCounter;
        return target;
    }
}