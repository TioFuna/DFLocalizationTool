package file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;

import translations.Translation;
import app.StartApp;

public class TranslationFiles extends StartApp {

	private List<Translation> listTranslations = new ArrayList<Translation>();
	private Boolean btnCancel = false;
	
	public Boolean getBtnCancel() {
		return btnCancel;
	}
	public void setBtnCancel(Boolean btnCancel) {
		this.btnCancel = btnCancel;
	}

	public TranslationFiles() {
	}

	public Boolean translate(JProgressBar progressBar) throws IOException {
		
		progressBar.setMaximum(listTranslations.size());
		
		String fileName = "";
		String fileNameTemp = "";
		String content = "";
		
		Path pathOriginal = null;
		Path pathLocalized = null;
		Path pathLocalizedTemp = null;
		
		int i = 1;
		for (Translation translation : listTranslations) {
			// Cancel process
			if (btnCancel) {
				btnCancel = false;
				return false;
			}
			progressBar.setValue(i);
			
			fileName = translation.getFileName();
			if ((fileName != null | fileName != "") & !fileName.equalsIgnoreCase(fileNameTemp)) {
				
				// get original file
				FindFile findFile = new FindFile();
				File oFile  = findFile.findFile(fileName, new File(dirBase));
				
				pathOriginal = Paths.get(oFile.getAbsolutePath());
				pathLocalized = Paths.get(oFile.getAbsolutePath().replace(dirBase, getDirLocalized()));
				
				// create localized file
				CreateFile createFile = new CreateFile();
				createFile.createFile(pathLocalized.toString());
				
				// write localized file
				if (pathLocalizedTemp != null) {
					Files.write(pathLocalizedTemp, content.getBytes(charsetOut));
				}
				
				// get original content
				if (translation.getCharset()==null) {
					content = new String(Files.readAllBytes(pathOriginal), charset);
				}else {
					content = new String(Files.readAllBytes(pathOriginal), translation.getCharset());
				}
				
				fileNameTemp = fileName;
				pathLocalizedTemp = pathLocalized;
			}
			
			// translate
			String tmpID = Pattern.quote(translation.getMsgid()); // use quote to avoid problems with brackets [ ]
			String tmpMSG = translation.getMsgstr();
			content = content.replaceAll(tmpID, tmpMSG);
			i++;
		}
		
		// write last localized file
		if (pathLocalizedTemp != null) {
			Files.write(pathLocalizedTemp, content.getBytes(charsetOut));
		}
		return true;
	}
	
	public Boolean read() throws IOException {
		
		// read translations files	
		File[] poFiles = getFiles();
		if (poFiles==null || poFiles.length==0) return false;
		
		for (File file : poFiles) {
			
			// Get string lines from file
			Path path = Paths.get(file.getAbsolutePath());
			List<String> fileLines = Files.readAllLines(path, charset);
			
			for (int i = 0; i < fileLines.size(); i++) {
				
				// verify line id - file name
				if(fileLines.get(i).startsWith(POFiles.fileID)){
					String fileName = fileLines.get(i).substring(POFiles.fileID.length()).split(":")[0];	// get only file name
					i++;
					
					// verify line id - main item
					if(fileLines.get(i).startsWith(POFiles.topID))	i++;
					
					// verify line id - original text
					if(fileLines.get(i).startsWith(POFiles.oriID)){
						String msgid = fileLines.get(i).substring(POFiles.oriID.length());
						i++;
						
						// original text with more than one line
						// verify line id - translated text
						while (!fileLines.get(i).startsWith(POFiles.transID)){
							msgid += fileLines.get(i).substring(1);
							i++;
						}
						
						String msgstr = fileLines.get(i).substring(POFiles.transID.length());
						i++;
						
						Charset charsetOriginal = null;
						if(fileLines.get(i).startsWith(POFiles.codeID)){
							charsetOriginal = Charset.forName(fileLines.get(i).substring(POFiles.codeID.length()));
						}
						
						msgid = msgid.replaceAll("\"", "");					// remove quotes "
						msgstr = msgstr.replaceAll("\"", "");				// remove quotes "
						
						if (msgstr.startsWith("[") & msgstr.endsWith("]")) {
							msgid = msgid.substring(1, msgid.length()-1);		// remove brackets [ ]
							msgstr = msgstr.substring(1, msgstr.length()-1);	// remove brackets [ ]
						}
						
						// verif empty translation or equals
						if (!(msgstr.isEmpty() ||  msgstr.equals(msgid))) {
							listTranslations.add(new Translation(fileName, msgid, msgstr, null, charsetOriginal));
						}
						
					}
				}
			}
		}
		return true;
	}
	
	private File[] getFiles() {
		File dir = new File(getDirTranslations());
		FileFilterEnds filter = new FileFilterEnds(".po");
		File[] files = dir.listFiles(filter);
		return files;
	}
	
	public void createPOFileExample() throws IOException {
		
		String fileName = "ExamplePOFile.txt";
		String content = "";
		content += "Put in this directory translations files\r\n";
		content += "* This files must have the extension '.po'\r\n";
		content += "* Header lines are ignored\r\n\r\n";
		content += "In .po files you'll encounter like this...\r\n";
		content += "-- Some header lines\r\n\r\n";
		content += "-- " + POFiles.fileID + "filename.txt:lines where are found\r\n";
		content += "-- " + POFiles.topID + "\"Top Item Text\"\r\n";
		content += "-- " + POFiles.oriID + "\"Original Text\"\r\n";
		content += "-- " + POFiles.transID + "\"Translated Text\"\r\n";
		content += "-- " + POFiles.codeID + "\"Code page of original file\"\r\n\r\n";
		content += "--- EXAMPLE ---\r\n";
		content += POFiles.fileID + "book_art.txt:12\r\n";
		content += POFiles.oriID + "\"My Friend [NAME]\"\r\n";
		content += POFiles.transID + "\"Meu Amigo [NAME]\"\r\n";
		content += POFiles.codeID + "\"UTF-8\"";
		
		Path path = Paths.get(getDirTranslations()+File.separator+fileName);
		CreateFile createFile = new CreateFile();
		createFile.createFile(path.toString());
		
		Files.write(path, content.getBytes(charset));
	}
}
