package com.khoi.demo.JDBCConnection;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

// Java SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

@Controller

public class WebBaseSQL {

    private String csvContent="";

    @GetMapping("/sql")
    public String jdbc(){
        return "sql";
    }

    @PostMapping("/sql")
    // @ResponseBody
    public String jdbcPost(@RequestParam(name="query") String query, Model model){
        String DATABASE = "jdbc:sqlite:database/TempPopulation.db";
        // String DATABASE = "jdbc:sqlite:database/User.db";

        System.out.println("Input query is: " + query);
        Connection conn = null;

        try {
            String th="";
            String td="";
            String table="";
            conn =  DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            try{
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()){
                    th="";
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                        th+="<th>"+rs.getMetaData().getColumnName(i)+"</th>";
                        td+="<td>"+rs.getString(i)+"</td>";
                        csvContent+=rs.getString(i)+",";
                    }
                    csvContent=csvContent.substring(0,csvContent.length()-1)+"\n";
                    table+="<tr>"+td+"</tr>";
                    
                    td="";
                }
                
                csvContent= th.replace("<th>", "").replace("</th>", ",")+"\n"+csvContent;

                if (table.equals("")){
                    table="<tr><td>No results found</td></tr>";
                    csvContent="";
                }
                else{
                    table="<table><tr>"+th+"</tr>"+table+"</table>";
                }
    
                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("Using DML query");
                stmt.executeUpdate(query);
                return "sql";
            }
            
            model.addAttribute("queryResult", table);
        } catch (Exception e) {
            System.out.println("Result: Failed -"+e.getMessage());
            model.addAttribute("queryResult", query + " get error: " + e.getMessage());
        }

        model.addAttribute("queryValue", query);
        return "sql";
    }

    // @GetMapping("/sql/get_csv")
    // public ResponseEntity<ByteArrayResource> downloadCSV(){
    //     ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes());
    //     Random rand = new Random();
    //     int rand_int = rand.nextInt(9000)-rand.nextInt(1000);
    //     return ResponseEntity.ok()
    //             .header("Content-Disposition", "attachment; filename=Content_"+rand_int+".csv")
    //             .contentLength(csvContent.getBytes().length)
    //             .body(resource);
    // }

    @GetMapping("/sql/get_csv")
    public ResponseEntity<ByteArrayResource> downloadCSV(){
        if (csvContent != ""){
            try{
                ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes());
                Random rand = new Random();
                int rand_int = rand.nextInt(9000)-rand.nextInt(1000);
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=Content_"+rand_int+".csv")
                        .contentLength(csvContent.getBytes().length)
                        .body(resource);
            } catch (Exception e){
                System.out.println("Download CSV failed with reason: "+e.getMessage()); 
            }
        }
        
        return ResponseEntity.badRequest().build();
    }
}
