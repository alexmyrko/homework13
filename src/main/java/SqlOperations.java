import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class SqlOperations {
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static String host;
    private static String login;
    private static String password;
    private static Connection connection;
    private static MysqlDataSource mysqlDS;

    public static Connection initConnection() throws SQLException {
        Properties property = new Properties();

        try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties")){
            property.load(fileInputStream);

            host = property.getProperty("url");
            login = property.getProperty("username");
            password = property.getProperty("password");

            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(host);
            mysqlDS.setUser(login);
            mysqlDS.setPassword(password);

        } catch (IOException e) {
            System.err.println("Property file does not exists");
        }
        return DriverManager.getConnection(host, login, password);
    }

    public static void createDB(Connection connection) {
        try (
             Statement statement = connection.createStatement();
        ) {
            statement.addBatch("DROP SCHEMA airport");
            statement.addBatch("CREATE SCHEMA IF NOT EXISTS airport;");
            statement.addBatch("USE airport;");
            statement.addBatch("SET NAMES utf8;");
            statement.executeBatch();
            statement.clearBatch();

            String createPlanes = "CREATE TABLE IF NOT EXISTS `planes` (\n" +
                    "`model_id` INT AUTO_INCREMENT NOT NULL,\n" +
                    "`model` VARCHAR(20) NOT NULL,\n" +
                    "`seats` INT NOT NULL,\n" +
                    "PRIMARY KEY (`model_id`));";

            String createSerialNumbers = "CREATE TABLE IF NOT EXISTS `serial_numbers` (\n" +
                    "`id` INT AUTO_INCREMENT NOT NULL,\n" +
                    "`model_id` INT NOT NULL,\n" +
                    "`serial_number` VARCHAR(16) NOT NULL,\n" +
                    "PRIMARY KEY (`id`));";

            String createPilots = "CREATE TABLE IF NOT EXISTS `pilots` (\n" +
                    "`pilot_id` INT AUTO_INCREMENT NOT NULL,\n" +
                    "`name` VARCHAR(40) NOT NULL,\n" +
                    "`age` INT NOT NULL,\n" +
                    "PRIMARY KEY (`pilot_id`));";

            String createPlanesAndPilots = "CREATE TABLE IF NOT EXISTS `planes_and_pilots`(\n" +
                    "`id` INT AUTO_INCREMENT NOT NULL,\n" +
                    "`pilot_id` INT NOT NULL,\n" +
                    "`model_id` INT NOT NULL,\n" +
                    "PRIMARY KEY (`id`));";

            statement.execute(createPlanes);
            statement.execute(createSerialNumbers);
            statement.execute(createPilots);
            statement.execute(createPlanesAndPilots);

            statement.execute("INSERT INTO planes VALUES " +
                    "(1,'Airbus A320',150)," +
                    "(2, 'Boeing 747',410)," +
                    "(3,'Embraer E195',115);");
            statement.execute("INSERT INTO serial_numbers VALUES " +
                    "(1,1,'HA-LSC'),(2,1,'HA-SCM')," +
                    "(3,2,'UR-PSK'),(4,3,'UR-EMG');");
            statement.execute("INSERT INTO pilots VALUES" +
                    "(1,'Erich Smidt',30),(2,'Jonas Ericsson',47),(3,'Peter Kuba',27),(4,'Umberto Gomes',38);");
            statement.execute("INSERT INTO `planes_and_pilots` VALUES (1,1,2),(2,1,3),(3,2,1),(4,2,2),(5,3,3),(6,4,1),(7,4,3);");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<Pilot> getPilots(Connection connection) throws SQLException {
        List<Pilot> pilots = new ArrayList<>();
        String query = "SELECT name FROM pilots;";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                pilots.add(new Pilot(name));
            }
        }
        return pilots;
    }

    public static List<Plane> getPlaneModels(Connection connection) throws SQLException {
        List<Plane> planes = new ArrayList<>();
            String query = "SELECT serial_numbers.model_id, planes.model, seats, COUNT(serial_numbers.model_id) as quantity " +
                    "FROM planes \n" +
                    "INNER JOIN serial_numbers ON planes.model_id = serial_numbers.model_id\n" +
                    "GROUP BY planes.model;";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String model = resultSet.getString("model");
                int seats = resultSet.getInt("seats");
                int quantity = resultSet.getInt("quantity");
                planes.add(new Plane(model, seats, quantity));
            }
        }
        return planes;
    }


    public static List<Pilot> getPilotsByPlane(Connection connection, List<Pilot> pilots, Plane planeModel) throws SQLException {
        List<Pilot> pilotsByPlane = new ArrayList<>();
        String query = "SELECT pilots.name FROM pilots\n" +
                "INNER JOIN planes_and_pilots ON pilots.pilot_id = planes_and_pilots.pilot_id\n" +
                "INNER JOIN planes ON planes_and_pilots.model_id = planes.model_id\n" +
                "WHERE planes.model = '" + planeModel.getModel() + "';";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                for(Pilot pilot : pilots) {
                    if (pilot.getName().equals(name))
                        pilotsByPlane.add(pilot);
                }
            }
        }
        return pilotsByPlane;
    }
}
