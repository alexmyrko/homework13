import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static List<Plane> planes;
    private static List<Plane> sortedPlanes;
    private static List<Pilot> pilots;
    private static Map<Plane, Integer> crews;
    private static int capacity;
    public static void main(String[] args) {
        crews = new HashMap<>();
        try(Connection connection = SqlOperations.initConnection()){
            SqlOperations.createDB(connection);
            planes = SqlOperations.getPlaneModels(connection);
            pilots = SqlOperations.getPilots(connection);
            sortedPlanes = planes.stream().sorted(Comparator.comparing(Plane::getSeats).reversed()).collect(Collectors.toList());

            for (Plane plane : sortedPlanes) {
                List<Pilot> pilotsForCurrentPlane = SqlOperations.getPilotsByPlane(connection, pilots, plane);
                int pilotsQuantity = pilotsForCurrentPlane.size();
                if (plane.getQuantity() >= pilotsQuantity / 2)
                    for (int i = 0; i < (pilotsQuantity - pilotsQuantity % 2); i = i + 2) {
                        pilots.remove(pilotsForCurrentPlane.get(i));
                        pilots.remove(pilotsForCurrentPlane.get(i + 1));
                        if (crews.get(plane) == null)
                            crews.put(plane, 1);
                        else crews.put(plane, crews.get(plane) + 1);
                    }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(crews.size());
        System.out.println("Available planes and crews for them:");
        for (Map.Entry<Plane,Integer> entry : crews.entrySet()) {
            capacity += entry.getKey().getSeats();
            System.out.println(entry.getKey().getModel() + " - " + entry.getValue());
        }
        System.out.println("Total airport capacity by passengers in planes: " + capacity);
    }
}
