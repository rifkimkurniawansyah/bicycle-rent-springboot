package rifki.bicycle_rent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rifki.bicycle_rent.model.Rent;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO rents (completed, user_id, bicycle_id, started_at, ends_at, price) VALUES (:completed, :user_id, :bicycle_id, :startedAt, :endsAt, :price)", nativeQuery = true)
    void createRent(@Param("completed") Boolean completed, @Param("user_id") Long user_id, @Param("bicycle_id") Long bicycle_id, @Param("startedAt") LocalDate startedAt, @Param("endsAt") LocalDate endsAt, @Param("price") Long price);

    @Query(value = "SELECT * FROM rents", nativeQuery = true)
    List<Rent> getAllRents();

    @Query(value = "SELECT * FROM rents WHERE id = :id", nativeQuery = true)
    Rent getRentById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE rents SET completed = :completed, user_id = :user_id, bicycle_id = :bicycle_id, started_at = :startedAt, ends_at = :endsAt, price = :price WHERE id = :id", nativeQuery = true)
    void updateRentById(@Param("id") Long id, @Param("completed") Boolean completed, @Param("user_id") Long user_id, @Param("bicycle_id") Long bicycle_id, @Param("startedAt") LocalDate startedAt, @Param("endsAt") LocalDate endsAt, @Param("price") Long price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM rents WHERE id = :id", nativeQuery = true)
    void deleteRentById(@Param("id") Long id);
}
