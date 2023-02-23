package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ajax.systems.company.hubs.utils.Assets;
import ajax.systems.company.hubs.utils.HubUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

@Component
public class AboutController implements Initializable{
	
	@FXML
	private Label versionLabel;
	
	@FXML
	private Label distributionLabel;
	
	@FXML
	private Label jreVersionLabel;
	
	@FXML
	private Label feedBackLabel;
	
	@FXML
	private GridPane infoContainer;
	
	@FXML
	private HBox feedbackContainer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		distributionLabel.setText(System.getProperty("os.name"));
		if(StringUtils.hasText(getClass().getPackage().getImplementationVersion())) {
			versionLabel.setText(getClass().getPackage().getImplementationVersion());
		}
		jreVersionLabel.setText(System.getProperty("java.version"));
		feedbackContainer.setManaged(false);
		feedbackContainer.setVisible(false);
		feedBackLabel.setContentDisplay(ContentDisplay.RIGHT);
		feedBackLabel.setGraphic(new ImageView(new Image(HubUtils.getAsset(Assets.FEEDBACK_COLORED_ICON.getPath()))));
		feedBackLabel.setOnMouseClicked(event -> {
			infoContainer.setVisible(false);
		});
	}

}
