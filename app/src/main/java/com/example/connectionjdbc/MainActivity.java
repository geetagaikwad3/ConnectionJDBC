package com.example.connectionjdbc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     Connection connection = null;
        try
        {

          dbManager = new DBManager(this);
         dbManager.open();

//            Properties prop = new Properties();//config.toProperties();
//            prop.setProperty("readOnly", "true");
//            prop.setProperty("open_mode", "1");
//            prop.setProperty("immutable", "true");
//            prop.setProperty("mode", "ro");
//            prop.setProperty("journal_mode", "OFF");
            // create a database connection
            SQLiteConfig config = new SQLiteConfig();
            config.setExplicitReadOnly(true);
            String jdbcUrl = "jdbc:sqlite:" + "/data/data/" + getPackageName() + "/JOURNALDEV_COUNTRIES.db";
            connection = DriverManager.getConnection(jdbcUrl);//"jdbc:sqlite:COUNTRIES.db",prop);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'Geeta')");
            statement.executeUpdate("insert into person values(2, 'Pratik')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }
}