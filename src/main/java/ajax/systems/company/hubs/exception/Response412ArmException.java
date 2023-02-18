package ajax.systems.company.hubs.exception;

import ajax.systems.company.hubs.dto.error.ArmingError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Response412ArmException extends RuntimeException{
	
	
	private String message;
	private ArmingError ArmingError; 
	
	
	public Response412ArmException(String message) {
		super(message);
	}

}
