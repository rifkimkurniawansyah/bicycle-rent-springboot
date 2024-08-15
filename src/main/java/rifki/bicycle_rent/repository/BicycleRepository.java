package rifki.bicycle_rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rifki.bicycle_rent.model.Bicycle;

import java.util.List;

@Repository
public interface BicycleRepository extends JpaRepository<Bicycle, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bicycles (name, available, price) VALUES (:name, :available, :price)", nativeQuery = true)
    void createBicycle(@Param("name") String name, @Param("available") Boolean available, @Param("price") Long price);

    @Query(value = "SELECT * FROM bicycles", nativeQuery = true)
    List<Bicycle> getAllBicycles();

    @Query(value = "SELECT * FROM bicycles WHERE id = :id", nativeQuery = true)
    Bicycle getBicycleById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bicycles SET name = :name, available = :available, price = :price WHERE id = :id", nativeQuery = true)
    void updateBicycleById(@Param("id") Long id, @Param("name") String name, @Param("available") Boolean available, @Param("price") Long price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM bicycles WHERE id = :id", nativeQuery = true)
    void deleteBicycleById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bicycles SET available = :available WHERE id = :id", nativeQuery = true)
    void updateBicycleAvailable(@Param("id") Long id, @Param("available") Boolean available);
}
