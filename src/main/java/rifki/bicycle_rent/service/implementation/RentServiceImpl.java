package rifki.bicycle_rent.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rifki.bicycle_rent.model.Bicycle;
import rifki.bicycle_rent.model.Rent;
import rifki.bicycle_rent.model.User;
import rifki.bicycle_rent.repository.BicycleRepository;
import rifki.bicycle_rent.repository.RentRepository;
import rifki.bicycle_rent.repository.UserRepository;
import rifki.bicycle_rent.service.RentService;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final BicycleRepository bicycleRepository;

    @Override
    public void createRent(Rent rent) {
        User user = userRepository.getUserById(rent.getUser().getId());
        Bicycle bicycle = bicycleRepository.getBicycleById(rent.getBicycle().getId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pengguna tidak ditemukan");
        }

        if (bicycle == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sepeda tidak ditemukan");
        }

        //Logic ini berfungsi untuk memeriksa jika sepeda dengan Id tertentu sudah disewa, maka tidak dapat disewa dengan Id rent yang lain
        if(!bicycle.getAvailable()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sepeda ini sudah disewa oleh pengguna lain, silahkan pilih sepeda yang masih tersedia");
        }

        // Cek jika field completed dan price tidak boleh di-set
        if (rent.getCompleted() != null || rent.getPrice() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'completed' dan 'price' tidak boleh di-set saat membuat sewa baru");
        }

        //Berfungsi untuk menghitung durasi waktu sewa (dalam hari)
        Long daysOfRent = ChronoUnit.DAYS.between(rent.getStartedAt(), rent.getEndsAt());

        //Menghitung biaya yang harus dikeluarkan pelanggan berdasarkan harga sewa sepeda per hari
        Long rentalCost = daysOfRent * bicycle.getPrice();

        //Jika saldo pelanggan tidak mencukupi maka tidak dapat melakukan sewa
        if(user.getBalance() < rentalCost){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo anda tidak mencukupi, silakan top up terlebih dahulu");
        }

        //Mengurangi saldo pelanggan dengan harga sewa
        userRepository.updateUserBalance(user.getId(), user.getBalance() - rentalCost);


        //Mengatur ketersediaan sepeda pada Id tertentu menjadi false jika sedang disewa
        bicycleRepository.updateBicycleAvailable(bicycle.getId(), false);
        rent.setPrice(rentalCost);

        //Saat sewa dibuat pengguna tidak harus memasukkan "completed" dan otomatis di set menjadi "false"
        rent.setCompleted(false);

        rentRepository.createRent(
                rent.getCompleted(),
                user.getId(),
                bicycle.getId(),
                rent.getStartedAt(),
                rent.getEndsAt(),
                rent.getPrice()
        );
    }

    @Override
    public List<Rent> getAllRents() {
        return rentRepository.getAllRents().stream().collect(Collectors.toList());
    }

    @Override
    public Rent getRentById(Long id) {
        Rent rent = rentRepository.getRentById(id);
        if (rent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sewa dengan Id ini tidak ditemukan");
        }
        return rent;
    }

    @Override
    public void updateRentById(Long id, Rent rent) {
        Rent existRent = rentRepository.getRentById(id);
        if (existRent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sewa dengan Id ini tidak ditemukan");
        }

        // Tidak mengizinkan untuk merubah id pengguna
        if (rent.getUser() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sewa dengan Id ini tidak ditemukan");
        }

        // Tidak mengizinkan untuk merubah status completed atau harga
        if (rent.getCompleted() != null || rent.getPrice() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'completed' dan 'price' tidak boleh di-set saat mengupdate sewa");
        }

        // Update sepeda yang digunakan dalam rent
        Bicycle newBicycle = bicycleRepository.getBicycleById(rent.getBicycle().getId());
        if (newBicycle == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id sepeda tidak ditemukan");
        }

        // Mengembalikan ketersediaan sepeda sebelumnya menjadi true dan membuat ketersediaan sepeda yang baru dipilih menjadi false
        Bicycle oldBicycle = existRent.getBicycle();
        if (!oldBicycle.getId().equals(newBicycle.getId())) {
            if (!newBicycle.getAvailable()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sepeda ini sudah disewa oleh pengguna lain, silahkan pilih sepeda yang masih tersedia");
            }
            //Membuat sepeda lama jadi tersedia (true)
            bicycleRepository.updateBicycleAvailable(oldBicycle.getId(), true);
            //Membuat sepeda baru jadi tidak tersedia (false)
            bicycleRepository.updateBicycleAvailable(newBicycle.getId(), false);
        }

        // Menghitung ulang harga sewa berdasarkan tanggal sewa yang sudah diperbaharui
        Long daysOfRent = ChronoUnit.DAYS.between(rent.getStartedAt(), rent.getEndsAt());
        Long rentalCost = daysOfRent * newBicycle.getPrice();

        // Cek jika saldo pelanggan mencukupi
        User user = userRepository.getUserById(existRent.getUser().getId());
        if (user.getBalance() < rentalCost) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo anda tidak mencukupi, silahkan top up terlebih dahulu");
        }

        // Update saldo pelanggan
        userRepository.updateUserBalance(user.getId(), user.getBalance() - rentalCost);

        // Update data rent yang sudah diperbaharui
        rentRepository.updateRentById(
                id,
                existRent.getCompleted(),
                user.getId(),
                newBicycle.getId(),
                rent.getStartedAt(),
                rent.getEndsAt(),
                rentalCost
        );
    }

    @Override
    public void deleteRentById(Long id) {
        Rent rent = rentRepository.getRentById(id);
        if (rent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Sewa tidak ditemukan");
        }

        //Apabila rent dihapus maka status ketersediaan sepeda berubah menjadi true kembali
        Bicycle bicycle = rent.getBicycle();
        bicycleRepository.updateBicycleAvailable(bicycle.getId(), true);

        rentRepository.deleteRentById(id);
    }

}
