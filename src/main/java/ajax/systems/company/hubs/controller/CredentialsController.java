package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import ajax.systems.company.hubs.model.Credentials;
import ajax.systems.company.hubs.utils.Constants;
import ajax.systems.company.hubs.utils.HubUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

@Component
public class CredentialsController implements Initializable{
	
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
	
	@FXML
	private JFXCheckBox ricordamiOption;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Credentials credentials = HubUtils.getCredentialsFromCache();
		if(credentials != null) {
			if(StringUtils.hasText(credentials.getCompanyId()))
				companyIdField.setText(credentials.getCompanyId());
			if(StringUtils.hasText(credentials.getXApiKey()))
				apiKeyField.setText(credentials.getXApiKey());
			if(StringUtils.hasText(credentials.getXCompanyToken()))
				companyTokenField.setText(credentials.getXCompanyToken());
			loginButtom.setDisable(!isCredentialsValid());
			ricordamiOption.setSelected(true);
		}
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
		if(ricordamiOption.isSelected()) {
			HubUtils.storeCredentialsToCache(credentials);
		}else {
			HubUtils.clearCredentialsCache();
		}
		mainController.onCredentialsFilledUp(credentials);
	}
	
	private boolean isCredentialsValid() {
		return StringUtils.hasText(companyIdField.getText()) && companyIdField.getText().length() > Constants.MIN_CREDENTIAL_INPUT_LEN &&
				StringUtils.hasText(companyTokenField.getText()) && companyTokenField.getText().length() > Constants.MIN_CREDENTIAL_INPUT_LEN &&
				StringUtils.hasText(apiKeyField.getText()) && apiKeyField.getText().length() > Constants.MIN_CREDENTIAL_INPUT_LEN;
	}
	
	
	
	

	
}
