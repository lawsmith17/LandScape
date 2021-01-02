package com.example.landscape.classes;

public class MetricSystem {

    String CurrentDistanceType;

    public MetricSystem() {
        CurrentDistanceType = "Meters";
    }

    public MetricSystem(String currentDistanceType) {
        CurrentDistanceType = currentDistanceType;
    }

    public String getCurrentDistanceType() {
        return CurrentDistanceType;
    }

    public void setCurrentDistanceType(String currentDistanceType) {
        CurrentDistanceType = currentDistanceType;
    }
}
