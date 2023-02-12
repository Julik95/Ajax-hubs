package ajax.systems.company.hubs.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXSnackbarLayout;

import ajax.systems.company.hubs.dto.group.Group;
import ajax.systems.company.hubs.dto.hub.HubCompanyBinding;
import ajax.systems.company.hubs.dto.hub.HubDetail;
import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.dto.object.ObjectBriefInfo;
import ajax.systems.company.hubs.exception.Response4xxException;
import ajax.systems.company.hubs.exception.Response5xxException;
import ajax.systems.company.hubs.model.CompanyHub;
import ajax.systems.company.hubs.model.Credentials;
import ajax.systems.company.hubs.model.DataSingleton;
import ajax.systems.company.hubs.service.IAjaxGroupService;
import ajax.systems.company.hubs.service.IAjaxHubService;
import ajax.systems.company.hubs.service.IAjaxObjectService;
import ajax.systems.company.hubs.utils.Constants;
import ajax.systems.company.hubs.view.ViewName;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


@Component
public class MainFrameController extends MainController implements Initializable{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigurableApplicationContext springContext;
	
	@Autowired
	private IAjaxHubService hubService;
	
	@Autowired
	private IAjaxObjectService objectService;
	
	@Autowired
	private IAjaxGroupService groupService;
	
	@FXML
	private StackPane rootStackPane;
	
	@FXML
	private VBox loadingPane;
	
	@FXML
	private Label loadingText;
	
