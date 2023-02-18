package ajax.systems.company.hubs.service;

import org.springframework.http.ResponseEntity;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.model.Credentials;

public interface IAjaxGroupService {
	
	ResponseEntity<Group[]> listGroupsPerHub(Credentials credentials, String hubId);
	ResponseEntity<Void> controlHubState(Credentials credentials, HubStateCmd cmd, String hubId, String groupId, boolean ignoreProblems);

}
