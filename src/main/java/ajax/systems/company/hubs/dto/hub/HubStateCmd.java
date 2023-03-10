package ajax.systems.company.hubs.dto.hub;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HubStateCmd {

	ARM("ARM"),
	DISARM("DISARM"),
	NIGHT_MODE_ON("NIGHT_MODE_ON"),
	NIGHT_MODE_OFF("NIGHT_MODE_OFF");
	
	String comand;
	
	
}