	private Credentials credentials;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		switchToView(ViewName.CREDENTIALS);
	}
	
	
	public void showLoadingPane() {
		loadingPane.setVisible(true);
		FadeTransition ft = new FadeTransition(Duration.millis(750), loadingPane);
	    ft.setFromValue(0);
	    ft.setToValue(0.75);
	    ft.setCycleCount(1);
	    ft.setAutoReverse(false);
	    ft.play();
	}
	
	public void hideLoadingPane(Runnable onFinished) {
		FadeTransition ft = new FadeTransition(Duration.millis(750), loadingPane);
	    ft.setFromValue(0.75);
	    ft.setToValue(0);
	    ft.setCycleCount(1);
	    ft.setAutoReverse(false);
	    ft.setOnFinished(event -> {
	    	loadingPane.setVisible(false);
	    	if(onFinished != null) {
	    		onFinished.run();
	    	}
	    });
	    ft.play();
	}
	
	public void showDialog(Node heading, Node content) {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXDialog dialog = new JFXDialog(rootStackPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
		dialogLayout.setHeading(heading);
		dialogLayout.setBody(content);
		dialog.setOnMouseClicked((event) ->{
			dialog.close();
		});
		dialog.show();
	}
	public void handleException(Exception ex, String title){
		JFXDialogLayout alertLayout = new JFXDialogLayout();
		Label heading = new Label(title);
		heading.getStyleClass().add("error-heading");
		Label messageTxt = new Label();
		messageTxt.getStyleClass().add("error-message");
		if(ex instanceof Response4xxException) {
			Response4xxException error = (Response4xxException) ex;
			if(error.getError() != null) {
				if(StringUtils.hasText(error.getError().getMessage())) {
					messageTxt.setText(error.getError().getMessage());
				}else if(StringUtils.hasText(error.getError().getError())) {
					messageTxt.setText(error.getError().getError());
				}
			}else {
				messageTxt.setText(error.getMessage());
			}
		}else if(ex instanceof Response5xxException) {
			Response5xxException error = (Response5xxException) ex;
			if(error.getError() != null) {
				if(StringUtils.hasText(error.getError().getMessage())) {
					messageTxt.setText(error.getError().getMessage());
				}else if(StringUtils.hasText(error.getError().getError())) {
					messageTxt.setText(error.getError().getError());
				}
			}else {
				messageTxt.setText(error.getMessage());
			}
		}else {
			messageTxt.setText(ex.getMessage());
		}
		alertLayout.setHeading(heading);
		alertLayout.setBody(new StackPane(messageTxt));
		JFXButton action = new JFXButton("Chiudi");
		alertLayout.setActions(action);
		JFXDialog dialog = new JFXDialog(rootStackPane, alertLayout, JFXDialog.DialogTransition.CENTER);
		action.setOnMouseClicked((event) ->{
			dialog.close();
		});
		hideLoadingPane(()->{ dialog.show(); });
	}
	
	
	private void retrieveDataFromAjax(Credentials credentials) {
		Thread retrieveDataExecutor = new Thread(() -> {
			if(credentials != null) {
				updateLoadingText("Start retrieving data from Ajax Systems");
				try {
					ResponseEntity<HubCompanyBinding[]> response = hubService.listHubsPerCompany(credentials);
					if(response != null){
						this.credentials = credentials;
						updateLoadingText(String.format("Retrieving %d hubs from Ajax Systems", response.getBody().length));
						List<CompanyHub> companyHubs = enrichHubDetails(credentials, response.getBody());
						if(companyHubs != null) {
							updateLoadingText(String.format("Hub'sdData have been retrieved, preparing UI", response.getBody().length));
							Platform.runLater(() -> {
								DataSingleton.getInstance().setData(companyHubs);
								switchToView(ViewName.HUBS_LIST);
							});
						}
						
					}
				}catch(Response5xxException ex) {
					ex.printStackTrace();
					handleException(ex, "Error occured during retrieving data from Ajax Systems");
				}catch(Response4xxException ex) {
					ex.printStackTrace();
					handleException(ex, "Error occured during retrieving data from Ajax Systems");
				}catch(Exception ex) {
					ex.printStackTrace();
					handleException(ex, "Error occured during retrieving data from Ajax Systems");
				}
			}else {
				
			}
		});
		retrieveDataExecutor.setName(Constants.GET_DATA_THREAD_NAME);
		retrieveDataExecutor.setDaemon(true);
		retrieveDataExecutor.start();
	}
	
	
	private List<CompanyHub> enrichHubDetails(Credentials credentials, HubCompanyBinding[] hubs) {
		List<CompanyHub> result = new ArrayList<>();
		for(HubCompanyBinding hubBinding : hubs) {
			updateLoadingText(String.format("Start retrieving details for hub: %s", hubBinding.getHubId()));
			CompanyHub companyHub = new CompanyHub();
			companyHub.setHubId(hubBinding.getHubId());
			ExecutorService hubDetailsRequestExecutor = Executors.newSingleThreadExecutor();
			Callable<ResponseEntity<HubDetail>> callableHubDetailsRequest = () -> {
				ResponseEntity<HubDetail> hubDetailResponse = hubService.getHubDetails(credentials, hubBinding.getHubId());
				return hubDetailResponse;
			};
			ExecutorService hubObjectsRequestExecutor = Executors.newSingleThreadExecutor();
			Callable<ResponseEntity<ObjectBriefInfo[]>> callableHubObjectsRequest = () -> {
				ResponseEntity<ObjectBriefInfo[]> hubObjectResponse = objectService.getAvailableObjectsOfHub(credentials, hubBinding.getHubId());
				return hubObjectResponse;
			};
			ExecutorService hubGroupsRequestExecutor = Executors.newSingleThreadExecutor();
			Callable<ResponseEntity<Group[]>> callableHubGroupsRequest = () -> {
				ResponseEntity<Group[]> groupResponse = groupService.listGroupsPerHub(credentials, hubBinding.getHubId());
				return groupResponse;
			};
			Future<ResponseEntity<HubDetail>> hubDetailsResponseFuture = hubDetailsRequestExecutor.submit(callableHubDetailsRequest);
			Future<ResponseEntity<ObjectBriefInfo[]>> hubObjectsResponseFuture = hubObjectsRequestExecutor.submit(callableHubObjectsRequest);
			Future<ResponseEntity<Group[]>> hubGroupsResponseFuture = hubGroupsRequestExecutor.submit(callableHubGroupsRequest);
			
			try {
				ResponseEntity<HubDetail> hubDetailsResponse = hubDetailsResponseFuture.get();
				ResponseEntity<ObjectBriefInfo[]> hubObjectsResponse = hubObjectsResponseFuture.get();
				ResponseEntity<Group[]> hubGroupsResponse = hubGroupsResponseFuture.get();
				if(hubDetailsResponse != null) {
					companyHub.setHubDetails(hubDetailsResponse.getBody());
				}
				if(hubGroupsResponse != null) {
					companyHub.setGroups(hubGroupsResponse.getBody());
				}
				if(hubObjectsResponse != null) {
					companyHub.setObjectInfoes(hubObjectsResponse.getBody());
				}
				
			}catch(Exception ex) {
				logger.warn("Error occured during parallel service call to Ajax Systems",ex);
				ex.printStackTrace();
			}
			hubDetailsRequestExecutor.shutdown();
			hubObjectsRequestExecutor.shutdown();
			hubGroupsRequestExecutor.shutdown();
			result.add(companyHub);
		}
		return result;
	}


	@Override
	void onCredentialsFilledUp(Credentials credentials) {
		showLoadingPane();
		logger.info("Credentials for Ajax API: has been setted");
		retrieveDataFromAjax(credentials);
	}
	
	@Override
	void updateLoadingText(String text) {
		Platform.runLater(() ->{
			loadingText.setText(text);
		});
	}


	@Override
	void switchToView(ViewName viewName) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(viewName.getName()));
		loader.setControllerFactory(springContext::getBean);
		try {
			Parent root = loader.load();
			if(root != null) {
				if(rootStackPane.getChildren().size() > 1) {
					rootStackPane.getChildren().remove(0);
				}
				rootStackPane.getChildren().add(0, root);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void showSnackBar(String message) {
		JFXSnackbar snackbar = new JFXSnackbar(rootStackPane);
		snackbar.setPrefWidth(450);
		snackbar.fireEvent(new SnackbarEvent( new JFXSnackbarLayout(message),Duration.millis(2500), null));
	}


	@Override
	public void controlHubState(String hubId, HubStateCmd cmd) {
		hubService.controlHubState(credentials, cmd, hubId);
		
	}


	@Override
	public void controlGroupState(String hubId, String groupId, HubStateCmd cmd) {
		groupService.controlHubState(credentials, cmd, hubId, groupId);
	}

}
