package ajax.systems.company.hubs.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
public class AjaxApiRestTemplate {

	private RestTemplate restTemplate;
	private RestTemplateBuilder restTemplateBuilder;
	private RestTemplateConfigProperties restTemplateConfig;
	
	
	public AjaxApiRestTemplate(RestTemplateBuilder restTemplateBuilder, RestTemplateConfigProperties restTemplateConfig){
		this.restTemplateBuilder = restTemplateBuilder;
		this.restTemplateConfig = restTemplateConfig;
		this.restTemplate = build();
	}
	
	private RestTemplate build() {
		return restTemplateBuilder
				.uriTemplateHandler(new DefaultUriBuilderFactory(restTemplateConfig.getHostname()))
				.errorHandler(new RestTemplateErrorHandler())
				.setConnectTimeout(Duration.ofMillis(restTemplateConfig.getConnectionTimeout()))
				.build();
	}
	
	public RestTemplate getTemplate() {
		return restTemplate;
	}
}
