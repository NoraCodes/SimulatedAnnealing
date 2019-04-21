package codes.nora.simulatedAnnealing;

public class TravelingSalesman extends SimulatedAnnealing {
    static protected void initializeProblem() {
        // Create and add our cities
        TourManager.addCity(new City(60, 200));
        TourManager.addCity(new City(180, 200));
        TourManager.addCity(new City(80, 180));
        TourManager.addCity(new City(140, 180));
        TourManager.addCity(new City(20, 160));
        TourManager.addCity(new City(100, 160));
        TourManager.addCity(new City(200, 160));
        TourManager.addCity(new City(140, 140));
        TourManager.addCity(new City(40, 120));
        TourManager.addCity(new City(100, 120));
        TourManager.addCity(new City(180, 100));
        TourManager.addCity(new City(60, 80));
        TourManager.addCity(new City(120, 80));
        TourManager.addCity(new City(180, 60));
        TourManager.addCity(new City(20, 40));
        TourManager.addCity(new City(100, 40));
        TourManager.addCity(new City(200, 40));
        TourManager.addCity(new City(20, 20));
        TourManager.addCity(new City(60, 20));
        TourManager.addCity(new City(160, 20));
    }
}
