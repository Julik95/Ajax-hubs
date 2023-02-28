package ajax.systems.company.hubs.dto.hub;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HubCompanyBindingState {

	PENDING_APPROVAL, APPROVED, PENDING_REMOVAL;
	
}
