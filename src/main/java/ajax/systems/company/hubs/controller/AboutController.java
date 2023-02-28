package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ajax.systems.company.hubs.utils.Assets;
import ajax.systems.company.hubs.utils.HubUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

@Component
public class AboutController implements Initializable{
	
	@FXML
	private Label versionLabel;
	
	@FXML
	private Label distributionLabel;
	
	@FXML
	private Label jreVersionLabel;
	
	@FXML
	private GridPane infoContainer;
	
	@FXML
	private ImageView logoImageView;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logoImageView.setImage(new Image(HubUtils.getAsset(Assets.LOGO_AJAX)));
		distributionLabel.setText(System.getProperty("os.name"));
		if(StringUtils.hasText(getClass().getPackage().getImplementationVersion())) {
			versionLabel.setText(getClass().getPackage().getImplementationVersion());
		}
		jreVersionLabel.setText(System.getProperty("java.version"));
		
	}

}
