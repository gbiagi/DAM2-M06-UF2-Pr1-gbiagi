package com.project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        try {
            String url =  "jbdc:sqlite:for_honor.db";
            conn = DriverManager.getConnection();
        }
    }
}
