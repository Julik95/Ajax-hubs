package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.HubColor;
import ajax.systems.company.hubs.dto.hub.HubDetail;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.model.CompanyHub;
import ajax.systems.company.hubs.model.DataSingleton;
import ajax.systems.company.hubs.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


@Component
public class SingleHubController implements Initializable{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@FXML
	private ImageView hubImage;
	
	@FXML
	private Label hubName;
	
	private JFXPopup options;
	
	@FXML
	private VBox singleHubWrapper;
	
	private MainController mainController;

	
	private final Integer MAX_HUB_NAME_LEN = 32;
	private final String GROUP_DIALOG_HEADING_LABEL = "Selezionare l'area sulla quale effettuare l'operazione";
	private final String ALARM_ARMED_MESSAGE = "Impianto %s è stato inserito";
	private final String ALARM_DISARMED_MESSAGE = "Impianto %s è stato disinserito";
	private final String GROUP_ARMED_MESSAGE = "L'area %s è stata inserita";
	private final String GROUP_DISARMED_MESSAGE = "L'area %s è stata disinserita";
	private final String HUB_TOOLTIP_TEXT = "Nome Hub: %s\nObject ID: %s";
	private final String AREA_SELECT_PROMT_TEXT  = "Selezionare Area";
	private final String ARM_LABEL = "Inserisci";
	private final String DISARM_LABEL = "Disinserisci";
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		CompanyHub companyHub = (CompanyHub) DataSingleton.getInstance().getData();
		if(companyHub != null) {
			if(companyHub.getHubDetails() != null) {
				initImageForHub(companyHub.getHubDetails());
			}
			if(companyHub.getObjectInfoes() != null && companyHub.getObjectInfoes().length == 1) {
				hubName.setText(adjustHubName(companyHub.getObjectInfoes()[0].getName()));
				String tooltipTxt = String.format(HUB_TOOLTIP_TEXT,companyHub.getObjectInfoes()[0].getName(), companyHub.getObjectInfoes()[0].getId());
				Tooltip tooltip = new Tooltip(tooltipTxt);
				tooltip.setStyle("-fx-font-size: 12");
				Tooltip.install(hubImage, tooltip);
				
				if(companyHub.getGroups() == null || companyHub.getGroups().length == 0 ||
						(companyHub.getHubDetails( )!= null && !companyHub.getHubDetails().getGroupsEnabled())) {
					initPopup(companyHub.getHubId(), companyHub.getObjectInfoes()[0].getName());
				}
			}
			
		}
		hubImage.setOnMouseClicked(mouseEvent ->{
			if(options != null) {
				logger.info("HUB {} actions pop-up", companyHub.getHubId());
				options.show(hubImage, PopupVPosition.TOP, PopupHPosition.LEFT, mouseEvent.getX(), mouseEvent.getY());
			}else {
				logger.info("HUB {} groups dialog", companyHub.getHubId());
				mainController.showDialog(initGroupsDialogHeading(), initGroupsDialogContent(companyHub.getHubId(), companyHub.getGroups()));
			}
		});
		logger.info("HUB's View {} has been initialized", companyHub.getHubId());
	}
	
	private Node initGroupsDialogHeading() {
		Label label = new Label(GROUP_DIALOG_HEADING_LABEL);
		label.getStyleClass().add("dialog-title");
		label.setWrapText(false);
		return label;
	}
	
	private Node initGroupsDialogContent(String hubId, Group[] groups) {
		VBox container = new VBox(20);
		container.setPadding(new Insets(20, 20, 20, 20));
		container.setAlignment(Pos.CENTER);
		HBox actionsContainer = new HBox(15);
		actionsContainer.setPadding(new Insets(20, 20, 0, 20));
		actionsContainer.setAlignment(Pos.CENTER);
		JFXComboBox<Group> groupsOptions = new JFXComboBox<>();
		groupsOptions.valueProperty().addListener(selectedGroup -> {
			if(!container.getChildren().contains(actionsContainer)) {
				container.getChildren().add(actionsContainer);
			}
		});
		groupsOptions.getStyleClass().add("custom-combobox");
		groupsOptions.setMinWidth(200);
		groupsOptions.setPromptText(AREA_SELECT_PROMT_TEXT);
		for(Group group : groups) {
			groupsOptions.getItems().add(group);
		}
		JFXButton disarm = new JFXButton(DISARM_LABEL);
		disarm.setOnMouseClicked(event ->{
			Group selectedGroup = groupsOptions.getSelectionModel().getSelectedItem();
			if(selectedGroup != null) {
				try {
					mainController.controlGroupState(hubId, selectedGroup.getId(), HubStateCmd.DISARM);
					mainController.showSnackBar(String.format(GROUP_DISARMED_MESSAGE, selectedGroup.getGroupName()));
					logger.info("Disarming the group {}", selectedGroup.getId());
				}catch(Exception ex) {
					logger.error("Error occured during disarming hub's {} group {}", hubId, selectedGroup.getId());
					mainController.handleException(ex, String.format("Error occured during disarming hub's %s group %s", hubId, selectedGroup.getId()));
				}
				
			}
		});
		disarm.setButtonType(ButtonType.RAISED);
		disarm.setGraphic(new ImageView(new Image(getClass().getResource(Constants.DISARM_WHITE_ICON).toExternalForm())));
		disarm.getStyleClass().addAll("custom-button", "back-green");
		JFXButton arm = new JFXButton(ARM_LABEL);
		arm.getStyleClass().addAll("custom-button", "back-red");
		arm.setButtonType(ButtonType.RAISED);
		arm.setGraphic(new ImageView(new Image(getClass().getResource(Constants.ARM_WHITE_ICON).toExternalForm())));
		arm.setOnMouseClicked(event ->{
			Group selectedGroup = groupsOptions.getSelectionModel().getSelectedItem();
			if(selectedGroup != null) {
				try {
					mainController.controlGroupState(hubId, selectedGroup.getId(), HubStateCmd.ARM);
					mainController.showSnackBar(String.format(GROUP_ARMED_MESSAGE, selectedGroup.getGroupName()));
					logger.info("Arming the group {}", selectedGroup.getId());
				}catch(Exception ex) {
					logger.error("Error occured during arming hub's {} group {}", hubId, selectedGroup.getId());
					mainController.handleException(ex, String.format("Error occured during arming hub's %s group %s", hubId, selectedGroup.getId()));
				}
				
			}
		});
		actionsContainer.getChildren().addAll(disarm, arm);
		container.getChildren().add(groupsOptions);
		return container;
	}
	
	private String adjustHubName(String hubName) {
		if(StringUtils.hasText(hubName) && hubName.length() > MAX_HUB_NAME_LEN) {
			return hubName.substring(0, MAX_HUB_NAME_LEN) + "...";
		}
		return hubName;
	}
	
	private void initImageForHub(HubDetail hubDetail) {
		Image image = new Image(getClass().getResource(Constants.BLACK_HUB_IMAGE).toExternalForm());
		if(hubDetail != null ) {
			if(hubDetail.getColor() != null && HubColor.WHITE.equals(hubDetail.getColor())) {
				image = new Image(getClass().getResource(Constants.WHITE_HUB_IMAGE).toExternalForm());
			}
			if(hubDetail.getState() != null) {
				String sateCss = hubDetail.getState().getHubState().toLowerCase();
				hubImage.getStyleClass().add(sateCss);
			}
			hubImage.setImage(image);
		}
	}
	
	private void initPopup(String hubId, String objectName) {
		if(StringUtils.hasText(hubId)) {
			VBox container = new VBox();
			Label armOption = new Label(ARM_LABEL);
			armOption.setGraphic(new ImageView(new Image(getClass().getResource(Constants.ARM_RED_ICON).toExternalForm())));
			armOption.getStyleClass().addAll("menu-option", "arm-option");
			Label disarmOption = new Label(DISARM_LABEL);
			disarmOption.getStyleClass().addAll("menu-option", "disarm-option");
			disarmOption.setGraphic(new ImageView(new Image(getClass().getResource(Constants.DISARM_GREEN_ICON).toExternalForm())));
			armOption.setOnMouseClicked((event) ->{
				options.hide();
				try {
					mainController.controlHubState(hubId, HubStateCmd.ARM);
					mainController.showSnackBar(String.format(ALARM_ARMED_MESSAGE, objectName));
					logger.info("Arming the HUB {}", hubId);
				}catch(Exception ex) {
					logger.error("Error occured during arming hub {}", hubId);
					mainController.handleException(ex, String.format("Error occured during arming hub %s", hubId ));
				}
				
			});
			disarmOption.setOnMouseClicked((event) ->{
				options.hide();
				try {
					mainController.controlHubState(hubId, HubStateCmd.DISARM);
					mainController.showSnackBar(String.format(ALARM_DISARMED_MESSAGE, objectName));
					logger.info("Diasarming the HUB {}", hubId);
				}catch(Exception ex) {
					logger.error("Error occured during disarming hub {}", hubId);
					mainController.handleException(ex, String.format("Error occured during disarming hub ", hubId));
				}
				
			});
			container.getChildren().addAll(armOption, disarmOption);
			options = new JFXPopup(container);
		}
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

}
