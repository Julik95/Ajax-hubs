package ajax.systems.company.hubs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import ajax.systems.company.hubs.utils.HubUtils;
import ajax.systems.company.hubs.view.JFXDraggableDecorator;
import ajax.systems.company.hubs.view.ViewName;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
public class AjaxHubsApplication extends Application{

	private Logger logger = LoggerFactory.getLogger(getClass());
	private ConfigurableApplicationContext springContext;
	private Parent root;
	
	
	public static void run(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		if(root != null) {
			JFXDraggableDecorator decorator = new JFXDraggableDecorator(stage, root, false, true, true);
	        decorator.setCustomMaximize(true);
	        decorator.getStylesheets().addAll(HubUtils.getAllStyleAssets());
	        Scene scene = new Scene(decorator);
			stage.setScene(scene);
			stage.setTitle("Ajax security system");
			stage.show();
		}
	}
	
	public void init() {
		springContext = SpringApplication.run(AjaxHubsApplication.class);
		root = getRoot(springContext);
	}
	
	public void stop() {
		springContext.close();
		Platform.exit();
	}
	
	private Parent getRoot(ConfigurableApplicationContext springContext) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewName.MAIN_FRAME.getName()));
			loader.setControllerFactory(springContext::getBean);
			Parent root = loader.load();
			return root;
		} catch (IOException e) {
			logger.error("Error occured during credentials fxml parsing", e);
			return null;
		}
	}
	
	

}
