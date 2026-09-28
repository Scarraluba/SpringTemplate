package concrete.goonies.bulkbusketbackend.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 04:19
 * Author: Scarra Luba
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Role {
    private Long id;
    private String name;
    private String permission;

    public enum RoleType {
        ROLE_USER,
        ROLE_MANAGER,
        ROLE_ADMIN,
        ROLE_SYSADMIN
    }

}
