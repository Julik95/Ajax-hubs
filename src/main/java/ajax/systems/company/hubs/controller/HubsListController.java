package ajax.systems.company.hubs.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jfoenix.controls.JFXTextField;

import ajax.systems.company.hubs.dto.object.ObjectBriefInfo;
import ajax.systems.company.hubs.model.CompanyHub;
import ajax.systems.company.hubs.model.DataSingleton;
import ajax.systems.company.hubs.utils.Constants;
import ajax.systems.company.hubs.view.ViewName;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

@Component
public class HubsListController implements Initializable{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurableApplicationContext springContext;
	
	@Autowired
	private MainController mainController;
	
	@FXML
	private Pane hubsListRoot;
	
	@FXML
	private ImageView refreshIcon;
	
	@FXML
	private JFXTextField searchHubField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainController.hideLoadingPane(null);
		if(DataSingleton.getInstance().getData()!= null) {
			List<CompanyHub> hubs = (List<CompanyHub>) DataSingleton.getInstance().getData();
			hubs.forEach(companyHub -> {
				initHub(companyHub);
			});
			initTopPanel(hubs);
		}
		logger.info("Hub's list has been initialized");
		
	}

	private void initHub(CompanyHub companyHub) {
		logger.info("Inizializzo hub's UI: {}", companyHub.getHubId());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewName.SINGLE_HUB.getName()));
			loader.setControllerFactory(springContext::getBean);
			DataSingleton.getInstance().setData(companyHub);
			Parent hubPane = loader.load();
			SingleHubController controller = (SingleHubController) loader.getController();
			hubPane.setUserData(controller);
			hubsListRoot.getChildren().add(hubPane);
		} catch (IOException ex) {
			mainController.handleException(ex, String.format("Error occured during loading HUB's()%s view", companyHub.getHubId()));
			logger.warn("Error occured during loading HUB's({}) view", companyHub.getHubId());
		}
	}
	
	private void initTopPanel(List<CompanyHub> hubs) {
		refreshIcon.setImage(new Image(getClass().getResource(Constants.REFRESH_WHITE_ICON).toExternalForm()));
		refreshIcon.setOnMouseClicked(event -> {
			Thread refreshWorker = new Thread(() -> {
				Platform.runLater(()->{mainController.showLoadingPane();}); 
				List<CompanyHub> companyHubs = mainController.getCompanyHubs();
				if(companyHubs != null) {
					hubsListRoot.getChildren().stream().forEach(child -> {
						SingleHubController controller = (SingleHubController) child.getUserData();
						if(controller != null) {
							String hubId = controller.getCompanyHub().getHubId();
							Optional<CompanyHub> hub = companyHubs.stream()
									.filter(companyHub -> companyHub!=null && hubId.equalsIgnoreCase(companyHub.getHubId()))
									.findFirst();
							hub.ifPresent(companyHub -> {
								controller.refreshCompanyHub(companyHub);
							});
						}
					});
					
				}
				Platform.runLater(()->{mainController.hideLoadingPane(null);}); 
			});
			refreshWorker.setDaemon(true);
			refreshWorker.start();
		});
		ObservableList<Node> childNodesOrigin = FXCollections.observableArrayList(hubsListRoot.getChildren());
		searchHubField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.length() > 3 ) {
				hubsListRoot.getChildren().clear();
				hubsListRoot.getChildren().addAll(childNodesOrigin.stream()
						.filter(node -> {
							if(node.getUserData() != null) {
								CompanyHub hub = (CompanyHub) node.getUserData();
								if(hub.getObjectInfoes() != null && hub.getObjectInfoes().length == 1) {
									ObjectBriefInfo object = hub.getObjectInfoes()[0];
									return (StringUtils.hasLength(object.getName()) && object.getName().toLowerCase().startsWith(newValue.toLowerCase())) || 
											(StringUtils.hasLength(object.getId()) && object.getId().toLowerCase().startsWith(newValue.toLowerCase()));
								}
							}
							return false;
				}).collect(Collectors.toList()));
			}else if("".equals(newValue)) {
				hubsListRoot.getChildren().clear();
				hubsListRoot.getChildren().addAll(childNodesOrigin);
			}
		});
	}
}
