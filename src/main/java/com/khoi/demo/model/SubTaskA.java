package com.khoi.demo.model;

public class SubTaskA {
    private int startingYear;
    private int endingYear;
    private int timePeriod;
    private double averageTemp;
    private long averagePopulation;
    private double averageTempDifference;
    private String regionName;
    private int type;
    private double fromValue;
    private double toValue;
    private String selectedOption;
    private String startingYearStr;
    private String filterOption;
    private String filterSortOption;
    private boolean enableSim;

    public SubTaskA(){
    }

    public SubTaskA (int startingYear, String regionName, double averageTemp, double averageTempDifference, long averagePopulation, int timePeriod){
        this.startingYear = startingYear;
        this.regionName = regionName;
        this.averageTemp = averageTemp;
        this.averageTempDifference = averageTempDifference;
        this.averagePopulation = averagePopulation;
        this.timePeriod = timePeriod;
    }

    public SubTaskA (int startingYear, int endingYear, int timePeriod, double averageTemp, double averageTempDifference, long averagePopulation, String regionName, int type){
        this.startingYear = startingYear;
        this.endingYear = endingYear;
        this.timePeriod = timePeriod;
        this.averageTemp = averageTemp;
        this.averageTempDifference = averageTempDifference;
        this.averagePopulation = averagePopulation;
        this.regionName = regionName;
        
        this.type = type;
    }

    public SubTaskA (double averageTemp, long averagePopulation, String regionName, int type, double fromValue, double toValue){
        this.averageTemp = averageTemp;
        this.averagePopulation = averagePopulation;
        this.regionName = regionName;
        this.type = type;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
    
    public boolean isEnableSim() {
        return enableSim;
    }

    public void setEnableSim(boolean enableSim) {
        this.enableSim = enableSim;
    }

    public String getFilterSortOption() {
        return filterSortOption;
    }

    public void setFilterSortOption(String filterSortOption) {
        this.filterSortOption = filterSortOption;
    }

    public String getFilterOption() {
        return filterOption;
    }

    public void setFilterOption(String filterOption) {
        this.filterOption = filterOption;
    }

    public String getStartingYearStr() {
        return startingYearStr;
    }

    public void setStartingYearStr(String startingYearStr) {
        this.startingYearStr = startingYearStr;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue = toValue;
    }

    public long getAveragePopulation() {
        return averagePopulation;
    }

    public void setAveragePopulation(long averagePopulation) {
        this.averagePopulation = averagePopulation;
    }

    public double getAverageTemp(){
        return averageTemp;
    }

    public int getStartingYear(){
        return startingYear;
    }

    public int getEndingYear(){
        return endingYear;
    }

    public double getAverageTempDifference(){
        return averageTempDifference;
    }

    public String getRegionName(){
        return regionName;
    }

    public int getType(){
        return type;
    }

    public void setAverageTemp(double averageTemp){
        this.averageTemp = averageTemp;
    }

    public void setStartingYear(int startingYear){
        this.startingYear = startingYear;
    }

    public void setEndingYear(int endingYear){
        this.endingYear = endingYear;
    }

    public void setAverageTempDifference(double averageTempDifference){
        this.averageTempDifference = averageTempDifference;
    }

    public void setRegionName(String regionName){
        this.regionName = regionName;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
    public String toString() {
        return  "Region Name: " + regionName +
                "\nType: " + type +
                "\nStarting year: " + startingYear + 
                "\nEnding Year: " + endingYear + 
                "\nTime Period: " + timePeriod +
                "\nfromValue: " + fromValue +
                "\ntoValue: " + toValue +
                "\nAverage Population: " + averagePopulation +
                "\nAverage Temperature: " + averageTemp +
                "\nAverage Temperature difference: " + averageTempDifference + "\n\n";
    }
}
