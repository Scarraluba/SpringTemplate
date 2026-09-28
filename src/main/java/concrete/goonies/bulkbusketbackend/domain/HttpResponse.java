package concrete.goonies.bulkbusketbackend.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Project: SecureCapture
 * Created: 2026/05/28 13:15
 * Author: Scarra Luba
 */
@Data
@SuperBuilder
@JsonInclude(NON_DEFAULT)
public class HttpResponse {

    protected String timeStamp;
    protected String reason;
    protected String message;
    protected String devMessage;

    protected int statusCode;

    protected HttpStatus status;

    protected Map<?,?> data;


}
