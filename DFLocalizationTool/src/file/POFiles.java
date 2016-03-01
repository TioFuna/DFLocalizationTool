package file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;

import translations.Translation;
import app.StartApp;

public class POFiles extends StartApp {

	public static final String fileID = "#: ";
	public static final String oriID = "msgid ";
	public static final String topID = "msgctxt ";
	public static final String transID = "msgstr ";
	public static final String codeID = "codeIN: ";
	
	private String originalFileNames = "OriginalFileNames.txt";
	private String poFileResult = "PlainText.txt";
	private String fileNameErrors = "";
	private List<String> listFileNames = new ArrayList<String>();
	private List<Translation> listPOFiles = new ArrayList<Translation>();
	public Boolean btnCancel = false;

	public POFiles(){
	}

	public String getOriginalFileNames() {
		return originalFileNames;
	}
	public void setOriginalFileNames(String originalFileNames) {
		this.originalFileNames = originalFileNames;
	}

	public String getPoFileResult() {
		return poFileResult;
	}
	public void setPoFileResult(String poFileResult) {
		this.poFileResult = poFileResult;
	}
	
	public String getFileNameErrors() {
		return fileNameErrors;
	}
	public void setFileNameErrors(String fileNameErrors) {
		this.fileNameErrors = fileNameErrors;
	}
	
	public List<String> getListFileNames() {
		return listFileNames;
	}
	public void setListFileNames(List<String> listFileNames) {
		this.listFileNames = listFileNames;
	}

	public List<Translation> getListPOFiles() {
		return listPOFiles;
	}
	public void setListPOFiles(List<Translation> listPOFiles) {
		this.listPOFiles = listPOFiles;
	}
	
	public Boolean getBtnCancel() {
		return btnCancel;
	}
	public void setBtnCancel(Boolean btnCancel) {
		this.btnCancel = btnCancel;
	}
	
	public Boolean createPOFiles(JProgressBar progressBar) throws IOException {
		
		progressBar.setMaximum(listPOFiles.size());
		
		// create content PO File
		String content = "";
		String newLine = "\r\n";
		
		int i = 1;
		for (Translation translation : listPOFiles) {
			// Cancel process
			if (btnCancel) {
				btnCancel = false;
				return false;
			}
			
			progressBar.setValue(i);
			content += newLine;
			content += fileID + translation.getFileName() + translation.getLines() + newLine;
			content += oriID + translation.getMsgid() + newLine;
			content += transID + translation.getMsgstr() + newLine;
			content += codeID + translation.getCharset() + newLine;
			i++;
			//System.out.println(i++ + "/" + listPOFiles.size());
		}
		
		// create PO file
		Path path = Paths.get(getDirPO() + File.separator + poFileResult);
		CreateFile createFile = new CreateFile();
		createFile.createFile(path.toString());
		
		Files.write(path, content.getBytes(charset));
		System.out.println("Total of " + listPOFiles.size() + " lines to translate.");
		btnCancel = false;
		return true;
	}
	
