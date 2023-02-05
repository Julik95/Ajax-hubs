package ajax.systems.company.hubs.model;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.HubDetail;
import ajax.systems.company.hubs.dto.object.ObjectBriefInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyHub {
	
	private String hubId;
	private HubDetail hubDetails;
	private ObjectBriefInfo[] objectInfoes;
	private Group[] groups;
	

}
