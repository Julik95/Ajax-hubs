package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

@Component
public class GroupArmDialog implements Initializable{
	
	
	private SingleHubController singleHubController;
	
	@FXML
	private JFXComboBox<Group> gropsCombobox;
	
	@FXML
	private JFXButton disarmGroupButton;
	
	@FXML
	private JFXButton armGroupButton;
	
	@FXML
	private HBox optionsContainer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gropsCombobox.valueProperty().addListener(selectedGroup -> {
			optionsContainer.setVisible(true);
		});
		disarmGroupButton.setButtonType(ButtonType.FLAT);
		disarmGroupButton.setGraphic(new ImageView(new Image(getClass().getResource(Constants.DISARM_GREEN_ICON).toExternalForm())));
		armGroupButton.setButtonType(ButtonType.FLAT);
		armGroupButton.setGraphic(new ImageView(new Image(getClass().getResource(Constants.ARM_RED_ICON).toExternalForm())));
		disarmGroupButton.setOnMouseClicked(event ->{
			singleHubController.hideDialog();
			Group selectedGroup = gropsCombobox.getSelectionModel().getSelectedItem();
			if(selectedGroup != null) {
				new Thread(() -> {
					singleHubController.disarmHubGroup(selectedGroup.getId());
				}).start();
			}
		});
		armGroupButton.setOnMouseClicked(event ->{
			singleHubController.hideDialog();
			Group selectedGroup = gropsCombobox.getSelectionModel().getSelectedItem();
			if(selectedGroup != null) {
				new Thread(() -> {
					singleHubController.armHubGroup(selectedGroup.getId(), false);
				}).start();
			}
		});
	}
	
	public void setSingleHubController(SingleHubController singleHubController) {
		this.singleHubController = singleHubController;
	}
	
	protected void build() {
		if(singleHubController.getCompanyHub()!=null) {
			for(Group group : singleHubController.getCompanyHub().getGroups()) {
				gropsCombobox.getItems().add(group);
			}
		}
	}
	
	
	

}
