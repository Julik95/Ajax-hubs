package ajax.systems.company.hubs.utils;

import javafx.application.Platform;

public class HubUtils {
	
	public static String translateHubState(String hubStateCode) {
		switch(hubStateCode) {
			case "DISARMED": return "Disinserito";
			case "ARMED": return "Inserito";
			case "NIGHT_MODE": return "Modalità noturna";
			case "ARMED_NIGHT_MODE_ON": return "Inserito modalità noturna";
			case "ARMED_NIGHT_MODE_OFF": return "Inserito senza modalità noturna";
			case "DISARMED_NIGHT_MODE_ON": return "Disinserito modalità noturna";
			case "DISARMED_NIGHT_MODE_OFF": return "Disinserito sensa modalità noturna";
			case "PARTIALLY_ARMED_NIGHT_MODE_ON": return "Inserito parzialmente con modalità noturna";
			case "PARTIALLY_ARMED_NIGHT_MODE_OFF": return "Inserito parzialmente sensa modalità noturna";
		}
		return null;
	}
	

}
