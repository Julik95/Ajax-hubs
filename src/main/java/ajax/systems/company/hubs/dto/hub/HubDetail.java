
package ajax.systems.company.hubs.dto.hub;

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
public class HubDetail {
	
	private String name;
	private HubState state;
	private HubColor color ;
	private Boolean groupsEnabled;
	private HubSubType hubSubtype;
	private HubFibraConnectionDetails fibraConnectionDetails;
	private List<ActiveChannels> activeChannels;
	

}
