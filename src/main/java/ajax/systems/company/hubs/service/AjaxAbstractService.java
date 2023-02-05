package ajax.systems.company.hubs.service;

import org.springframework.http.HttpHeaders;

import ajax.systems.company.hubs.model.Credentials;
import ajax.systems.company.hubs.utils.Constants;

public abstract class AjaxAbstractService{

	HttpHeaders getDefaultHeaders(Credentials credentials) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(Constants.X_API_KEY_HEADER, credentials.getXApiKey());
		headers.set(Constants.X_COMPANY_TOKEN_HEADER, credentials.getXCompanyToken());
		return headers;
	}
}
