
public class Plane {
    private String model;
    private int seats;
    private int quantity;

    public Plane(String model, int seats, int quantity) {
        this.model = model;
        this.seats = seats;
        this.quantity = quantity;
    }

    public String getModel() {
        return model;
    }

    public int getSeats() {
        return seats;
    }

    public int getQuantity() {
        return quantity;
    }
}
