package com.khoi.demo.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Cache.Connection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.khoi.demo.JDBCConnection.JDBCConnection;
import com.khoi.demo.model.Input;
import com.khoi.demo.model.SubTaskA;
import com.khoi.demo.model.SubTaskB;

import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {
  @GetMapping (value = {"/home", "/home/", "/index"})
  public String homePage() {
    return "redirect:/";
  }

  @GetMapping("/")
  public String showIndex(Model model, HttpSession session) {
    //Add username from their session id
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));
    // model.addAttribute("username", "Guest");
    return "index";
  }

  @GetMapping("/contribute")
  public String showContribute(HttpSession session, Model model) {
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));
    return "Other/contribute";
  }

  @GetMapping("/overview")
  public String inputData(Model model, HttpSession session) {
    //Add username from their session id
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));
    // model.addAttribute("username", "Guest");
    JDBCConnection jdbc = new JDBCConnection();
    Random random = new Random();

    int min = 1960;
    int max = 2013;
    int timePeriod = max - min + 1;
    int startYear, endYear;
    do {
      startYear = random.nextInt(timePeriod) + min;
    } while (startYear == 2013);
    do {
      endYear = random.nextInt(timePeriod) + min;
    } while (endYear <= startYear || endYear == 1960);

    long startPopulationWorld = jdbc.getWorldPopuByYear(startYear);
    long endPopulationWorld = jdbc.getWorldPopuByYear(endYear);
    long populationRaw = jdbc.getWorldPopulationRaw(startYear, endYear);

    String startPopulationWorldStr = String.format( "%,d", startPopulationWorld);
    startPopulationWorldStr = startPopulationWorldStr.replace(',', '.');
    String endPopulationWorldStr = String.format("%,d", endPopulationWorld);
    endPopulationWorldStr = endPopulationWorldStr.replace(',', '.');
    String populationRawStr = String.format("%,d", populationRaw);
    populationRawStr = populationRawStr.replace(',', '.');

    String temperatureProportion = String.format("%.3f", jdbc.getWorldTemperatureProportion(startYear, endYear));
    String temperatureRaw = String.format("%.3f", jdbc.getWorldTemperatureRaw(startYear, endYear));
    String populationProportion = String.format("%.3f", jdbc.getWorldPopulationProportion(startYear, endYear));

    model.addAttribute("input", new Input());
    model.addAttribute("startYearGet", startYear);
    model.addAttribute("endYearGet", endYear);
    model.addAttribute("countryNameGet", jdbc.getResultByCountryName().get(random.nextInt(206)));
    model.addAttribute("startPopulationWorld", startPopulationWorldStr);
    model.addAttribute("startTempWorld", jdbc.getWorldTempByYear(startYear));
    model.addAttribute("endPopulationWorld", endPopulationWorldStr);
    model.addAttribute("endTempWorld", jdbc.getWorldTempByYear(endYear));
    model.addAttribute("temperatureProportion", temperatureProportion);
    model.addAttribute("temperatureRaw", temperatureRaw);
    model.addAttribute("populationProportion", populationProportion);
    model.addAttribute("populationRaw", populationRawStr);
    model.addAttribute("timePeriod", endYear - startYear);
    model.addAttribute("sortByTempDesc", jdbc.getCountriesLatestSortTempDesc());
    model.addAttribute("sortByPopuDesc", jdbc.getCountriesLatestSortPopuDesc());
    model.addAttribute("sortByTempAsc", jdbc.getCountriesLatestSortTempAsc());
    model.addAttribute("sortByPopuAsc", jdbc.getCountriesLatestSortPopuAsc());

    return "Level2/data-overview";
  }
  
  @PostMapping("/Countries")
  public String countriesSubmit(@ModelAttribute Input input, Model model, HttpSession session) {
    JDBCConnection jdbc = new JDBCConnection();
    //Add username from their session id
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));

    ArrayList<Input> emptyInput = jdbc.emptyArrayListInput();
    input.setCountryName(jdbc.capitalizeWords(input.getCountryName()));

    ArrayList<Input> inputsCityAveAsc = new ArrayList<>();
    ArrayList<Input> inputsStateAveAsc = new ArrayList<>();
    ArrayList<Input> inputsCityMinAsc = new ArrayList<>();
    ArrayList<Input> inputsStateMinAsc = new ArrayList<>();
    ArrayList<Input> inputsCityMaxAsc = new ArrayList<>();
    ArrayList<Input> inputsStateMaxAsc = new ArrayList<>();

    ArrayList<Input> inputsCityAveDesc = new ArrayList<>();
    ArrayList<Input> inputsStateAveDesc = new ArrayList<>();
    ArrayList<Input> inputsCityMinDesc = new ArrayList<>();
    ArrayList<Input> inputsStateMinDesc = new ArrayList<>();
    ArrayList<Input> inputsCityMaxDesc = new ArrayList<>();
    ArrayList<Input> inputsStateMaxDesc = new ArrayList<>();

    inputsCityAveAsc = jdbc.getStartEndCityStateAvgTempChangeOrder(input.getCountryName(), "City", input.getStartYear(), input.getEndYear(), "ASC");
    inputsStateAveAsc = jdbc.getStartEndCityStateAvgTempChangeOrder(input.getCountryName(), "State", input.getStartYear(), input.getEndYear(), "ASC");
    inputsCityMinAsc = jdbc.getStartEndCityStateMinTempChangeOrder(input.getCountryName(), "City", input.getStartYear(), input.getEndYear(), "ASC");
    inputsStateMinAsc = jdbc.getStartEndCityStateMinTempChangeOrder(input.getCountryName(), "State", input.getStartYear(), input.getEndYear(), "ASC");
    inputsCityMaxAsc = jdbc.getStartEndCityStateMaxTempChangeOrder(input.getCountryName(), "City", input.getStartYear(), input.getEndYear(), "ASC");
    inputsStateMaxAsc = jdbc.getStartEndCityStateMaxTempChangeOrder(input.getCountryName(), "State", input.getStartYear(), input.getEndYear(), "ASC");

    inputsCityAveDesc = jdbc.getStartEndCityStateAvgTempChangeOrder(input.getCountryName(), "City", input.getStartYear(), input.getEndYear(), "DESC");
    inputsStateAveDesc = jdbc.getStartEndCityStateAvgTempChangeOrder(input.getCountryName(), "State", input.getStartYear(), input.getEndYear(), "DESC");
    inputsCityMinDesc = jdbc.getStartEndCityStateMinTempChangeOrder(input.getCountryName(), "City", input.getStartYear(), input.getEndYear(), "DESC");
    inputsStateMinDesc = jdbc.getStartEndCityStateMinTempChangeOrder(input.getCountryName(), "State", input.getStartYear(), input.getEndYear(), "DESC");
    inputsCityMaxDesc = jdbc.getStartEndCityStateMaxTempChangeOrder(input.getCountryName(), "City", input.getStartYear(), input.getEndYear(), "DESC");
    inputsStateMaxDesc = jdbc.getStartEndCityStateMaxTempChangeOrder(input.getCountryName(), "State", input.getStartYear(), input.getEndYear(), "DESC");

    long startPopulationCountry = jdbc.getCountriesPopuByYear(input.getCountryName(), (int)input.getStartYear());
    String startPopulationCountryStr = String.format( "%,d", startPopulationCountry);
    startPopulationCountryStr = startPopulationCountryStr.replace(',', '.');

    long endPopulationCountry = jdbc.getCountriesPopuByYear(input.getCountryName(), (int)input.getEndYear());
    String endPopulationCountryStr = String.format("%,d", endPopulationCountry);
    endPopulationCountryStr = endPopulationCountryStr.replace(',', '.');

    long populationRaw = jdbc.getCountryPopulationRaw(input.getCountryName(), input.getStartYear(), input.getEndYear());
    String populationRawStr = String.format("%,d", populationRaw);
    populationRawStr = populationRawStr.replace(',', '.');

    String temperatureProportion = String.format("%.3f", jdbc.getCountryTemperatureProportion(input.getCountryName(), input.getStartYear(), input.getEndYear()));
    String temperatureRaw = String.format("%.3f", jdbc.getCountryTemperatureRaw(input.getCountryName(), input.getStartYear(), input.getEndYear()));
    String populationProportion = String.format("%.3f", jdbc.getCountryPopulationProportion(input.getCountryName(), input.getStartYear(), input.getEndYear()));

    model.addAttribute("resultCountry", jdbc.getResultByCountryName());
    model.addAttribute("ciStNum", inputsCityAveDesc.size() + inputsStateAveDesc.size());

    if (!inputsCityAveDesc.isEmpty()) {
      model.addAttribute("cityAveDesc", inputsCityAveDesc);
      model.addAttribute("cityMinDesc", inputsCityMinDesc);
      model.addAttribute("cityMaxDesc", inputsCityMaxDesc);
      model.addAttribute("cityAveAsc", inputsCityAveAsc);
      model.addAttribute("cityMinAsc", inputsCityMinAsc);
      model.addAttribute("cityMaxAsc", inputsCityMaxAsc);
    } else {
      model.addAttribute("cityAveDesc", emptyInput);
      model.addAttribute("cityMinDesc", emptyInput);
      model.addAttribute("cityMaxDesc", emptyInput);
      model.addAttribute("cityAveAsc", emptyInput);
      model.addAttribute("cityMinAsc", emptyInput);
      model.addAttribute("cityMaxAsc", emptyInput);
    } if (!inputsStateAveDesc.isEmpty()) {
      model.addAttribute("stateAveDesc", inputsStateAveDesc);
      model.addAttribute("stateMinDesc", inputsStateMinDesc);
      model.addAttribute("stateMaxDesc", inputsStateMaxDesc);
      model.addAttribute("stateAveAsc", inputsStateAveAsc);
      model.addAttribute("stateMinAsc", inputsStateMinAsc);
      model.addAttribute("stateMaxAsc", inputsStateMaxAsc);
    } else {
      model.addAttribute("stateAveDesc", emptyInput);
      model.addAttribute("stateMinDesc", emptyInput);
      model.addAttribute("stateMaxDesc", emptyInput);
      model.addAttribute("stateAveAsc", emptyInput);
      model.addAttribute("stateMinAsc", emptyInput);
      model.addAttribute("stateMaxAsc", emptyInput);
    }
    model.addAttribute("countryCode", jdbc.getCountriesCodeByCountry(input.getCountryName()));

    if (input.getStartYear() >= 1960 && input.getStartYear() <= 2013 && input.getEndYear() >= 1960 && input.getEndYear() <= 2013) {
      model.addAttribute("startPopulationCountry", startPopulationCountryStr);
      model.addAttribute("startTempCountry", jdbc.getCountriesTempByYear(input.getCountryName(), (int)input.getStartYear()));
      model.addAttribute("endPopulationCountry", endPopulationCountryStr);
      model.addAttribute("endTempCountry", jdbc.getCountriesTempByYear(input.getCountryName(), (int)input.getEndYear()));
      model.addAttribute("temperatureProportion", temperatureProportion);
      model.addAttribute("temperatureRaw", temperatureRaw);
      model.addAttribute("populationProportion", populationProportion);
      model.addAttribute("populationRaw", populationRawStr);
    } else if (input.getStartYear() >= 1750 && input.getStartYear() <= 1959 && input.getEndYear() >= 1960 && input.getEndYear() <= 2013) {
      model.addAttribute("startPopulationCountry", "N/A");
      model.addAttribute("startTempCountry", jdbc.getCountriesTempByYear(input.getCountryName(), (int)input.getStartYear()));
      model.addAttribute("endPopulationCountry", endPopulationCountryStr);
      model.addAttribute("endTempCountry", jdbc.getCountriesTempByYear(input.getCountryName(), (int)input.getEndYear()));
      model.addAttribute("temperatureProportion", temperatureProportion);
      model.addAttribute("temperatureRaw", temperatureRaw);
      model.addAttribute("populationProportion", "N/A");
      model.addAttribute("populationRaw", "N/A");
    } else if (input.getStartYear() >= 1750 && input.getStartYear() <= 1959 && input.getEndYear() >= 1750 && input.getEndYear() <= 1959) {
      model.addAttribute("startPopulationCountry", "N/A");
      model.addAttribute("startTempCountry", jdbc.getCountriesTempByYear(input.getCountryName(), (int)input.getStartYear()));
      model.addAttribute("endPopulationCountry", "N/A");
      model.addAttribute("endTempCountry", jdbc.getCountriesTempByYear(input.getCountryName(), (int)input.getEndYear()));
      model.addAttribute("temperatureProportion", temperatureProportion);
      model.addAttribute("temperatureRaw", temperatureRaw);
      model.addAttribute("populationProportion", "N/A");
      model.addAttribute("populationRaw", "N/A");
    } else {
      model.addAttribute("startPopulationCountry", "N/A");
      model.addAttribute("startTempCountry", "N/A");
      model.addAttribute("endPopulationCountry", "N/A");
      model.addAttribute("endTempCountry", "N/A");
      model.addAttribute("temperatureProportion", "N/A");
      model.addAttribute("temperatureRaw", "N/A");
      model.addAttribute("populationProportion", "N/A");
      model.addAttribute("populationRaw", "N/A");
    }

    model.addAttribute("timePeriod", input.getEndYear() - input.getStartYear());
    model.addAttribute("sortByTempDesc", jdbc.getCountriesLatestSortTempDesc());
    model.addAttribute("sortByPopuDesc", jdbc.getCountriesLatestSortPopuDesc());
    model.addAttribute("sortByTempAsc", jdbc.getCountriesLatestSortTempAsc());
    model.addAttribute("sortByPopuAsc", jdbc.getCountriesLatestSortPopuAsc());

    System.out.println("Country name: " + input.getCountryName());
    System.out.println("Start year: " + input.getStartYear());
    System.out.println("End year: " + input.getEndYear());

    return "Level2/resultCountries";
  }

  @PostMapping("/World")
  public String worldSubmit(@ModelAttribute Input input, Model model, HttpSession session) {
    JDBCConnection jdbc = new JDBCConnection();
    //Add username from their session id
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));
    // model.addAttribute("username", "Guest");

    long startPopulationWorld = jdbc.getWorldPopuByYear((int)input.getStartYear());
    String startPopulationWorldStr = String.format( "%,d", startPopulationWorld);
    startPopulationWorldStr = startPopulationWorldStr.replace(',', '.');

    long endPopulationWorld = jdbc.getWorldPopuByYear((int)input.getEndYear());
    String endPopulationWorldStr = String.format("%,d", endPopulationWorld);
    endPopulationWorldStr = endPopulationWorldStr.replace(',', '.');

    long populationRaw = jdbc.getWorldPopulationRaw(input.getStartYear(), input.getEndYear());
    String populationRawStr = String.format("%,d", populationRaw);
    populationRawStr = populationRawStr.replace(',', '.');

    String temperatureProportion = String.format("%.3f", jdbc.getWorldTemperatureProportion(input.getStartYear(), input.getEndYear()));
    String temperatureRaw = String.format("%.3f", jdbc.getWorldTemperatureRaw(input.getStartYear(), input.getEndYear()));
    String populationProportion = String.format("%.3f", jdbc.getWorldPopulationProportion(input.getStartYear(), input.getEndYear()));

    
    if (input.getStartYear() >= 1960 && input.getStartYear() <= 2013 && input.getEndYear() >= 1960 && input.getEndYear() <= 2013) {
      model.addAttribute("startPopulationWorld", startPopulationWorldStr);
      model.addAttribute("startTempWorld", jdbc.getWorldTempByYear((int)input.getStartYear()));
      model.addAttribute("endPopulationWorld", endPopulationWorldStr);
      model.addAttribute("endTempWorld", jdbc.getWorldTempByYear((int)input.getEndYear()));
      model.addAttribute("temperatureProportion", temperatureProportion);
      model.addAttribute("temperatureRaw", temperatureRaw);
      model.addAttribute("populationProportion", populationProportion);
      model.addAttribute("populationRaw", populationRawStr);
    } else if (input.getStartYear() >= 1750 && input.getStartYear() <= 1959 && input.getEndYear() >= 1960 && input.getEndYear() <= 2013) {
      model.addAttribute("startPopulationWorld", "N/A");
      model.addAttribute("startTempWorld", jdbc.getWorldTempByYear((int)input.getStartYear()));
      model.addAttribute("endPopulationWorld", endPopulationWorldStr);
      model.addAttribute("endTempWorld", jdbc.getWorldTempByYear((int)input.getEndYear()));
      model.addAttribute("temperatureProportion", temperatureProportion);
      model.addAttribute("temperatureRaw", temperatureRaw);
      model.addAttribute("populationProportion", "N/A");
      model.addAttribute("populationRaw", "N/A");
    } else if (input.getStartYear() >= 1750 && input.getStartYear() <= 1959 && input.getEndYear() >= 1750 && input.getEndYear() <= 1959) {
      model.addAttribute("startPopulationWorld", "N/A");
      model.addAttribute("startTempWorld", jdbc.getWorldTempByYear((int)input.getStartYear()));
      model.addAttribute("endPopulationWorld", "N/A");
      model.addAttribute("endTempWorld", jdbc.getWorldTempByYear((int)input.getEndYear()));
      model.addAttribute("temperatureProportion", temperatureProportion);
      model.addAttribute("temperatureRaw", temperatureRaw);
      model.addAttribute("populationProportion", "N/A");
      model.addAttribute("populationRaw", "N/A");
    } else {
      model.addAttribute("startPopulationWorld", "N/A");
      model.addAttribute("startTempWorld", "N/A");
      model.addAttribute("endPopulationWorld", "N/A");
      model.addAttribute("endTempWorld", "N/A");
      model.addAttribute("temperatureProportion", "N/A");
      model.addAttribute("temperatureRaw", "N/A");
      model.addAttribute("populationProportion", "N/A");
      model.addAttribute("populationRaw", "N/A");
    }
    
    model.addAttribute("timePeriod", input.getEndYear() - input.getStartYear());
    model.addAttribute("sortByTempDesc", jdbc.getCountriesLatestSortTempDesc());
    model.addAttribute("sortByPopuDesc", jdbc.getCountriesLatestSortPopuDesc());
    model.addAttribute("sortByTempAsc", jdbc.getCountriesLatestSortTempAsc());
    model.addAttribute("sortByPopuAsc", jdbc.getCountriesLatestSortPopuAsc());

    System.out.println("Start year: " + input.getStartYear());
    System.out.println("End year: " + input.getEndYear());
    // System.out.println("Order: " + input.getOrder());
    System.out.println("Sort by: " + input.getSortBy());

    return "Level2/resultWorld";
  }

  @GetMapping("/advance")
  public String showAdvanced(Model model, HttpSession session) {
    //Add username from their session id
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));

    // model.addAttribute("username", "Guest");
    model.addAttribute("input", new SubTaskA());
    // model.addAttribute("input1", new SubTaskB());
    model.addAttribute("regionName", "World");
    model.addAttribute("avgTemp", "9.02°C");
    // model.addAttribute("avgPopu", "5033872900");
    model.addAttribute("startYear", 1960);
    model.addAttribute("endYear", 2013);
    model.addAttribute("startYearPopu", "3.031.564.839");
    model.addAttribute("startYearTemp", "8.584°C");
    model.addAttribute("endYearPopu", "7.229.184.551");
    model.addAttribute("endYearTemp", "9.607°C");
    return "Level3/advance-data-overview";
  }

  @PostMapping("/Advance")
  public String showResult(@ModelAttribute SubTaskA input, Model model, HttpSession session) {
    JDBCConnection jdbc = new JDBCConnection();
    //Add username from their session id
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));

    model.addAttribute("input", input);
    int[] startIntArray = null;
    int[] endIntArray = null;

    if (input.getStartingYearStr().contains("/")) {
      String[] stringArray = input.getStartingYearStr().split("/");
      startIntArray = new int[stringArray.length];
      endIntArray = new int[stringArray.length];

      for (int i = 0; i < stringArray.length; i++) {
        startIntArray[i] = Integer.parseInt(stringArray[i]);
        endIntArray[i] = startIntArray[i] + input.getTimePeriod();
      }
      input.setStartingYear(startIntArray[0]);
      input.setEndingYear(input.getStartingYear() + input.getTimePeriod());
    } else {
      input.setStartingYear(Integer.parseInt(input.getStartingYearStr()));
      input.setEndingYear(input.getStartingYear() + input.getTimePeriod());
      startIntArray = new int[1];
      endIntArray = new int[1];
      startIntArray[0] = input.getStartingYear();
      endIntArray[0] = input.getEndingYear();
    }

    String[] regionNames = null;
    if ((input.getSelectedOption().equalsIgnoreCase("country") || input.getSelectedOption().equalsIgnoreCase("city") || input.getSelectedOption().equalsIgnoreCase("state"))  && input.getRegionName().contains("/")) {
      regionNames = input.getRegionName().split("/");
      for (int i = 0; i < regionNames.length; i++) {
        regionNames[i] = jdbc.capitalizeWords(regionNames[i]);
      }
    } else {
      input.setRegionName(jdbc.capitalizeWords(input.getRegionName()));
      regionNames = input.getRegionName().split("/");
    }

    ArrayList<SubTaskA> emptyArrayListA = jdbc.emptyArrayListSubTaskA();
    ArrayList<SubTaskB> emptyArrayListB = jdbc.emptyArrayListSubTaskB();

    if (!input.getRegionName().contains("/") && !input.getStartingYearStr().contains("/")) {
      if (input.getSelectedOption().equalsIgnoreCase("global")) {
        long startPopulationWorld = jdbc.getWorldPopuByYear(input.getStartingYear());
        String startPopulationWorldStr = String.format( "%,d", startPopulationWorld);
        startPopulationWorldStr = startPopulationWorldStr.replace(',', '.');

        long endPopulationWorld = jdbc.getWorldPopuByYear(input.getEndingYear());
        String endPopulationWorldStr = String.format("%,d", endPopulationWorld);
        endPopulationWorldStr = endPopulationWorldStr.replace(',', '.');

        String worldAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), "World", 0));

        model.addAttribute("regionName", "World");
        model.addAttribute("avgTemp",  worldAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationWorldStr);
        model.addAttribute("startYearTemp", jdbc.getWorldTempByYear(input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationWorldStr);
        model.addAttribute("endYearTemp", jdbc.getWorldTempByYear(input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
      } else if (input.getSelectedOption().equalsIgnoreCase("country")) {
        long startPopulationCountry = jdbc.getCountriesPopuByYear(input.getRegionName(), input.getStartingYear());
        String startPopulationCountryStr = String.format( "%,d", startPopulationCountry);
        startPopulationCountryStr = startPopulationCountryStr.replace(',', '.');

        String countriesAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), input.getRegionName(), 1));

        long endPopulationCountry = jdbc.getCountriesPopuByYear(input.getRegionName(), input.getEndingYear());
        String endPopulationCountryStr = String.format("%,d", endPopulationCountry);
        endPopulationCountryStr = endPopulationCountryStr.replace(',', '.');

        model.addAttribute("regionName", input.getRegionName());
        model.addAttribute("avgTemp",  countriesAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationCountryStr);
        model.addAttribute("startYearTemp", jdbc.getCountriesTempByYear(input.getRegionName(), input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationCountryStr);
        model.addAttribute("endYearTemp", jdbc.getCountriesTempByYear(input.getRegionName(), input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
      }
      else if (input.getSelectedOption().equalsIgnoreCase("state")) {

        String stateAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), input.getRegionName(), 2));

        model.addAttribute("regionName", input.getRegionName());
        model.addAttribute("avgTemp",  stateAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearTemp", jdbc.getStateTempByYear(input.getRegionName(), input.getStartingYear()));
        model.addAttribute("endYearTemp", jdbc.getStateTempByYear(input.getRegionName(), input.getEndingYear()));
        model.addAttribute("startYearPopu", "N/A");
        model.addAttribute("endYearPopu", "N/A");
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
      } else {

        String cityAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), input.getRegionName(), 3));

        model.addAttribute("regionName", input.getRegionName());
        model.addAttribute("avgTemp",  cityAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearTemp", jdbc.getCityTempByYear(input.getRegionName(), input.getStartingYear()));
        model.addAttribute("endYearTemp", jdbc.getCityTempByYear(input.getRegionName(), input.getEndingYear()));
        model.addAttribute("startYearPopu", "N/A");
        model.addAttribute("endYearPopu", "N/A");
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
      }

      model.addAttribute("filterPopuSortPopuDesc", emptyArrayListA);
      model.addAttribute("filterPopuSortPopuAsc", emptyArrayListA);
      model.addAttribute("filterPopuSortTempDesc", emptyArrayListA);
      model.addAttribute("filterPopuSortTempAsc", emptyArrayListA);
      model.addAttribute("filterTempSortPopuDesc", emptyArrayListA);
      model.addAttribute("filterTempSortPopuAsc", emptyArrayListA);
      model.addAttribute("filterTempSortTempDesc", emptyArrayListA);
      model.addAttribute("filterTempSortTempAsc", emptyArrayListA);
      model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
      model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
      model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
      model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
      model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
      model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
      model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
      model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
      model.addAttribute("diffInAve", emptyArrayListA);
      model.addAttribute("diffInAveDesc", emptyArrayListA);
      model.addAttribute("diffInAveAsc", emptyArrayListA);
    }
    
    else if (!input.getRegionName().contains("/") && input.getStartingYearStr().contains("/")) {
      if (input.getSelectedOption().equalsIgnoreCase("global")) {
        long startPopulationWorld = jdbc.getWorldPopuByYear(input.getStartingYear());
        String startPopulationWorldStr = String.format( "%,d", startPopulationWorld);
        startPopulationWorldStr = startPopulationWorldStr.replace(',', '.');

        long endPopulationWorld = jdbc.getWorldPopuByYear(input.getEndingYear());
        String endPopulationWorldStr = String.format("%,d", endPopulationWorld);
        endPopulationWorldStr = endPopulationWorldStr.replace(',', '.');

        String worldAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), "World", 0));

        ArrayList<SubTaskA> something = jdbc.SubTaskATask3(startIntArray, input.getTimePeriod(), "World", 0);

        model.addAttribute("regionName", "World");
        model.addAttribute("avgTemp",  worldAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationWorldStr);
        model.addAttribute("startYearTemp", jdbc.getWorldTempByYear(input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationWorldStr);
        model.addAttribute("endYearTemp", jdbc.getWorldTempByYear(input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something.isEmpty()) {
          model.addAttribute("diffInAve", something);
        } else {
          model.addAttribute("diffInAve", emptyArrayListA);
        }
      } else if (input.getSelectedOption().equalsIgnoreCase("country")) {
        long startPopulationCountry = jdbc.getCountriesPopuByYear(input.getRegionName(), input.getStartingYear());
        String startPopulationCountryStr = String.format( "%,d", startPopulationCountry);
        startPopulationCountryStr = startPopulationCountryStr.replace(',', '.');

        long endPopulationCountry = jdbc.getCountriesPopuByYear(input.getRegionName(), input.getEndingYear());
        String endPopulationCountryStr = String.format("%,d", endPopulationCountry);
        endPopulationCountryStr = endPopulationCountryStr.replace(',', '.');

        String countryAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), input.getRegionName(), 1));
        ArrayList<SubTaskA> something = jdbc.SubTaskATask3(startIntArray, input.getTimePeriod(), input.getRegionName(), 1);

        model.addAttribute("regionName", input.getRegionName());
        model.addAttribute("avgTemp",  countryAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationCountryStr);
        model.addAttribute("startYearTemp", jdbc.getCountriesTempByYear(input.getRegionName(), input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationCountryStr);
        model.addAttribute("endYearTemp", jdbc.getCountriesTempByYear(input.getRegionName(), input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something.isEmpty()) {
          model.addAttribute("diffInAve", something);
        } else {
          model.addAttribute("diffInAve", emptyArrayListA);
        }
      }
      else if (input.getSelectedOption().equalsIgnoreCase("state")) {

        String stateAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), input.getRegionName(), 2));
        ArrayList<SubTaskA> something = jdbc.SubTaskATask3(startIntArray, input.getTimePeriod(), input.getRegionName(), 2);

        model.addAttribute("regionName", input.getRegionName());
        model.addAttribute("avgTemp",  stateAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearTemp", jdbc.getStateTempByYear(input.getRegionName(), input.getStartingYear()));
        model.addAttribute("endYearTemp", jdbc.getStateTempByYear(input.getRegionName(), input.getEndingYear()));
        model.addAttribute("startYearPopu", "N/A");
        model.addAttribute("endYearPopu", "N/A");
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something.isEmpty()) {
          model.addAttribute("diffInAve", something);
        } else {
          model.addAttribute("diffInAve", emptyArrayListA);
        }
      } else {

        String cityAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), input.getRegionName(), 3));
        ArrayList<SubTaskA> something = jdbc.SubTaskATask3(startIntArray, input.getTimePeriod(), input.getRegionName(), 3);

        model.addAttribute("regionName", input.getRegionName());
        model.addAttribute("avgTemp",  cityAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearTemp", jdbc.getCityTempByYear(input.getRegionName(), input.getStartingYear()));
        model.addAttribute("endYearTemp", jdbc.getCityTempByYear(input.getRegionName(), input.getEndingYear()));
        model.addAttribute("startYearPopu", "N/A");
        model.addAttribute("endYearPopu", "N/A");
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something.isEmpty()) {
          model.addAttribute("diffInAve", something);
        } else {
          model.addAttribute("diffInAve", emptyArrayListA);
        }
      }

      model.addAttribute("filterPopuSortPopuDesc", emptyArrayListA);
      model.addAttribute("filterPopuSortPopuAsc", emptyArrayListA);
      model.addAttribute("filterPopuSortTempDesc", emptyArrayListA);
      model.addAttribute("filterPopuSortTempAsc", emptyArrayListA);
      model.addAttribute("filterTempSortPopuDesc", emptyArrayListA);
      model.addAttribute("filterTempSortPopuAsc", emptyArrayListA);
      model.addAttribute("filterTempSortTempDesc", emptyArrayListA);
      model.addAttribute("filterTempSortTempAsc", emptyArrayListA);
      model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
      model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
      model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
      model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
      model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
      model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
      model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
      model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
      model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
      model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
      model.addAttribute("diffInAveDesc", emptyArrayListA);
      model.addAttribute("diffInAveAsc", emptyArrayListA);
    }

    else if (input.getRegionName().contains("/") && input.getStartingYearStr().contains("/")) {
      if (input.getSelectedOption().equalsIgnoreCase("global")) {
        long startPopulationWorld = jdbc.getWorldPopuByYear(input.getStartingYear());
        String startPopulationWorldStr = String.format( "%,d", startPopulationWorld);
        startPopulationWorldStr = startPopulationWorldStr.replace(',', '.');

        long endPopulationWorld = jdbc.getWorldPopuByYear(input.getEndingYear());
        String endPopulationWorldStr = String.format("%,d", endPopulationWorld);
        endPopulationWorldStr = endPopulationWorldStr.replace(',', '.');

        String worldAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), "World", 0));
        ArrayList<SubTaskA> something = jdbc.SubTaskATask3(startIntArray, input.getTimePeriod(), "World", 0);

        model.addAttribute("regionName", "World");
        model.addAttribute("avgTemp", worldAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationWorldStr);
        model.addAttribute("startYearTemp", jdbc.getWorldTempByYear(input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationWorldStr);
        model.addAttribute("endYearTemp", jdbc.getWorldTempByYear(input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something.isEmpty()) {
          model.addAttribute("diffInAve", something);
        } else {
          model.addAttribute("diffInAve", emptyArrayListA);
        }

        model.addAttribute("filterPopuSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempAsc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterTempSortTempDesc", emptyArrayListA);
        model.addAttribute("filterTempSortTempAsc", emptyArrayListA);
        model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
        model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
        model.addAttribute("diffInAveDesc", emptyArrayListA);
        model.addAttribute("diffInAveAsc", emptyArrayListA);
      } else if (input.getSelectedOption().equalsIgnoreCase("country") && input.getFilterOption().equalsIgnoreCase("nofilter")) {
        long startPopulationCountry = jdbc.getCountriesPopuByYear(regionNames[0], input.getStartingYear());
        String startPopulationCountryStr = String.format( "%,d", startPopulationCountry);
        startPopulationCountryStr = startPopulationCountryStr.replace(',', '.');

        long endPopulationCountry = jdbc.getCountriesPopuByYear(regionNames[0], input.getEndingYear());
        String endPopulationCountryStr = String.format("%,d", endPopulationCountry);
        endPopulationCountryStr = endPopulationCountryStr.replace(',', '.');

        String countryAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1));
        ArrayList<SubTaskA> something1 = jdbc.SubTaskATask4NoFilter(startIntArray, input.getTimePeriod(), regionNames, 1, true);
        ArrayList<SubTaskA> something2 = jdbc.SubTaskATask4NoFilter(startIntArray, input.getTimePeriod(), regionNames, 1, false);

        model.addAttribute("regionName", regionNames[0]);
        model.addAttribute("avgTemp", countryAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationCountryStr);
        model.addAttribute("startYearTemp", jdbc.getCountriesTempByYear(regionNames[0], input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationCountryStr);
        model.addAttribute("endYearTemp", jdbc.getCountriesTempByYear(regionNames[0], input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something1.isEmpty()) {
          model.addAttribute("diffInAveDesc", something1);
        } else {
          model.addAttribute("diffInAveDesc", emptyArrayListA);
        }
        if(!something2.isEmpty()) {
          model.addAttribute("diffInAveAsc", something2);
        } else {
          model.addAttribute("diffInAveAsc", emptyArrayListA);
        }

        model.addAttribute("filterPopuSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempAsc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterTempSortTempDesc", emptyArrayListA);
        model.addAttribute("filterTempSortTempAsc", emptyArrayListA);
        // model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
        model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
        model.addAttribute("diffInAve", emptyArrayListA);
      } else if (input.getSelectedOption().equalsIgnoreCase("country") && input.getFilterOption().equalsIgnoreCase("filter") && input.getFilterSortOption().equalsIgnoreCase("popu")) {
        long startPopulationCountry = jdbc.getCountriesPopuByYear(regionNames[0], input.getStartingYear());
        String startPopulationCountryStr = String.format( "%,d", startPopulationCountry);
        startPopulationCountryStr = startPopulationCountryStr.replace(',', '.');

        long endPopulationCountry = jdbc.getCountriesPopuByYear(regionNames[0], input.getEndingYear());
        String endPopulationCountryStr = String.format("%,d", endPopulationCountry);
        endPopulationCountryStr = endPopulationCountryStr.replace(',', '.');

        String countryAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1));
        ArrayList<SubTaskA> something1 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 2, true, true);
        ArrayList<SubTaskA> something2 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 2, true, false);
        ArrayList<SubTaskA> something3 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 2, false, true);
        ArrayList<SubTaskA> something4 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 2, false, false);

        model.addAttribute("regionName", regionNames[0]);
        model.addAttribute("avgTemp", countryAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationCountryStr);
        model.addAttribute("startYearTemp", jdbc.getCountriesTempByYear(regionNames[0], input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationCountryStr);
        model.addAttribute("endYearTemp", jdbc.getCountriesTempByYear(regionNames[0], input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something1.isEmpty()) {
          model.addAttribute("filterPopuSortPopuDesc", something1);
        } else {
          model.addAttribute("filterPopuSortPopuDesc", emptyArrayListA);
        }
        if(!something2.isEmpty()) {
          model.addAttribute("filterPopuSortPopuAsc", something2);
        } else {
          model.addAttribute("filterPopuSortPopuAsc", emptyArrayListA);
        }
        if(!something3.isEmpty()) {
          model.addAttribute("filterPopuSortTempDesc", something3);
        } else {
          model.addAttribute("filterPopuSortTempDesc", emptyArrayListA);
        }
        if(!something4.isEmpty()) {
          model.addAttribute("filterPopuSortTempAsc", something4);
        } else {
          model.addAttribute("filterPopuSortTempAsc", emptyArrayListA);
        }
        
        model.addAttribute("diffInAveDesc", emptyArrayListA);
        model.addAttribute("diffInAveAsc", emptyArrayListA);
        // model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
        model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
        model.addAttribute("diffInAve", emptyArrayListA);
      } else if (input.getSelectedOption().equalsIgnoreCase("country") && input.getFilterOption().equalsIgnoreCase("filter") && input.getFilterSortOption().equalsIgnoreCase("temp")) {
        long startPopulationCountry = jdbc.getCountriesPopuByYear(regionNames[0], input.getStartingYear());
        String startPopulationCountryStr = String.format( "%,d", startPopulationCountry);
        startPopulationCountryStr = startPopulationCountryStr.replace(',', '.');

        long endPopulationCountry = jdbc.getCountriesPopuByYear(regionNames[0], input.getEndingYear());
        String endPopulationCountryStr = String.format("%,d", endPopulationCountry);
        endPopulationCountryStr = endPopulationCountryStr.replace(',', '.');

        String countryAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1));
        ArrayList<SubTaskA> something1 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 1, true, true);
        ArrayList<SubTaskA> something2 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 1, true, false);
        ArrayList<SubTaskA> something3 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 1, false, true);
        ArrayList<SubTaskA> something4 = jdbc.SubTaskATask4WithFilter(startIntArray, input.getTimePeriod(), regionNames, 1, input.getFromValue(), input.getToValue(), 1, false, false);

        model.addAttribute("regionName", regionNames[0]);
        model.addAttribute("avgTemp", countryAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearPopu", startPopulationCountryStr);
        model.addAttribute("startYearTemp", jdbc.getCountriesTempByYear(regionNames[0], input.getStartingYear()));
        model.addAttribute("endYearPopu", endPopulationCountryStr);
        model.addAttribute("endYearTemp", jdbc.getCountriesTempByYear(regionNames[0], input.getEndingYear()));
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something1.isEmpty()) {
          model.addAttribute("filterTempSortPopuDesc", something1);
        } else {
          model.addAttribute("filterTempSortPopuDesc", emptyArrayListA);
        }
        if(!something2.isEmpty()) {
          model.addAttribute("filterTempSortPopuAsc", something2);
        } else {
          model.addAttribute("filterTempSortPopuAsc", emptyArrayListA);
        }
        if(!something3.isEmpty()) {
          model.addAttribute("filterTempSortTempDesc", something3);
        } else {
          model.addAttribute("filterTempSortTempDesc", emptyArrayListA);
        }
        if(!something4.isEmpty()) {
          model.addAttribute("filterTempSortTempAsc", something4);
        } else {
          model.addAttribute("filterTempSortTempAsc", emptyArrayListA);
        }
        
        model.addAttribute("diffInAveDesc", emptyArrayListA);
        model.addAttribute("diffInAveAsc", emptyArrayListA);
        // model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
        // model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
        model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
        model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
        model.addAttribute("diffInAve", emptyArrayListA);
      }
      else if (input.getSelectedOption().equalsIgnoreCase("state")) {

        String stateAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 2));
        ArrayList<SubTaskA> something1 = jdbc.SubTaskATask4NoFilter(startIntArray, input.getTimePeriod(), regionNames, 2, true);
        ArrayList<SubTaskA> something2 = jdbc.SubTaskATask4NoFilter(startIntArray, input.getTimePeriod(), regionNames, 2, false);

        model.addAttribute("regionName", regionNames[0]);
        model.addAttribute("avgTemp", stateAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearTemp", jdbc.getStateTempByYear(regionNames[0], input.getStartingYear()));
        model.addAttribute("endYearTemp", jdbc.getStateTempByYear(regionNames[0], input.getEndingYear()));
        model.addAttribute("startYearPopu", "N/A");
        model.addAttribute("endYearPopu", "N/A");
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something1.isEmpty()) {
          model.addAttribute("diffInAveDesc", something1);
        } else {
          model.addAttribute("diffInAveDesc", emptyArrayListA);
        }
        if(!something2.isEmpty()) {
          model.addAttribute("diffInAveAsc", something2);
        } else {
          model.addAttribute("diffInAveAsc", emptyArrayListA);
        }

        model.addAttribute("filterPopuSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempAsc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterTempSortTempDesc", emptyArrayListA);
        model.addAttribute("filterTempSortTempAsc", emptyArrayListA);
        model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
        // model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
        // model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
        // model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
        // model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
        model.addAttribute("diffInAve", emptyArrayListA);
      } else {

        String cityAvgTemp = String.format("%.3f", jdbc.SubTaskATask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 3));
        ArrayList<SubTaskA> something1 = jdbc.SubTaskATask4NoFilter(startIntArray, input.getTimePeriod(), regionNames, 3, true);
        ArrayList<SubTaskA> something2 = jdbc.SubTaskATask4NoFilter(startIntArray, input.getTimePeriod(), regionNames, 3, false);

        model.addAttribute("regionName", regionNames[0]);
        model.addAttribute("avgTemp", cityAvgTemp);
        model.addAttribute("startYear", input.getStartingYear());
        model.addAttribute("endYear", input.getEndingYear());
        model.addAttribute("startYearTemp", jdbc.getCityTempByYear(regionNames[0], input.getStartingYear()));
        model.addAttribute("endYearTemp", jdbc.getCityTempByYear(regionNames[0], input.getEndingYear()));
        model.addAttribute("startYearPopu", "N/A");
        model.addAttribute("endYearPopu", "N/A");
        model.addAttribute("array1", startIntArray);
        model.addAttribute("timePeriod", input.getTimePeriod());
        model.addAttribute("array2", endIntArray);
        if(!something1.isEmpty()) {
          model.addAttribute("diffInAveDesc", something1);
        } else {
          model.addAttribute("diffInAveDesc", emptyArrayListA);
        }
        if(!something2.isEmpty()) {
          model.addAttribute("diffInAveAsc", something2);
        } else {
          model.addAttribute("diffInAveAsc", emptyArrayListA);
        }

        model.addAttribute("filterPopuSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempDesc", emptyArrayListA);
        model.addAttribute("filterPopuSortTempAsc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuDesc", emptyArrayListA);
        model.addAttribute("filterTempSortPopuAsc", emptyArrayListA);
        model.addAttribute("filterTempSortTempDesc", emptyArrayListA);
        model.addAttribute("filterTempSortTempAsc", emptyArrayListA);
        model.addAttribute("CountryAbsoluteMostSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryAbsoluteMostSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryAbsoluteMostSimBoth", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryAbsoluteLeastSimBoth", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryRelativeMostSimBoth", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimOnlyPopu", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimOnlyTemp", emptyArrayListB);
        model.addAttribute("CountryRelativeLeastSimBoth", emptyArrayListB);
        // model.addAttribute("CiStAbsoluteMostSimTemp", emptyArrayListB);
        // model.addAttribute("CiStAbsoluteLeastSimTemp", emptyArrayListB);
        // model.addAttribute("CiStRelativeMostSimTemp", emptyArrayListB);
        // model.addAttribute("CiStRelativeLeastSimTemp", emptyArrayListB);
        model.addAttribute("diffInAve", emptyArrayListA);
      }
    }

    if (input.isEnableSim() == true) {
      if (input.getSelectedOption().equalsIgnoreCase("country")) {
        ArrayList<SubTaskB> something1 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 2, 1, true, true);
        if (something1.size() > 10) {
          something1 = new ArrayList<>(something1.subList(0, 10));
        }
        ArrayList<SubTaskB> something2 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 1, 1, true, true);
        if (something2.size() > 10) {
          something2 = new ArrayList<>(something2.subList(0, 10));
        }
        ArrayList<SubTaskB> something3 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 3, 1, true, true);
        if (something3.size() > 10) {
          something3 = new ArrayList<>(something3.subList(0, 10));
        }
        ArrayList<SubTaskB> something4 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 2, 1, false, true);
        if (something4.size() > 10) {
          something4 = new ArrayList<>(something4.subList(0, 10));
        }
        ArrayList<SubTaskB> something5 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 1, 1, false, true);
        if (something5.size() > 10) {
          something5 = new ArrayList<>(something5.subList(0, 10));
        }
        ArrayList<SubTaskB> something6 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 3, 1, false, true);
        if (something6.size() > 10) {
          something6 = new ArrayList<>(something6.subList(0, 10));
        }
        ArrayList<SubTaskB> something7 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 2, 2, true, true);
        if (something7.size() > 10) {
          something7 = new ArrayList<>(something7.subList(0, 10));
        }
        ArrayList<SubTaskB> something8 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 1, 2, true, true);
        if (something8.size() > 10) {
          something8 = new ArrayList<>(something8.subList(0, 10));
        }
        ArrayList<SubTaskB> something9 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 3, 2, true, true);
        if (something9.size() > 10) {
          something9 = new ArrayList<>(something9.subList(0, 10));
        }
        ArrayList<SubTaskB> something10 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 2, 2, false, true);
        if (something10.size() > 10) {
          something10 = new ArrayList<>(something10.subList(0, 10));
        }
        ArrayList<SubTaskB> something11 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 1, 2, false, true);
        if (something11.size() > 10) {
          something11 = new ArrayList<>(something11.subList(0, 10));
        }
        ArrayList<SubTaskB> something12 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 1, 3, 2, false, true);
        if (something12.size() > 10) {
          something12 = new ArrayList<>(something12.subList(0, 10));
        }

        model.addAttribute("CountryAbsoluteMostSimOnlyPopu", something1);
        model.addAttribute("CountryAbsoluteMostSimOnlyTemp", something2);
        model.addAttribute("CountryAbsoluteMostSimBoth", something3);
        model.addAttribute("CountryAbsoluteLeastSimOnlyPopu", something4);
        model.addAttribute("CountryAbsoluteLeastSimOnlyTemp", something5);
        model.addAttribute("CountryAbsoluteLeastSimBoth", something6);
        model.addAttribute("CountryRelativeMostSimOnlyPopu", something7);
        model.addAttribute("CountryRelativeMostSimOnlyTemp", something8);
        model.addAttribute("CountryRelativeMostSimBoth", something9);
        model.addAttribute("CountryRelativeLeastSimOnlyPopu", something10);
        model.addAttribute("CountryRelativeLeastSimOnlyTemp", something11);
        model.addAttribute("CountryRelativeLeastSimBoth", something12);
      } else if (input.getSelectedOption().equalsIgnoreCase("state")) {
        ArrayList<SubTaskB> something1 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 2, 1, 1, true, true);
        ArrayList<SubTaskB> something2 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 2, 1, 1, false, true);
        ArrayList<SubTaskB> something3 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 2, 1, 2, true, true);
        ArrayList<SubTaskB> something4 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 2, 1, 2, false, true);

        if (something1.size() > 10) {
          something1 = new ArrayList<>(something1.subList(0, 10));
        }
        if (something2.size() > 10) {
          something2 = new ArrayList<>(something2.subList(0, 10));
        }
        if (something3.size() > 10) {
          something3 = new ArrayList<>(something3.subList(0, 10));
        }
        if (something4.size() > 10) {
          something4 = new ArrayList<>(something4.subList(0, 10));
        }
        model.addAttribute("CiStAbsoluteMostSimTemp", something1);
        model.addAttribute("CiStAbsoluteLeastSimTemp", something2);
        model.addAttribute("CiStRelativeMostSimTemp", something3);
        model.addAttribute("CiStRelativeLeastSimTemp", something4);
      } else if (input.getSelectedOption().equalsIgnoreCase("city")) {
        ArrayList<SubTaskB> something1 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 3, 1, 1, true, true);
        ArrayList<SubTaskB> something2 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 3, 1, 1, false, true);
        ArrayList<SubTaskB> something3 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 3, 1, 2, true, true);
        ArrayList<SubTaskB> something4 = jdbc.subTaskBTask2(input.getStartingYear(), input.getTimePeriod(), regionNames[0], 3, 1, 2, false, true);

        if (something1.size() > 10) {
          something1 = new ArrayList<>(something1.subList(0, 10));
        }
        if (something2.size() > 10) {
          something2 = new ArrayList<>(something2.subList(0, 10));
        }
        if (something3.size() > 10) {
          something3 = new ArrayList<>(something3.subList(0, 10));
        }
        if (something4.size() > 10) {
          something4 = new ArrayList<>(something4.subList(0, 10));
        }
        model.addAttribute("CiStAbsoluteMostSimTemp", something1);
        model.addAttribute("CiStAbsoluteLeastSimTemp", something2);
        model.addAttribute("CiStRelativeMostSimTemp", something3);
        model.addAttribute("CiStRelativeLeastSimTemp", something4);
      }
    }

    return "Level3/advance-data-overview";
  }

  // @PostMapping("/AdvanceWorld")
  // public String showResult4(@ModelAttribute SubTaskA input, Model model) {
  //   JDBCConnection jdbc = new JDBCConnection();
  //   model.addAttribute("input", input);
  //   input.setEndingYear(input.getStartingYear() + input.getTimePeriod());

  //   return "Level3/advance-data-overview";
  // }

  // @PostMapping("/Advance1")
  // public String showResult1(@ModelAttribute SubTaskA input, Model model) {
  //   return "Level3/advance-data-overview";
  // }

  // @PostMapping("/Advance2")
  // public String showResult2(@ModelAttribute SubTaskB input, Model model) {
  //   return "Level3/advance-data-overview";
  // }

  @GetMapping("/error")
  public String showError() {
    return "Error";
  }
  
  @GetMapping("/Persona")
  public String showPersona(Model model, HttpSession session) {
    //Add username from their session id
    return "redirect:/about";
  }
  
  @GetMapping("/about")
  public String showAbout(Model model, HttpSession session) {
    //Add username from their session id
    model.addAttribute("username", UserMaintenance.getUsername(session.getId()));

    try {
    JDBCConnection jdbc = new JDBCConnection();
    String[][] Persona = jdbc.getPersona();
    
    String[] PersonaStr = {"Persona1","Persona2","Persona3","Persona4"};
    Integer i = 0;
    
      for (String p : PersonaStr) {
        model.addAttribute(p+"", Persona[i][0]);
        model.addAttribute(p+"Background", Persona[i][1].split("<br>"));
        model.addAttribute(p+"Goal", Persona[i][2].split("<br>"));
        model.addAttribute(p+"IdealExperince", Persona[i][3].split("<br>"));
        model.addAttribute(p+"SkillExperince", Persona[i][4].split("<br>"));
        i++;
      }
    } catch (Exception e) {
      System.out.println("Error in Persona"+ e.getMessage());

    }
    
    return "Other/Persona";
  }

}
