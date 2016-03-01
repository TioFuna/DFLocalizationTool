package app;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.swing.SwingUtilities;

import view.ViewSwing;

public class StartApp {

	protected static String dirBase = null; //"E:/DF/df_42_05_win";
	protected static String dirLocalization = "Localization";
	protected static String dirTranslations = "Translations";
	protected static String dirLocalized = "Localized";
	protected static String dirPO = "PO";
	protected static Charset charsetOut = StandardCharsets.UTF_8;
	protected static Charset charset = StandardCharsets.UTF_8;
	protected static Charset charsetAlt_1 = Charset.forName("Cp1252"); // ANSI
	protected static Charset charsetAlt_2 = Charset.forName("Cp437"); // OEM-US
	
	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ViewSwing();
			}
		});
		
		/*
		// Console
		ViewConsole mw = new ViewConsole();
		mw.start();
		*/
	}
	
	public static String getDirTranslations() {
		StringBuilder path = new StringBuilder();
		path.append(dirBase);
		path.append(File.separator);
		path.append(dirLocalization);
		path.append(File.separator);
		path.append(dirTranslations);
		return path.toString();
	}
	
	public static String getDirLocalized() {
		StringBuilder path = new StringBuilder();
		path.append(dirBase);
		path.append(File.separator);
		path.append(dirLocalization);
		path.append(File.separator);
		path.append(dirLocalized);
		return path.toString();
	}
	
	public static String getDirPO() {
		StringBuilder path = new StringBuilder();
		path.append(dirBase);
		path.append(File.separator);
		path.append(dirLocalization);
		path.append(File.separator);
		path.append(dirPO);
		return path.toString();
	}
	
}
