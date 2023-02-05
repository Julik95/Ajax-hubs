package ajax.systems.company.hubs.controller;


public abstract class ViewController {

	abstract void handleException(Exception ex, String title);
	abstract void removeCurrentContent();
}
