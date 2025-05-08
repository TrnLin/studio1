package com.khoi.demo.controller;
// Java SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Java Security
import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;

// Spring Boot
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
// import javax.servlet.http.HttpSession

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class UserMaintenance {
    private static String username;


    //TODO: database path may change 
    public static String DATABASE = "jdbc:sqlite:database/user.db";
    
    public UserMaintenance(){
        username = null;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model){
        String username = getUsername(session.getId());
        model.addAttribute("username", username);
        if (!username.toUpperCase().equals("GUEST")) {
            //IF USER ALREADY LOGGED IN LOGOUT THEN
            return "redirect:/LoginManage";
        }

        return "/Other/login";
    }


    @GetMapping("/register")
    public String registerPage(HttpSession session){
        String username = getUsername(session.getId());
        if (!username.toUpperCase().equals("GUEST")) {
            System.out.println("User already logged in. Redirected to home page");
            return "redirect:/";
        }

        return "/Other/sign-up";
    }

    @PostMapping("/login")
    @ResponseBody
    public String Singin(@RequestParam String username, @RequestParam String password, HttpSession session){
        System.out.println("Username : "+username);
        if(authentication(username, encrypString(password))){
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = DriverManager.getConnection(DATABASE);
                stmt = conn.createStatement();
                stmt.setQueryTimeout(30);
                stmt.executeUpdate("UPDATE USER SET SESSION_ID='"+session.getId()+"' WHERE USER_NAME='"+username+"'");
                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("Failed to update session id with reason: " + e.getMessage());
                
                return "FAILED";
            }

            return "PASS";
        }
        return "FAILED";
    }
   
    @PostMapping("/register")
    @ResponseBody
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        // get attribute from registerPage.html
        password = encrypString(password);
        System.out.println("Sign-up User: "+ username);
        if (username == null || password == null || username.length() == 0 || password.length() == 0) {
            System.out.println("Password and username cannot be empty");
            return "registerPage";
        } else {
            //CREATE SQL TABLE IF NOT EXISTS
            DML("CREATE TABLE IF NOT EXISTS " +
                "USER(USER_NAME VARCHAR(30), "+
                "PASSWD VARCHAR(100),"+
                "SESSION_ID VARCHAR (100) UNIQUE, "+ 
                "DOWNLOAD_PERMIT BOOL DEFAULT 'TRUE' NOT NULL,"+
                "PRIMARY KEY (USER_NAME))");

            Connection conn = null;
            try {
                conn = DriverManager.getConnection(DATABASE);
                Statement statement = conn.createStatement();
                statement.setQueryTimeout(30);
                statement.executeUpdate("INSERT INTO USER(USER_NAME,PASSWD,DOWNLOAD_PERMIT) VALUES ('"+username+"', '"+password+"', FALSE)");
                statement.close();
                conn.close();

                return "PASS";        
            } catch (Exception e) {
                System.out.println("Registration failed with reason: " + e.getMessage());  
                return "FAIL";      
            }

        }
    }

    @GetMapping("/logout")
    @ResponseBody
    public String logout(HttpSession session){
        System.out.println("Logout: "+session.getId());
        
        //This clear current session of logged in user
        if (!username.toUpperCase().equals("GUEST")){
            DML("UPDATE USER SET SESSION_ID=NULL WHERE SESSION_ID='"+session.getId()+"'");
            return "<script>alert('Log out success!!');\nwindow.location.href='/'</script>";
        } else 
            return "<script>window.location.href='/login'</script>";
    }

    //THIS FUNCTION IS TO UPDATE DML QUERY DATABASE
    private void DML(String query){
        Connection conn = null;
                try {
            System.out.println("DML update: "+ query);
            // Create a connection to the database
            conn = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(120);

            // The Query
            statement.executeUpdate(query);
            
            System.out.println("DML update: success");
            statement.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQL query "+query+" failed with reason: " + e.getMessage());
        } 
        
    }

    //THIS METHOD TO ENCRYPT STRING
    private String encrypString(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();

            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            System.out.println("String encrypted as: "+sb.toString());
            return sb.toString();
        } catch (Exception e ) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    //THIS METHOD TO AUTHENTICATE USER
    private boolean authentication(String user, String passwd){
        Connection conn = null;
        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);

            // The Query
            String query = "SELECT PASSWD FROM USER WHERE USER_NAME='"+user+"'";
            ResultSet results = stmt.executeQuery(query);

            // Comparing text
            Boolean isMatch = passwd.equals(results.getString("PASSWD"));
            if (isMatch){
                System.out.println("Authentication: success");
            } else {
                System.out.println("Authentication: failed");
                
            }

            stmt.close();
            conn.close();
            return isMatch;
        } catch (Exception e) {
            System.out.println("Authentication: failed - " + e.getMessage());
            return false;
        } 
        
    }

    //THIS METHOD TO GET USERNAME BASED ON SESSION ID
    public static String getUsername(String SESSION_ID){
        Connection conn = null;

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(DATABASE);
            Statement stmt = conn.createStatement();
            stmt.setQueryTimeout(30);
            //QUERY USERNAME BASED ON SESSION ID
            ResultSet rs = stmt.executeQuery("SELECT USER_NAME FROM USER WHERE SESSION_ID='"+SESSION_ID+"'");
            username = rs.getString("USER_NAME");
            if (username == null) username = "Guest";
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Failed to get username with reason: " + e.getMessage());
            username = "Guest";
        }
        return username;
    }

    @GetMapping("/LoginManage")
    public String LoginManageHandler(HttpSession session, Model model){

        if (getUsername(session.getId()).toUpperCase().equals("GUEST")) {
            //IF USER ALREADY LOGGED IN LOGOUT THEN
            return "redirect:/";
        }
        model.addAttribute("username", getUsername(session.getId()));
        model.addAttribute("email", getUsername(session.getId())+"@gmail.com");

        return "/Other/loginmanage";
  }
}
