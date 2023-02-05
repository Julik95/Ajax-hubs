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

import ajax.systems.company.hubs.config.AjaxApiRestTemplate;
import ajax.systems.company.hubs.dto.object.ObjectBriefInfo;
import ajax.systems.company.hubs.model.Credentials;

@Component
public class AjaxObjectService extends AjaxAbstractService implements IAjaxObjectService{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String LIST_OBJECTS_OF_HUB_PATH = "/company/{companyId}/objects?hubId={hubIdValue}&enrich={enrichValue}";
	
	@Autowired
	private AjaxApiRestTemplate ajaxApiRestTemplate;

	@Override
	public ResponseEntity<ObjectBriefInfo[]> getAvailableObjectsOfHub(Credentials credentials, String hubId) {
		if(credentials != null) {
			logger.info("GET request to retrieve object list for hub: {}", hubId);
			HttpEntity<Void> requestEntity = new HttpEntity<>(getDefaultHeaders(credentials));
			Map<String, String> qeryParams = new HashMap<>();
			qeryParams.put("hubIdValue", hubId);
			qeryParams.put("companyId", credentials.getCompanyId());
			qeryParams.put("enrichValue", "false");
			ResponseEntity<ObjectBriefInfo[]> response = ajaxApiRestTemplate.getTemplate()
					.exchange(LIST_OBJECTS_OF_HUB_PATH, HttpMethod.GET, requestEntity, ObjectBriefInfo[].class, qeryParams);
			return response;
		}
		logger.warn("Credentials are empty or null during hub's object list request");
		return null;
	}

}
