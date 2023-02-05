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
import ajax.systems.company.hubs.dto.hub.HubCompanyBinding;
import ajax.systems.company.hubs.dto.hub.HubDetail;
import ajax.systems.company.hubs.model.Credentials;

@Component
public class AjaxHubService extends AjaxAbstractService implements IAjaxHubService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	private String LIST_HUBS_PATH = "/company/{companyId}/hubs";
	private String HUB_DETAILS_PATH = "/company/{companyId}/hubs/{hubId}";

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

}
