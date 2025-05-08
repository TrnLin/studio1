package com.khoi.demo.JDBCConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.khoi.demo.model.Input;
import com.khoi.demo.model.SubTaskA;
import com.khoi.demo.model.SubTaskB;

public class JDBCConnection {
    public JDBCConnection() {
    }

    public ArrayList<Input> emptyArrayListInput() {
        ArrayList<Input> result = new ArrayList<Input>();
        String notApplicableCitiesStates = "N/A";
        double notApplicableAveTemp = -1;
        double notApplicableMaxTemp = -1;
        double notApplicableMinTemp = -1;
        Input input = new Input(notApplicableCitiesStates, notApplicableAveTemp, notApplicableMaxTemp, notApplicableMinTemp);
        result.add(input);
        return result;
    }

    public ArrayList<SubTaskA> emptyArrayListSubTaskA() {
        ArrayList<SubTaskA> result = new ArrayList<SubTaskA>();
        int notApplicableStartingYear = -1;
        String notApplicableRegionName = "N/A";
        double notApplicableAverageTemp = Double.NaN;
        double notApplicableAverageTempDifference = Double.NaN;
        long notApplicableAveragePopulation = -1;
        int notApplicableTimePeriod = -1;
        SubTaskA subTaskA = new SubTaskA(notApplicableStartingYear, notApplicableRegionName, notApplicableAverageTemp, notApplicableAverageTempDifference, notApplicableAveragePopulation, notApplicableTimePeriod);
        result.add(subTaskA);
        return result;
    }

    public ArrayList<SubTaskB> emptyArrayListSubTaskB() {
        ArrayList<SubTaskB> result = new ArrayList<SubTaskB>();
        int notApplicableStartingYear = -1;
        String notApplicableRegionName = "N/A";
        double notApplicableAverageTemp = Double.NaN;
        int notApplicableTimePeriod = -1;
        long notApplicableAveragePopulation = -1;
        SubTaskB subTaskB = new SubTaskB(notApplicableStartingYear, notApplicableRegionName, notApplicableAverageTemp, notApplicableAveragePopulation, notApplicableTimePeriod);
        result.add(subTaskB);
        return result;
    }

    public String capitalizeWords(String input) {
        if (input == null || input.isEmpty() || input.equalsIgnoreCase("Côte D'Ivoire")) {
          return input;
        }
        String[] words = input.split("\\s+");
        String output = "";
        for (String word : words) {
          output += word.substring(0, 1).toUpperCase();
          output += word.substring(1).toLowerCase();
          output += " ";
        }
        return output.trim();
      }      

    private static final String DATABASE2 = "jdbc:sqlite:database/TempPopulation.db";
    private static final String DATABASE1 = "jdbc:sqlite:database/climatechange.db";

