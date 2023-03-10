package ajax.systems.company.hubs.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	
	@Bean
	public AjaxApiRestTemplate getAjaxApiRestTemplate(RestTemplateBuilder restTemplateBuilder, RestTemplateConfigProperties restTemplateConfig) {
		return new AjaxApiRestTemplate(restTemplateBuilder, restTemplateConfig);
	}
}
