package ajax.systems.company.hubs.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.jfoenix.controls.JFXTextField;

import ajax.systems.company.hubs.dto.object.ObjectBriefInfo;
import ajax.systems.company.hubs.model.CompanyHub;
import ajax.systems.company.hubs.model.DataSingleton;
import ajax.systems.company.hubs.utils.Assets;
import ajax.systems.company.hubs.utils.HubUtils;
import ajax.systems.company.hubs.view.ViewName;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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
	private ImageView refreshIcon,aboutIcon, paletteIcon;
	
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
		paletteIcon.setImage(new Image(HubUtils.getAsset(Assets.PALETTE_WHITE_ICON)));
		paletteIcon.setOnMouseClicked(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewName.COLOR_STATE_LEGEND.getName()));
				loader.setControllerFactory(springContext::getBean);
				Parent root = loader.load();
				if(root != null) {
					Label title = new Label("Legenda stai/colori impianto");
					title.getStyleClass().addAll("title-dialog", "font-18");
					mainController.getDialog(title, root).show();
				}
			}catch(IOException ex) {
				mainController.handleException(ex,"Error occured during loading color staet legend view");
				logger.warn("Error occured during loading color staet legend view");
				ex.printStackTrace();
			}
		});
		aboutIcon.setImage(new Image(HubUtils.getAsset(Assets.INFO_WHITE_ICON)));
		aboutIcon.setOnMouseClicked(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewName.ABOUT.getName()));
				loader.setControllerFactory(springContext::getBean);
				Parent root = loader.load();
				if(root != null) {
					Label abountTitle = new Label("Informazioni");
					abountTitle.getStyleClass().addAll("title-dialog", "font-18");
					mainController.getDialog(abountTitle, root).show();
				}
			}catch(IOException ex) {
				mainController.handleException(ex,"Error occured during loading about view");
				logger.warn("Error occured during loading about view");
				ex.printStackTrace();
			}
		});
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
			ex.printStackTrace();
		}
	}
	
	private void initTopPanel(List<CompanyHub> hubs) {
		refreshIcon.setImage(new Image(HubUtils.getAsset(Assets.REFRESH_WHITE_ICON)));
		refreshIcon.setOnMouseClicked(event -> {
			Thread refreshWorker = new Thread(() -> {
				Platform.runLater(()->{mainController.showLoadingPane();}); 
				List<CompanyHub> companyHubs = mainController.getCompanyHubs();
				if(!CollectionUtils.isEmpty(companyHubs)) {
					List<Node> toRemove = new ArrayList<>();
					for(Node child : hubsListRoot.getChildren()) {
						SingleHubController controller = (SingleHubController) child.getUserData();
						if(controller != null) {
							String hubId = controller.getCompanyHub().getHubId();
							if(StringUtils.hasText(hubId)) {
								Optional<CompanyHub> hub = companyHubs.stream()
										.filter(companyHub -> companyHub!=null && hubId.equalsIgnoreCase(companyHub.getHubId()))
										.findFirst();
								if(hub.isPresent()) {
									controller.refreshCompanyHub(hub.get());
									companyHubs.remove(hub.get());
								}else {
									toRemove.add(child);
								}
							}
						}
					}
					if(toRemove.size() > 0) {
						Platform.runLater(() -> {
							hubsListRoot.getChildren().removeAll(toRemove);
						});
					}
					if(companyHubs.size() > 0) {
						Platform.runLater(()-> {});Platform.runLater(()-> {
							companyHubs.forEach(companyHub -> {
								initHub(companyHub);
							});
						});
					}
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
								SingleHubController hubController = (SingleHubController) node.getUserData();
								CompanyHub hub = hubController.getCompanyHub();
								if(hub != null) {
									if(hub.getHubDetails() != null) {
										return (StringUtils.hasLength(hub.getHubDetails().getName()) && hub.getHubDetails().getName().toLowerCase().startsWith(newValue.toLowerCase()));
									}
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
