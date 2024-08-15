package rifki.bicycle_rent.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rifki.bicycle_rent.model.User;
import rifki.bicycle_rent.repository.UserRepository;
import rifki.bicycle_rent.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.createUser(user.getName(), user.getBalance());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers().stream().collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengguna dengan Id ini tidak ditemukan");
        }
        return user;
    }

    @Override
    public void updateUserById(Long id, User user) {
        getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengguna dengan Id ini tidak ditemukan");
        }
        userRepository.updateUserById(user.getName(), user.getBalance(), id);
    }

    @Override
    public void deleteUserById(Long id) {
        getUserById(id);
        userRepository.deleteUserById(id);
    }

    @Override
    public void topUpBalanceUser(Long userId, Long topUpBalance){
        User user = userRepository.getUserById(userId);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pengguna dengan Id ini tidak ditemukan");
        }
        if(topUpBalance <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Top Up tidak boleh negatif");
        }
        Long newBalance = user.getBalance() + topUpBalance;
        userRepository.topUpBalanceUser(userId, topUpBalance);
    }
}
