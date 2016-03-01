package file;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilterEnds implements FilenameFilter {

	private String terminoFileName;
	
	public FileFilterEnds(String terminoFileName) {
		this.terminoFileName = terminoFileName;
	}
	
	@Override
	public boolean accept(File dir, String fileName) {
		return fileName.toLowerCase().endsWith(this.terminoFileName.toLowerCase());
	}

}
