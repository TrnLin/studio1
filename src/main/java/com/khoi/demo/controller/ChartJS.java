package com.khoi.demo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChartJS {

    // THIS CLASS WILL HANDLE POST REQUEST FROM WEBBROWSER 
    // THEN TRANFORM IT INTO JSON FROMAT USE IT FOR CHARTJS
    private String DATABASE= "jdbc:sqlite:database/TempPopulation.db";
    private String csvContent = "";
    private int download_startYear=0;
    private int download_endYear=0;
    private String[] download_regions;
    private String download_type=null;
    private String csv_header="";


    //THIS METHOD IS FOR WORLD DATA FOR OVERVIEW SECTION POPULATION CHART
    @PostMapping("/chartData/WorldOverview/Population")
    @ResponseBody
    public String worldOverviewPop(@RequestParam String startYear, @RequestParam String endYear){
        return getData("SELECT Year,Population FROM POPULATION WHERE (COUNTRY_NAME=\"World\") AND(YEAR BETWEEN "+startYear+" AND "+endYear+")");
    }

    //THIS METHOD IS FOR WORLD DATA FOR OVERVIEW SECTION TEMPERATURE CHART
    @PostMapping("/chartData/WorldOverview/Temperature")
    @ResponseBody
    public String worldOverviewTemp(@RequestParam String startYear, @RequestParam String endYear){
        return getData("SELECT Year,AverageTemperature as Temp,DataInterpolated as TempDataInterpolated FROM GlobalYearlyTemp WHERE (year BETWEEN " + startYear + " AND " + endYear+")");
    }


    //THIS METHOD IS FOR COUNTRY DATA FOR OVERVIEW SECTION POPULATION CHART
    @PostMapping("/chartData/CountryOverview/Population")
    @ResponseBody
    public String countryOverviewPop(@RequestParam String startYear, @RequestParam String endYear,
        @RequestParam String country){
        return getData("SELECT Year,Population FROM POPULATION WHERE (COUNTRY_NAME=\"" + country + "\") AND(YEAR BETWEEN \""+startYear+"\" AND \""+endYear+"\")");
    }

    //THIS METHOD IS FOR COUNTRY DATA FOR OVERVIEW SECTION TEMPERATURE CHART
    @PostMapping("/chartData/CountryOverview/Temperature")
    @ResponseBody
    public String countryOverviewTemp(@RequestParam String startYear, @RequestParam String endYear,
        @RequestParam String country){
        return getData("SELECT Year,AverageTemperature as Temp,DataInterpolated as TempDataInterpolated FROM GlobalYearlyLandTempByCountry WHERE (Country=\""+country+"\") AND (year BETWEEN \"" + startYear + "\" AND \"" + endYear+"\")");
    }
    
    @PostMapping("/chartData/WorldAdvance")
    @ResponseBody
    public String lvl3PostHandler(@RequestParam String region, @RequestParam String startYear, String type,
        @RequestParam String period, @RequestParam String fromRange, @RequestParam String toRange){
        System.out.println(type);

        // THIS METHOD WILL HANDLE POST REQUEST FROM WEBBROWSER
        try{
            String[] regions = region.replace("/",",").split(",");
            String[] years = startYear.replace("/",",").split(",");
            int startY = Integer.parseInt(years[0]);
            int endY = startY;
            if (years.length>1){
                endY = Integer.parseInt(years[1]);
                for(int i =0; i < years.length; i++){
                    if(Integer.parseInt(years[i])<startY)
                        startY = Integer.parseInt(years[i]);

                    if(Integer.parseInt(years[i])>endY)
                        endY = Integer.parseInt(years[i]);
                }
            } 
            return lvl3Handler(regions, startY, endY+Integer.parseInt(period),type);
        } catch (Exception e) {
            System.out.println("Level 3 ChartJS.java: "+e.getMessage());
        }
        return "{\"Error\":[0]}";
    }

    private String lvl3Handler(String[] regions, int  startYear, int endYear, String type){
        String data="";
        this.download_startYear = startYear;
        this.download_endYear = endYear;
        this.download_regions = regions;
        this.download_type = type;
        switch (type) {
            case "global":
                    data = getData("SELECT Year, AverageTemperature from GlobalYearlyTemp WHERE Year BETWEEN "+startYear+" AND "+endYear);
                    data=data.replace("AverageTemperature", "Global Temperature");
                break;
            case "country":
                for (String country : regions) {
                    String tmp="";
                    tmp = getData("SELECT Year, AverageTemperature from GlobalYearlyLandTempByCountry WHERE Year BETWEEN "+startYear+" AND "+endYear+" AND Country=\""+country+"\"");
                    if (data=="")
                        data+= tmp.substring(tmp.indexOf("{")+1, tmp.indexOf("}"))+",";
                    else{
                        data+= "\""+tmp.substring(tmp.indexOf("AverageTemperature"), tmp.indexOf("}"))+",";
                    }
                    data=data.replace("AverageTemperature", country);
                    
                    }
                data = "{"+data.substring(0, data.length()-1)+"}";
                break;
            case "city":
                for (String city : regions) {
                    String tmp="";
                    tmp = getData("SELECT Year, AverageTemperature from GlobalYearlyLandTempByCity WHERE Year BETWEEN "+startYear+" AND "+endYear+" AND city=\""+city+"\"");
                    if (data=="")
                        data+= tmp.substring(tmp.indexOf("{")+1, tmp.indexOf("}"))+",";
                    else{
                        data+= "\""+tmp.substring(tmp.indexOf("AverageTemperature"), tmp.indexOf("}"))+",";
                    }
                    data=data.replace("AverageTemperature", city);
                    
                    }
                data = "{"+data.substring(0, data.length()-1)+"}";
                break;
            case "state":
                for (String state : regions) {
                    String tmp="";
                    tmp = getData("SELECT Year, AverageTemperature from GlobalYearlyLandTempByState WHERE Year BETWEEN "+startYear+" AND "+endYear+" AND state=\""+state+"\"");
                    if (data=="")
                        data+= tmp.substring(tmp.indexOf("{")+1, tmp.indexOf("}"))+",";
                    else{
                        data+= "\""+tmp.substring(tmp.indexOf("AverageTemperature"), tmp.indexOf("}"))+",";
                    }
                    data=data.replace("AverageTemperature", state);
                    
                    }
                data = "{"+data.substring(0, data.length()-1)+"}";
                break;
            default:
                break;
        }
        
        
        
        // System.out.println(data);
        return data;
    }

    //THIS METHOD CONVERT QUERY RESULTSET INTO JSON FORMAT
    public String getData(String query){
        //START WITH INITIALIZING VARIABLES DEFAULT VALUE OF NULL
        String text = null;
        Connection conn = null;
        Statement stmnt =null;
        ResultSet rs = null;

        System.out.println(query);
        try {
            //CREATE CONNECTION TO DATABASE
            conn = DriverManager.getConnection(DATABASE);

            //CREATE STATEMENT AND STATEMENT TIMEOUT SET IT TO 60 SECONDS
            stmnt = conn.createStatement();
            stmnt.setQueryTimeout(60);

            try{
                //EXECUTE QUERY AND GET RESULTSET
                rs = stmnt.executeQuery(query);

                //COLLOUMNS WILL BE STORED IN ARRAY IN FORM OF TEXT
                String[] arr = new String[rs.getMetaData().getColumnCount()];
                while (rs.next()){
                    for (int i = 0; i < arr.length; i++) {
                        String tmp = rs.getString(i+1);

                        //THIS WILL CONVERT BOOLEAN VALUE INTO 1 AND 0
                        if ("TRUE".equals(tmp.toUpperCase()))
                            tmp = "1";
                        else if ("FALSE".equals(tmp.toUpperCase()))
                            tmp = "0";

                        //THIS WRITE DATA INTO ARRAY
                        if (arr[i]==null)
                            arr[i]= tmp;
                        else
                            arr[i]+=","+tmp;
                    }
                }

                //THIS WILL CONVERT ARRAY INTO JSON FORMAT WITH COLUMN NAME
                for (int i = 0; i < arr.length; i++) {
                    if (text==null)
                        text= "\""+rs.getMetaData().getColumnName(i+1)+"\":["+arr[i]+"]";
                    else
                        text+=",\""+rs.getMetaData().getColumnName(i+1)+"\":["+arr[i]+"]";
                }

                text = "{"+text+"}";
            } catch (Exception resultException){
                //IF QUERY FAILED THIS WILL NOTIFY THE ERROR
                System.out.println(query+"\nFailed to get query: "+ resultException.getMessage());
            }
            
            //CLOSE CONNECTION
            if (rs!=null) rs.close();
            if (stmnt!=null) stmnt.close();
            if (rs!=null) rs.close();
        } catch (Exception e) {
            //IF CONNECTION FAILED THIS WILL NOTIFY THE ERROR
            System.out.println("Get data failed with reason: "+ e.getMessage());
        }
        return text;
    }

    //THIS METHOD GET QUERY AND RETURN CSV FORMAT
    private String getCsvContent(String query){
        Connection conn = null;
        Statement stmnt =null;
        ResultSet rs = null;
        String csv_body ="";
        String newline="";

        try {
            conn = DriverManager.getConnection(DATABASE);
            stmnt = conn.createStatement();
            stmnt.setQueryTimeout(60);

            rs = stmnt.executeQuery(query);

            while (rs.next()){
                this.csv_header="";
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    csv_header+=rs.getMetaData().getColumnName(i)+",";
                    if (newline=="")
                        newline = rs.getString(i);
                    else
                        newline += ","+rs.getString(i);
                }

                if (newline.length()>0){
                    csv_header = csv_header.substring(0, csv_header.length()-1);
                    csv_body+=newline+"\n";
                }
                
                newline="";
            }
            return csv_body;
        } catch (Exception e) {
            System.out.println("csvContent failed with reason: "+ e.getMessage());
        }
        return "";
    }

    //THIS HANDLING LEVEL 3 DOWNLOAD CSV FEATURE
    @GetMapping("/Advance/Download")
    public ResponseEntity<ByteArrayResource> downloadCSV(HttpSession session){
        csvContent="";
        try{
            switch (download_type) {
                case "global":
                    csvContent = getCsvContent("SELECT * from GlobalYearlyTemp WHERE Year BETWEEN "+download_startYear+" AND "+download_endYear);
                    break;
                case "country":
                    for (String region:download_regions){
                        csvContent += getCsvContent("SELECT * from GlobalYearlyLandTempByCountry WHERE Year BETWEEN "+download_startYear+" AND "+download_endYear+" AND Country=\""+region+"\"");
                    }
                    break;

                case "state":
                    for (String region:download_regions){
                        csvContent += getCsvContent("SELECT * from GlobalYearlyLandTempByState WHERE Year BETWEEN "+download_startYear+" AND "+download_endYear+" AND State=\""+region+"\"");
                    }
                    break;
                case "city":
                    for (String region:download_regions){
                        csvContent += getCsvContent("SELECT * from GlobalYearlyLandTempByCity WHERE Year BETWEEN "+download_startYear+" AND "+download_endYear+" AND City=\""+region+"\"");
                    }
                    break;
                default:
                    break;
                }
            
            if (csvContent != "" && download_startYear!=0 && download_endYear!=0 && download_type!=null && !UserMaintenance.getUsername(session.getId()).equals("Guest")){
                csvContent = csv_header+"\n"+csvContent;
                ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes());
                csv_header="";

                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=Content_Temperature_"+this.download_type+"_Data.csv")
                        .contentLength(csvContent.getBytes().length)
                        .body(resource);
            }
        } catch (Exception e){
            System.out.println("CSV feature failed with reason: "+e.getMessage()); 
        }
        return ResponseEntity.badRequest().build();
    }
    
}