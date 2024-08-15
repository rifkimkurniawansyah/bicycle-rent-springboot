package rifki.bicycle_rent.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rifki.bicycle_rent.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (name, balance) VALUES (:name, :balance)", nativeQuery = true)
    void createUser(@Param("name") String name, @Param("balance") Long balance);

    @Query(value = "SELECT * FROM users", nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = "SELECT * FROM users WHERE id = :id", nativeQuery = true)
    User getUserById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET name = :name, balance = :balance WHERE id = :id", nativeQuery = true)
    void updateUserById(@Param("name") String name, @Param("balance") Long balance, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE id = :id", nativeQuery = true)
    void deleteUserById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET balance = balance + :topUpBalance WHERE id = :id", nativeQuery = true)
    void topUpBalanceUser(@Param("id") Long id, @Param("topUpBalance") Long topUpBalance);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET balance = :balance WHERE id = :id", nativeQuery = true)
    void updateUserBalance(@Param("id") Long id, @Param("balance") Long balance);
}
