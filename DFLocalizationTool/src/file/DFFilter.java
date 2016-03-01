package file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DFFilter extends FileFilter {

	// Accept all directories and Dwarf Fortress.exe
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		
		String extension = "Dwarf Fortress.exe";
		if (f.getName().equals(extension)) {
			return true;
		} else {
			return false;
		}
	}

	// The description of this filter
	public String getDescription() {
		return "Dwarf Fortress.exe";
	}
}