	public void readOriginals() throws IOException {
		
		Charset charsetOriginal = charset;
		fileNameErrors = "";
		Path pathOriginal = null;
		
		for (String fileName : listFileNames) {
			// find original file
			FindFile findFile = new FindFile();
			File oFile  = findFile.findFile(fileName, new File(dirBase));
			
			// Get string lines from file
			pathOriginal = Paths.get(oFile.getAbsolutePath());
			List<String> fileLines = new ArrayList<String>();;
			try{
				fileLines = Files.readAllLines(pathOriginal, charset);
			}catch (IOException e1){
				// errors by reading a file with a different code from UTF-8
				if (e1.getMessage().contains("Input length = 1")) {
					try {
						// ANSI (Cp1252) windows format is another code in original files
						fileLines = Files.readAllLines(pathOriginal, charsetAlt_1);
						charsetOriginal = charsetAlt_1;
					}catch (IOException e2){
						if (e2.getMessage().contains("Input length = 1")) {
							try {
								// OEM-US (Cp437) MS-DOS format is another code in original files
								fileLines = Files.readAllLines(pathOriginal, charsetAlt_2);
								charsetOriginal = charsetAlt_2;
							}catch (IOException e3){
								if (e3.getMessage().contains("Input length = 1")) {
									fileNameErrors += fileName + "<br>";
									continue;
								}
							}
							
						}
					}
				}
			}
			
			Boolean isRAW = fileName.startsWith(fileLines.get(0));
			
			for (int i = 0; i < fileLines.size(); i++) {
				
				String line = fileLines.get(i);
				
				List<String> substrings = new ArrayList<String>();
				if (isRAW) {
					// get text inside brackets
					if (!isValidLine(line)) continue;
					substrings = getSubstringBrackets(line);
				}else {
					if (isValidWords(line)) {
						addItem(fileName, "\""+line+"\"", i+1, charsetOriginal);
					}
					continue;
				}
				
				for (String part : substrings) {
					String[] split = part.split(":");
					Integer untilHere = null;
					for (int j = 0; j < split.length; j++) {
						// text to translate have lower case
						if (isValidWords(split[j])) {
							// Validates some texts
							// creature tile
							if (split[j].startsWith("'")) break;
							
							// dir/file
							if (split[j].endsWith(".txt")) break;
							
							untilHere = j;
						}
					}
					// no valid words in this part 
					if (untilHere==null) continue;
					
					// mount text until words to be translated
					String text = "[";
					for (int j = 0; j <= untilHere; j++) {
						if (j!=0) text += ":";
						text += split[j];
					}
					text += "]";
					
					addItem(fileName, "\""+text+"\"", i+1, charsetOriginal);
				}
			}
		}
	}
	
	private void addItem(String fileName, String text, Integer line, Charset charset) {
		// verify if item exists
		Translation item = getItem(fileName, text, listPOFiles);
		if (item==null) {
			listPOFiles.add(new Translation(fileName, text, "\"\"", ":"+line, charset));
		}else {
			item.setLines(item.getLines() + ":"+line);
		}
		
		//System.out.println(listPOFiles.size() + " - " + fileName + " - " + text + " - " + ":"+line);
	}
	
	private Translation getItem(String fileName, String text, List<Translation> list) {
		for (Translation translation : list) {
			String oFileName = translation.getFileName();
			String oText = translation.getMsgid();
			if (oFileName.equals(fileName) & oText.equals(text)) {
				return translation;
			}
		}
		return null;
	}
	
	private Boolean isValidLine(String line) {
		return line.matches("(.*?)\\[(.*?)([a-z])(.*?)\\](.*?)");
		//return line.matches("(.*?)\\[(.*?)\\](.*?)");
	}
	
	private Boolean isValidWords(String line) {
		return !line.equals(line.toUpperCase());
	}
	
	public List<String> getSubstringBrackets(String line) {
		
		Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(line);

	    List<String> substring = new ArrayList<String>();

	    int pos = -1;
	    while (matcher.find(pos+1)){
	        pos = matcher.start();
	        substring.add(matcher.group(1));
	    }
	    return substring;
	}

	public Boolean readFileNames() throws IOException {
		// Get string lines from file
		Path path = Paths.get(getDirPO()+File.separator+originalFileNames);
		List<String> fileLines = Files.readAllLines(path, charset);
		for (String line : fileLines) {
			line = line.replace(" ", "");	// clean line
			if (!(line.isEmpty() || line.startsWith("--"))) {
				listFileNames.add(line);
			}
		}
		if (listFileNames.isEmpty()) return false;
		
		return true;
	}
	
	public Boolean existsOriginalFileNames() {
		Path path = Paths.get(getDirPO()+File.separator+originalFileNames);
		return Files.exists(path);
	}
	
	public void createOriginalFileNamesExample() throws IOException {
		
		String content = "";
		content += "----- EXAMPLE -----\r\n";
		content += "book_art.txt\r\n";
		content += "item_instrument_example.txt";
		
		Path path = Paths.get(getDirPO()+File.separator+originalFileNames);
		CreateFile createFile = new CreateFile();
		createFile.createFile(path.toString());
		
		Files.write(path, content.getBytes(charset));
	}

}
