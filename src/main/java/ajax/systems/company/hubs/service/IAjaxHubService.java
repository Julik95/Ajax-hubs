package ajax.systems.company.hubs.service;

import org.springframework.http.ResponseEntity;

import ajax.systems.company.hubs.dto.hub.HubCompanyBinding;
import ajax.systems.company.hubs.dto.hub.HubDetail;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.model.Credentials;

public interface IAjaxHubService {
	
	ResponseEntity<HubCompanyBinding[]> listHubsPerCompany(Credentials credentials);
	ResponseEntity<HubDetail> getHubDetails(Credentials credentials, String hubId);
	ResponseEntity<Void> controlHubState(Credentials credentials, HubStateCmd cmd, String hubId, boolean ignoreProblems);

}
