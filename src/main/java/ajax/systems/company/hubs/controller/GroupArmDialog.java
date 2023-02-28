package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.group.State;
import ajax.systems.company.hubs.utils.Assets;
import ajax.systems.company.hubs.utils.HubUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

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
		optionsContainer.setManaged(false);
		gropsCombobox.valueProperty().addListener((ov, oldVal, newVal) -> {
			optionsContainer.getChildren().clear();
			if(newVal.getState() == State.ARMED) {
				optionsContainer.getChildren().add(disarmGroupButton);
			}else if(newVal.getState() == State.DISARMED) {
				optionsContainer.getChildren().add(armGroupButton);
			}
			optionsContainer.setManaged(true);
			optionsContainer.setVisible(true);
		});
		disarmGroupButton.setButtonType(ButtonType.FLAT);
		disarmGroupButton.setGraphic(new ImageView(new Image(HubUtils.getAsset(Assets.DISARM_GREEN_ICON))));
		armGroupButton.setButtonType(ButtonType.FLAT);
		armGroupButton.setGraphic(new ImageView(new Image(HubUtils.getAsset(Assets.ARM_RED_ICON))));
		disarmGroupButton.setOnMouseClicked(event ->{
			singleHubController.hideDialog();
			Group selectedGroup = gropsCombobox.getSelectionModel().getSelectedItem();
			if(selectedGroup != null) {
				selectedGroup.setState(State.DISARMED);
				new Thread(() -> {
					singleHubController.disarmHubGroup(selectedGroup.getId());
				}).start();
			}
		});
		armGroupButton.setOnMouseClicked(event ->{
			singleHubController.hideDialog();
			Group selectedGroup = gropsCombobox.getSelectionModel().getSelectedItem();
			if(selectedGroup != null) {
				selectedGroup.setState(State.ARMED);
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
		gropsCombobox.setCellFactory(new Callback<ListView<Group>, ListCell<Group>>(){
			public ListCell<Group> call(ListView<Group> p){
				ListCell<Group> cell = new ListCell<Group>() {
					protected void updateItem(Group item, boolean empty) {
						 super.updateItem(item, empty);
						 if(item != null) {
							 setText(item.getGroupName());
							 if(item.getState() == State.ARMED) {
								 setTextFill(Color.rgb(229, 57, 53));
							 }
							 if(item.getState() == State.DISARMED) {
								 setTextFill(Color.rgb(67, 160, 71));
							 }
						 }
					}
				};
				return cell;
			}
		});
		
	}
	

}
