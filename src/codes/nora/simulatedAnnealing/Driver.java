package codes.nora.simulatedAnnealing;

public class Driver {
    // Calculate the acceptance probability
    public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    public static void main(String[] args) {
        TravelingSalesman.initializeSetup(1);
        TravelingSalesman t = new TravelingSalesman();
        t.simulatedAnnealing(1);

        System.out.println("Tour: " + t.toString());
    }
}
