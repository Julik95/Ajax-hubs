package ajax.systems.company.hubs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HubUtils {
	
	private static Logger logger = LoggerFactory.getLogger(HubUtils.class);
	
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
	
	
	public static String getAsset(Assets asset) {
		File file = new File(Constants.ASSETS_DIR +asset.getPath());
		String absPath = "";
		if(file.exists()) {
			absPath = "file:///" + file.getAbsolutePath().replace("\\", "/");
		}
		return absPath;
	}
	
	public static String getAsset(String fileName) {
		File file = new File(Constants.ASSETS_DIR +fileName);
		String absPath = "";
		if(file.exists()) {
			absPath = "file:///" + file.getAbsolutePath().replace("\\", "/");
		}
		return absPath;
	}
	
	public static List<String> getAllStyleAssets() {
		ArrayList<String> result = new ArrayList();
		result.add(getAsset("css/fonts-style/jfoenix-fonts.css"));
		String stylesDir = "css";
		File cssDir = new File(Constants.ASSETS_DIR+stylesDir);
		for (File css : cssDir.listFiles()) {
			if(css.isFile() && FilenameUtils.getExtension(css.getAbsolutePath()).equalsIgnoreCase("css")) {
				logger.info("Retrieving styleshet {}", css.getAbsolutePath());
				result.add("file:///" + css.getAbsolutePath().replace("\\", "/"));
			}		
		}
		return result;
	}
	

}
