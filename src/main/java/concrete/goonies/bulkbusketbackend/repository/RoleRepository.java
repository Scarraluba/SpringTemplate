package concrete.goonies.bulkbusketbackend.repository;

import concrete.goonies.bulkbusketbackend.domain.Role;

import java.util.Collection;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 02:57
 * Author: Scarra Luba
 */

public interface RoleRepository<T extends Role> {

    //Basic CRUD Operations

    T create(T data);

    Collection<T> list(int page, int pageSize);

    T get(Long id);

    T update(T data);

    Boolean delete(Long id);

    //More Complex Operations

    void addRoleToUser(Long userId, String roleName);

    Role getRoleByUserId(Long userId);

    Role getRoleByUserEmail(String email);

    void updateUserRole(Long userId, String roleName);

}
