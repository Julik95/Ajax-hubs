package ajax.systems.company.hubs.dto.group;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum State {

	ARMED("ARMED"),
	DISARMED("DISARMED");
	
	private String state;
}
