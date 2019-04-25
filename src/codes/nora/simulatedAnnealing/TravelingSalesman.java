package codes.nora.simulatedAnnealing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TravelingSalesman extends SimulatedAnnealing {
    private static void initializeProblem() {
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

    public static double getTemperature() {
        long timeSpent = System.currentTimeMillis() - startTime;
        // Scale by 1/x and end at 0.
        double theTemperature = startTemperature*((-1/21) + (1/(1 + 20*timeSpent/timeToRun)));
        return Math.max(theTemperature, 0);
    }

    private ArrayList tour = new ArrayList<City>();

    public ArrayList getTour() {
        return tour;
    }

    @Override
    protected void randomize() {
        for (int cityIndex = 0; cityIndex < TourManager.numberOfCities(); cityIndex++) {
            tour.add(TourManager.getCity(cityIndex));
        }
        Collections.shuffle(tour);
    }

    @Override
    protected void localOptimization() {
        boolean hasChanged = true;
        // Keep going until swapping elements no longer helps. I don't think this will get into an infinite loop but
        // who really knows???
        while (hasChanged == true) {
            hasChanged = false;
            for (int cityIndex = 0; cityIndex < tour.size(); cityIndex++) {
                // This could be optimized by just swapping positions in memory and storing them in some array with a
                // moving starting index but I'm, ... what do they call it? ... lazy? I think that's the word.
                City previousCity = (City) tour.get((cityIndex - 1) % tour.size());
                City thisCity = (City) tour.get(cityIndex);
                City nextCity = (City) tour.get((cityIndex + 1) % tour.size());
                City nexterCity = (City) tour.get((cityIndex + 2) % tour.size());

                // The current path is (prev -> this -> next -> nexter)
                // Try (prev -> next -> this -> nexter)
                // We can ignore the (this <-> next) connection since both have it.
                if (previousCity.distanceTo(thisCity) + nextCity.distanceTo(nexterCity) <
                        previousCity.distanceTo(nextCity) + thisCity.distanceTo(nexterCity)) {
                    tour.set(cityIndex, nextCity);
                    tour.set((cityIndex + 1) % tour.size(), thisCity);
                    hasChanged = true;
                }
            }
        }
    }

    @Override
    protected double getCost() {
        double totalDistance = 0;
        for (int cityIndex = 0; cityIndex < TourManager.numberOfCities(); cityIndex++) {
            totalDistance += ((City) tour.get(cityIndex)).distanceTo((City) tour.get((cityIndex + 1)%tour.size()));
        }
        return totalDistance;
    }

    private Random rand = new Random();
    @Override
    private boolean acceptSolution(double costOfPrevious, double costOfNew) {
        if (costOfNew < costOfPrevious) {
            return true;
        } else {
            return (Math.exp(-(costOfNew - costOfPrevious)/getTemperature()) < rand.nextDouble());
        }
    }

    @Override
    public String toString() {
        String all = "";
        if (tour.size() == 0) {
            return all;
        }
        for (int cityIndex = 0; cityIndex < tour.size() + 1; cityIndex++) {
            int xcoord = ((City) tour.get(cityIndex%tour.size())).getX();
            int ycoord = ((City) tour.get(cityIndex%tour.size())).getY();
            all += "(" + Integer.toString(xcoord) + "," + Integer.toString(ycoord) + ")";
            if (cityIndex != tour.size()) {
                all += " | ";
            }
        }
        return all;
    }

    @Override
    protected void mutate() {
        int firstSwapee = rand.nextInt(tour.size());
        // Second swapee should be at least 2 away since 1 away has already been tried in local optimization.
        int secondSwapee = (rand.nextInt(Math.max(tour.size() - 3, 1)) + firstSwapee)%tour.size();

        City temp = (City) tour.get(firstSwapee);
        tour.set(firstSwapee, tour.get(secondSwapee));
        tour.set(secondSwapee, temp);
    }
}
