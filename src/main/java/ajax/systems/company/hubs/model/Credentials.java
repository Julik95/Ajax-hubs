package ajax.systems.company.hubs.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class Credentials {
	
	private String XCompanyToken;
	private String CompanyId;
	private String XApiKey;

}
