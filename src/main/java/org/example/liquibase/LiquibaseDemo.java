package org.example.liquibase;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.utils.ConnectionManager;

import java.sql.Connection;

/**
 * The `LiquibaseDemo` class is responsible for running Liquibase database migrations. It uses Liquibase to apply
 * changesets defined in a changelog file to the connected database.
 */
public class LiquibaseDemo {
    /**
     * A singleton instance of the `LiquibaseDemo` class.
     */
    private static final LiquibaseDemo liquibaseDemo = new LiquibaseDemo();

    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations() {
        try {
            Connection connection = ConnectionManager.getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the singleton instance of the `LiquibaseDemo` class.
     *
     * @return The singleton instance.
     */
    public static LiquibaseDemo getInstance() {
        return liquibaseDemo;
    }
}