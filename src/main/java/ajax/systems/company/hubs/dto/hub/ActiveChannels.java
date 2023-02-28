package ajax.systems.company.hubs.dto.hub;

import com.fasterxml.jackson.annotation.JsonFormat;


@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ActiveChannels {

	GSM, WIFI, ETHERNET;
	
}
