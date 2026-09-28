package concrete.goonies.bulkbusketbackend.exception;

/**
 * Project: SecureCapture
 * Created: 2026/05/25 21:01
 * Author: Scarra Luba
 */

public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
