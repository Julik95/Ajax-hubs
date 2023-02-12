package ajax.systems.company.hubs.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewName {

	CREDENTIALS("/fxml/credentials.fxml"),
	HUBS_LIST("/fxml/hubs-list.fxml"),
	SINGLE_HUB("/fxml/single-hub.fxml");
	
	private String name;
}
