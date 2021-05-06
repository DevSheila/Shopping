package models;


import org.sql2o.*;

public class DB {

    private static String connectionString = "jdbc:postgresql://ec2-3-217-219-146.compute-1.amazonaws.com"; //!
    public static Sql2o sql2o = new Sql2o(connectionString, "lwdzostdabzqmg", "19c576a5ca05dff80e24fea0f040c9d1ef382dd5d59dc0cda310407285d1fe19"); //!

}
