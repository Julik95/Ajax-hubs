package ajax.systems.company.hubs.dto.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Group {
	
	private String id;
	private String groupName;
	private State state;
	
	

    public String toString() {
    	return groupName;
    }
}
