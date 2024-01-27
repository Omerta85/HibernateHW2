import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String model;
    private CarType type;
    private int power;
    private double price;
    private int year;

    public Car(){}

    public Car(String model, CarType type, int power, double price, int year) {
        this.model = model;
        this.type = type;
        this.power = power;
        this.price = price;
        this.year = year;
    }
}
