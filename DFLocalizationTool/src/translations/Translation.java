package translations;

import java.nio.charset.Charset;

public class Translation {
	
	private String fileName;
	private String msgid;
	private String msgstr;
	private String lines;
	private Charset charset;
	
	public Translation() {
	}

	public Translation(String fileName, String msgid, String msgstr, String lines, Charset charset) {
		super();
		this.fileName = fileName;
		this.msgid = msgid;
		this.msgstr = msgstr;
		this.lines = lines;
		this.charset = charset;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getMsgstr() {
		return msgstr;
	}
	public void setMsgstr(String msgstr) {
		this.msgstr = msgstr;
	}

	public String getLines() {
		return lines;
	}
	public void setLines(String lines) {
		this.lines = lines;
	}
	
	public Charset getCharset() {
		return charset;
	}
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {
		return "Translation { "
				+ "fileName=" + fileName
				+ ", msgid=" + msgid
				+ ", msgstr=" + msgstr
				+ ", lines=" + lines
				+ ", charset=" + charset.toString()
				+ " }";
	}
	
}
