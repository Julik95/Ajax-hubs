package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

import ajax.systems.company.hubs.exception.Response4xxException;
import ajax.systems.company.hubs.exception.Response5xxException;
import ajax.systems.company.hubs.model.Credentials;
import ajax.systems.company.hubs.utils.Constants;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

@Component
public class CredentialsController extends ViewController implements Initializable{
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MainController mainController;
	
	@FXML
	private JFXTextField companyIdField;
	
	@FXML
	private JFXTextField companyTokenField;
	
	@FXML
	private JFXTextField apiKeyField;
	
	@FXML
	private JFXButton loginButtom;
	
	@FXML
	private VBox loadingPane;
	
	@FXML
	private Label loadingText;
	
	@FXML
	private StackPane rootStackPane;
	
	@FXML
	private BorderPane rootContentPane;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainController.setActiveView(this);
		companyIdField.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButtom.setDisable(!isCredentialsValid());
		});
		companyTokenField.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButtom.setDisable(!isCredentialsValid());
		});
		apiKeyField.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButtom.setDisable(!isCredentialsValid());
		});
	}

	
	public void handleLogin(){
		Credentials credentials = new Credentials();
		credentials.setCompanyId(companyIdField.getText().trim());
		credentials.setXApiKey(apiKeyField.getText().trim());
		credentials.setXCompanyToken(companyTokenField.getText().trim());
		mainController.setCredentials(credentials);
		logger.info("Credentials for Ajax API: has been setted");
		showLoadingPane();
		mainController.retrieveDataFromAjax();
	}
	
	private boolean isCredentialsValid() {
		return StringUtils.hasText(companyIdField.getText()) && companyIdField.getText().length() > Constants.MIN_CREDENTIAL_INPUT_LEN &&
				StringUtils.hasText(companyTokenField.getText()) && companyTokenField.getText().length() > Constants.MIN_CREDENTIAL_INPUT_LEN &&
				StringUtils.hasText(apiKeyField.getText()) && apiKeyField.getText().length() > Constants.MIN_CREDENTIAL_INPUT_LEN;
	}
	
	private void showLoadingPane() {
		loadingPane.setVisible(true);
		FadeTransition ft = new FadeTransition(Duration.millis(750), loadingPane);
	    ft.setFromValue(0);
	    ft.setToValue(0.75);
	    ft.setCycleCount(1);
	    ft.setAutoReverse(false);
	    ft.play();
	}
	
	private void hideLoadingPane(Runnable onFinished) {
		FadeTransition ft = new FadeTransition(Duration.millis(750), loadingPane);
	    ft.setFromValue(0.75);
	    ft.setToValue(0);
	    ft.setCycleCount(1);
	    ft.setAutoReverse(false);
	    ft.setOnFinished(event -> {
	    	loadingPane.setVisible(false);
	    	onFinished.run();
	    });
	    ft.play();
	}
	
	void handleException(Exception ex, String title){
		JFXDialogLayout alertLayout = new JFXDialogLayout();
		Label heading = new Label(title);
		heading.getStyleClass().add("error-heading");
		Label messageTxt = new Label();
		messageTxt.getStyleClass().add("error-message");
		if(ex instanceof Response4xxException) {
			Response4xxException error = (Response4xxException) ex;
			if(error.getError() != null) {
				if(StringUtils.hasText(error.getError().getMessage())) {
					messageTxt.setText(error.getError().getMessage());
				}else if(StringUtils.hasText(error.getError().getError())) {
					messageTxt.setText(error.getError().getError());
				}
			}else {
				messageTxt.setText(error.getMessage());
			}
		}else if(ex instanceof Response5xxException) {
			Response5xxException error = (Response5xxException) ex;
			if(error.getError() != null) {
				if(StringUtils.hasText(error.getError().getMessage())) {
					messageTxt.setText(error.getError().getMessage());
				}else if(StringUtils.hasText(error.getError().getError())) {
					messageTxt.setText(error.getError().getError());
				}
			}else {
				messageTxt.setText(error.getMessage());
			}
		}else {
			messageTxt.setText(ex.getMessage());
		}
		alertLayout.setHeading(heading);
		alertLayout.setBody(new StackPane(messageTxt));
		JFXButton action = new JFXButton("Chiudi");
		alertLayout.setActions(action);
		JFXDialog dialog = new JFXDialog(rootStackPane, alertLayout, JFXDialog.DialogTransition.CENTER);
		action.setOnMouseClicked((event) ->{
			dialog.close();
		});
		hideLoadingPane(()->{ dialog.show(); });
	}


	@Override
	void removeCurrentContent() {
		
	}
	
}
