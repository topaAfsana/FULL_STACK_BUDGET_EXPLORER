package com.topaGroup2.controller;


import com.topaGroup2.model.ResultHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
public class SqlController {

    @Autowired
    JdbcTemplate jdbc;

    public Connection createDBConnection() throws ClassNotFoundException, SQLException{
        String dbUrl = "jdbc:mysql://localhost:3306/TOPADB?serverTimezone=UTC";

        //Database Username
        String username = "root";

        //Database Password
        String password = "Tishan@2016";

        //Load mysql jdbc driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        //Create Connection to DB
        Connection con = DriverManager.getConnection(dbUrl, username, password);

        //Create Statement Object

        return con;

    }


//1.CREATE TABLE-DONE
    @RequestMapping(value = "/create_table", method = RequestMethod.POST)
    public String createTable(@RequestParam() String tableName) {
        jdbc.execute(" CREATE TABLE `TOPADB`.`" + tableName + "` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `expense` VARCHAR(45) NOT NULL,\n" +
                "  `amount` INT NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);\n");
        return "TABLE CREATED";
    }


//    //1.FIND TABLE-DONE
//    @RequestMapping(value = "/find_table", method = RequestMethod.GET)
//    public List<Tables> findTable(@RequestParam() String tableName) {
//        List<Tables> list= new ArrayList<>();;
//        try{
//            Connection con=createDBConnection();
//            Statement stmt = con.createStatement();
//            //Query to Execute
//            String query = "show tables like \""+tableName+"\"";
//
//            ResultSet rs = stmt.executeQuery(query);
//
//            // While Loop to iterate through all data and print results
//
//            while (rs.next()) {
//                Tables tables = new Tables();
//                tables.setTables(rs.getString(1));
//                list.add(tables);
//            }
//            con.close();
//        }
//        catch (ClassNotFoundException | SQLException sqlEx)
//        {System.out.println(sqlEx.getMessage());}
//
//        return  list;
//    }



    //1.FIND TABLE-DONE
    @RequestMapping(value = "/find_table", method = RequestMethod.GET)
    public String findTable(@RequestParam() String tableName) {
        List<String> list= new ArrayList<>();
        try{
            Connection con=createDBConnection();
            Statement stmt = con.createStatement();
            //Query to Execute
            String query = "show tables";

            ResultSet rs = stmt.executeQuery(query);

            // While Loop to iterate through all data and print results

            while (rs.next()) {
                String table = rs.getString(1);
                list.add(table);
            }
            con.close();
        }
        catch (ClassNotFoundException | SQLException sqlEx)
        {System.out.println(sqlEx.getMessage());}

        System.out.println(list);
        for(String mytab:list){
            if(mytab.equalsIgnoreCase(tableName)){
                System.out.println("TABLE EXIST");
                return "FOUND";

            } }

        return null;
    }





//    @RequestMapping(value="/add_record",method= RequestMethod.POST)
//    public String addRecord(@RequestParam() String tableName,String expense,int amount){
//        jdbc.execute("INSERT INTO TOPADB."+tableName+"(id,expense,amount)VALUES(id,"+expense+","+amount+");");
//        return "RECORD ADDED";
//
//    }

    //2.INSERT INTO TABLE-DONE-WITH PARAM
    @RequestMapping(value = "/add_record", method = RequestMethod.POST)
    public String addRecord(@RequestParam() String tableName, String expense, int amount) {
        jdbc.execute("INSERT INTO `TOPADB`.`" + tableName + "`(`id`,`expense`,`amount`)VALUES(id,\"" + expense + "\"," + amount + ");");
        return "RECORD ADDED";

    }

//    //2.INSERT INTO TABLE-DONE---//WORKS WITHOUT PARAM
//    @GetMapping(value = "/add_record")
//    public String addRecord() {
//        jdbc.execute("INSERT INTO `TOPADB`.`FEBRUARY`(`id`,`expense`,`amount`)VALUES(id,\"test\",500)");
//        return "RECORD ADDED";
//    }


    @RequestMapping("/getAllRecord")
    public void index() throws ClassNotFoundException, SQLException {

        //Connection URL Syntax: "jdbc:mysql://ipaddress:portnumber/db_name"
        String dbUrl = "jdbc:mysql://localhost:3306/TOPADB?serverTimezone=UTC";

        //Database Username
        String username = "root";

        //Database Password
        String password = "Tishan@2016";

        //Query to Execute
        String query = "select *  from FEBRUARY;";

        //Load mysql jdbc driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        //Create Connection to DB
        Connection con = DriverManager.getConnection(dbUrl, username, password);

        //Create Statement Object
        Statement stmt = con.createStatement();

        // Execute the SQL Query. Store results in ResultSet
        ResultSet rs = stmt.executeQuery(query);

        // While Loop to iterate through all data and print results
        while (rs.next()) {
            int id = rs.getInt(1);
            String expense = rs.getString(2);
            int amount = rs.getInt(3);

            System.out.println(id + expense + "  " + amount);
        }
        // closing DB Connection
        con.close();
    }

//3. GET AND SHOW ALL RECORD-DONE
    @RequestMapping("/showAllRecord")
    public List<ResultHolder> show(@RequestParam() String tableName) throws ClassNotFoundException, SQLException {

        Connection con=createDBConnection();

        //Create Statement Object
        Statement stmt = con.createStatement();
        //Query to Execute
        String query = "select *  from "+tableName+";";
        // Execute the SQL Query. Store results in ResultSet
        ResultSet rs = stmt.executeQuery(query);

        // While Loop to iterate through all data and print results
        List<ResultHolder> list = new ArrayList<>();
        while (rs.next()) {
            ResultHolder resultHolder = new ResultHolder();
            resultHolder.setId(rs.getInt(1));
            resultHolder.setExpense(rs.getString(2));
            resultHolder.setAmount(rs.getInt(3));
            list.add(resultHolder);
        }
        con.close();

        return  list;
    }






//EXPERIMENT
    @RequestMapping("/show_table")
    public void showTable(@RequestParam String tableName){

        String SQL = "SELECT * FROM "+tableName+"";
        List<ResultHolder> results= jdbc.query(SQL,new ResultSetExtractor<List<ResultHolder>>(){
            public List<ResultHolder> extractData(
                    ResultSet rs) throws SQLException, DataAccessException {
                List<ResultHolder> list = new ArrayList<>();
                while(rs.next()){
                    ResultHolder resultHolder = new ResultHolder();
                    resultHolder.setId(rs.getInt(1));
                    resultHolder.setExpense(rs.getString(2));
                    resultHolder.setAmount(rs.getInt(3));
                    list.add(resultHolder);
                }
                return list;

            }
        });



    }



//4.TOTAL OF SUM
//    @RequestMapping("/test")
//    public String index1(){
//        jdbc.execute("SELECT SUM(ID) FROM TOPA_TABLE");
////        jdbc.execute("DESCRIBE pet");
//        return"SUMATION SUCCESSFULL";
//    }

}
