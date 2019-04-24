public interface Constants {

    // Constants
    int PROBLEM_SIZE = 4;
    int WHITE_PIXEL = 10;
    int BLACK_PIXEL = -10;

    /** FLAGS **/
    // Debug: std output messages
    boolean DEBUG = true;

    // Training
    int EPOCHS = 10;
    double LEARNING_RATE = -1; // -1 for learning rate = 1/(1+epoch#)
    double MOMENTUM = 0.3;

    double TRESHOLD = -5;


    // IO
    String PATH = "src/cfg/";
    String FILENAME = "backup.pcpt";

    // JavaFX
    double SQUARE_SIZE = 50;
    double HEIGHT = 300;
    double WIDTH = 800;

}
