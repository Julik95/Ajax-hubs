package ajax.systems.company.hubs.dto.hub;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HubState {
	
	DISARMED("DISARMED"),
	ARMED("ARMED"),
	NIGHT_MODE("NIGHT_MODE"),
	ARMED_NIGHT_MODE_ON("ARMED_NIGHT_MODE_ON"),
	ARMED_NIGHT_MODE_OFF("ARMED_NIGHT_MODE_OFF"),
	DISARMED_NIGHT_MODE_ON("DISARMED_NIGHT_MODE_ON"),
	DISARMED_NIGHT_MODE_OFF("DISARMED_NIGHT_MODE_OFF"),
	PARTIALLY_ARMED_NIGHT_MODE_ON("PARTIALLY_ARMED_NIGHT_MODE_ON"),
	PARTIALLY_ARMED_NIGHT_MODE_OFF("PARTIALLY_ARMED_NIGHT_MODE_OFF");
	
	private String hubState;

}
