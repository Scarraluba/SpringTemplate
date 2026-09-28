package concrete.goonies.bulkbusketbackend.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 02:53
 * Author: Scarra Luba
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class User {

    private Long id;

    @NotEmpty(message = "name cannot be empty")
    private String name;

    @Email(message = "invalid email address")
    @NotEmpty(message = "email cannot be empty")
    private String email;

    @NotEmpty(message = "password cannot be empty")
    private String password;

    private String status;

    private String role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum HouseholdStatus {
        ACTIVE,
        PENDING,
        SUSPENDED
    }

    public enum VerificationType
    {
        ACCOUNT("ACCOUNT"),
        PASSWORD("PASSWORD");

        private final  String type;

        VerificationType(String type) {
            this.type = type;
        }

        public String getType() {
            return type.toLowerCase();
        }
    }
}
