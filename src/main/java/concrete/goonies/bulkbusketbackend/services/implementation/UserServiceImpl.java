package concrete.goonies.bulkbusketbackend.services.implementation;

import concrete.goonies.bulkbusketbackend.domain.Role;
import concrete.goonies.bulkbusketbackend.domain.User;
import concrete.goonies.bulkbusketbackend.dto.UserDTO;
import concrete.goonies.bulkbusketbackend.dto.mapper.UserDTOMapper;
import concrete.goonies.bulkbusketbackend.repository.RoleRepository;
import concrete.goonies.bulkbusketbackend.repository.UserRepository;
import concrete.goonies.bulkbusketbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static concrete.goonies.bulkbusketbackend.dto.mapper.UserDTOMapper.fromUser;

/**
 * Project: SecureCapture
 * Created: 2026/05/28 13:03
 * Author: Scarra Luba
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDTO createInsert(User user) {
        return maptoUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return  maptoUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        userRepository.sendVerificationCode( user);
    }

    private UserDTO maptoUserDTO(User user){
        return fromUser(user,roleRepository.getRoleByUserId(user.getId()));
    }
    @Override
    public User getUser(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        return fromUser(userRepository.verifyCode( email,  code));
    }
}







