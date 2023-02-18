package ajax.systems.company.hubs.dto.hub;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HubSubType {

	HUB_HYBRID_4G("HUB_HYBRID_4G"),
	HUB_HYBRID("HUB_HYBRID"),
	HUB_2_PLUS("HUB_2_PLUS"),
	HUB_2_4G("HUB_2_4G"),
	HUB_2("HUB_2"),
	HUB_PLUS("HUB_PLUS"),
	HUB("HUB");
	
	private String type;
}
