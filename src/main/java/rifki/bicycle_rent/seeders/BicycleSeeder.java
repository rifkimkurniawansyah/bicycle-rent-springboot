package rifki.bicycle_rent.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rifki.bicycle_rent.model.Bicycle;
import rifki.bicycle_rent.repository.BicycleRepository;

@Component
@RequiredArgsConstructor

//File seeder untuk menambahkan sepeda secara otomatis
public class BicycleSeeder implements CommandLineRunner {
    private final BicycleRepository bicycleRepository;

    @Override
    public void run(String... args) throws Exception{
        createBicycle("Sepeda Gunung Polygon", 27000L, true);
        createBicycle("Sepeda Balap Specialized", 33000L, true);
        createBicycle("Sepeda Lipat Brompton", 45000L, true);
        createBicycle("Sepeda Elektrik Xiaomi", 57000L, true);
        createBicycle("Sepeda BMX United", 18000L, true);
    }

    public void createBicycle(String name, Long price, Boolean available){
        bicycleRepository.createBicycle(name, available, price);
    }
}
