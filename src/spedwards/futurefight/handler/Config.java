package spedwards.futurefight.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

public class Config {
	
	private File dir = null;
	private File file = null;
	private Class clazz = null;

	public Config(String program, String fileName, Class root) throws IOException {
		this.dir = new File(System.getProperty("user.home") + File.separator + program);
		
		if (!this.dir.exists()) {
			this.dir.mkdir();
		}
		
		this.file = new File(this.dir + File.separator + fileName);
		if (!this.file.exists()) {
			this.file.createNewFile();
			
			if (root.getName().equals(JSONArray.class.getName())) {
				Files.write(this.file.toPath(), "[]".getBytes());
			} else if (root.getName().equals(JSONObject.class.getName())) {
				Files.write(this.file.toPath(), "{}".getBytes());
			}
		}
		
		this.clazz = root;
	}
	
	public <T> T readConfig() {
		try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
			StringBuilder sb = new StringBuilder();
			String line;
			
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			if (this.clazz == JSONArray.class) {
				return (T) new JSONArray(sb.toString());
			} else if (this.clazz == JSONObject.class) {
				return (T) new JSONObject(sb.toString());
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONArray readConfigArray() {
		return readConfig();
	}
	
	public JSONObject readConfigObject() {
		return readConfig();
	}
	
	public void setConfig(JSONArray json) {
		try {
			PrintWriter writer = new PrintWriter(this.file, "UTF-8");
			json.write(writer);
			writer.close();
		} catch(FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void setConfig(JSONObject json) {
		try {
			PrintWriter writer = new PrintWriter(this.file, "UTF-8");
			json.write(writer);
			writer.close();
		} catch(FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
