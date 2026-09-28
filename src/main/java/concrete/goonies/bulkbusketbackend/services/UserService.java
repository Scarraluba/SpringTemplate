package concrete.goonies.bulkbusketbackend.services;

import concrete.goonies.bulkbusketbackend.domain.User;
import concrete.goonies.bulkbusketbackend.dto.UserDTO;
import org.jspecify.annotations.Nullable;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 05:05
 * Author: Scarra Luba
 */

public interface UserService {

    UserDTO createInsert(User user);

    UserDTO getUserByEmail(@Nullable String email);

    void sendVerificationCode(UserDTO user);

    User getUser(String email);

    UserDTO verifyCode(String email, String code);
}