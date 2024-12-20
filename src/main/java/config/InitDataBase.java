package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitDataBase {
    public static void initializeDatabase() throws SQLException {
        String adminUrl = "jdbc:postgresql://localhost:5432/postgres";
        String adminUsername = "postgres";
        String adminPassword = "CODERJAVA";

        String databaseName = "apache_camel_example";
        String databaseUrl = "jdbc:postgresql://localhost:5432/" + databaseName;
        try (Connection adminConnection = DriverManager.getConnection(adminUrl,adminUsername,adminPassword);
             Statement adminStatement = adminConnection.createStatement()) {
            String crateDatabase = "CREATE DATABASE " + databaseName;
            adminStatement.executeUpdate(crateDatabase);
            System.out.println("База данных '" + databaseName + "' успешно создана.");
        }catch (Exception e) {
            if (e.getMessage().contains("already exists")) {
                System.out.println("База данных '" + databaseName + "' уже существует.");
            }else {
                throw new RuntimeException("Ошибка при создании базы данных", e);
            }
        }
        try (Connection newDbConnection = DriverManager.getConnection(databaseUrl,adminUsername,adminPassword);
             Statement dbStatement = newDbConnection.createStatement()) {
            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS employees (
                        id INT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        email VARCHAR(50) NOT NULL
                    );
                    """;
            dbStatement.execute(createTableSQL);
            System.out.println("Таблица 'users' создана в базе данных '" + databaseName + "'.");
        } catch (Exception e) {
                throw new RuntimeException("Ошибка при создании таблиц в базе данных", e);
        }
    }
    public static void populateDatabase() throws SQLException {
        String newDatabaseUrl = "jdbc:postgresql://localhost:5432/apache_camel_example";
        String username = "postgres";
        String password = "CODERJAVA";

        try (Connection connection = DriverManager.getConnection(newDatabaseUrl, username, password);
             Statement statement = connection.createStatement()) {
            String insertDataSQL = """
                    INSERT INTO employees (id,name, email) VALUES
                    (1,'Vasya', 'vasy@gmail.com'),
                    (2,'Petrovich', 'petrovich@yndex.com'),
                    (3,'Elon', 'elonX@gmail.com')
                    ON CONFLICT DO NOTHING;
                    """;
            int rowsInserted = statement.executeUpdate(insertDataSQL);
            System.out.println("Добавлено " + rowsInserted + " строк(и) в таблицу 'employees'.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при заполнении таблицы 'users'", e);
        }
    }
}

