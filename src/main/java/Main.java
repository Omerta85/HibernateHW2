import org.hibernate.cfg.Configuration;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    private final SessionFactory sessionFactory;
    public  Main() {
        Dotenv dotenv = Dotenv.configure().load();
        String dbUrl = dotenv.get("DB_URL");
        String dbUsername = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");

        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.url", dbUrl)
                .setProperty("hibernate.connection.username", dbUsername)
                .setProperty("hibernate.connection.password", dbPassword)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(Car.class)
                .addAnnotatedClass(Owner.class)
                .addAnnotatedClass(DriveLicense.class);

        sessionFactory = configuration.buildSessionFactory();
    }
    public void saveOwner(Owner owner) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(owner);
            transaction.commit();
        }
    }

    public List<Owner> getAllOwners() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Owner", Owner.class).list();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();

        // Додавання власника з автомобілями та посвідченням водія в базу даних
        Car car1 = new Car("Toyota Camry", CarType.SEDAN, 200, 25000, 2022);
        Car car2 = new Car("Honda CR-V", CarType.SUV, 180, 30000, 2022);
        Car car3 = new Car("Ford Focus", CarType.HATCHBACK, 150, 20000, 2023);
        Car car4 = new Car("Audi Q7", CarType.SUV, 260, 60000, 2023);
        Car car5 = new Car("Tesla Model 3", CarType.SEDAN, 250, 60000, 2023);

        DriveLicense driveLicense = new DriveLicense("ABC123");
        DriveLicense driveLicense2 = new DriveLicense("456ABC");
        DriveLicense driveLicense3 = new DriveLicense("123XYZ");

        Owner owner = new Owner("Марія Климович", List.of(car1, car2), driveLicense);
        main.saveOwner(owner);
        Owner owner2 = new Owner("Мирослав Заставний", List.of(car3, car4), driveLicense2);
        main.saveOwner(owner2);
        Owner owner3 = new Owner("Ельвіра Кук", List.of(car5), driveLicense3);
        main.saveOwner(owner3);


        // Дістання всіх власників в межах транзакції
        try (Session session = main.sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            List<Owner> owners = session.createQuery("FROM Owner", Owner.class).list();

            // Виведення інформації про власників
            for (Owner o : owners) {
                System.out.println("Owner: " + o.getName());
                System.out.println("Cars: ");
                for (Car c : o.getCars()) {
                    System.out.println("  - " + c.getModel());
                }
                System.out.println("Drive License Series: " + o.getDriveLicense().getSeries());
                System.out.println();
            }

            transaction.commit();
        }

        // Закриваємо sessionFactory
        main.sessionFactory.close();
    }
}
