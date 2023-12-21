package com.project;

import java.awt.*;
import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in); // System.in és global
    public static void main(String[] args) throws SQLException {

        String menu = "Escull una opcio:\n1) Mostrar taula.\n2) Mostrar personatges per facció\n3) Mostrar millor atacant per facció\n4) Mostrar millor defensor per facció\n5) Sortir";

        String filePath = "./src/main/resources/assets/BBDD_ForHonor.db";
        Connection conn = null;
        boolean sortir = false;
        try {
            String url = "jdbc:sqlite:" + filePath;
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("BBDD driver: " +
                        meta.getDriverName());
                System.out.println("BBDD SQLite connected");
                System.out.println("Creating tables...");
                // Crear taules
                crearTaules(conn);
                // Insertar dades
                // insertarDades(conn); Comentat per evitar errors de clau primària al estar ja creades les taules
                while (!sortir) {
                    {
                        System.out.println(menu);
                        int opcio = Integer.valueOf(llegirLinia("Opció:"));
                        switch (opcio) {
                            case 1:
                                mostrarTaula(conn);
                                break;
                            case 2:
                                mostrarPersonatgesPerFaccio(conn);
                                break;
                            case 3:
                                mostrarMillorAtacantPerFaccio(conn);
                                break;
                            case 4:
                                mostrarMillorDefensorPerFaccio(conn);
                                break;
                            case 5:
                                conn.close();
                                System.out.println("Connexió tancada");
                                sortir = true;
                                break;
                            default:
                                System.out.println("Opció incorrecta");
                                break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void crearTaules(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Faccio (\n" +
                "   id INT PRIMARY KEY,\n" +
                "   nom VARCHAR(15),\n" +
                "   resum VARCHAR(500)\n" +
                ");";
        stmt.executeUpdate(sql);
        stmt.close();
        sql = "CREATE TABLE IF NOT EXISTS Personatge (\n" +
                "    id INT PRIMARY KEY,\n" +
                "    nom VARCHAR(15),\n" +
                "    atac FLOAT,\n" +
                "    defensa FLOAT,\n" +
                "    idFaccio INT,\n" +
                "    FOREIGN KEY (idFaccio) REFERENCES Faccio(id)\n" +
                ");";
        stmt.executeUpdate(sql);
        stmt.close();
    }
    public static void insertarDades(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "INSERT INTO Faccio (id, nom, resum) VALUES\n" +
                "    (1, 'Knight', 'The guardians of our people'),\n" +
                "    (2, 'Viking', 'The favored of the gods'),\n" +
                "    (3, 'Samurai', 'The Samurai of the Dawn Empire');";
        stmt.executeUpdate(sql);
        stmt.close();
        sql = "INSERT INTO Personatge (id, nom, atac, defensa, idFaccio) VALUES\n" +
                "(1, 'Warden', 75.0, 60.0, 1),\n" +
                "(2, 'Conqueror', 70.0, 70.0, 1),\n" +
                "(3, 'Peacekeeper', 85.0, 50.0, 1),\n" +
                "(4, 'Berserker', 90.0, 80.0, 2),\n" +
                "(5, 'Warlord', 85.0, 75.0, 2),\n" +
                "(6, 'Raider', 95.0, 85.0, 2),\n" +
                "(7, 'Orochi', 75.0, 65.0, 3),\n" +
                "(8, 'Nobushi', 80.0, 70.0, 3),\n" +
                "(9, 'Kensei', 70.0, 60.0, 3);";
        stmt.executeUpdate(sql);
        stmt.close();
    }
    static public String llegirLinia (String text) {
        System.out.print(text);
        return in.nextLine();
    }
    public static void mostrarTaula(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String menu = "Escull la taula:\n1) Faccio\n2) Personatge\n";
        System.out.println(menu);
        int opcio = Integer.valueOf(llegirLinia("Opció:"));
        String taula = "";
        switch (opcio) {
            case 1:
                taula = "Faccio";
                break;
            case 2:
                taula = "Personatge";
                break;
            default:
                System.out.println("Opció incorrecta");
                break;
        }
        String sql = "Select * from " + taula + ";";

        ResultSet rs = stmt.executeQuery(sql);
        if (taula.equals("Faccio")) {
            while (rs.next()) {
                System.out.println("ID:" + rs.getInt(1));
                System.out.println("Nom: " + rs.getString(2));
                System.out.println("Resum: " + rs.getString(3));
            }
        } else {
            while (rs.next()) {
                System.out.println("ID:" + rs.getInt(1));
                System.out.println("Nom: " + rs.getString(2));
                System.out.println("Atac: " + rs.getFloat(3));
                System.out.println("Defensa: " + rs.getFloat(4));
                System.out.println("ID Faccio: " + rs.getInt(5));
            }
        }
        rs.close();
        stmt.close();
    }
    public static void mostrarPersonatgesPerFaccio(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String menu = "Escull una facció:\n1) Knight\n2) Viking\n3) Samurai";
        System.out.println(menu);
        int opcio = Integer.valueOf(llegirLinia("Opció:"));
        String faccio = "";
        switch (opcio) {
            case 1:
                faccio = "Knight";
                break;
            case 2:
                faccio = "Viking";
                break;
            case 3:
                faccio = "Samurai";
                break;
            default:
                System.out.println("Opció incorrecta");
                break;
        }
        String sql = "SELECT * FROM Personatge WHERE idFaccio = " + opcio + ";";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.printf("Faccio: " + faccio + "\n");
            System.out.println("ID:" + rs.getInt(1));
            System.out.println("Nom: " + rs.getString(2));
            System.out.println("Atac: " + rs.getFloat(3));
            System.out.println("Defensa: " + rs.getFloat(4));
            System.out.println("ID Faccio: " + rs.getInt(5));
        }
        rs.close();
        stmt.close();
    }
    public static void mostrarMillorAtacantPerFaccio(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String menu = "Escull una facció:\n1) Knight\n2) Viking\n3) Samurai";
        System.out.println(menu);
        int opcio = Integer.valueOf(llegirLinia("Opció:"));
        String faccio = "";
        switch (opcio) {
            case 1:
                faccio = "Knight";
                break;
            case 2:
                faccio = "Viking";
                break;
            case 3:
                faccio = "Samurai";
                break;
            default:
                System.out.println("Opció incorrecta");
                break;
        }
        String sql = "SELECT * FROM Personatge WHERE idFaccio = " + opcio + " ORDER BY atac DESC LIMIT 1;";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.printf("Faccio: " + faccio + "\n");
            System.out.println("ID:" + rs.getInt(1));
            System.out.println("Nom: " + rs.getString(2));
            System.out.println("Atac: " + rs.getFloat(3));
            System.out.println("Defensa: " + rs.getFloat(4));
            System.out.println("ID Faccio: " + rs.getInt(5));
        }
        rs.close();
        stmt.close();
    }
    public static void mostrarMillorDefensorPerFaccio(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String menu = "Escull una facció:\n1) Knight\n2) Viking\n3) Samurai";
        System.out.println(menu);
        int opcio = Integer.valueOf(llegirLinia("Opció:"));
        String faccio = "";
        switch (opcio) {
            case 1:
                faccio = "Knight";
                break;
            case 2:
                faccio = "Viking";
                break;
            case 3:
                faccio = "Samurai";
                break;
            default:
                System.out.println("Opció incorrecta");
                break;
        }
        String sql = "SELECT * FROM Personatge WHERE idFaccio = " + opcio + " ORDER BY defensa DESC LIMIT 1;";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.printf("Faccio: " + faccio + "\n");
            System.out.println("ID:" + rs.getInt(1));
            System.out.println("Nom: " + rs.getString(2));
            System.out.println("Atac: " + rs.getFloat(3));
            System.out.println("Defensa: " + rs.getFloat(4));
            System.out.println("ID Faccio: " + rs.getInt(5));
        }
        rs.close();
        stmt.close();
    }
}
