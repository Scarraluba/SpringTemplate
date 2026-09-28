package concrete.goonies.bulkbusketbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Project: SecureCapture
 * Created: 2026/05/28 12:48
 * Author: Scarra Luba
 */
@Data
public class UserDTO {
    private Long id;

    private String name;
    private String email;

    private String role;
    private String permissions;

    private String status;
    private LocalDateTime createdAt;

    private String phone;

}
