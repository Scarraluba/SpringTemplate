package concrete.goonies.bulkbusketbackend.repository;

import concrete.goonies.bulkbusketbackend.domain.User;
import concrete.goonies.bulkbusketbackend.dto.UserDTO;

import java.util.Collection;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 02:57
 * Author: Scarra Luba
 */

public interface UserRepository<T extends User> {

    T create(T data);

    Collection<T> list(int page, int pageSize);

    T get(Long id);

    T update(T data);

    Boolean delete(Long id);

    T getUserByEmail(String email);

    void sendVerificationCode(UserDTO user);

    User verifyCode(String email, String code);
}
