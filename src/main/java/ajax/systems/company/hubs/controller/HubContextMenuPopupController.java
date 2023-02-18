package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ajax.systems.company.hubs.dto.hub.HubState;
import ajax.systems.company.hubs.utils.Constants;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

@Component
public class HubContextMenuPopupController implements Initializable{
	
	
	@FXML
	private Label armOption;
	
	@FXML
	private Label armNightModeOption;
	
	@FXML
	private Label disarmOption;
	
	@FXML
	private Label manageGroupsOption;
	
	private SingleHubController singleHubController;
	
	@FXML
	private Pane containerPane;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ImageView armRedIcon = new ImageView(new Image(getClass().getResource(Constants.ARM_RED_ICON).toExternalForm()));
		ImageView armLightRedIcon = new ImageView(new Image(getClass().getResource(Constants.ARM_LIGHT_RED_ICON).toExternalForm()));
		armOption.graphicProperty().bind(Bindings.when(armOption.hoverProperty()).then(armLightRedIcon).otherwise(armRedIcon));
		armOption.setOnMouseClicked(event -> {
			singleHubController.hideOptions();
			new Thread(() -> {
				singleHubController.armHub(false);
			}).start();
		});
		ImageView armNightVioletIcon = new ImageView(new Image(getClass().getResource(Constants.ARM_NIGHT_VIOLET_ICON).toExternalForm()));
		ImageView armNightLightVioletIcon = new ImageView(new Image(getClass().getResource(Constants.ARM_NIGHT_LIGHT_VIOLET_ICON).toExternalForm()));
		armNightModeOption.graphicProperty().bind(Bindings.when(armNightModeOption.hoverProperty()).then(armNightLightVioletIcon).otherwise(armNightVioletIcon));
		armNightModeOption.setOnMouseClicked(event -> {
			singleHubController.hideOptions();
			new Thread(() -> {
				singleHubController.armNightMode(false);
			}).start();
		});
		ImageView disarmGreenIcon = new ImageView(new Image(getClass().getResource(Constants.DISARM_GREEN_ICON).toExternalForm()));
		ImageView disarmLightGreenIcon = new ImageView(new Image(getClass().getResource(Constants.DISARM_LIGHT_GREEN_ICON).toExternalForm()));
		disarmOption.graphicProperty().bind(Bindings.when(disarmOption.hoverProperty()).then(disarmLightGreenIcon).otherwise(disarmGreenIcon));
		disarmOption.setOnMouseClicked(event -> {
			singleHubController.hideOptions();
			new Thread(() -> {
				singleHubController.disarmHub();
			}).start();
		});
		ImageView manageGroupsOrangeIcon = new ImageView(new Image(getClass().getResource(Constants.GROUPS_ORANGE_ICON).toExternalForm()));
		ImageView manageGroupsLightOrangeIcon = new ImageView(new Image(getClass().getResource(Constants.GROUPS_LIGHT_ORANGE_ICON).toExternalForm()));
		manageGroupsOption.graphicProperty().bind(Bindings.when(manageGroupsOption.hoverProperty()).then(manageGroupsLightOrangeIcon).otherwise(manageGroupsOrangeIcon));
		manageGroupsOption.setOnMouseClicked(event -> {
			singleHubController.hideOptions();
			singleHubController.showGroupsDialog();
		});

	}
	
	protected void build() {
		if(singleHubController.getCompanyHub() != null) {
			if(singleHubController.getCompanyHub().getGroups() == null || singleHubController.getCompanyHub().getGroups().length == 0 || 
					singleHubController.getCompanyHub().getHubDetails() == null || !singleHubController.getCompanyHub().getHubDetails().getGroupsEnabled()) {
				containerPane.getChildren().remove(manageGroupsOption);
			}
			if(singleHubController.getCompanyHub().getHubDetails() != null) {
				HubState state = singleHubController.getCompanyHub().getHubDetails().getState();
				switch(state) {
					case ARMED_NIGHT_MODE_ON:
						containerPane.getChildren().remove(armOption);
						containerPane.getChildren().remove(armNightModeOption);
						break;
					case ARMED:
					case ARMED_NIGHT_MODE_OFF:
						containerPane.getChildren().remove(armOption);
						break;
					case DISARMED:
					case DISARMED_NIGHT_MODE_OFF:
						containerPane.getChildren().remove(disarmOption);
						break;
					case NIGHT_MODE:
					case DISARMED_NIGHT_MODE_ON:
						containerPane.getChildren().remove(armNightModeOption);
						break;
				}
			}
		}
		
	}
	
	public void setSingleHubController(SingleHubController singleHubController) {
		this.singleHubController = singleHubController;
	}

}
