package rifki.bicycle_rent.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rifki.bicycle_rent.model.Bicycle;
import rifki.bicycle_rent.repository.BicycleRepository;
import rifki.bicycle_rent.service.BicycleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BicycleServiceImpl implements BicycleService {
    private BicycleRepository bicycleRepository;
    @Override
    public void createBicycle(Bicycle bicycle) {
        // Memastikan pengguna tidak dapat mengatur field 'available' secara manual
        if (bicycle.getAvailable() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'available' tidak boleh diatur saat membuat sepeda baru.");
        }
        // Otomatis set available ke true saat sepeda baru dibuat
        bicycle.setAvailable(true);

        // Simpan sepeda ke database
        bicycleRepository.createBicycle(bicycle.getName(), bicycle.getAvailable(), bicycle.getPrice());
    }

    @Override
    public List<Bicycle> getAllBicycle() {
        return bicycleRepository.getAllBicycles().stream().collect(Collectors.toList());
    }

    @Override
    public Bicycle getBicycleById(Long id) {
        Bicycle bicycle = bicycleRepository.getBicycleById(id);
        if (bicycle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id sepeda tidak ditemukan");
        }
        return bicycle;
    }

    public void updateBicycleById(Long id, Bicycle bicycle) {
        Bicycle existBicycle = getBicycleById(id);

        // Memastikan pengguna tidak dapat mengatur field 'available' secara manual
        if (bicycle.getAvailable() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'available' tidak boleh diatur saat mengupdate sepeda.");
        }

        // Mempertahankan nilai 'available' dari sepeda yang sudah ada
        boolean currentAvailable = existBicycle.getAvailable();

        // Perbarui informasi sepeda tanpa mengubah 'available'
        bicycleRepository.updateBicycleById(id, bicycle.getName(), currentAvailable, bicycle.getPrice());
    }

    @Override
    public void deleteBicycleById(Long id) {
        getBicycleById(id);
        bicycleRepository.deleteBicycleById(id);
    }

}
