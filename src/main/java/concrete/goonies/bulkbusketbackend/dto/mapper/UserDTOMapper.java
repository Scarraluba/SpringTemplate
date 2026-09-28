package concrete.goonies.bulkbusketbackend.dto.mapper;

import concrete.goonies.bulkbusketbackend.domain.Role;
import concrete.goonies.bulkbusketbackend.domain.User;
import concrete.goonies.bulkbusketbackend.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Project: SecureCapture
 * Created: 2026/05/28 12:54
 * Author: Scarra Luba
 */
@Component
public class UserDTOMapper {

    public static UserDTO fromUser(User user){

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }
    public static UserDTO fromUser(User user, Role role){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);

        userDTO.setRole(role.getName());
        userDTO.setPermissions(role.getPermission());

        return userDTO;
    }
    public static User toUser(UserDTO user){

        User userDTO = new User();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }
}
