package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import app.StartApp;
import exception.ApplicationException;
import file.DFFilter;
import file.POFiles;
import file.TranslationFiles;

public class ViewSwing	extends StartApp 
						implements ActionListener  {

	public final int CREATEPOFILE = 0;
	public final int TRANSLATEFILES = 1;
	
	private JPanel pnlInfo, pnlPath, pnlTranslate, pnlButtons, pnlProgressBar, pnlMain;
	private JLabel lblInfo, lblPath, lblCode;
	private JButton btnPath, btnTranslate, btnPOFiles, btnCancel;
	private JComboBox<String> cbLanguage;
	private JScrollPane spInfo;
	private JFileChooser fcPath;
	private JProgressBar progressBar;
	private Task task;
	
	private String txtInfo = "";
	private String languages[] = {" English", " Spanish", " Portuguese", " Russian"};
	
	private TranslationFiles translationFiles = new TranslationFiles();
	private POFiles poFile = new POFiles();
	
	public ViewSwing() {
		
		/*
		Aviso Legal
		Este programa irá alterar alguns arquivos do jogo original,
		e pode conprometer o bom funcionamento. Caso encontre algum erro
		e queira reportar, lembre-se de identificar o uso desta modificação.
		*/
		
		// PANEL INFORMER
		txtInfo = "<html><font color='red'><b><u>Disclaimer</u></b><br>";
		txtInfo += "This program will change some files from the original game,<br>";
		txtInfo += "and can conprometer proper functioning. If you find a mistake<br>";
		txtInfo += "and want to report, remember to identify the use of this modification.</html>";
		
		lblInfo = new JLabel(txtInfo);
		lblInfo.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblInfo.setOpaque(true);
		lblInfo.setBackground(Color.WHITE);
		lblInfo.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 0));
		
		// Wrap the JLabel inside a JScrollPane
		spInfo = new JScrollPane(lblInfo);
		spInfo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		spInfo.getVerticalScrollBar().setUnitIncrement(6);
		spInfo.setBorder(BorderFactory.createEtchedBorder());
		
		pnlInfo = new JPanel();
		pnlInfo.setMinimumSize(new Dimension(200, 55));
		pnlInfo.setPreferredSize(new Dimension(450, 90));
		pnlInfo.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 10));
		pnlInfo.setLayout(new BorderLayout());
		pnlInfo.add(spInfo, BorderLayout.CENTER);
		
		// PANEL PATH
		btnPath = new JButton("Pick File");
		btnPath.setToolTipText("Select path to Dwarf Fortress.exe");
		lblPath = new JLabel(" ??? \\Dwarf Fortress.exe");
		
		pnlPath = new JPanel();
		pnlPath.setMinimumSize(new Dimension(200, 65));
		pnlPath.setPreferredSize(new Dimension(450, 65));
		pnlPath.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		pnlPath.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 10, 0, 10),
				BorderFactory.createTitledBorder(" Path 'Dwarf Fortress.exe' ")));
		pnlPath.setLayout(new FlowLayout(FlowLayout.LEADING));
		pnlPath.add(btnPath);
		pnlPath.add(lblPath);
		
		// PANEL TRANSLATE
		cbLanguage = new JComboBox<String>(languages);
		cbLanguage.setToolTipText("Select codepage to translated files");
		cbLanguage.setBackground(Color.WHITE);
		cbLanguage.setMaximumRowCount(6);
		lblCode = new JLabel("Codepage: UTF-8");
		
		pnlTranslate = new JPanel();
		pnlTranslate.setMinimumSize(new Dimension(200, 65));
		pnlTranslate.setPreferredSize(new Dimension(450, 65));
		pnlTranslate.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		pnlTranslate.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 10, 0, 10),
				BorderFactory.createTitledBorder(" Translate to... ")));
		pnlTranslate.setLayout(new FlowLayout(FlowLayout.LEADING));
		pnlTranslate.add(cbLanguage);
		pnlTranslate.add(lblCode);
		
		// PANEL BUTTONS
		btnTranslate = new JButton("Translate Files");
		btnTranslate.setToolTipText("This will translate(replace) only plain texts(*.txt)");
		btnTranslate.setAlignmentY(Component.CENTER_ALIGNMENT);
		btnPOFiles = new JButton("Create PO Files");
		btnPOFiles.setToolTipText("This will create PO Files based on the file " + poFile.getOriginalFileNames());
		btnPOFiles.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		pnlButtons = new JPanel();
		pnlButtons.setMinimumSize(new Dimension(200, 45));
		pnlButtons.setPreferredSize(new Dimension(450, 45));
		pnlButtons.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.X_AXIS));
		pnlButtons.add(Box.createHorizontalGlue());
		pnlButtons.add(btnTranslate, pnlButtons);
		pnlButtons.add(Box.createHorizontalGlue());
		pnlButtons.add(btnPOFiles, pnlButtons);
		pnlButtons.add(Box.createHorizontalGlue());
		
		// PANEL PROGRESS BAR
		progressBar = new JProgressBar(0, 100);
		progressBar.setAlignmentY(Component.CENTER_ALIGNMENT);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(330, 20));
        btnCancel = new JButton("Cancel");
        btnCancel.setAlignmentY(Component.CENTER_ALIGNMENT);
		btnCancel.addActionListener(this);
		
		pnlProgressBar = new JPanel();
		pnlProgressBar.setMinimumSize(new Dimension(200, 35));
		pnlProgressBar.setPreferredSize(new Dimension(450, 35));
		pnlProgressBar.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		pnlProgressBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray));
		pnlProgressBar.setLayout(new BoxLayout(pnlProgressBar, BoxLayout.X_AXIS));
		pnlProgressBar.add(Box.createHorizontalGlue());
		pnlProgressBar.add(progressBar, pnlProgressBar);
		pnlProgressBar.add(Box.createRigidArea(new Dimension(10, 0)));
		pnlProgressBar.add(btnCancel, pnlProgressBar);
		pnlProgressBar.add(Box.createHorizontalGlue());
		
		// Listeners
		btnPath.addActionListener(this);
		cbLanguage.addActionListener(this);
		btnTranslate.addActionListener(this);
		btnPOFiles.addActionListener(this);
		btnCancel.addActionListener(this);
		
		pnlMain = new JPanel();
		pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
		pnlMain.add(pnlInfo, pnlMain);
		pnlMain.add(pnlPath, pnlMain);
		pnlMain.add(pnlTranslate, pnlMain);
		pnlMain.add(pnlButtons, pnlMain);
		pnlMain.add(pnlProgressBar, pnlMain);
		
		enableCancelButtom(false);
		createAndShowGUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		// Handle File Chooser button action.
		if (ae.getSource() == btnPath) {
			fcPath = new JFileChooser();
			fcPath.addChoosableFileFilter(new DFFilter());
			fcPath.setAcceptAllFileFilterUsed(false);
			
			int returnVal = fcPath.showOpenDialog(btnPath);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fcPath.getSelectedFile();
				lblPath.setText(file.getAbsolutePath());
				dirBase = file.getParent();
			}
		}
		
		// Handle Language combo box.
		if (ae.getSource() == cbLanguage) {
			switch (cbLanguage.getSelectedIndex()) {
			case 0:
				lblCode.setText("Codepage: UTF-8");
				charsetOut = StandardCharsets.UTF_8;
				break;
			case 1:
				lblCode.setText("Codepage: Cp850");
				charsetOut = Charset.forName("Cp850");
				break;
			case 2:
				lblCode.setText("Codepage: Cp860");
				charsetOut = Charset.forName("Cp860");
				break;
			case 3:
				lblCode.setText("Codepage: Cp866");
				charsetOut = Charset.forName("Cp866");
				break;
			default:
				break;
			}
		}
		
		// Handle Translate button action.
		if (ae.getSource() == btnTranslate) {
			
			if (isValidDir("Translations")) {
				
				try {
					// read translations files ".po"
					if (translationFiles.read()) {
						
						task = new Task(TRANSLATEFILES);
				        task.execute();
						
					}else {
						System.out.println("Translations files read failed!!!");
						txtInfo = "<html><font color='red'><b><u>Missing PO Files</u></b></font><br>";
						txtInfo += "There were no PO files to make translations.<br>";
						txtInfo += "Please, go to <b>'" + getDirTranslations().replace(dirBase, "");
						txtInfo += "'</b> directory<br>";
						txtInfo += "to put/view translations files (*.po) to be used.</html>";
						lblInfo.setText(txtInfo);
					}
				}catch (ApplicationException | IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		// Handle PO Files button action.
		if (ae.getSource() == btnPOFiles) {
			
			if (isValidDir("PO")) {
				
				String ofn = poFile.getOriginalFileNames();
				if (poFile.existsOriginalFileNames()) {
					try {
						if (poFile.readFileNames()) {
							
							poFile.readOriginals();
							
					        task = new Task(CREATEPOFILE);
					        task.execute();
					        
						}else {
							// List is empty
							System.out.println("List of Orginal File Names is empty!!!");
							txtInfo = "<html><font color='red'><b><u>";
							txtInfo += ofn + " is empty";
							txtInfo += "</u></b></font><br>";
							txtInfo += "Please, go to <b>'" + getDirPO().replace(dirBase, "");
							txtInfo += File.separator + ofn + "'</b><br>";
							txtInfo += "and write the list of filenames to be used,<br>";
							txtInfo += "one filename in each row.</html>";
							lblInfo.setText(txtInfo);
						}
					} catch (ApplicationException | IOException e) {
						System.out.println(e.getMessage());
					}
				}else {
					// File don't exists
					System.out.println("File '" + ofn + "' was not found!!!");
					txtInfo = "<html><font color='red'><b><u>";
					txtInfo += "File '" + ofn + "' was not found";
					txtInfo += "</u></b></font><br>";
					txtInfo += "Please, go to <b>'" + getDirPO().replace(dirBase, "");
					txtInfo += "'</b> directory<br>";
					txtInfo += "and create file <b>'" + ofn + "'</b><br>";
					txtInfo += "with a list of Original File Names to be used.</html>";
					lblInfo.setText(txtInfo);
				}
			}
		}
		// Handle PO Files button action.
		if (ae.getSource() == btnCancel) {
			poFile.setBtnCancel(true);
		}
	}

    class Task extends SwingWorker<Void, Void> {
    	
    	private int option;
    	
        public Task(int option) {
			super();
			this.option = option;
		}

		@Override
        public Void doInBackground() throws IOException {
			
			enableCancelButtom(true);
			
			switch (option) {
			case CREATEPOFILE:
				txtInfo = "<html><font color='blue'><b><u>";
				txtInfo += "Create PO File";
				txtInfo += "</u></b></font><br>";
				txtInfo += "Process start...</html>";
				lblInfo.setText(txtInfo);
				progressBar.setValue(0);
				if (poFile.createPOFiles(progressBar)) {
					System.out.println("PO file created with success!!!");
					txtInfo = "<html><font color='green'><b><u>";
					txtInfo += "PO File created with success";
					txtInfo += "</u></b></font><br>";
					txtInfo += "Please, go to <b>'" + getDirPO().replace(dirBase, "");
					txtInfo += "'</b> directory<br>";
					txtInfo += "and the file <b>'" + poFile.getPoFileResult();
					txtInfo += "'</b> will be there,<br>";
					txtInfo += "with a list of words to be translated.";
				}else {
					System.out.println("Create PO file canceled!!!");
					txtInfo = "<html><font color='red'><b><u>";
					txtInfo += "Create PO File canceled";
					txtInfo += "</u></b></font><br>";
					txtInfo += "PO Files <b>NOT</b> created.";
					progressBar.setValue(0);
				}
				// List errors
				if (poFile.getFileNameErrors() != "") {
					txtInfo += "<br><font color='red'><b><u>";
					txtInfo += "File Code Errors";
					txtInfo += "</u></b></font><br>";
					txtInfo += poFile.getFileNameErrors();
				}
				txtInfo += "</html>";
				lblInfo.setText(txtInfo);
				break;
			case TRANSLATEFILES:
				txtInfo = "<html><font color='blue'><b><u>";
				txtInfo += "Translate Files";
				txtInfo += "</u></b></font><br>";
				txtInfo += "Process start...</html>";
				lblInfo.setText(txtInfo);
				progressBar.setValue(0);
				if (translationFiles.translate(progressBar)) {
					System.out.println("Translations made with success!!!");
					txtInfo = "<html><font color='green'><b><u>";
					txtInfo += "Translations made with success";
					txtInfo += "</u></b></font><br>";
					txtInfo += "Please, go to <b>'" + getDirLocalized().replace(dirBase, "");
					txtInfo += "'</b> directory<br>";
					txtInfo += "and all files localized will be there.<br>";
					txtInfo += "Backup originals, copy localized files and paste/replace<br>";
					txtInfo += "in the original game directory <b>'";
					txtInfo += dirBase.replace(new File(dirBase).getParent(), "") + "'</b>";
				}else {
					System.out.println("Translate Files canceled!!!");
					txtInfo = "<html><font color='red'><b><u>";
					txtInfo += "Translate Files canceled";
					txtInfo += "</u></b></font><br>";
					txtInfo += "All Translate Files <b>NOT</b> created.<br>";
					txtInfo += "Please, delete files from <b>'" + getDirLocalized().replace(dirBase, "");
					txtInfo += "'</b> directory<br>";
					txtInfo += "To avoid errors wich next translations.";
					progressBar.setValue(0);
				}
				txtInfo += "</html>";
				lblInfo.setText(txtInfo);
				break;
			default:
				break;
			}
			
			enableCancelButtom(false);
			
            return null;
        }

        @Override
        public void done() {
            btnPOFiles.setEnabled(true);
        }
    }
    
    private Boolean isValidDir(String dir) {
		
		txtInfo = "<html><font color='red'><b><u>Missing Path</u></b></font><br>";
		
		if (dirBase == null) {
			txtInfo += "Please, select path of <b>'Dwarf Fortress.exe'</b><br>";
			txtInfo += "pressing button <b>'" + btnPath.getText() + "'</b></html>";
			lblInfo.setText(txtInfo);
			return false;
		}
		
		txtInfo += "Please, create path ...";
		txtInfo += dirBase.replace(new File(dirBase).getParent(), "");
		txtInfo += "<b><font color='blue'>";
		
		switch (dir) {
		case "Translations":
			if (Files.exists(Paths.get(getDirTranslations()))) {
				return true;
			}else {
				txtInfo += getDirTranslations().replace(dirBase, "");
			}
			break;
		case "PO":
			if (Files.exists(Paths.get(getDirPO()))) {
				return true;
			}else {
				txtInfo += getDirPO().replace(dirBase, "");
			}
			break;
		default:
			break;
		}
		
		txtInfo += "</font></b></html>";
		lblInfo.setText(txtInfo);
		
		return createDirExamples();
	}
    
    private Boolean createDirExamples() {
		//Custom button text
		Object[] options = {"Yes, please", "No, thanks"};
		String title = "Create Directories/Example Files";
		String question = "Directories and required files not found.\n";
		question += "Would you like to create directories and example files?";
		int n = JOptionPane.showOptionDialog(lblInfo, question, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		if (n == 1) return false;
		
		// Create dir/examples
		try {
			translationFiles.createPOFileExample();
			poFile.createOriginalFileNamesExample();
		} catch (ApplicationException | IOException e) {
			System.out.println(e.getMessage());
		}
		
		return true;
	}
    
    private void createAndShowGUI() {
    	
    	//ImageIcon iconDF = new ImageIcon("E:/DF/DF.png");
    	ImageIcon iconDF = new ImageIcon(getClass().getResource("/DF.png"));
    	
        //Create and set up the window.
        JFrame frame = new JFrame("Dwarf Fortress Localization Tool   --- By @TioFuna ---");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(iconDF.getImage());
        frame.add(pnlMain);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);	// center the application window
    }
    
    public void enableCancelButtom(Boolean enable) {
		
		btnCancel.setEnabled(enable);
		
		btnPath.setEnabled(!enable);
		cbLanguage.setEnabled(!enable);
		btnTranslate.setEnabled(!enable);
		btnPOFiles.setEnabled(!enable);
	}
}