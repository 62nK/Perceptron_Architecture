package perceptron;

public interface PerceptronInterface {

    // Types
    enum SQUASHING_TYPE {step, sign, sigmoid, linear}
    SQUASHING_TYPE SQUASHING_FUNCTION = SQUASHING_TYPE.step;

    // Methods
    void feedForward(double[] input);
    void resetSynapses();
    String toString();
    String outputToString();

    // Flags
    boolean DEBUG = true;

    // Constants
    int AXON_COUNT = 2;

}
