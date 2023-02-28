package ajax.systems.company.hubs.model;

import java.io.Serializable;

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
public class Credentials implements Serializable{
	
	private static final long serialVersionUID = 2939276016759597586L;
	
	
	private String XCompanyToken;
	private String CompanyId;
	private String XApiKey;

}
