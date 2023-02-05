package ajax.systems.company.hubs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="rest.conf")
public class RestTemplateConfigProperties {
	
	private Integer connectionTimeout;
	private String hostname;

}
