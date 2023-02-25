package ajax.systems.company.hubs;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import ajax.systems.company.hubs.AjaxHubsJavaFXApplication.StageReadyEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@Component
public class AjaxHubsInitializer implements ApplicationListener<StageReadyEvent>{

	@Override
	public void onApplicationEvent(StageReadyEvent event) {
		
		Label label = new Label("Hello World");
		Stage stage = event.getStage();
		if(stage!= null) {
			StackPane root = new StackPane(label);
			root.setPrefWidth(300);
			root.setPrefHeight(300);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			stage.show();
		}
	}

}
