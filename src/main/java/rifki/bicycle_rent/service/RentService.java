package rifki.bicycle_rent.service;
import rifki.bicycle_rent.model.Rent;

import java.util.List;

public interface RentService {
    void createRent(Rent rent);
    List<Rent> getAllRents();
    Rent getRentById(Long id);
    void updateRentById(Long id, Rent rent);
    void deleteRentById(Long id);
}
