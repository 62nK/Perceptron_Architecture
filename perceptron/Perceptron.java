package perceptron;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Perceptron implements PerceptronInterface{

    // Parameters
    private double[] dendrites, synapses; // inputs and weights
    private double[] axons; // output
    private double[] previousEpochDelta, currentEpochDeltaAccumulator;
    private double threshold, networkError;

    // Constructors
    public Perceptron(int dendritesCount, double threshold){
        dendrites = new double[dendritesCount];
        synapses = new double[dendritesCount];
        axons = new double[AXON_COUNT];
        previousEpochDelta = new double[dendritesCount];
        currentEpochDeltaAccumulator = new double[dendritesCount];
        this.threshold = threshold;
        initSynapses();
    }
    public Perceptron(Map<String, Map> perceptronMap){
        Map<String, Double> synapseMap = perceptronMap.get("synapseMap");
        Map<String, Integer> info = perceptronMap.get("info");
        Map<String, Double> threshold = perceptronMap.get("threshold");
        int dendritesCount = synapseMap.size();
        dendrites = new double[dendritesCount];
        synapses = new double[dendritesCount];
        axons = new double[AXON_COUNT];
        previousEpochDelta = new double[dendritesCount];
        currentEpochDeltaAccumulator = new double[dendritesCount];
        this.threshold = threshold.get("T");
        for (int index=0; index<synapses.length; index++)
            synapses[index] = synapseMap.get("sy"+index);
    }

    // Methods
    public void feedForward(double[] input){
        setDendrites(input);
        axons = squashing(sum(dendrites));
    }
    public void train(int epochs, double learningRate, ArrayList<InputTargetPair> inputTargetPairs, double alpha){
        for(int epoch=1; epoch<=epochs; epoch++){
            boolean isNewEpoch = true;
            if(DEBUG) System.out.println("Epoch: "+epoch+" ~ Learning Rate="+(learningRate<0?(1.0/(1+epoch)):learningRate));
            double error = 0;
            for(InputTargetPair inputTargetPair: inputTargetPairs) {
                if(inputTargetPair.getInputSize()==dendrites.length && inputTargetPair.getTargetSize()==axons.length) {
                    feedForward(inputTargetPair.getInput());
                    error+=2*(inputTargetPair.getTarget()[0] - axons[0]);
                    adjustSynapses(learningRate<0?(1.0/(1+epoch)):learningRate, inputTargetPair.getTarget(), alpha, isNewEpoch);
                    isNewEpoch = false;
                    networkError = error*error;

                }
            }
            networkError/=2;
        }
    }

    // Getters
    public double[] getAxons(){
        return axons;
    }
    public double getNetworkError() {
        return networkError;
    }

    // Setters
    public void setDendrites(double[] dendrites){
        this.dendrites = dendrites;
    }
    public void resetSynapses(){
        initSynapses();
    }

    // Services
    private void initSynapses(){
        for(int index=0; index<synapses.length; index++)
                synapses[index] = Math.random()*0.5+0.5;
    }
    private double[] squashing(double s){
        double[] squashing = new double[2];
        switch (SQUASHING_FUNCTION){
            case sign:
                squashing[0] = s> threshold ?1:-1;
                squashing[1] = s>= threshold ?-1:0;
                break;
            case step:
                squashing[0] = s> threshold ?1:0;
                squashing[1] = s>= threshold ?0:1;
                break;
            case linear:
                squashing[0] = s;
                squashing[1] = -s;
                break;
            case sigmoid:
                squashing[0] = sigmoid(s);
                squashing[1] = sigmoid(-s);
                break;
        }
        return squashing;
    }
    private double sigmoid(double s){
        return 1/(1+Math.exp(-s));
    }
    private double sum(double[] dendrites){
        double sum = 0;
        for(int index=0; index<dendrites.length; index++)
            sum += dendrites[index]*synapses[index];
        return sum;
    }
    public void adjustSynapses(double learningRate, double[] target, double alpha, boolean isNewEpoch){
        double delta;
        for(int index=0; index<synapses.length; index++) {
            if(isNewEpoch) {
                previousEpochDelta[index] = currentEpochDeltaAccumulator[index];
                currentEpochDeltaAccumulator[index] = 0;
            }
            delta = learningRate * (target[0] - axons[0]) * dendrites[index];
            currentEpochDeltaAccumulator[index] += delta;
            synapses[index] += delta + alpha * previousEpochDelta[index];
        }
    }
    public void test(ArrayList<InputTargetPair> inputTargetPairs){
        double error = 0;
        for(InputTargetPair inputTargetPair: inputTargetPairs) {
            if(inputTargetPair.getInputSize()==dendrites.length && inputTargetPair.getTargetSize()==axons.length) {
                feedForward(inputTargetPair.getInput());
                error+=2*(inputTargetPair.getTarget()[0] - axons[0]);
                networkError= error*error;
            }
        }
        networkError/=2;
    }


    // IO
    public Map<String, Map> toMap(){
        Map<String, Map> perceptronMap = new HashMap<>();

        Map<String, Double> synapseMap = new HashMap<>();
        Map<String, Integer> info = new HashMap<>();
        Map<String, Double> thresholdMap = new HashMap<>();

        for(int i=0; i<synapses.length; i++)
            synapseMap.put("sy"+i, synapses[i]);

        thresholdMap.put("T", threshold);

        perceptronMap.put("synapseMap", synapseMap);
        perceptronMap.put("info", info);
        perceptronMap.put("threshold", thresholdMap);

        return perceptronMap;
    }
    public void toNeurons(Map<String, Map> perceptronMap){
        Map<String, Double> synapseMap = perceptronMap.get("synapseMap");
        Map<String, Integer> info = perceptronMap.get("info");
        Map<String, Double> threshold = perceptronMap.get("threshold");
        int dendritesCount = synapseMap.size();
        dendrites = new double[dendritesCount];
        synapses = new double[dendritesCount];
        axons = new double[AXON_COUNT];
        previousEpochDelta = new double[dendritesCount];
        currentEpochDeltaAccumulator = new double[dendritesCount];
        this.threshold = threshold.get("T");
        for (int index=0; index<synapses.length; index++)
            synapses[index] = synapseMap.get("sy"+index);
    }
    public String outputToString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<axons.length; i++) stringBuilder.append("["+axons[i]+"]");
        stringBuilder.append("Network Error:"+networkError);
        return stringBuilder.toString();
    }
    public void writePerceptronToFile(String filename){
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(toMap());
            os.close();
        } catch (IOException e){e.printStackTrace();}
    }
    public static Map<String, Map> readPerceptronFromFile(String filename){
        Map<String, Map> perceptronMap = new HashMap<>();
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
            perceptronMap = (HashMap<String, Map>)is.readObject();
            is.close();
        }
        catch (IOException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
        return perceptronMap;
    }
    public String toString(){
        return toMap().toString();
    }
    public String toString(String title){
        return title+": "+toString();
    }


}
