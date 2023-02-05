package ajax.systems.company.hubs.dto.error;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorMessage {
	
	
	private String message;
	private String messageId;
	private List<Error> errors;
	private Date timestamp;
	private Integer status;
	private String error;
	private String path;

}
