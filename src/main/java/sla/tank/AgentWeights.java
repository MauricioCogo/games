package sla.tank;

import java.util.Random;

public class AgentWeights {
    private double[][] weights; // [numActions][numInputs]
    private int numActions;
    private int numInputs;
    private Random rng = new Random();

    public AgentWeights(int numActions, int numInputs) {
        this.numActions = numActions;
        this.numInputs = numInputs;
        this.weights = new double[numActions][numInputs];

        // inicializa com valores aleatórios pequenos
        for (int i = 0; i < numActions; i++) {
            for (int j = 0; j < numInputs; j++) {
                weights[i][j] = rng.nextGaussian() * 0.1; 
            }
        }
    }

    // calcula a saída pra um vetor de entradas
    public double[] forward(double[] inputs) {
        double[] outputs = new double[numActions];

        for (int i = 0; i < numActions; i++) {
            double sum = 0;
            for (int j = 0; j < numInputs; j++) {
                sum += inputs[j] * weights[i][j];
            }
            outputs[i] = Math.tanh(sum); // squash entre -1 e 1
        }

        return outputs;
    }

    // escolhe a ação com maior valor
    public int chooseAction(double[] inputs) {
        double[] outputs = forward(inputs);
        int best = 0;
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[i] > outputs[best]) {
                best = i;
            }
        }
        return best;
    }

    // muta os pesos (pra algoritmo genético)
    public void mutate(double rate, double magnitude) {
        for (int i = 0; i < numActions; i++) {
            for (int j = 0; j < numInputs; j++) {
                if (rng.nextDouble() < rate) {
                    weights[i][j] += rng.nextGaussian() * magnitude;
                }
            }
        }
    }

    // crossover simples (filho mistura pesos dos pais)
    public static AgentWeights crossover(AgentWeights a, AgentWeights b) {
        if (a.numActions != b.numActions || a.numInputs != b.numInputs) {
            throw new IllegalArgumentException("Dimensões diferentes");
        }
        AgentWeights child = new AgentWeights(a.numActions, a.numInputs);
        for (int i = 0; i < a.numActions; i++) {
            for (int j = 0; j < a.numInputs; j++) {
                child.weights[i][j] = (rngStatic.nextBoolean() ? a.weights[i][j] : b.weights[i][j]);
            }
        }
        return child;
    }

    // um Random estático só pra crossover
    private static Random rngStatic = new Random();
}
