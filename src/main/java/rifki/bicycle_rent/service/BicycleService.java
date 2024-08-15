package rifki.bicycle_rent.service;
import rifki.bicycle_rent.model.Bicycle;

import java.util.List;

public interface BicycleService {
    void createBicycle(Bicycle bicycle);
    List<Bicycle> getAllBicycle();
    Bicycle getBicycleById(Long id);
    void updateBicycleById(Long id, Bicycle bicycle);
    void deleteBicycleById(Long id);
}
