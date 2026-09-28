package concrete.goonies.bulkbusketbackend.repository.rowmapper;

import concrete.goonies.bulkbusketbackend.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Project: SecureCapture
 * Created: 2026/05/26 11:57
 * Author: Scarra Luba
 */

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
             .email(rs.getString("email"))
//                .address(rs.getString("address"))
//                .phone(rs.getString("phone"))
//                .bio(rs.getString("bio"))
//                .title(rs.getString("title"))
//                .imageUrl(rs.getString("image_url"))
//                .enabled(rs.getBoolean("enabled"))
//                .locked(rs.getBoolean("is_locked"))
//                .usingMfa(rs.getBoolean("using_Mfa"))
                .status(rs.getString("status"))
               // .role(rs.getString("role"))
                .createdAt(rs.getTimestamp("created_date").toLocalDateTime())
               // .updatedAt(rs.getTimestamp("updatedAt ").toLocalDateTime())


                .build();
    }
}
