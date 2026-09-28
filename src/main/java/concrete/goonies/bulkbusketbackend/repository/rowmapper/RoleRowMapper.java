package concrete.goonies.bulkbusketbackend.repository.rowmapper;

import concrete.goonies.bulkbusketbackend.domain.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Project: SecureCapture
 * Created: 2026/05/26 11:57
 * Author: Scarra Luba
 */

public class RoleRowMapper implements RowMapper<Role>{
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Role.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .permission(rs.getString("permission"))
                .build();
    }
}
