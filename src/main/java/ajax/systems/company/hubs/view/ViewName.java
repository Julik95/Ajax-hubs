package ajax.systems.company.hubs.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewName {

	CREDENTIALS("/fxml/credentials.fxml"),
	HUBS_LIST("/fxml/hubs-list.fxml"),
	SINGLE_HUB("/fxml/single-hub.fxml"),
	ABOUT("/fxml/about.fxml"),
	MAIN_FRAME("/fxml/main-frame.fxml"),
	CONTEXT_MENU("/fxml/hub-context-popup.fxml"),
	GROUP_ARM_DIALOG("/fxml/group-arm-dialog.fxml");
	
	private String name;
}
