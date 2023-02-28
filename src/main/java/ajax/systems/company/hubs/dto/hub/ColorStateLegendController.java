package ajax.systems.company.hubs.dto.hub;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

import ajax.systems.company.hubs.utils.Assets;
import ajax.systems.company.hubs.utils.HubUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


@Component
public class ColorStateLegendController implements Initializable{
	
	
	@FXML
	private ImageView dotRed,dotViolet,dotOrange, dotGrey, dotGreen; 

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dotRed.setImage(new Image(HubUtils.getAsset(Assets.DOT_RED_ICON.getPath())));
		dotViolet.setImage(new Image(HubUtils.getAsset(Assets.DOT_VIOLET_ICON.getPath())));
		dotOrange.setImage(new Image(HubUtils.getAsset(Assets.DOT_ORANGE_ICON.getPath())));
		dotGrey.setImage(new Image(HubUtils.getAsset(Assets.DOT_GREY_ICON.getPath())));
		dotGreen.setImage(new Image(HubUtils.getAsset(Assets.DOT_GREEN_ICON.getPath())));
		
	}

}
