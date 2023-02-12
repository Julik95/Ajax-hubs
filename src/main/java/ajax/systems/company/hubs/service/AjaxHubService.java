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
import ajax.systems.company.hubs.dto.hub.ControlStateRequest;
import ajax.systems.company.hubs.dto.hub.HubCompanyBinding;
import ajax.systems.company.hubs.dto.hub.HubDetail;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.model.Credentials;

@Component
public class AjaxHubService extends AjaxAbstractService implements IAjaxHubService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private String LIST_HUBS_PATH = "/company/{companyId}/hubs";
	private String HUB_DETAILS_PATH = "/company/{companyId}/hubs/{hubId}";
	private String HUB_CONTROL_STATE_PATH = "/company/{companyId}/hubs/{hubId}/commands/arming";

	@Autowired
	private AjaxApiRestTemplate ajaxApiRestTemplate;
	
	@Override
	public ResponseEntity<HubCompanyBinding[]> listHubsPerCompany(Credentials credentials) {
		if(credentials != null) {
			logger.info("GET request to retrieve hub list for company: {}", credentials.getCompanyId());
			HttpEntity<Void> requestEntity = new HttpEntity<>(getDefaultHeaders(credentials));
			Map<String, String> params = new HashMap<>();
			params.put("companyId", credentials.getCompanyId());
			ResponseEntity<HubCompanyBinding[]> response = ajaxApiRestTemplate.getTemplate()
					.exchange(LIST_HUBS_PATH, HttpMethod.GET, requestEntity, HubCompanyBinding[].class, params);
			return response;
		}
		logger.warn("Credentials are empty or null during hub list request");
		return null;
	}

	@Override
	public ResponseEntity<HubDetail> getHubDetails(Credentials credentials, String hubId) {
		if(credentials != null && StringUtils.hasText(hubId)) {
			logger.info("GET request to retrieve hub details for hub: {}", hubId);
			HttpEntity<Void> requestEntity = new HttpEntity<>(getDefaultHeaders(credentials));
			Map<String, String> params = new HashMap<>();
			params.put("companyId", credentials.getCompanyId());
			params.put("hubId", hubId);
			ResponseEntity<HubDetail> response = ajaxApiRestTemplate.getTemplate()
					.exchange(HUB_DETAILS_PATH, HttpMethod.GET, requestEntity, HubDetail.class, params);
			return response;
		}
		logger.warn("Credentials or hub ID is empty or null during hub details request");
		return null;
	}

	@Override
	public ResponseEntity<Void> controlHubState(Credentials credentials, HubStateCmd cmd, String hubId) {
		if(StringUtils.hasText(hubId) && StringUtils.hasText(credentials.getCompanyId())) {
			logger.info("PUT request to change hub's {} state to: {}", hubId, cmd.getComand());
			ControlStateRequest request = new ControlStateRequest(true, cmd);
			HttpEntity<ControlStateRequest> requestEntity = new HttpEntity<>(request, getDefaultHeaders(credentials));
			Map<String, String> params = new HashMap<>();
			params.put("companyId", credentials.getCompanyId());
			params.put("hubId", hubId);
			ResponseEntity<Void> response = ajaxApiRestTemplate.getTemplate()
					.exchange(HUB_CONTROL_STATE_PATH, HttpMethod.PUT, requestEntity, Void.class, params);
			return response;
		}
		return null;
	}

}
