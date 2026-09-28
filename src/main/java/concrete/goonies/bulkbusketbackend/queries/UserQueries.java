package concrete.goonies.bulkbusketbackend.queries;

/**
 * Project: BulkBusketBackend
 * Created: 2026/09/26 03:15
 * Author: Scarra Luba
 */

public class UserQueries {

    public static final String COUNT_USER_EMAIL_QUERY =
            "SELECT COUNT(*) FROM households WHERE email = :email";

    public static final String INSERT_USER_QUERY =
            "INSERT INTO households(name, email, password) " +
                    "VALUES(:name, :email, :password) RETURNING id";

    public static final String INSERT_ROLE_TO_USER_QUERY =
            "INSERT INTO user_roles (user_id, role_id) " +
                    "VALUES (:userId, :roleId)";

    public static final String SELECT_ROLE_BY_NAME_QUERY =
            "SELECT * FROM roles WHERE name = :name";

    public static final String SELECT_USER_BY_EMAIL_QUERY =
            "SELECT h.*, r.name AS role, r.permission " +
                    "FROM households h " +
                    "JOIN user_roles ur ON ur.user_id = h.id " +
                    "JOIN roles r ON r.id = ur.role_id " +
                    "WHERE h.email = :email";

    public static final String SELECT_USER_BY_ID_QUERY =
            "SELECT r.id, r.name, r.permission " +
                    "FROM roles r " +
                    "JOIN user_roles ur ON ur.role_id = r.id " +
                    "JOIN households h ON h.id = ur.user_id " +
                    "WHERE h.id = :id";

    public static final String INSERT_VERIFICATION_URL_QUERY =
            "INSERT INTO account_verification (user_id, url) " +
                    "VALUES (:userId, :url)";

    public static final String DELETE_VERIFICATION_CODE_BY_ID =
            "DELETE FROM two_factor_verification WHERE user_id = :id";

    public static final String INSERT_VERIFICATION_CODE_BY_ID =
            "INSERT INTO two_factor_verification " +
                    "(user_id, code, expiration_date) " +
                    "VALUES (:userId, :code, :expirationDate)";

    public static final String SELECT_USER_BY_CODE_QUERY =
            "SELECT * FROM households " +
                    "WHERE id = (" +
                    "SELECT user_id " +
                    "FROM two_factor_verification " +
                    "WHERE code = :code" +
                    ")";

    public static final String DELETE_CODE =
            "DELETE FROM two_factor_verification " +
                    "WHERE code = :code";

    public static final String SELECT_CODE_EXPIRATION_DATE =
            "SELECT expiration_date < now() " +
                    "AS is_expired FROM two_factor_verification " +
                    "WHERE code = :code";
}
