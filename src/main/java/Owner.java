import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private List<Car> cars;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id")
    private  DriveLicense driveLicense;
    public Owner(){}

    public Owner(String name, List<Car> cars, DriveLicense driveLicense) {
        this.name = name;
        this.cars = cars;
        this.driveLicense = driveLicense;
    }
}
