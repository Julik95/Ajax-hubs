package ajax.systems.company.hubs.service;

import org.springframework.http.ResponseEntity;

import ajax.systems.company.hubs.dto.object.ObjectBriefInfo;
import ajax.systems.company.hubs.model.Credentials;

public interface IAjaxObjectService {
	
	ResponseEntity<ObjectBriefInfo[]> getAvailableObjectsOfHub(Credentials credentials, String hubId);
}
