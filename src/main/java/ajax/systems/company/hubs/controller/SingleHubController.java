package ajax.systems.company.hubs.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXPopup.PopupHPosition;
import com.jfoenix.controls.JFXPopup.PopupVPosition;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.HubColor;
import ajax.systems.company.hubs.dto.hub.HubState;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.dto.hub.HubSubType;
import ajax.systems.company.hubs.exception.Response412ArmException;
import ajax.systems.company.hubs.model.CompanyHub;
import ajax.systems.company.hubs.model.DataSingleton;
import ajax.systems.company.hubs.utils.Constants;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


@Component
@Scope("prototype")
public class SingleHubController implements Initializable{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private CompanyHub companyHub;
	
	@FXML
	private ImageView hubImage;
	
	@FXML
	private Label hubName;
	
	private JFXPopup options;
	
	@FXML
	private VBox singleHubWrapper;
	
	@Autowired
	private MainController mainController;
	
	private JFXDialog dialog;

	
	private final Integer MAX_HUB_NAME_LEN = 32;
	private final String GROUP_DIALOG_HEADING_LABEL = "Selezionare l'area sulla quale effettuare l'operazione";
	private final String ALARM_ARMED_MESSAGE = "Impianto %s è stato inserito";
	private final String ALARM_NIGHT_MODE_ARMED_MESSAGE = "Impianto %s è stato inserito su modalità noturna";
	private final String ALARM_DISARMED_MESSAGE = "Impianto %s è stato diainserito";
	private final String HUB_TOOLTIP_TEXT = "Nome Hub: %s\nObject ID: %s";
	private final String ARM_ERRORS_HEADING = "Ci sono un paio di problemi";
	private final String ARM_GENERIC_ERROR_HEADING = "Errore imprevisto";
	private final String ARM_ERROR_MESSAHE = "%s presenta qualche problema. Vorresti ricontrollare o procedere lo stesso con l'inserimento?";
	private final String ARM_ERRORS_MESSAHE = "%s presenta un paio di problemi. Vorresti ricontrollare o procedere lo stesso con l'inserimento?";
	private final String CONTROL_TEXT = "Controllo";
	private final String IGNOR_TEXT = "Ignora ed inserisci";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		companyHub = (CompanyHub) DataSingleton.getInstance().getData();
		if(companyHub != null) {
			if(companyHub.getHubDetails() != null) {
				initImageForHub();
			}
			if(companyHub.getObjectInfoes() != null && companyHub.getObjectInfoes().length == 1) {
				hubName.setText(adjustHubName(companyHub.getObjectInfoes()[0].getName()));
				String tooltipTxt = String.format(HUB_TOOLTIP_TEXT,companyHub.getObjectInfoes()[0].getName(), companyHub.getObjectInfoes()[0].getId());
				Tooltip tooltip = new Tooltip(tooltipTxt);
				tooltip.setStyle("-fx-font-size: 12");
				Tooltip.install(hubImage, tooltip);
			}
			
		}
		hubImage.setOnMouseClicked(mouseEvent ->{
			if(options == null) {
				initPopup();	
			}
			logger.info("HUB {} actions pop-up", companyHub.getHubId());
			options.show(hubImage, PopupVPosition.TOP, PopupHPosition.LEFT, mouseEvent.getX(), mouseEvent.getY());
		});
		logger.info("HUB's View {} has been initialized", companyHub.getHubId());
	}
	
	protected Group[] getHubGroups() {
		return this.companyHub.getGroups();
	}
	
	protected void showGroupsDialog() {
		Node content = initGroupsDialogContent();
		if(content != null) {
			dialog = mainController.showDialog(initGroupsDialogHeading(), content);
		}
		
	}
	
	protected void hideDialog() {
		if(dialog != null) {
			dialog.close();
			dialog = null;
		}
	}
	
	private Node initGroupsDialogHeading() {
		Label label = new Label(GROUP_DIALOG_HEADING_LABEL);
		label.getStyleClass().add("dialog-title");
		label.setWrapText(false);
		return label;
	}
	
	private Node initGroupsDialogContent() {
		if(StringUtils.hasText(companyHub.getHubId())) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/group-arm-dialog.fxml"));
			try {
				VBox rootPopup = loader.load();
				GroupArmDialog controller = (GroupArmDialog) loader.getController();
				if(controller != null) {
					controller.setSingleHubController(this);
					controller.build();
				}
				return rootPopup;
			} catch (IOException e) {
				mainController.handleException(e, "Errore durante la visualizzazione del context menu.");
				e.printStackTrace();
				
			}
		}
		return null;
	}
	
	private String adjustHubName(String hubName) {
		if(StringUtils.hasText(hubName) && hubName.length() > MAX_HUB_NAME_LEN) {
			return hubName.substring(0, MAX_HUB_NAME_LEN) + "...";
		}
		return hubName;
	}
	
	private void initImageForHub() {
		if(companyHub.getHubDetails() != null ) {
			String hubColor = "black";
			String hubType = "";
			if(companyHub.getHubDetails().getColor() != null && HubColor.WHITE.equals(companyHub.getHubDetails().getColor())) {
				hubColor = "white";
			}
			if(companyHub.getHubDetails().getHubSubtype() == HubSubType.HUB_HYBRID || companyHub.getHubDetails().getHubSubtype() == HubSubType.HUB_HYBRID_4G) {
				hubType = "hybrid-";
			}
			String hubImagePath = "/assets/" + hubType + hubColor + "-hub.png";
			onHubStateChange();
			hubImage.setImage(new Image(getClass().getResource(hubImagePath).toExternalForm()));
		}else {
			
		}
	}
	
	private void onHubStateChange() {
		if(companyHub.getHubDetails().getState() != null) {
			hubImage.getStyleClass().clear();
			String sateCss = companyHub.getHubDetails().getState().getHubState().toLowerCase();
			hubImage.getStyleClass().add(sateCss);
		}
	}
	
	private void initPopup() {
		if(StringUtils.hasText(companyHub.getHubId())) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hub-context-popup.fxml"));
			try {
				VBox rootPopup = loader.load();
				HubContextMenuPopupController controller = (HubContextMenuPopupController) loader.getController();
				if(controller != null) {
					controller.setSingleHubController(this);
					controller.build();
				}
				options = new JFXPopup(rootPopup);
			} catch (IOException e) {
				mainController.handleException(e, "Errore durante la visualizzazione del context menu.");
				e.printStackTrace();
			}
		}
	}
	
	public CompanyHub getCompanyHub() {
		return companyHub;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	protected void hideOptions() {
		options.hide();
	}
	protected void diasArmHub() {
		try {
			mainController.controlHubState(companyHub.getHubId(), HubStateCmd.DISARM, true);
			Platform.runLater(() -> {
				companyHub.getHubDetails().setState(HubState.DISARMED);
				options = null;
				mainController.showSnackBar(String.format(ALARM_DISARMED_MESSAGE, companyHub.getObjectInfoes()[0].getName()));
				onHubStateChange();
			});
			logger.info("Diasarming the HUB {}", companyHub.getHubId());
		}catch(Exception ex) {
			logger.error("Error occured during disarming hub {}", companyHub.getHubId());
			mainController.handleException(ex, String.format("Error occured during disarming hub ", companyHub.getHubId()));
		}
	}
	protected void armHub(boolean ignoreProblems) {
		try {
			mainController.controlHubState(companyHub.getHubId(), HubStateCmd.ARM, ignoreProblems);
			Platform.runLater(() -> {
				companyHub.getHubDetails().setState(HubState.ARMED);
				options = null;
				mainController.showSnackBar(String.format(ALARM_ARMED_MESSAGE, companyHub.getObjectInfoes()[0].getName()));
				onHubStateChange();
			});
			logger.info("Arming the HUB {}", companyHub.getHubId());
		}catch(Response412ArmException ex412) {
			logger.warn("Errors occured during arming {}", ex412.getMessage());
			Platform.runLater(() -> {
				handleArmingException(ex412, "Impianto "+companyHub.getObjectInfoes()[0].getName(), ()->{armHub(true);}, false);
			});
		}catch(Exception ex) {
			logger.error("Error occured during arming hub {}", companyHub.getHubId());
			mainController.handleException(ex, String.format("Error occured during arming hub %s", companyHub.getHubId() ));
		}
	}
	
	protected void armNightMode(boolean ignoreProblems) {
		try {
			mainController.controlHubState(companyHub.getHubId(), HubStateCmd.NIGHT_MODE_ON, ignoreProblems);
			Platform.runLater(() -> {
				companyHub.getHubDetails().setState(HubState.NIGHT_MODE);
				options = null;
				mainController.showSnackBar(String.format(ALARM_NIGHT_MODE_ARMED_MESSAGE, companyHub.getObjectInfoes()[0].getName()));
				onHubStateChange();
			});
			logger.info("Arming night mode the HUB {}", companyHub.getHubId());
		}catch(Response412ArmException ex412) {
			logger.warn("Errors occured during arming night mode {}", ex412.getMessage());
			Platform.runLater(() -> {
				handleArmingException(ex412, "Impianto "+companyHub.getObjectInfoes()[0].getName(), ()->{armNightMode(true);}, true);
			});
		}catch(Exception ex) {
			logger.error("Error occured during arming hight mode hub {}", companyHub.getHubId());
			mainController.handleException(ex, String.format("Error occured during arming night mode hub %s", companyHub.getHubId() ));
		}
	}
	
	protected void controlGroupState(String groupId, HubStateCmd cmd, boolean ignoreProblems) {
		mainController.controlGroupState(companyHub.getHubId(), groupId, cmd, ignoreProblems);
	}

	private void handleArmingException(Response412ArmException ex412,String name, Runnable ingoreAction, boolean isNightMode) {
		String headingMessage;
		String bodyMessage = String.format(ARM_ERROR_MESSAHE, name);
		if(ex412 != null && ex412.getArmingError() != null) {
			if(ex412.getArmingError().getRejectionReasons().size() > 1) {
				headingMessage = ARM_ERRORS_HEADING;
				bodyMessage = String.format(ARM_ERRORS_MESSAHE, name);
			}else {
				headingMessage = ARM_ERRORS_HEADING;
			}
		}else {
			headingMessage = ARM_GENERIC_ERROR_HEADING;
		}
		JFXButton controlAction = new JFXButton(CONTROL_TEXT);
		controlAction.setGraphic(new ImageView(new Image(getClass().getResource(Constants.CHECK_BLUE_ICON).toExternalForm())));
		controlAction.setButtonType(ButtonType.FLAT);
		controlAction.getStyleClass().addAll("custom-button", "text-blue");
		JFXButton ignoreAction = new JFXButton(IGNOR_TEXT);
		ImageView ignoreButtonIcon = new ImageView(new Image(getClass().getResource(Constants.ARM_WHITE_ICON).toExternalForm()));
		String ignoreButtonStyleClass = "back-red";
		if(isNightMode) {
			ignoreButtonIcon.setImage(new Image(getClass().getResource(Constants.ARM_WHITE_ICON).toExternalForm()));
			ignoreButtonStyleClass = "back-violet";
		}
		ignoreAction.setGraphic(ignoreButtonIcon);
		ignoreAction.getStyleClass().addAll("custom-button", ignoreButtonStyleClass);
		ignoreAction.setButtonType(ButtonType.RAISED);
		JFXDialog dialog = mainController.handleArmException(headingMessage, bodyMessage, ignoreAction, controlAction);
		controlAction.setOnMouseClicked(event -> dialog.close());
		ignoreAction.setOnMouseClicked(event -> {
			dialog.close();
			new Thread(ingoreAction).start();
		});
		dialog.show();
	}
	
}
