package concrete.goonies.bulkbusketbackend.configs.handlers;

import concrete.goonies.bulkbusketbackend.domain.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;

import static java.time.LocalTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Project: SecureCapture
 * Created: 2026/07/17 12:16
 * Author: Scarra Luba
 */
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(now().toString())
                .reason("Lack Auth")
                .status(HttpStatus.UNAUTHORIZED)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream,httpResponse);
        outputStream.flush();
    }
}
