package ajax.systems.company.hubs.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ajax.systems.company.hubs.dto.error.ArmingError;
import ajax.systems.company.hubs.dto.error.ErrorMessage;
import ajax.systems.company.hubs.exception.Response412ArmException;
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
		if(httpResponse.getStatusCode().is4xxClientError()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(httpResponse.getBody(), baos);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray()); 
			ErrorMessage errorMessage = parseError(bais, ErrorMessage.class);
			ArmingError armingError = parseError(bais, ArmingError.class);	
			if(errorMessage != null) {
				throw new Response4xxException(httpResponse.getStatusText(), httpResponse.getStatusCode(), errorMessage);
			}else if(armingError != null) {
				throw new Response412ArmException(armingError.getMessage(), armingError);
			}else {
				if(StringUtils.hasText(httpResponse.getStatusText()))
					throw new Response4xxException(httpResponse.getStatusText(), httpResponse.getStatusCode());
				else {
					throw new Response4xxException(httpResponse.getStatusCode().toString(), httpResponse.getStatusCode());
				}
			}
    	}else if(httpResponse.getStatusCode().is5xxServerError()) {
    		ObjectMapper mapper = new ObjectMapper();
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
	
	private <T> T parseError(ByteArrayInputStream body, Class<T> typeRef) {
		body.reset();
		ObjectMapper mapper = new ObjectMapper();
		try {
			T object = mapper.readValue(body, typeRef);
			return object;
		}catch(Exception ex) {
			return null;
		}
	}

}
