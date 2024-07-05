package org.example.liquibase;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.utils.ConfigUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * The `LiquibaseManager` class is responsible for running Liquibase database migrations. It uses Liquibase to apply
 * change sets defined in a changelog file to the connected database.
 */
public class LiquibaseManager {
    /**
     * A singleton instance of the `LiquibaseManager` class.
     */
    private static final LiquibaseManager liquibaseManager = new LiquibaseManager();
    private static final String CHANGELOG_PATH = ConfigUtil.getProperty("liquibase.changeLogFile");
    private static final String LIQUIBASE_SCHEMA_NAME = ConfigUtil.getProperty("liquibase.schemaName");
    private static final String SQL_CREATE_LIQUIBASE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS " + LIQUIBASE_SCHEMA_NAME;

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_LIQUIBASE_SCHEMA)){
            preparedStatement.execute();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(LIQUIBASE_SCHEMA_NAME);
            Liquibase liquibase = new Liquibase(CHANGELOG_PATH, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Миграции успешно выполнены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rolls back database migrations to undo the creation of tables.
     *
     * @param connection The database connection.
     */
    public void rollbackToCreateTables(Connection connection) {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(LIQUIBASE_SCHEMA_NAME);
            Liquibase liquibase = new Liquibase(CHANGELOG_PATH, new ClassLoaderResourceAccessor(), database);
            liquibase.rollback(4, null);
            System.out.println("Миграции успешно отменены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the singleton instance of the `LiquibaseManager` class.
     *
     * @return The singleton instance.
     */
    public static LiquibaseManager getInstance() {
        return liquibaseManager;
    }
}