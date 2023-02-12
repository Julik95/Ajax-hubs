package ajax.systems.company.hubs.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ajax.systems.company.hubs.config.AjaxApiRestTemplate;
import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.ControlStateRequest;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.model.Credentials;


@Component
public class AjaxGroupService extends AjaxAbstractService implements IAjaxGroupService{
	
private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String LIST_GROUP_OF_HUB_PATH = "/company/{companyId}/hubs/{hubId}/groups";
	private String GROUP_CONTROL_STATE_PATH = "/company/{companyId}/hubs/{hubId}/groups/{groupId}/commands/arming";
	
	@Autowired
	private AjaxApiRestTemplate ajaxApiRestTemplate;

	@Override
	public ResponseEntity<Group[]> listGroupsPerHub(Credentials credentials, String hubId) {
		if(credentials != null) {
			logger.info("GET request to retrieve group list for hub: {}", hubId);
			HttpEntity<Void> requestEntity = new HttpEntity<>(getDefaultHeaders(credentials));
			Map<String, String> params = new HashMap<>();
			params.put("companyId", credentials.getCompanyId());
			params.put("hubId", hubId);
			ResponseEntity<Group[]> response = ajaxApiRestTemplate.getTemplate()
					.exchange(LIST_GROUP_OF_HUB_PATH, HttpMethod.GET, requestEntity, Group[].class, params);
			return response;
		}
		throw new UnsupportedOperationException("Credentials are null");
	}

	@Override
	public ResponseEntity<Void> controlHubState(Credentials credentials, HubStateCmd cmd, String hubId,
			String groupId) {
		if(StringUtils.hasText(hubId) && StringUtils.hasText(groupId) &&StringUtils.hasText(credentials.getCompanyId())) {
			logger.info("PUT request to change hub's {} group {} state to: {}", hubId, groupId, cmd.getComand());
			ControlStateRequest request = new ControlStateRequest(true, cmd);
			HttpEntity<ControlStateRequest> requestEntity = new HttpEntity<>(request, getDefaultHeaders(credentials));
			Map<String, String> params = new HashMap<>();
			params.put("companyId", credentials.getCompanyId());
			params.put("hubId", hubId);
			params.put("groupId", groupId);
			ResponseEntity<Void> response = ajaxApiRestTemplate.getTemplate()
					.exchange(GROUP_CONTROL_STATE_PATH, HttpMethod.PUT, requestEntity, Void.class, params);
			return response;
		}
		throw new UnsupportedOperationException("Some of the paraeters are empty or null");
	}

}
