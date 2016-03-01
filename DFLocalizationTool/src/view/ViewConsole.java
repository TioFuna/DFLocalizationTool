package view;

import java.util.Scanner;

public class ViewConsole {

	private final int OPT_FINAL = 9;
	private Scanner scan = new Scanner(System.in);
	
	public void start() {
		int option = OPT_FINAL;
		do {
			showMenu();
			option = getOption();
			switch (option) {
			case 1:textFiles();break;
			case 2:poFiles();break;
			case OPT_FINAL:exit();break;
			default:break;
			}
		} while (option != OPT_FINAL);
		System.out.println("------------   END   -------------");
	}
	
	private void showMenu() {
		System.out.println("------------   MENU   ------------");
		System.out.println(" 1 - Text Files");
		System.out.println(" 2 - PO Files");
		System.out.println(" 3 - ");
		System.out.println(" " + OPT_FINAL + " - Exit");
		System.out.println("----------------------------------");
	}
	
	private int getOption() {
		System.out.println("Choose an option:");
		String opcao = this.scan.nextLine();
		return Integer.valueOf(opcao);
	}
	
	private void exit() {
		System.out.println("Obrigado pela preferência.");
	}
	
	private void textFiles() {
		System.out.println("----------------------------------");
		System.out.println("            TEXT FILES            ");
		System.out.println("----------------------------------");
		/*
		// read translations files ".po"
		try {
			TranslationFiles translationFiles = new TranslationFiles();
			translationFiles.read();
			System.out.println("Translations files read with success!!!");
			//for (Translation translation : listTranslations) {
			//	System.out.println(translation.toString());
			//}
			
			translationFiles.translate();
			System.out.println("Translations made with success!!!");
			
		} catch (ApplicationException | IOException e) {
			System.out.println(e.getMessage());
		}
		*/
		System.out.println("----------------------------------");
	}
	
	private void poFiles() {
		System.out.println("----------------------------------");
		System.out.println("             PO FILES             ");
		System.out.println("----------------------------------");
		/*
		// read file names
		try {
			POFiles poFile = new POFiles();
			poFile.createPOFiles();
			System.out.println("PO files created with success!!!");
			
		} catch (ApplicationException | IOException e) {
			System.out.println(e.getMessage());
		}
		*/
		System.out.println("----------------------------------");
	}
}
