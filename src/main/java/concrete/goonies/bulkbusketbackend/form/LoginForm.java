package concrete.goonies.bulkbusketbackend.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @project: SecureCapture
 * @createdAt: 2026/07/27 15:21
 * @author: Scarra Luba
 */

@Data
public class LoginForm {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
