package file;

import java.io.File;
import java.io.IOException;

public class CreateFile {

	public CreateFile() {
	}

	public File createFile(String fileName) throws IOException{
		File file = new File(fileName);
		file.mkdirs();
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		return file;
	}
	
}
