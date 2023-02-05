package ajax.systems.company.hubs.service;

import org.springframework.http.ResponseEntity;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.model.Credentials;

public interface IAjaxGroupService {
	
	ResponseEntity<Group[]> listGroupsPerHub(Credentials credentials, String hubId);

}