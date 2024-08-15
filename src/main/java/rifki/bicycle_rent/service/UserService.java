package rifki.bicycle_rent.service;

import rifki.bicycle_rent.model.User;

import java.util.List;

public interface UserService {
    void createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    void updateUserById(Long id, User user);
    void deleteUserById(Long id);
    void topUpBalanceUser(Long userId, Long topUpBalance);
}
