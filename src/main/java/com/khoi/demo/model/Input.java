package com.khoi.demo.model;

public class Input {

  private String countryName;
  private long startYear;
  private long endYear;
  private String order;
  private String sortBy;
  private String cityState;
  private String type;

  public Input() {
  }

  public Input(String city, double avgTemp, double minTemp, double maxTemp) {
    this.city = city;
    this.avgTemp = avgTemp;
    this.minTemp = minTemp;
    this.maxTemp = maxTemp;
  }

  public String getCountryName() {
    return countryName;
  }
  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }
  public long getStartYear() {
    return startYear;
  }
  public void setStartYear(long startYear) {
    this.startYear = startYear;
  }
  public long getEndYear() {
    return endYear;
  }
  public void setEndYear(long endYear) {
    this.endYear = endYear;
  }
  public String getOrder() {
    return order;
  }
  public void setOrder(String order) {
    this.order = order;
  }
  public String getSortBy() {
    return sortBy;
  }
  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }
  public String getCityState() {
    return cityState;
  }
  public void setCityState(String cityState) {
    this.cityState = cityState;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  private String city;
  private double avgTemp;
  private double minTemp;
  private double maxTemp;
  private String avgTempStr;
  private String minTempStr;
  private String maxTempStr;

  public String getAvgTempStr() {
    return avgTempStr;
  }
  public void setAvgTempStr(String avgTempStr) {
    this.avgTempStr = avgTempStr;
  }
  public String getMinTempStr() {
    return minTempStr;
  }
  public void setMinTempStr(String minTempStr) {
    this.minTempStr = minTempStr;
  }
  public String getMaxTempStr() {
    return maxTempStr;
  }
  public void setMaxTempStr(String maxTempStr) {
    this.maxTempStr = maxTempStr;
  }
  public String getCity() {
      return city;
  }

  public void setCity(String city) {
      this.city = city;
  }

  public double getAvgTemp() {
      return avgTemp;
  }

  public void setAvgTemp(double avgTemp) {
      this.avgTemp = avgTemp;
  }

  public double getMinTemp() {
      return minTemp;
  }

  public void setMinTemp(double minTemp) {
      this.minTemp = minTemp;
  }

  public double getMaxTemp() {
      return maxTemp;
  }

  public void setMaxTemp(double maxTemp) {
      this.maxTemp = maxTemp;
  }
  
  private String countries;
  private double temp;
  private long population;
  private String tempStr;
  private String populationStr;

  public String getCountries() {
    return countries;
  }
  public void setCountries(String countries) {
    this.countries = countries;
  }
  public double getTemp() {
    return temp;
  }
  public void setTemp(double temp) {
    this.temp = temp;
  }
  public long getPopulation() {
    return population;
  }
  public void setPopulation(long population) {
    this.population = population;
  }
  public String getTempStr() {
    return tempStr;
  }
  public void setTempStr(String tempStr) {
    this.tempStr = tempStr;
  }
  public String getPopulationStr() {
    return populationStr;
  }
  public void setPopulationStr(String populationStr) {
    this.populationStr = populationStr;
  }

}