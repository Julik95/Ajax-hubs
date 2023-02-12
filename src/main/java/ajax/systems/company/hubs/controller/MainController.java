package ajax.systems.company.hubs.controller;

import ajax.systems.company.hubs.dto.hub.HubStateCmd;
import ajax.systems.company.hubs.model.Credentials;
import ajax.systems.company.hubs.view.ViewName;
import javafx.scene.Node;


public abstract class MainController {
	
	abstract void onCredentialsFilledUp(Credentials credentials);
	abstract void updateLoadingText(String text);
	abstract void switchToView(ViewName viewName);
	abstract public void showLoadingPane();
	abstract void hideLoadingPane(Runnable onFinished);
	abstract public void handleException(Exception ex, String title);
	abstract void showSnackBar(String message);
	abstract void showDialog(Node heading, Node content);
	abstract public void controlHubState(String hubId, HubStateCmd cmd);
	abstract public void controlGroupState(String hubId, String groupId, HubStateCmd cmd);
}
