package ajax.systems.company.hubs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

@SpringBootApplication
public class AjaxHubsApplication extends Application{

	private Logger logger = LoggerFactory.getLogger(getClass());
	private ConfigurableApplicationContext springContext;
	private Scene scene;
	
	public static void run(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		if(scene != null) {
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setTitle("Ajax security system");
			stage.getIcons().add(new Image("file:imgs/favico.png"));
			stage.show();
		}
	}
	
	public void init() {
		springContext = SpringApplication.run(AjaxHubsApplication.class);
		this.scene = initScene(springContext);
	}
	
	public void stop() {
		springContext.close();
		Platform.exit();
	}
	
	private Scene initScene(ConfigurableApplicationContext springContext) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/credentials.fxml"));
			loader.setControllerFactory(springContext::getBean);
			Parent root = loader.load();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
			return scene;
		} catch (IOException e) {
			logger.error("Error occured during credentials fxml parsing", e);
			return null;
		}
	}

}
