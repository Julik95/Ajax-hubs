package ajax.systems.company.hubs.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ajax.systems.company.hubs.dto.error.ErrorMessage;
import ajax.systems.company.hubs.exception.Response4xxException;
import ajax.systems.company.hubs.exception.Response5xxException;

public class RestTemplateErrorHandler implements ResponseErrorHandler{
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
		return httpResponse.getStatusCode().is4xxClientError() ||  httpResponse.getStatusCode().is5xxServerError();
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		logger.error("Error occured {} during request to the Ajax API", httpResponse.getStatusCode().toString());
		ObjectMapper mapper = new ObjectMapper();
		if(httpResponse.getStatusCode().is4xxClientError()) {
			try {
				ErrorMessage errorMessage = mapper.readValue(httpResponse.getBody(), ErrorMessage.class);
				throw new Response4xxException(httpResponse.getStatusText(), httpResponse.getStatusCode(), errorMessage);
			}catch(Exception ex) {
				if(StringUtils.hasText(httpResponse.getStatusText()))
					throw new Response4xxException(httpResponse.getStatusText(), httpResponse.getStatusCode());
				else {
					throw new Response4xxException(httpResponse.getStatusCode().toString(), httpResponse.getStatusCode());
				}
			}
    	}else if(httpResponse.getStatusCode().is5xxServerError()) {
    		try {
				ErrorMessage errorMessage = mapper.readValue(httpResponse.getBody(), ErrorMessage.class);
				throw new Response5xxException(httpResponse.getStatusText(), httpResponse.getStatusCode(), errorMessage);
			}catch(Exception ex) {
				if(StringUtils.hasText(httpResponse.getStatusText()))
					throw new Response5xxException(httpResponse.getStatusText(), httpResponse.getStatusCode());
				else {
					throw new Response5xxException(httpResponse.getStatusCode().toString(), httpResponse.getStatusCode());
				}
			}
    	}
		
	}

}