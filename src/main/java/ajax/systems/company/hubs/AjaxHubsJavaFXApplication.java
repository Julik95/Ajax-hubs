package ajax.systems.company.hubs;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;


public class AjaxHubsJavaFXApplication extends Application{
	
	private ConfigurableApplicationContext springContext;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.springContext.publishEvent(new StageReadyEvent(primaryStage));
		
	}

	
	public void init() {
		ApplicationContextInitializer<GenericApplicationContext> initializer = 
				ac -> {
					ac.registerBean(Application.class, () -> AjaxHubsJavaFXApplication.this);
					ac.registerBean(Parameters.class, this::getParameters);
					ac.registerBean(HostServices.class, this::getHostServices);
				};
		this.springContext = new SpringApplicationBuilder()
				.sources(Main.class)
				.initializers(initializer)
				.run(getParameters().getRaw().toArray(new String[0]));
	}
	
	public void stop() {
		this.springContext.close();
		Platform.exit();
	}
	
	
	class StageReadyEvent extends ApplicationEvent{
		
		private Stage stage;

		public StageReadyEvent(Stage stage) {
			super(stage);
			this.stage = stage;
			
		}
		
		public Stage getStage() {
			return stage;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
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
			stage.getIcons().add(new Image("file:assets/favico.png"));
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
	
	*/

}
