package com.khoi.demo.model;

public class SubTaskB {

    // region type 1 = country, 2 = state, 3 = city
    
    private int startingYear;
    private int endingYear;
    private int timePeriod;
    private double temp;
    private long population;
    private double differenceScore;
    private String regionName;
    private int type;
    // private int numberOfSimResults;

    public SubTaskB(){
    }

    public SubTaskB (int startingYear, String regionName, double avgTemp, long population, int timePeriod){
        this.startingYear = startingYear;
        this.regionName = regionName;
        this.temp = avgTemp;
        this.population = population;
        this.timePeriod = timePeriod;
    }

    public SubTaskB (int startingYear, int endingYear, int timePeriod, double avgTemp, long population, double differenceScore, String regionName, int type){
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        this.timePeriod = timePeriod;
        this.temp = avgTemp;
        this.population = population;
        this.differenceScore = differenceScore;
        this.regionName = regionName;
        this.type = type;
    }

    // public int getNumberOfSimResults() {
    //     return numberOfSimResults;
    // }

    public double getTemp(){
        return temp;
    }

    public int getStartingYear(){
        return startingYear;
    }

    public int getEndingYear(){
        return endingYear;
    }

    public double getDifferenceScore(){
        return differenceScore;
    }

    public long getPopulation(){
        return population;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getRegionName(){
        return regionName;
    }

    public int getType(){
        return type;
    }

    public void setTemp(double temp){
        this.temp = temp;
    }

    public void setStartingYear(int startingYear){
        this.startingYear = startingYear;
    }

    public void setEndingYear(int endingYear){
        this.endingYear = endingYear;
    }

    public void setDifferenceScore(double differenceScore){
        this.differenceScore = differenceScore;
    }

    public void setPopulation(int avgPopulation){
        this.population = avgPopulation;
    }

    public void setRegionName(String regionName){
        this.regionName = regionName;
    }

    public void setType(int type){
        this.type = type;
    }

    // public void setNumberOfSimResults(int numberOfSimResults) {
    //     this.numberOfSimResults = numberOfSimResults;
    // }

    @Override
    public String toString() {
        return  "Region Name: " + regionName +
                "\nType: " + type +
                "\nStarting year: " + startingYear + 
                "\nEnding Year: " + endingYear + 
                "\nTime Period: " + timePeriod +
                "\nAverageTemperature: " + temp + 
                "\nAverage Population: " + population + 
                "\nDifference Score: " + differenceScore + "\n\n";
    }
}
