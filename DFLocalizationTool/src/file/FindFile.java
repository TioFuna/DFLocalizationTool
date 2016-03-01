package file;

import java.io.File;

import app.StartApp;

public class FindFile extends StartApp{
	
	public FindFile() {
	}

	public File findFile(String name, File dir) {
        File[] list = dir.listFiles();
        if(list != null) {
        	for (File file : list) {
                if (file.isDirectory()) {
                	// don't search in Localization folder
                	if (!file.getName().equals(dirLocalization)) {
                		File tempFile = findFile(name, file);
                    	if (tempFile!=null) {
                    		return tempFile;
                    	}
					}
                }
                else if (name.equalsIgnoreCase(file.getName())) {
                    //System.out.println(file.getParentFile());
                    return file;
                }
            }
        }
        return null;
    }

}
