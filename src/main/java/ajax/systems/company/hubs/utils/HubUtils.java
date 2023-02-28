package ajax.systems.company.hubs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ajax.systems.company.hubs.model.Credentials;

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
			absPath = "file:" + file.getPath().replace("\\", "/");
		}
		return absPath;
	}
	
	public static String getAsset(String fileName) {
		File file = new File(Constants.ASSETS_DIR +fileName);
		String absPath = "";
		if(file.exists()) {
			absPath = "file:" + file.getPath().replace("\\", "/");
		}
		return absPath;
	}
	
	public static Credentials getCredentialsFromCache() {
		FileInputStream fileInput;
		Credentials credentials = null;
		try {
			fileInput = new FileInputStream(new File(Constants.CACHE_FILE_PATH + File.separatorChar+ Constants.CACHE_FILE_NAME));
			ObjectInputStream oi = new ObjectInputStream(fileInput);
			if(oi != null) {
				credentials = (Credentials) oi.readObject();
			}
		} catch (FileNotFoundException e) {
			logger.warn("Unenable to retrieve cache file: {}", e.getMessage());
		} catch (IOException e) {
			logger.warn("Unenable to retrieve cache file: {}", e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.warn("Unenable to retrieve cache file: {}", e.getMessage());
		}
		return credentials;
	}
	
	public static void clearCredentialsCache() {
		new File(Constants.CACHE_FILE_PATH + File.separatorChar+ Constants.CACHE_FILE_NAME).delete();
	}
	
	public static void storeCredentialsToCache(Credentials credentials) {
		try {
			Files.createDirectories(Paths.get(Constants.CACHE_FILE_PATH));
            FileOutputStream fileOut = new FileOutputStream(Constants.CACHE_FILE_PATH + File.separatorChar+ Constants.CACHE_FILE_NAME);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(credentials);
            objectOut.close();
            fileOut.close();
            logger.info("Cache file has been successefully updated");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public static List<String> getAllStyleAssets() {
		ArrayList<String> result = new ArrayList();
		result.add(getAsset("css/fonts-style/jfoenix-fonts.css"));
		result.add(getAsset("css/style.css"));
		return result;
	}
	

}
