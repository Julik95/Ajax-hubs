package ajax.systems.company.hubs.controller;

import ajax.systems.company.hubs.model.Credentials;
import ajax.systems.company.hubs.view.ViewName;


public abstract class MainController {
	
	abstract void onCredentialsFilledUp(Credentials credentials);
	abstract void updateLoadingText(String text);
	
	abstract void switchToView(ViewName viewName);
}
