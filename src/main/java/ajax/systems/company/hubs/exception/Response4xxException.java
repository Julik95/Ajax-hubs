package ajax.systems.company.hubs.exception;

import org.springframework.http.HttpStatus;

import ajax.systems.company.hubs.dto.error.ErrorMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response4xxException extends RuntimeException{

	private String message;
	private HttpStatus httpStatus;
	private ErrorMessage error;
	
	public Response4xxException(String message, HttpStatus httpStatus) {
		super(message);
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	public Response4xxException(String message, HttpStatus httpStatus, ErrorMessage error) {
		super(message);
		this.message = message;
		this.httpStatus = httpStatus;
		this.error = error;
	}
}
