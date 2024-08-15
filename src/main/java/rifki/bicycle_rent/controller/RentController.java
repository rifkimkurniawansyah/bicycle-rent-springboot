package rifki.bicycle_rent.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rifki.bicycle_rent.model.Rent;
import rifki.bicycle_rent.service.RentService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/rents")
@AllArgsConstructor
public class RentController {
    private final RentService rentService;

    @PostMapping
    public ResponseEntity<String> createRent(@RequestBody Rent rent){
        try {
            rentService.createRent(rent);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sewa Berhasil dibuat");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping
    public ResponseEntity<List<Rent>> getAllRent(){
        return ResponseEntity.ok(rentService.getAllRents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentById(@PathVariable Long id) {
        try {
            Rent rent = rentService.getRentById(id);
            return ResponseEntity.ok(rent);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateRentById(@PathVariable Long id, @RequestBody Rent rent) {
        try {
            rentService.updateRentById(id, rent);
            return ResponseEntity.ok("Sewa berhasil diupdate");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRentById(@PathVariable Long id) {
        try {
            rentService.deleteRentById(id);
            return ResponseEntity.ok("Sewa berhasil dihapus");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

}
