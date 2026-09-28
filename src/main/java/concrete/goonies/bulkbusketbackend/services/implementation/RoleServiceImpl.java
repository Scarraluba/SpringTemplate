package concrete.goonies.bulkbusketbackend.services.implementation;

import concrete.goonies.bulkbusketbackend.domain.Role;
import concrete.goonies.bulkbusketbackend.domain.User;
import concrete.goonies.bulkbusketbackend.dto.UserDTO;
import concrete.goonies.bulkbusketbackend.dto.mapper.UserDTOMapper;
import concrete.goonies.bulkbusketbackend.repository.RoleRepository;
import concrete.goonies.bulkbusketbackend.repository.UserRepository;
import concrete.goonies.bulkbusketbackend.services.RoleService;
import concrete.goonies.bulkbusketbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Project: SecureCapture
 * Created: 2026/05/28 13:03
 * Author: Scarra Luba
 */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository<Role> roleRepository;
    @Override
    public Role getRoleByUserId(Long id) {
        return roleRepository.getRoleByUserId(id);
    }
}