    public ArrayList<String> getResultByCountryName() { //change the return type (TODO) (this method gets an ArrayList of country names)
        ArrayList<String> Countries = new ArrayList<String>(); //change the return type(TODO)

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC database
            connection = DriverManager.getConnection(DATABASE1); //Change database (TODO)

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population WHERE Year = (SELECT Max(Year) FROM Population) AND (Country_Name != 'World' AND Country_Name != 'Asia') ORDER BY Country_Name;"; // change the query(TODO)
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String countryName     = results.getString("Country_Name"); //Change the "Country" to the desired column (TODO)

                // Store the movieName in the ArrayList to return
                Countries.add(countryName);
            } // If the result isn't an array, then reference getStartYear() method below (TODO)

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return Countries; //change return (TODO)
    }

    public double getWorldTemperatureProportion(long startYear, long endYear) {
        double change = 0;

        double worldTempStartYear = getWorldTempByYear((int)startYear);
        double worldTempEndYear = getWorldTempByYear((int)endYear);
        
        change = ((worldTempEndYear - worldTempStartYear) / worldTempStartYear) * 100;
        change = Math.abs(change);

        return change;
    }

    public double getWorldTemperatureRaw(long startYear, long endYear) {
        double change = 0;

        double worldTempStartYear = getWorldTempByYear((int)startYear);
        double worldTempEndYear = getWorldTempByYear((int)endYear);

        change = worldTempEndYear - worldTempStartYear;
        change = Math.abs(change);

        return change;
    }

    public double getWorldPopulationProportion(long startYear, long endYear) {
        double change;
        long worldPopuStartYear = getWorldPopuByYear((int)startYear);
        long worldPopuEndYear = getWorldPopuByYear((int)endYear);
        
        change = ((double)(worldPopuEndYear - worldPopuStartYear) / worldPopuStartYear) * 100;
        change = Math.abs(change);

        return change;
    }

    public long getWorldPopulationRaw(long startYear, long endYear) {
        long change;
        long worldPopuStartYear = getWorldPopuByYear((int)startYear);
        long worldPopuEndYear = getWorldPopuByYear((int)endYear);

        change = worldPopuEndYear - worldPopuStartYear;
        change = Math.abs(change);

        return change;
    }

    public double getCountryTemperatureProportion(String country, long startYear, long endYear) {
        double change = 0;

        double countryTempStartYear = getCountriesTempByYear(country, (int)startYear);
        double countryTempEndYear = getCountriesTempByYear(country, (int)endYear);
        
        change = ((countryTempEndYear - countryTempStartYear) / countryTempStartYear) * 100;
        change = Math.abs(change);

        return change;
    }

    public double getCountryTemperatureRaw(String country, long startYear, long endYear) {
        double change = 0;

        double countryTempStartYear = getCountriesTempByYear(country, (int)startYear);
        double countryTempEndYear = getCountriesTempByYear(country, (int)endYear);

        change = countryTempEndYear - countryTempStartYear;
        change = Math.abs(change);

        return change;
    }

    public double getCountryPopulationProportion(String country, long startYear, long endYear) {
        double change;
        long countryPopuStartYear = getCountriesPopuByYear(country, (int)startYear);
        long countryPopuEndYear = getCountriesPopuByYear(country, (int)endYear);
        
        change = ((double)(countryPopuEndYear - countryPopuStartYear) / countryPopuStartYear) * 100;
        change = Math.abs(change);

        return change;
    }

    public long getCountryPopulationRaw(String country, long startYear, long endYear) {
        long change;
        long countryPopuStartYear = getCountriesPopuByYear(country, (int)startYear);
        long countryPopuEndYear = getCountriesPopuByYear(country, (int)endYear);

        change = countryPopuEndYear - countryPopuStartYear;
        change = Math.abs(change);

        return change;
    }

    public int getStartEndGlobalYearlyLandTempYear(String MINorMAX) { //the available years for yearly temperature on land globally
        int year = JDBCConnection.getYear(MINorMAX, "LAND");
        return year;
    }

    public int getStartEndGlobalYearlyTempYear(String MINorMAX) { //the available years for yearly temperature globally
        int year = JDBCConnection.getYear(MINorMAX, "GLOBAL");
        return year;
    }

    public int getStartEndYearPopulation(String MINorMAX) { //the available years for population globally
        int year = JDBCConnection.getYear(MINorMAX, "POPULATION");;

        return year;
    }

    private static int getYear(String MINorMAX, String LANDorGLOBALorPopulation) { //the above 3 methods get the year from this method
        int year = 0;
        
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);//

            Statement statement = connection.createStatement();//
            statement.setQueryTimeout(30);

            String query = "";

            if(LANDorGLOBALorPopulation.equalsIgnoreCase("LAND")) {
                query = "SELECT " + MINorMAX + "(Year) FROM GlobalYearlyLandTempByCountry;";
            } else if (LANDorGLOBALorPopulation.equalsIgnoreCase("GLOBAL")) {
                query = "SELECT " + MINorMAX + "(Year) FROM GlobalYearlyTemp;";
            } else if (LANDorGLOBALorPopulation.equalsIgnoreCase("Population")) {
                query = "SELECT " + MINorMAX + "(Year) FROM Population;";
            }

            ResultSet results = statement.executeQuery(query);
            year     = results.getInt(1);

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return year;
    }

    public ArrayList<Input> getStartEndCityStateAvgTempChangeOrder(String countryName, String CityORState, long startYear, long endYear, String order) { //avgTemp change by proportion
        ArrayList<Input> inputs = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT g1.Year AS Year1, g1.Country, g1.";

            if(CityORState.equalsIgnoreCase("City") && order.equalsIgnoreCase("DESC")) {
                query = query + "City, ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByCity AS g1 JOIN GlobalYearlyLandTempByCity AS g2 ON g1.City = g2.City WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100) DESC";
            } else if(CityORState.equalsIgnoreCase("State") && order.equalsIgnoreCase("DESC")) {
                query = query + "State, ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByState AS g1 JOIN GlobalYearlyLandTempByState AS g2 ON g1.State = g2.State WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100) DESC;";
            } else if(CityORState.equalsIgnoreCase("City") && order.equalsIgnoreCase("ASC")) {
                query = query + "City, ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByCity AS g1 JOIN GlobalYearlyLandTempByCity AS g2 ON g1.City = g2.City WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100)";
            } else if(CityORState.equalsIgnoreCase("State") && order.equalsIgnoreCase("ASC")) {
                query = query + "State, ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByState AS g1 JOIN GlobalYearlyLandTempByState AS g2 ON g1.State = g2.State WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.AverageTemperature - g1.AverageTemperature) / g1.AverageTemperature) * 100);";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                Input input = new Input();
                double temp = 0;

                temp     = results.getDouble("Proportion");
                temp = Math.round(temp * 1000) / 1000.0;
                input.setAvgTemp(temp);
                inputs.add(input);
            }

            results = statement.executeQuery(query);

            int i = 0;

            while (results.next()) {
                String temp = "";

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getString("City");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getString("State");
                }

                inputs.get(i).setCity(temp);
                ++i;
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return inputs;
    }

    public ArrayList<Input> getStartEndCityStateMinTempChangeOrder(String countryName, String CityORState, long startYear, long endYear, String order) { //avgTemp change by proportion
        ArrayList<Input> inputs = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT g1.Year AS Year1, g1.Country, g1.";

            if(CityORState.equalsIgnoreCase("City") && order.equalsIgnoreCase("DESC")) {
                query = query + "City, ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByCity AS g1 JOIN GlobalYearlyLandTempByCity AS g2 ON g1.City = g2.City WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100) DESC";
            } else if(CityORState.equalsIgnoreCase("State") && order.equalsIgnoreCase("DESC")) {
                query = query + "State, ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByState AS g1 JOIN GlobalYearlyLandTempByState AS g2 ON g1.State = g2.State WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100) DESC;";
            } else if(CityORState.equalsIgnoreCase("City") && order.equalsIgnoreCase("ASC")) {
                query = query + "City, ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByCity AS g1 JOIN GlobalYearlyLandTempByCity AS g2 ON g1.City = g2.City WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100)";
            } else if(CityORState.equalsIgnoreCase("State") && order.equalsIgnoreCase("ASC")) {
                query = query + "State, ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByState AS g1 JOIN GlobalYearlyLandTempByState AS g2 ON g1.State = g2.State WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MinimumTemperature - g1.MinimumTemperature) / g1.MinimumTemperature) * 100);";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                Input input = new Input();
                double temp = 0;

                temp     = results.getDouble("Proportion");
                temp = Math.round(temp * 1000) / 1000.0;
                input.setMinTemp(temp);
                inputs.add(input);
            }

            results = statement.executeQuery(query);

            int i = 0;

            while (results.next()) {
                String temp = "";

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getString("City");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getString("State");
                }

                inputs.get(i).setCity(temp);
                ++i;
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return inputs;
    }

    public ArrayList<Input> getStartEndCityStateMaxTempChangeOrder(String countryName, String CityORState, long startYear, long endYear, String order) { //avgTemp change by proportion
        ArrayList<Input> inputs = new ArrayList<>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT g1.Year AS Year1, g1.Country, g1.";

            if(CityORState.equalsIgnoreCase("City") && order.equalsIgnoreCase("DESC")) {
                query = query + "City, ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByCity AS g1 JOIN GlobalYearlyLandTempByCity AS g2 ON g1.City = g2.City WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100) DESC";
            } else if(CityORState.equalsIgnoreCase("State") && order.equalsIgnoreCase("DESC")) {
                query = query + "State, ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByState AS g1 JOIN GlobalYearlyLandTempByState AS g2 ON g1.State = g2.State WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100) DESC;";
            } else if(CityORState.equalsIgnoreCase("City") && order.equalsIgnoreCase("ASC")) {
                query = query + "City, ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByCity AS g1 JOIN GlobalYearlyLandTempByCity AS g2 ON g1.City = g2.City WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100)";
            } else if(CityORState.equalsIgnoreCase("State") && order.equalsIgnoreCase("ASC")) {
                query = query + "State, ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100) AS Proportion FROM GlobalYearlyLandTempByState AS g1 JOIN GlobalYearlyLandTempByState AS g2 ON g1.State = g2.State WHERE (g1.Year = '" + startYear + "' AND g2.Year = '" + endYear + "') AND g1.Country = '" + countryName + "' ORDER BY ABS(((g2.MaximumTemperature - g1.MaximumTemperature) / g1.MaximumTemperature) * 100);";
            }

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                Input input = new Input();
                double temp = 0;

                temp     = results.getDouble("Proportion");
                temp = Math.round(temp * 1000) / 1000.0;
                input.setMaxTemp(temp);
                inputs.add(input);
            }

            results = statement.executeQuery(query);

            int i = 0;

            while (results.next()) {
                String temp = "";

                if(CityORState.equalsIgnoreCase("City")) {
                    temp     = results.getString("City");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    temp     = results.getString("State");
                }

                inputs.get(i).setCity(temp);
                ++i;
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return inputs;
    }

    public static ArrayList<String> getCitiesStateByCountryName(String countryName, String CityORState) { //cities/state of the country user typed
        ArrayList<String> CitiesState = new ArrayList<String>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempBy";

            if(CityORState.equalsIgnoreCase("City")) {
                query = query + "City WHERE Country = '" + countryName + "' AND Year = '1950';";
            } else if (CityORState.equalsIgnoreCase("State")) {
                query = query + "State WHERE Country = '" + countryName + "' AND Year = '1950';";
            }
            
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {

                String citiesState = "";

                if(CityORState.equalsIgnoreCase("City")) {
                    citiesState     = results.getString("City");
                } else if(CityORState.equalsIgnoreCase("State")) {
                    citiesState     = results.getString("State");
                }

                CitiesState.add(citiesState);
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return CitiesState;
    }

    public double getWorldTempByYear(int year) {
        double worldTemp = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyTemp WHERE Year = '" + year + "';";
            
            ResultSet results = statement.executeQuery(query);

            worldTemp     = results.getDouble("AverageTemperature");

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return worldTemp;
    }

    public long getWorldPopuByYear(int year) {
        long worldPopu = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population WHERE Year = '" + year + "' AND Country_Name = 'World';";
            
            ResultSet results = statement.executeQuery(query);

            worldPopu     = results.getLong("Population");

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return worldPopu;
    }

    public double getCountriesTempByYear(String country, int year) {
        double countriesTemp = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempByCountry WHERE Year = '" + year + "' AND Country = '" + country + "';";
            
            ResultSet results = statement.executeQuery(query);

            countriesTemp     = results.getDouble("AverageTemperature");

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return countriesTemp;
    }

    public long getCountriesPopuByYear(String country, int year) {
        long countriesPopu = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population WHERE Year = '" + year + "' AND Country_Name = '" + country +"';";
            
            ResultSet results = statement.executeQuery(query);

            countriesPopu     = results.getLong("Population");

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return countriesPopu;
    }

    public double getCityTempByYear(String city, int year) {
        double cityTemp = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempByCity WHERE Year = '" + year + "' AND City = '" + city + "';";
            
            ResultSet results = statement.executeQuery(query);

            cityTemp     = results.getDouble("AverageTemperature");

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return cityTemp;
    }

    public double getStateTempByYear(String state, int year) {
        double stateTemp = 0;

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM GlobalYearlyLandTempByState WHERE Year = '" + year + "' AND State = '" + state + "';";
            
            ResultSet results = statement.executeQuery(query);

            stateTemp     = results.getDouble("AverageTemperature");

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return stateTemp;
    }

    public String getCountriesCodeByCountry(String country) {
        String countriesCode = "";

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population WHERE Country_Name = '" + country +"';";
            
            ResultSet results = statement.executeQuery(query);

            countriesCode     = results.getString("Country_Code");

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return countriesCode;
    }

    public ArrayList<Input> getCountriesLatestSortPopuDesc() {
        ArrayList<Input> countriesSortPopu = new ArrayList<Input>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population AS p JOIN GlobalYearlyLandTempByCountry AS c ON p.Year = c.Year AND p.Country_Name = c.Country WHERE p.Year = (SELECT Max(Year) FROM Population) AND (Country_Name != 'World' AND Country_Name != 'Asia') ORDER BY Population DESC;";
            
            ResultSet results = statement.executeQuery(query);

            int i = 0;

            while (results.next()) {
                Input input = new Input();

                String countries = results.getString("Country_Name");

                input.setCountries(countries);
                countriesSortPopu.add(input);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                long popu     = results.getLong("Population");

                String popuStr = String.format( "%,d", popu);
                popuStr = popuStr.replace(',', '.');
                
                countriesSortPopu.get(i).setPopulationStr(popuStr);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                double temp     = results.getDouble("AverageTemperature");
                String tempStr = String.format("%.3f", temp);

                countriesSortPopu.get(i).setTempStr(tempStr);
                i+=1;

            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return countriesSortPopu;
    }

    public ArrayList<Input> getCountriesLatestSortTempDesc() {
        ArrayList<Input> countriesSortTemp = new ArrayList<Input>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population AS p JOIN GlobalYearlyLandTempByCountry AS c ON p.Year = c.Year AND p.Country_Name = c.Country WHERE p.Year = (SELECT Max(Year) FROM Population) AND (Country_Name != 'World' AND Country_Name != 'Asia') ORDER BY AverageTemperature DESC";
            
            ResultSet results = statement.executeQuery(query);

            int i = 0;

            while (results.next()) {
                Input input = new Input();

                String countries = results.getString("Country_Name");

                input.setCountries(countries);
                countriesSortTemp.add(input);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                long popu     = results.getLong("Population");

                String popuStr = String.format( "%,d", popu);
                popuStr = popuStr.replace(',', '.');
                
                countriesSortTemp.get(i).setPopulationStr(popuStr);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                double temp     = results.getDouble("AverageTemperature");
                String tempStr = String.format("%.3f", temp);

                countriesSortTemp.get(i).setTempStr(tempStr);
                i+=1;

            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return countriesSortTemp;
    }

    public ArrayList<Input> getCountriesLatestSortPopuAsc() {
        ArrayList<Input> countriesSortPopu = new ArrayList<Input>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population AS p JOIN GlobalYearlyLandTempByCountry AS c ON p.Year = c.Year AND p.Country_Name = c.Country WHERE p.Year = (SELECT Max(Year) FROM Population) AND (Country_Name != 'World' AND Country_Name != 'Asia') ORDER BY Population;";
            
            ResultSet results = statement.executeQuery(query);

            int i = 0;

            while (results.next()) {
                Input input = new Input();

                String countries = results.getString("Country_Name");

                input.setCountries(countries);
                countriesSortPopu.add(input);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                long popu     = results.getLong("Population");

                String popuStr = String.format( "%,d", popu);
                popuStr = popuStr.replace(',', '.');
                
                countriesSortPopu.get(i).setPopulationStr(popuStr);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                double temp     = results.getDouble("AverageTemperature");
                String tempStr = String.format("%.3f", temp);

                countriesSortPopu.get(i).setTempStr(tempStr);
                i+=1;

            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return countriesSortPopu;
    }

    public ArrayList<Input> getCountriesLatestSortTempAsc() {
        ArrayList<Input> countriesSortTemp = new ArrayList<Input>();

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Population AS p JOIN GlobalYearlyLandTempByCountry AS c ON p.Year = c.Year AND p.Country_Name = c.Country WHERE p.Year = (SELECT Max(Year) FROM Population) AND (Country_Name != 'World' AND Country_Name != 'Asia') ORDER BY AverageTemperature";
            
            ResultSet results = statement.executeQuery(query);

            int i = 0;

            while (results.next()) {
                Input input = new Input();

                String countries = results.getString("Country_Name");

                input.setCountries(countries);
                countriesSortTemp.add(input);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                long popu     = results.getLong("Population");

                String popuStr = String.format( "%,d", popu);
                popuStr = popuStr.replace(',', '.');
                
                countriesSortTemp.get(i).setPopulationStr(popuStr);
                i += 1;
            }

            results = statement.executeQuery(query);
            i = 0;

            while (results.next()) {
                double temp     = results.getDouble("AverageTemperature");
                String tempStr = String.format("%.3f", temp);

                countriesSortTemp.get(i).setTempStr(tempStr);
                i+=1;

            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return countriesSortTemp;
    }


    // subTask B

    public ArrayList<SubTaskB> subTaskBTask1(int startingYear, int timePeriod, String selectedRegion, int region, int option, int mode, boolean mostSimilar) {
        // region 1  = country, 2 = state, 3 = city
        // option 1 = temperature, 2 = population, 3 = both
        // mode 1 = absolute, 2 = relative
        ArrayList<SubTaskB> result = new ArrayList<SubTaskB>();
        switch(option){
            case 1:
                result = similarTemp(startingYear, timePeriod, selectedRegion, region, mode);
                break;
            case 2:
                if (region == 1){
                    result = similarPopulation(startingYear, timePeriod, selectedRegion, mode);
                    break;
                }
                else{
                    break;
                }
            case 3:
                if (region == 1){
                    result = similarTempAndPopulation(startingYear, timePeriod, selectedRegion, mode);
                    break;
                }
                else{
                    break;
                }
        }

        if(mostSimilar){
            SortLowToHigh(result);
        }
        else{
            SortHighToLow(result);
        }

        return result;
    }

    public static ArrayList<SubTaskB> similarTemp (int startingYear, int timePeriod, String selectedRegion, int region, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS";

            switch(region){
                case 1:
                    query = query + " (SELECT 1 FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?))";
                    break;
                case 2:
                    query = query + " (SELECT 1 FROM GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?))";
                    break;
                case 3:
                    query = query + " (SELECT 1 FROM GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?))";
                    break;
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters and execute the query
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }
            
            preparedStatement.close();
            resultSet.close();

            //Find available first and last year
            query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max";

            switch(region){
                case 1:
                    query = query + " FROM GlobalYearlyLandTempByCountry WHERE Country = ?";
                    break;
                case 2:
                    query = query + " FROM GlobalYearlyLandTempByState WHERE State = ?";
                    break;
                case 3:
                    query = query + " FROM GlobalYearlyLandTempByCity WHERE City = ?";
                    break;
            }

            preparedStatement = connection.prepareStatement(query);

            // Set the parameters and execute the query
            preparedStatement.setString(1, selectedRegion);
            ResultSet results = preparedStatement.executeQuery();

            int beginYear = 0;
            int endYear = 0;
            if (results.next()){
                beginYear = results.getInt("Min");
                endYear = results.getInt("Max");
            }

            // Don't forget to close the statement and the result set
            preparedStatement.close();
            results.close();


            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg ";

            switch(region){
                case 1:
                    query = query + "FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
                    break;
                case 2:
                    query = query + "FROM GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?)";
                    break;
                case 3:
                    query = query + "FROM GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?)";
                    break;
            }

            preparedStatement = connection.prepareStatement(query);

            // Set the parameters and execute the query
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            results = preparedStatement.executeQuery();

            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            preparedStatement.close();
            results.close();

            //Find average temperature of the selected region in various time period
            double average;
            double similarRate;
            for(int i = beginYear; i <= endYear - (timePeriod); i++){
                average = 0;
                query = "SELECT AVG(AverageTemperature) AS Avg ";

                switch(region){
                    case 1:
                        query = query + "FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
                        break;
                    case 2:
                        query = query + "FROM GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?)";
                        break;
                    case 3:
                        query = query + "FROM GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?)";
                        break;
                }

                preparedStatement = connection.prepareStatement(query);

                // Set the parameters and execute the query
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, i);
                preparedStatement.setInt(3, i + timePeriod);
                results = preparedStatement.executeQuery();

                while (results.next()) {
                    average = results.getDouble("Avg");
                }

                preparedStatement.close();
                results.close();

                if (mode == 1){
                    similarRate = Math.abs(average - avg);
                }
                else{
                    similarRate = Math.abs((average - avg) / avg) * 100;
                }


                SubTaskB temp = new SubTaskB(i, i + timePeriod, timePeriod, average, 0, similarRate, selectedRegion, region);
                result.add(temp);
                statement.close();
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public static ArrayList<SubTaskB> similarPopulation (int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // Prepare the SQL query
            String query = "SELECT EXISTS (SELECT 1 FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters and execute the query
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                return result;
            }

            
            preparedStatement.close();
            resultSet.close();

            
            query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = ?";
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters and execute the query
            preparedStatement.setString(1, selectedRegion);
            ResultSet results = preparedStatement.executeQuery();

            int beginYear = 0;
            int endYear = 0;
            if (results.next()){
                beginYear = results.getInt("Min");
                endYear = results.getInt("Max");
            }

            preparedStatement.close();
            results.close();


            // Prepare the SQL query
            query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters and execute the query
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            results = preparedStatement.executeQuery();

            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            // Don't forget to close the statement and the result set
            preparedStatement.close();
            results.close();

            //Find average population of the selected region in various time period
            double average;
            double similarRate;
            for(int i = beginYear; i <= endYear - (timePeriod); i++){
                average = 0;

                query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";
                preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, i);
                preparedStatement.setInt(3, i + timePeriod);
                results = preparedStatement.executeQuery();

                while (results.next()) {
                    average = results.getDouble("Avg");
                }

                preparedStatement.close();
                results.close();


                if (mode == 1){
                    similarRate = Math.abs(average - avg);
                }
                else{
                    similarRate = Math.abs((average - avg) / avg) * 100;
                }
                SubTaskB temp = new SubTaskB(i, i + timePeriod, timePeriod, 0, (long) average, similarRate, selectedRegion, 1);
                result.add(temp);
                statement.close();
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public static ArrayList<SubTaskB> similarTempAndPopulation (int startingYear, int timePeriod, String selectedRegion, int mode){
        ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS (SELECT 1 FROM GlobalYearlyLandTempByCountry AS Temperature JOIN Population ON Temperature.Country = Population.Country_Name WHERE Temperature.Country = ? AND (Population.Year BETWEEN ? AND ?))";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                return result;
            }

            // Prepare the SQL query
            query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = ?";
            preparedStatement = connection.prepareStatement(query);

            // Set the parameters and execute the query
            preparedStatement.setString(1, selectedRegion);
            ResultSet results = preparedStatement.executeQuery();

            int beginYear = 0;
            int endYear = 0;
            if (results.next()){
                beginYear = results.getInt("Min");
                endYear = results.getInt("Max");
            }

            // Don't forget to close the statement and the result set
            preparedStatement.close();
            results.close();


            // Find average population of the selected region in a certain time period
            query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            results = preparedStatement.executeQuery();
            double avgPopulation = 0;
            while (results.next()) {
                avgPopulation = results.getDouble("Avg");
            }
            preparedStatement.close();

            // Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            results = preparedStatement.executeQuery();
            double avgTemp = 0;
            while (results.next()) {
                avgTemp = results.getDouble("Avg");
            }
            preparedStatement.close();

            //Find average temperature of the selected region in various time period
            double averageTemp;
            double averagePopulation;
            double similarRate;
            for(int i = beginYear; i <= endYear - (timePeriod); i++){

                averageTemp = 0;
                averagePopulation = 0;

                // Prepare the SQL queries
                String populationQuery = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";
                PreparedStatement populationStatement = connection.prepareStatement(populationQuery);

                String temperatureQuery = "SELECT AVG(AverageTemperature) AS Avg FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
                PreparedStatement temperatureStatement = connection.prepareStatement(temperatureQuery);

                // Set the parameters and execute the queries
                populationStatement.setString(1, selectedRegion);
                populationStatement.setInt(2, i);
                populationStatement.setInt(3, i + timePeriod);
                results = populationStatement.executeQuery();
                while (results.next()) {
                    averagePopulation = results.getDouble("Avg");
                }

                temperatureStatement.setString(1, selectedRegion);
                temperatureStatement.setInt(2, i);
                temperatureStatement.setInt(3, i + timePeriod);
                results = temperatureStatement.executeQuery();
                while (results.next()) {
                    averageTemp = results.getDouble("Avg");
                }

                // Don't forget to close the statements and the result set
                populationStatement.close();
                temperatureStatement.close();
                results.close();

                if (mode == 1){
                    similarRate = Math.sqrt(Math.pow(averageTemp - avgTemp, 2) + Math.pow(averagePopulation - avgPopulation, 2));
                }
                else{
                    similarRate = Math.sqrt(Math.pow(((averageTemp - avgTemp) / avgTemp) * 100, 2) + Math.pow(((averagePopulation - avgPopulation) / avgPopulation) * 100, 2));
                }
                SubTaskB temp = new SubTaskB(i, i + timePeriod, timePeriod, averageTemp, (long) averagePopulation, similarRate, selectedRegion, 1);
                result.add(temp);
            }



        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }

    public ArrayList<SubTaskB> subTaskBTask2 (int startingYear, int timePeriod, String selectedRegion, int region, int option, int mode, boolean mostSimilar, boolean omit){
        // region 1  = country, 2 = state, 3 = city
        // option 1 = temperature, 2 = population, 3 = both
        // mode 1 = absolute, 2 = relative
        ArrayList<SubTaskB> result = new ArrayList<SubTaskB>();
        switch(option){
            case 1:
                result = similarTempTask2(startingYear, timePeriod, selectedRegion, region, mode, mostSimilar, omit);
                break;
            case 2:
                if (region == 1){
                    result = similarPopulationTask2(startingYear, timePeriod, selectedRegion, mode, mostSimilar, omit);
                    break;
                }
                else{
                    break;
                }
            case 3:
                if (region == 1){
                    result = similarTempAndPopulationTask2(startingYear, timePeriod, selectedRegion, mode, mostSimilar, omit);
                    break;
                }
                else{
                    break;
                }
        }

        if(mostSimilar){
            SortLowToHigh(result);
        }
        else{
            SortHighToLow(result);
        }
        
        return result;
    }

    public static ArrayList<SubTaskB> similarTempTask2 (int startingYear, int timePeriod, String selectedRegion, int region, int mode, boolean mostSimilar, boolean omit){
    ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the region exists
            String query = "SELECT EXISTS";

            switch(region){
                case 1:
                    query += " (SELECT 1 FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?))";
                    break;
                case 2:
                    query += " (SELECT 1 FROM GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?))";
                    break;
                case 3:
                    query += " (SELECT 1 FROM GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?))";
                    break;
            }

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }

            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg ";

            switch(region){
                case 1:
                    query += "FROM GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
                    break;
                case 2:
                    query += "FROM GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?)";
                    break;
                case 3:
                    query += "FROM GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?)";
                    break;
            }
            
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            ResultSet results = preparedStatement.executeQuery();

            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            statement.close();

            //Retrieve a list of all regions
            switch(region){
                case 1:
                    query = "SELECT DISTINCT Country FROM GlobalYearlyLandTempByCountry";
                    break;
                case 2:
                    query = "SELECT DISTINCT State FROM GlobalYearlyLandTempByState";
                    break;
                case 3:
                    query = "SELECT DISTINCT City FROM GlobalYearlyLandTempByCity";
                    break;
            }

            results = statement.executeQuery(query);
            ArrayList<String> data = new ArrayList<>();
            while (results.next()) {
                switch(region){
                    case 1:
                        data.add(results.getString("Country"));
                        break;
                    case 2:
                        data.add(results.getString("State"));
                        break;
                    case 3:
                        data.add(results.getString("City"));
                        break;
                }
            }

            statement.close();



            //Going through every region and find the average temperature in a certain time period
            int beginYear = 0;
            int endYear = 0;

            for(int x = 0; x < data.size(); x++){
                ArrayList<SubTaskB> avgTemp = new ArrayList<>();
                double average;
                double similarRate;
                String currentRegion = data.get(x);

                //Find available first and last year
                query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM ";

                switch(region){
                    case 1:
                        query += "GlobalYearlyLandTempByCountry WHERE Country = ?";
                        break;
                    case 2:
                        query += "GlobalYearlyLandTempByState WHERE State = ?";
                        break;
                    case 3:
                        query += "GlobalYearlyLandTempByCity WHERE City = ?";
                        break;
                }

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, currentRegion);
                results = preparedStatement.executeQuery();

                results = preparedStatement.executeQuery();
                if (results.next()){
                    beginYear = results.getInt("Min");
                    endYear = results.getInt("Max");
                }
                preparedStatement.close();

                //Find average temperature of the selected region in various time period and then get the highest similar rate
                for(int i = beginYear; i <= endYear - (timePeriod); i++){
                    average = 0;
                    
                    query = "SELECT AVG(AverageTemperature) AS Avg FROM ";

                    switch(region){
                        case 1:
                            query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND (Year BETWEEN ? AND ?)";
                            break;
                        case 2:
                            query += "GlobalYearlyLandTempByState WHERE State = ? AND (Year BETWEEN ? AND ?)";
                            break;
                        case 3:
                            query += "GlobalYearlyLandTempByCity WHERE City = ? AND (Year BETWEEN ? AND ?)";
                            break;
                    }

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, currentRegion);
                    preparedStatement.setInt(2, i);
                    preparedStatement.setInt(3, i + timePeriod);
                    results = preparedStatement.executeQuery();


                    while (results.next()) {
                        average = results.getDouble("Avg");
                    }

                    // Absolute or relative
                    if (mode == 1){
                        similarRate = Math.abs(average - avg) ;
                    }
                    else{
                        similarRate = Math.abs((average - avg) / avg) * 100;
                    }

                    SubTaskB temp = new SubTaskB(i, i + timePeriod, timePeriod, average, 0, similarRate, currentRegion, region);
                    avgTemp.add(temp);
                    statement.close();
                }

                if(mostSimilar){
                    SortLowToHigh(avgTemp);
                }
                else{
                    SortHighToLow(avgTemp);
                }
                if(omit){
                result.add(avgTemp.get(0));
                } else {
                    result.addAll(avgTemp);
                }
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    

        return result;
    }

    public static ArrayList<SubTaskB> similarPopulationTask2 (int startingYear, int timePeriod, String selectedRegion, int mode, boolean mostSimilar, boolean omit){
    ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the region exists
            String query = "SELECT EXISTS (SELECT 1 FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }
            preparedStatement.close();


            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);

            ResultSet results = preparedStatement.executeQuery();
            double avg = 0;
            while (results.next()) {
                avg = results.getDouble("Avg");
            }

            preparedStatement.close();

            //Retrieve a list of all other regions besides the selected one
            query = "SELECT DISTINCT Country_Name AS Country FROM Population";

            results = statement.executeQuery(query);
            ArrayList<String> data = new ArrayList<>();
            while (results.next()) {
                    data.add(results.getString("Country"));
            }

            statement.close();



            //Going through every region and find the average population in a certain time period
            int beginYear = 0;
            int endYear = 0;

            for(int x = 0; x < data.size(); x++){
                ArrayList<SubTaskB> avgTemp = new ArrayList<>();
                double average;
                double similarRate;
                String currentRegion = data.get(x);

                //Find available first and last year
                query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = ?";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, currentRegion);
                results = preparedStatement.executeQuery();

                if (results.next()){
                    beginYear = results.getInt("Min");
                    endYear = results.getInt("Max");
                }
                preparedStatement.close();

                //Find average population of the selected region in various time period and then get the highest similar rate
                for(int i = beginYear; i <= endYear - (timePeriod); i++){
                    average = 0;


                    query = "SELECT AVG(Population) AS Avg FROM Population WHERE Country_Name = ? AND (Year BETWEEN ? AND ?)";

                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, currentRegion);
                    preparedStatement.setInt(2, i);
                    preparedStatement.setInt(3, i + timePeriod);
                    results = preparedStatement.executeQuery();

                    if(results.next()) {
                        average = results.getDouble("Avg");
                    }

                    results.close();

                    // Absolute or relative
                    if (mode == 1){
                        similarRate = Math.abs(average - avg) ;
                    }
                    else{
                        similarRate = Math.abs((average - avg) / avg) * 100;
                    }

                    SubTaskB temp = new SubTaskB(i, i + timePeriod, timePeriod, 0, (long) average, similarRate, currentRegion, 1);
                    avgTemp.add(temp);

                    preparedStatement.close();

                }

                if(mostSimilar){
                    SortLowToHigh(avgTemp);
                }
                else{
                    SortHighToLow(avgTemp);
                }

                if(omit){
                    result.add(avgTemp.get(0));
                } else {
                    result.addAll(avgTemp);
                }
            }


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    

        return result;
    }

    public static ArrayList<SubTaskB> similarTempAndPopulationTask2 (int startingYear, int timePeriod, String selectedRegion, int mode, boolean mostSimilar, boolean omit){
    ArrayList<SubTaskB> result = new ArrayList<>();
        
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the region exists
            String query = "SELECT EXISTS (SELECT 1 FROM GlobalYearlyLandTempByCountry AS Temperature JOIN Population ON Temperature.Country = Population.Country_Name WHERE Temperature.Country = ? AND (Population.Year BETWEEN ? AND ?))";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRegion);
            preparedStatement.setInt(2, startingYear);
            preparedStatement.setInt(3, startingYear + timePeriod);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                return result;
            }
            
            preparedStatement.close();

            // Prepare and execute the first query
            String query1 = "SELECT AVG(Population.Population) AS AvgPopulation " +
            "FROM Population " +
            "WHERE Population.Country_Name = ? AND (Population.Year BETWEEN ? AND ?)";

            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement1.setString(1, selectedRegion);
            preparedStatement1.setInt(2, startingYear);
            preparedStatement1.setInt(3, startingYear + timePeriod);
            ResultSet results1 = preparedStatement1.executeQuery();

            double avgPopulation = 0;
            if (results1.next()) {
            avgPopulation = results1.getDouble("AvgPopulation");
            }
            preparedStatement1.close();

            // Prepare and execute the second query
            String query2 = "SELECT AVG(Temp.AverageTemperature) AS AvgTemp " +
            "FROM GlobalYearlyLandTempByCountry AS Temp " +
            "WHERE Temp.Country = ? AND (Temp.Year BETWEEN ? AND ?)";

            PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
            preparedStatement2.setString(1, selectedRegion);
            preparedStatement2.setInt(2, startingYear);
            preparedStatement2.setInt(3, startingYear + timePeriod);
            ResultSet results2 = preparedStatement2.executeQuery();

            double avgTemp = 0;
            if (results2.next()) {
            avgTemp = results2.getDouble("AvgTemp");
            }
            preparedStatement2.close();

            //Retrieve a list of all other regions besides the selected one

            query = "SELECT DISTINCT Population.Country_Name AS Country " +
               "FROM Population " +
               "JOIN GlobalYearlyLandTempByCountry ON Population.Country_Name = GlobalYearlyLandTempByCountry.Country";

            preparedStatement = connection.prepareStatement(query);
            ResultSet results = preparedStatement.executeQuery();

            ArrayList<String> data = new ArrayList<>();
            while (results.next()) {
                data.add(results.getString("Country"));
            }

            preparedStatement.close();

            //Going through every region and find the average temperature and population in a certain time period

            query1 = "SELECT AVG(Population.Population) AS AvgPopulation " +
                                "FROM Population " +
                                "WHERE Population.Country_Name = ? AND (Population.Year BETWEEN ? AND ?)";


            query2 = "SELECT AVG(Temp.AverageTemperature) AS AvgTemp " +
                                "FROM GlobalYearlyLandTempByCountry AS Temp " +
                                "WHERE Temp.Country = ? AND (Temp.Year BETWEEN ? AND ?)";
                        
            preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement2 = connection.prepareStatement(query2);
            
            for(int x = 0; x < data.size(); x++){
                String currentRegion = data.get(x);
                ArrayList<SubTaskB> tempList = new ArrayList<>();
                double averageTemp = 0;
                double averagePopulation = 0;
                double differenceScore = 0;

                preparedStatement1.setString(1, currentRegion);
                preparedStatement2.setString(1, currentRegion);

                //Find available first and last year

                query = "SELECT MIN(Year) AS Min, MAX(Year) AS Max FROM Population WHERE Country_Name = ?";
                int beginYear = 0;
                int endYear = 0;

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, selectedRegion);
                results = preparedStatement.executeQuery();

                if (results.next()){
                    beginYear = results.getInt("Min");
                    endYear = results.getInt("Max");
                }
                preparedStatement.close();

                //Find average population and temperature of the selected region in various time period and then get the highest similar rate
                for(int i = beginYear; i <= endYear - (timePeriod); i++){
                    
                    averageTemp = 0;
                    averagePopulation = 0;

                    preparedStatement1.setInt(2, i);
                    preparedStatement1.setInt(3, i + timePeriod);

                    preparedStatement2.setInt(2, i);
                    preparedStatement2.setInt(3, i + timePeriod);

                    results1 = preparedStatement1.executeQuery();


                    if (results1.next()) {
                        averagePopulation = results1.getDouble("AvgPopulation");
                    }

                    results2 = preparedStatement2.executeQuery();

                    if (results2.next()) {
                        averageTemp = results2.getDouble("AvgTemp");
                    }
                    
                    if (mode == 1){
                        differenceScore = Math.sqrt((averageTemp - avgTemp) * (averageTemp - avgTemp) + (averagePopulation - avgPopulation) * (averagePopulation - avgPopulation));
                    }
                    else {
                        differenceScore = Math.sqrt(Math.pow(((averageTemp - avgTemp) / avgTemp) * 100, 2) + Math.pow(((averagePopulation - avgPopulation) / avgPopulation) * 100, 2));
                    }

                    SubTaskB temp = new SubTaskB(i, i + timePeriod, timePeriod, averageTemp, (long) averagePopulation, differenceScore, currentRegion, 1);
                    tempList.add(temp);

                }

                if(mostSimilar){
                    SortLowToHigh(tempList);
                }
                else{
                    SortHighToLow(tempList);
                }

                if(omit){
                    result.add(tempList.get(0));
                } else {
                    result.addAll(tempList);
                }
            }


            preparedStatement1.close();
            preparedStatement2.close();


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    

        return result;
    }

    public static ArrayList<SubTaskB> SortHighToLow (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o2.getDifferenceScore(), o1.getDifferenceScore());
        }
    });

        return data;
    }

    public static ArrayList<SubTaskB> SortLowToHigh (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o1.getDifferenceScore(), o2.getDifferenceScore());
        }
    });

        return data;
    }

    public static ArrayList<SubTaskB> Reverse (ArrayList<SubTaskB> data){
    
        Collections.reverse(data);

        return data;
    }

    public ArrayList<SubTaskB> publicSortHighToLow (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o2.getDifferenceScore(), o1.getDifferenceScore());
        }
    });

        return data;
    }

    public ArrayList<SubTaskB> publicSortLowToHigh (ArrayList<SubTaskB> data){
    
    Collections.sort(data, new Comparator<SubTaskB>() {
        @Override
        public int compare(SubTaskB o1, SubTaskB o2) {
            return Double.compare(o1.getDifferenceScore(), o2.getDifferenceScore());
        }
    });

        return data;
    }

    public ArrayList<SubTaskB> publicReverse (ArrayList<SubTaskB> data){
    
        Collections.reverse(data);

        return data;
    }


    // Subtask A
    public double SubTaskATask2 (int startingYear, int timePeriod, String selectedRegion, int region){
        double averageTemp = 0;
        
        Connection connection = null;

        if (timePeriod == 0){
            System.out.println("Invalid time period");
            return averageTemp;
        }
        
        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS (SELECT 1 FROM ";

            switch(region){
                case 0:
                    query += "GlobalYearlyTemp WHERE Year BETWEEN ? AND ?)";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?)";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?)";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?)";
                    break;
            }  

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return averageTemp;
            }

            preparedStatement.close();

            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg FROM ";

            switch(region){
                case 0:
                    query += "GlobalYearlyTemp WHERE Year BETWEEN ? AND ?";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?";
                    break;
            }

            preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod);
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                averageTemp = resultSet.getDouble("Avg");
            }

            preparedStatement.close();

            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        
        return averageTemp;
    }

    
    public ArrayList<SubTaskA> SubTaskATask4NoFilter (int[] startingYears, int timePeriod, String[] selectedRegions, int region, boolean Desc){
        ArrayList<SubTaskA> result = new ArrayList<SubTaskA>();

        for(String selectedRegion : selectedRegions){
            for(int startingYear : startingYears){
                SubTaskA temp = differenceInAverage(startingYear, timePeriod, selectedRegion, region);
                result.add(temp);
            }
        }

        if(Desc){
            TaskASortDescATD(result);
        } else {
            TaskASortAscATD(result);
        }

        return result;
    }

    public ArrayList<SubTaskA> SubTaskATask4WithFilter (int[] startingYears, int timePeriod, String[] selectedRegions, int region, double startValue, double endValue, int mode, boolean sortByPopulation, boolean Desc){
        ArrayList<SubTaskA> data = new ArrayList<SubTaskA>();
        ArrayList<SubTaskA> result = new ArrayList<SubTaskA>();


        for(String selectedRegion : selectedRegions){
            for(int startingYear : startingYears){
                SubTaskA temp = differenceInAverage(startingYear, timePeriod, selectedRegion, region);
                data.add(temp);
            }
        }

        switch(mode){
            case 1:
                for(SubTaskA temp : data){
                    if(temp.getAverageTemp() >= startValue && temp.getAverageTemp() <= endValue){
                        result.add(temp);
                    }
                }

                break;
            case 2:
                for(SubTaskA temp : data){
                    if(temp.getAveragePopulation() >= startValue && temp.getAveragePopulation() <= endValue){
                        result.add(temp);
                    }
                }
                
                break;
        }

        if(sortByPopulation){
            if(Desc){
                TaskASortDESCPopulation(result);
            } else {
                TaskASortASCPopulation(result);
            }
        }
        else{
            if(Desc){
                TaskASortDESCAvgTemp(result);
            } else {
                TaskASortASCAvgTemp(result);
            }
        }

        return result;
    }

    public ArrayList<SubTaskA> SubTaskATask3 (int[] startingYears, int timePeriod, String selectedRegion, int region){
        ArrayList<SubTaskA> result = new ArrayList<SubTaskA>();

        for(int startingYear : startingYears){
            SubTaskA temp = differenceInAverage(startingYear, timePeriod, selectedRegion, region);
            result.add(temp);
        }

        return result;
    }

    public ArrayList<SubTaskA> SubTaskATask6 (double startValue, double endValue, int mode){
        ArrayList<SubTaskA> data = new ArrayList<>();

        Connection connection = null;

        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "";
            switch(mode){
                case 1:
                    query = "SELECT DISTINCT Country, AverageTemperature AS Avg FROM GlobalYearlyLandTempByCountry WHERE AverageTemperature BETWEEN ? AND ? AND YEAR = 2013";
                    break;
                case 2:
                    query = "SELECT DISTINCT Country_Name, Population FROM Population WHERE Population BETWEEN ? AND ? AND YEAR = 2013";
                    break;
            }    

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, startValue);
            preparedStatement.setDouble(2, endValue);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                switch(mode){
                    case 1:
                        SubTaskA temp = new SubTaskA(resultSet.getDouble("Avg"), 0, resultSet.getString("Country"), 1, startValue, endValue);
                        data.add(temp);
                        break;
                    case 2:
                        SubTaskA temp2 = new SubTaskA(0, resultSet.getLong("Population"), resultSet.getString("Country_Name"), 1, startValue, endValue);
                        data.add(temp2);
                        break;
                }
            }
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return data;
    }

    public static SubTaskA differenceInAverage (int startingYear, int timePeriod, String selectedRegion, int region){
        SubTaskA result = null;
        
        Connection connection = null;

        if (timePeriod == 0){
            System.out.println("Invalid time period");
            return result;
        }
        
        try{
            connection = DriverManager.getConnection(DATABASE1);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //Check if the country exists
            String query = "SELECT EXISTS (SELECT 1 FROM ";

            switch(region){
                case 0:
                    query += "GlobalYearlyTemp WHERE Year BETWEEN ? AND ?)";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?)";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?)";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?)";
                    break;
            }  

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && !resultSet.getBoolean(1)) {
                System.out.println("Does not exist\n");
                System.out.println(selectedRegion);
                return result;
            }

            preparedStatement.close();

            //Find average temperature of the selected region in a certain time period
            query = "SELECT AVG(AverageTemperature) AS Avg FROM ";

            switch(region){
                case 0:
                    query += "GlobalYearlyTemp WHERE Year BETWEEN ? AND ?";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?";
                    break;
            }

            preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod);
            }

            resultSet = preparedStatement.executeQuery();
            double avg = 0;
            while (resultSet.next()) {
                avg = resultSet.getDouble("Avg");
            }

            preparedStatement.close();

            //Find difference in temperature of the selected region
            query = "SELECT MinimumTemperature AS Min, MaximumTemperature AS Max FROM ";
            switch(region){
                case 0:
                    query += "GlobalYearlyTemp WHERE Year BETWEEN ? AND ?";
                    break;
                case 1:
                    query += "GlobalYearlyLandTempByCountry WHERE Country = ? AND Year BETWEEN ? AND ?";
                    break;
                case 2:
                    query += "GlobalYearlyLandTempByState WHERE State = ? AND Year BETWEEN ? AND ?";
                    break;
                case 3:
                    query += "GlobalYearlyLandTempByCity WHERE City = ? AND Year BETWEEN ? AND ?";
                    break;
            }

            preparedStatement = connection.prepareStatement(query);

            if(region != 0){
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod);
            } else {
                preparedStatement.setInt(1, startingYear);
                preparedStatement.setInt(2, startingYear + timePeriod);
            }
            
            resultSet = preparedStatement.executeQuery();
            double min = 0;
            double max = 0;
            double minTotal = 0;
            double maxTotal = 0;
            while (resultSet.next()) {
                min = resultSet.getDouble("Min");
                max = resultSet.getDouble("Max");
                minTotal += min;
                maxTotal += max;
            }
            double averageTempDifference = Math.abs((maxTotal - minTotal) / timePeriod);

            preparedStatement.close();

            double avgPopulation = 0;
            if(region == 1){
                query = "SELECT AVG(Population) AS AVG FROM Population WHERE Country_Name = ? AND Year BETWEEN ? AND ?";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, selectedRegion);
                preparedStatement.setInt(2, startingYear);
                preparedStatement.setInt(3, startingYear + timePeriod);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    avgPopulation = resultSet.getDouble("Avg");
                }

                preparedStatement.close();
            }
            result = new SubTaskA(startingYear, startingYear + timePeriod, timePeriod, avg, averageTempDifference, (long) avgPopulation, selectedRegion, region);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        
        return result;
    }

    public ArrayList<SubTaskA> TaskASortAscATD (ArrayList<SubTaskA> data){
    
        Collections.sort(data, new Comparator<SubTaskA>() {
            @Override
            public int compare(SubTaskA o1, SubTaskA o2) {
                return Double.compare(o1.getAverageTempDifference(), o2.getAverageTempDifference());
            }
        });
    
        return data;
    }

    public ArrayList<SubTaskA> TaskASortDescATD (ArrayList<SubTaskA> data){
    
        Collections.sort(data, new Comparator<SubTaskA>() {
            @Override
            public int compare(SubTaskA o1, SubTaskA o2) {
                return Double.compare(o2.getAverageTempDifference(), o1.getAverageTempDifference());
            }
        });
    
        return data;
    }

    public ArrayList<SubTaskA> TaskASortASCPopulation (ArrayList<SubTaskA> data){
        
        Collections.sort(data, new Comparator<SubTaskA>() {
            @Override
            public int compare(SubTaskA o1, SubTaskA o2) {
                return Long.compare(o1.getAveragePopulation(), o2.getAveragePopulation());
            }
        });
    
        return data;
    }

    public ArrayList<SubTaskA> TaskASortDESCPopulation (ArrayList<SubTaskA> data){
        
            Collections.sort(data, new Comparator<SubTaskA>() {
                @Override
                public int compare(SubTaskA o1, SubTaskA o2) {
                    return Long.compare(o2.getAveragePopulation(), o1.getAveragePopulation());
                }
            });
        
            return data;
    }

    public ArrayList<SubTaskA> TaskASortASCAvgTemp (ArrayList<SubTaskA> data){
        
            Collections.sort(data, new Comparator<SubTaskA>() {
                @Override
                public int compare(SubTaskA o1, SubTaskA o2) {
                    return Double.compare(o1.getAverageTemp(), o2.getAverageTemp());
                }
            });
        
            return data;
    }

    public ArrayList<SubTaskA> TaskASortDESCAvgTemp (ArrayList<SubTaskA> data){
        
            Collections.sort(data, new Comparator<SubTaskA>() {
                @Override
                public int compare(SubTaskA o1, SubTaskA o2) {
                    return Double.compare(o2.getAverageTemp(), o1.getAverageTemp());
                }
            });
        
            return data;
    }

    public ArrayList<SubTaskA> ReverseTaskA (ArrayList<SubTaskA> data){
        
            Collections.reverse(data);
        
            return data;
    }

    public String[][] getPersona(){
        String[][] result = new String[4][5];
        Connection connection = null;

        try{
            connection = DriverManager.getConnection("jdbc:sqlite:database/TempPopulation.db");

            Statement stmt = connection.createStatement();
            stmt.setQueryTimeout(30);

            String query = "select * from persona;";

            ResultSet rs = stmt.executeQuery(query);
            int i =0;
            while (rs.next()) {
                result[i][0] = "Persona " + rs.getString(1)+": "+rs.getString(2);
                result[i][1] = rs.getString(3);
                result[i][2] = rs.getString(4);
                result[i][3] = rs.getString(5);
                result[i][4] = rs.getString(6);
                i++;
            }

            stmt.close();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return result;
    }
}