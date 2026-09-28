package concrete.goonies.bulkbusketbackend.services;

import concrete.goonies.bulkbusketbackend.domain.Role;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/27 17:41
 * Author: Scarra Luba
 */

public interface RoleService {
    Role getRoleByUserId(Long id);
}
