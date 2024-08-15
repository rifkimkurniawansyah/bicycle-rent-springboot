package rifki.bicycle_rent.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rifki.bicycle_rent.model.Bicycle;
import rifki.bicycle_rent.service.BicycleService;

import java.util.List;

@RestController
@RequestMapping("/bicycles")
@AllArgsConstructor
public class BicycleController {
    private final BicycleService bicycleService;

    @PostMapping
    public ResponseEntity<String> createBicycle(@RequestBody Bicycle bicycle){
        try{
            bicycleService.createBicycle(bicycle);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sepeda berhasil dibuat");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @GetMapping
    public ResponseEntity<List<Bicycle>> getAllBicycles(){
        return ResponseEntity.ok(bicycleService.getAllBicycle());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBicycleById(@PathVariable Long id) {
        try {
            Bicycle bicycle = bicycleService.getBicycleById(id);
            return ResponseEntity.ok(bicycle);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBicycleById(@PathVariable Long id, @RequestBody Bicycle bicycle) {
        try {
            bicycleService.updateBicycleById(id, bicycle);
            return ResponseEntity.ok("Sepeda berhasil diupdate");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBicycleById(@PathVariable Long id) {
        try {
            bicycleService.deleteBicycleById(id);
            return ResponseEntity.ok("Sepeda berhasil dihapus");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}
