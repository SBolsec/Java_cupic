package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import hr.fer.oprpp1.hw08.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LJFrame;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LJStatusCarretInfo;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LJStatusLength;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LocalizableAction;

/**
 * Notepad like application.
 * 
 * @author sbolsec
 *
 */
public class JNotepadPP extends LJFrame {

	/** Generated serial version UID **/
	private static final long serialVersionUID = 2115546573199433581L;
	/** Provides localization **/
	private final FormLocalizationProvider flp = this.getFormLocalizationProvider();
	/** Model for working with multiple documents, also adds support for tabs **/
	private DefaultMultipleDocumentModel tabbedPane;
	/** Model for working with multiple documents **/
	private MultipleDocumentModel model;
	/** Clipboard used by cut, copy and paste **/
	private String clipboard = "";
	/** Part of status bar, displays length of current document **/
	private LJStatusLength length;
	/** Part of status bar, dispplays info about caret **/
	private LJStatusCarretInfo info;
	/** Current tab index **/
	private int currentTabIndex = 0;
	/** Previous tab index **/
	private int previousTabIndex = 0;
	/** Timer which updates the date and time in the status bar **/
	private Timer timer;

	/**
	 * Constructor.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(950, 600);
		setTitle("(" + flp.getString("unnamedName") + ")" + "  -  JNotepad++");

		initGUI();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});

		// when user changes selected tab
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();

				previousTabIndex = currentTabIndex;
				currentTabIndex = tabbedPane.getSelectedIndex();
				
				if (currentTabIndex == -1) {
					setTitle("JNotepad++");
					enableOrDisableActions(false);
					return;
				}
				
				try {
					model.getDocument(previousTabIndex).getTextComponent().removeCaretListener(caretListener);
				} catch (Exception ex) {}
				SingleDocumentModel doc = model.getDocument(currentTabIndex);
				doc.getTextComponent().addCaretListener(caretListener);
				caretListener.caretUpdate(new CaretEvent(model.getDocument(currentTabIndex).getTextComponent()) {
					private static final long serialVersionUID = 1L;
					@Override
					public int getDot() {
						return doc.getTextComponent().getCaret().getDot();
					}
					@Override
					public int getMark() {
						return doc.getTextComponent().getCaret().getMark();
					}
				});

				enableOrDisableActions(true);
				Path path = model.getDocument(currentTabIndex).getFilePath();
				String file = "";
				if (path != null)
					file = path.toString();
				setTitle((path != null ? file : "(" + flp.getString("unnamedName") + ")") + "  -  JNotepad++");
			}
		});

		// when user changes language
		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				if (tabbedPane.getSelectedIndex() == -1)
					return;
				if (model.getCurrentDocument().getFilePath() == null) {
					setTitle("(" + flp.getString("unnamedName") + ")" + "  -  JNotepad++");
					tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), flp.getString("unnamedName"));
				}
			}
		});
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		
		JPanel center = new JPanel(new BorderLayout());
		add(center, BorderLayout.CENTER);
		
		// Tabbed pane
		tabbedPane = new DefaultMultipleDocumentModel();
		model = (MultipleDocumentModel) tabbedPane;

		model.createNewDocument();
		JScrollPane scrollPane = new JScrollPane(model.getCurrentDocument().getTextComponent());
		String title = flp.getString("unnamedName");
		ImageIcon icon = new Util().getIcon("icons/greenDisk.png");
		String tip = flp.getString("unnamedTooltip");
		tabbedPane.addTab(title, icon, scrollPane, tip);
		
		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				if (tabbedPane.getSelectedIndex() == -1) {
					return;
				}
				SingleDocumentModel doc = model.getDocument(0);
				boolean isNull = doc.getFilePath() == null;
				tabbedPane.setTitleAt(0, isNull ? flp.getString("unnamedName") : doc.getFilePath().getFileName().toString());
				tabbedPane.setToolTipTextAt(0,
						isNull ? flp.getString("unnamedTooltip") : doc.getFilePath().toAbsolutePath().toString());
			}
		});

		center.add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		// Status bar
		JPanel statusBar = new JPanel(new GridLayout(1,3));
		statusBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		center.add(statusBar, BorderLayout.SOUTH);
		
		length = new LJStatusLength("statusBarLen", flp);
		length.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		info = new LJStatusCarretInfo(
				"statusBarLine", "statusBarCol", 
				"statusBarSel", flp);
		info.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		
		model.getCurrentDocument().getTextComponent().addCaretListener(caretListener);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd   HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String text = formatter.format(date);
		JLabel time = new JLabel(text);
		time.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		time.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		
		timer = new Timer(500, e -> time.setText(formatter.format(new Date(System.currentTimeMillis()))));
		timer.setRepeats(true);
		timer.start();		
		
		statusBar.add(length);
		statusBar.add(info);
		statusBar.add(time);

		createActions();
		createMenus();
		createToolbars();
	}

	/**
	 * Enables or disables actions based on given boolean. Used when there are no
	 * open tabs to disable certain actions.
	 * 
	 * @param status boolean which indicates whether the actions will be enabled or disabled
	 */
	private void enableOrDisableActions(boolean status) {
		saveDocumentAction.setEnabled(status);
		saveAsDocumentAction.setEnabled(status);
		closeDocumentAction.setEnabled(status);
		cutTextAction.setEnabled(status);
		copyTextAction.setEnabled(status);
		pasteTextAction.setEnabled(status);
		statisticsAction.setEnabled(status);
	}
	
	/**
	 * Caret listener that updates the status bar.
	 */
	private CaretListener caretListener = new CaretListener() {
		@Override
		public void caretUpdate(CaretEvent e) {
			JTextArea editor = (JTextArea) e.getSource();
			Caret c = editor.getCaret();
			
			String text = editor.getText();
			int dot = c.getDot();
			int mark = c.getMark();
			
			int selectionLength = Math.abs(dot - mark);
			
			int ln = 0;
			int col = 0;
			for (int i = 0; i < dot; i++) {
				char ch = text.charAt(i);
				if (ch == '\n' || ch == '\r') {
					ln++;
					col = 0;
				} else {
					col++;
				}
			}
			
			info.setValues(ln, col, selectionLength);
			length.setLength(text.length());
			
			if (selectionLength == 0) {
				enableOrDisableTextSelection(false);
			} else {
				enableOrDisableTextSelection(true);
			}
		}
	};
	
	/**
	 * Enables or disables action based on given boolean. Used when there
	 * is text selection.
	 * @param status status boolean which indicates whether the actions will be enabled or disabled
	 */
	private void enableOrDisableTextSelection(boolean status) {
		cutTextAction.setEnabled(status);
		copyTextAction.setEnabled(status);
		uppercaseAction.setEnabled(status);
		lowercaseAction.setEnabled(status);
		invertCaseAction.setEnabled(status);
		sortAscendingAction.setEnabled(status);
		sortDescendingAction.setEnabled(status);
		uniqueAction.setEnabled(status);
	}

	/**
	 * Checks for open unsaved documents before exiting the program. Asks user to
	 * save each unsaved doucment.
	 */
	private void closeWindow() {
		String[] options = new String[] { flp.getString("exitYes"), flp.getString("exitNo"),
				flp.getString("exitCancel") };
		String message = flp.getString("exitMessage");
		String title = flp.getString("exitTitle");

		Iterator<SingleDocumentModel> iter = model.iterator();
		while (iter.hasNext()) {
			SingleDocumentModel m = iter.next();
			if (m.isModified()) {
				String file;
				Path path = m.getFilePath();
				if (path != null) {
					file = path.getFileName().toString();
				} else {
					file = flp.getString("unnamedName");
				}

				int result = JOptionPane.showOptionDialog(JNotepadPP.this, file + " " + message, title,
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

				switch (result) {
				case JOptionPane.CLOSED_OPTION:
					return;
				case 0:
					if (path == null) {
						saveDocument(m, null, true);
						iter = model.iterator();
					} else {
						model.saveDocument(m, path);
					}
					continue;
				case 1:
					continue;
				case 2:
					return;
				}
			}
		}
		timer.stop();
		dispose();
	}
	
	/**
	 * Saves the document to the disk
	 * @param doc document to be saved
	 * @param openedFilePath file path of document
	 * @param shoDialog true if a file chooser needs to open to select file path
	 */
	private void saveDocument(SingleDocumentModel doc, Path openedFilePath, boolean shoDialog) {
		if (openedFilePath == null) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle(flp.getString("dialogSaveAs"));
			if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				String[] options = new String[] {flp.getString("dialogOK")};
				JOptionPane.showOptionDialog(
						JNotepadPP.this, flp.getString("dialogNothingSaved"), 
						flp.getString("dialogWarning"), JOptionPane.OK_OPTION, 
						JOptionPane.WARNING_MESSAGE, null, options, options[0]);
				return;
			}
			openedFilePath = jfc.getSelectedFile().toPath();
		}

		if (shoDialog && Files.exists(openedFilePath)) {
			String[] options = new String[] { flp.getString("yes"), flp.getString("no"),
					flp.getString("cancel") };
			String message = flp.getString("dialogFileExists");
			String title = flp.getString("exitTitle");

			int result = JOptionPane.showOptionDialog(JNotepadPP.this, message, title, JOptionPane.DEFAULT_OPTION,
					JOptionPane.WARNING_MESSAGE, null, options, options[0]);

			switch (result) {
			case JOptionPane.CLOSED_OPTION:
				return;
			case 0:
				break;
			case 1:
				return;
			case 2:
				return;
			}
		}

		try {
			model.saveDocument(doc, openedFilePath);
			doc = model.getCurrentDocument();
			setTitle(doc.getFilePath().toString() + "  -  JNotepad++");
		} catch (Exception e1) {
			String[] options = new String[] {flp.getString("dialogOK")};
			String message = String.format("%s %s.\n%s", 
					flp.getString("dialogFileSaveFail1"), 
					openedFilePath.toFile().getAbsolutePath(), 
					flp.getString("dialogFileSaveFail2")
			);
			JOptionPane.showOptionDialog(
					JNotepadPP.this, message, 
					flp.getString("dialogInfo"), JOptionPane.OK_OPTION, 
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			return;
		}
		
		String[] options = new String[] {flp.getString("dialogOK")};
		JOptionPane.showOptionDialog(
				JNotepadPP.this, flp.getString("dialogFileSaved"), 
				flp.getString("dialogInfo"), JOptionPane.OK_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
	
	}

	/**
	 * Action which creates new blank document.
	 */
	private Action createBlankDocumentAction = new LocalizableAction("newDocument", "descCreate", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
			JScrollPane scrollPane = new JScrollPane(model.getCurrentDocument().getTextComponent());
			String title = flp.getString("unnamedName");
			ImageIcon icon = new Util().getIcon("icons/greenDisk.png");
			String tip = flp.getString("unnamedTooltip");
			tabbedPane.addTab(title, icon, scrollPane, tip);
			flp.addLocalizationListener(new ILocalizationListener() {
				@Override
				public void localizationChanged() {
					int index = tabbedPane.getTabCount() - 1;
					tabbedPane.setTitleAt(index, flp.getString("unnamedName"));
					tabbedPane.setToolTipTextAt(index, flp.getString("unnamedTooltip"));
				}
			});
			tabbedPane.setSelectedIndex(model.getNumberOfDocuments() - 1);
			enableOrDisableTextSelection(false);
			if (clipboard.equals("")) pasteTextAction.setEnabled(false);
		}
	};

	/**
	 * Action which opens and loads a document.
	 */
	private Action openDocumentAction = new LocalizableAction("openDocument", "descOpen", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("openFile"));
			if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			if (!Files.isReadable(filePath)) {
				String[] options = new String[] {flp.getString("dialogOK")};
				String message = String.format("%s %s %s", 
						flp.getString("dialogFileNotExists1"), 
						fileName.getAbsolutePath(),
						flp.getString("dialogFileNotExists2")
				);
				JOptionPane.showOptionDialog(
						JNotepadPP.this, message, 
						flp.getString("dialogError"), JOptionPane.OK_OPTION, 
						JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				
				JOptionPane.showMessageDialog(JNotepadPP.this,
						"Datoteka " + fileName.getAbsolutePath() + " ne postoji!", "Pogreška",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				model.loadDocument(filePath);
			} catch (Exception ex) {
				String[] options = new String[] {flp.getString("dialogOK")};
				String message = String.format("%s %s.", flp.getString("dialogFileReadError"), fileName.getAbsolutePath());
				JOptionPane.showOptionDialog(
						JNotepadPP.this, message, 
						flp.getString("dialogError"), JOptionPane.OK_OPTION, 
						JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				return;
			}
			
			enableOrDisableTextSelection(false);
			if (clipboard.equals("")) pasteTextAction.setEnabled(false);
		}
	};

	/**
	 * Action which saves document.
	 */
	private Action saveDocumentAction = new LocalizableAction("saveDocument", "descSave", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = model.getCurrentDocument();
			saveDocument(doc, doc.getFilePath(), doc.getFilePath() == null);
		}
	};

	/**
	 * Action which saves document but propmpts user for where to save it.
	 */
	private Action saveAsDocumentAction = new LocalizableAction("saveAsDocument", "descSaveAs", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			saveDocument(model.getCurrentDocument(), null, true);
		}
	};

	/**
	 * Action which closes currently selected document. Prompts user to save it
	 * before closing if the document is unsaved.
	 */
	private Action closeDocumentAction = new LocalizableAction("closeDocument", "descClose", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel m = model.getCurrentDocument();

			if (m.isModified()) {
				String file;
				Path path = m.getFilePath();
				if (path != null) {
					file = path.getFileName().toString();
				} else {
					file = flp.getString("unnamedName");
				}

				String[] options = new String[] { flp.getString("exitYes"), flp.getString("exitNo"),
						flp.getString("exitCancel") };
				String message = flp.getString("exitMessage");
				String title = flp.getString("exitTitle");

				int result = JOptionPane.showOptionDialog(JNotepadPP.this, file + " " + message, title,
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

				switch (result) {
				case JOptionPane.CLOSED_OPTION:
					return;
				case 0:
					if (path == null) {
						saveDocument(m, m.getFilePath(), m.getFilePath() == null);
					}
					break;
				case 1:
					break;
				case 2:
					return;
				}
			}

			model.closeDocument(model.getCurrentDocument());
		}
	};

	/**
	 * Action which exits the program.
	 */
	private Action exitAction = new LocalizableAction("exit", "descExit", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			closeWindow();
		}
	};

	/**
	 * Action whihc cuts selected text and stores it into clipboard.
	 */
	private Action cutTextAction = new LocalizableAction("cutText", "descCut", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
			if (len == 0)
				return;
			int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			try {
				clipboard = doc.getText(offset, len);
				doc.remove(offset, len);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			pasteTextAction.setEnabled(true);
		}
	};

	/**
	 * Action which copies selected text and stores it into clipboard.
	 */
	private Action copyTextAction = new LocalizableAction("copyText", "descCopy", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
			if (len == 0)
				return;
			int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			try {
				clipboard = doc.getText(offset, len);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			pasteTextAction.setEnabled(true);
		}
	};

	/**
	 * Action which pastes text from the clipboard.
	 */
	private Action pasteTextAction = new LocalizableAction("pasteText", "descPaste", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			Document doc = editor.getDocument();

			try {
				doc.insertString(editor.getCaret().getDot(), clipboard, null);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
	};

	/**
	 * Action which shows statistical information about currently opened document.
	 */
	private Action statisticsAction = new LocalizableAction("stats", "descStats", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = model.getCurrentDocument();
			String text = doc.getTextComponent().getText();

			int totalChars = text.length();
			int numOfNonBlank = 0;
			int numOfLines = totalChars > 0 ? 1 : 0;

			for (char c : text.toCharArray()) {
				if (!Character.isWhitespace(c))
					numOfNonBlank++;
				if (c == '\n' || c == '\r')
					numOfLines++;
			}

			String s1 = flp.getString("stats1");
			String s2 = flp.getString("stats2");
			String s3 = flp.getString("stats3");
			String s4 = flp.getString("stats4");
			String message = String.format("%s %d %s %d %s %d %s", s1, totalChars, s2, numOfNonBlank, s3, numOfLines,
					s4);

			String[] options = new String[] {flp.getString("dialogOK")};
			JOptionPane.showOptionDialog(
					JNotepadPP.this, message, 
					flp.getString("stats"), JOptionPane.OK_OPTION, 
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		}
	};
	
	/**
	 * Action which turns selected text to uppercase.
	 */
	private Action uppercaseAction = new LocalizableAction("uppercase", "descUppercase", flp) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeString(String::toUpperCase);
		}
	};
	
	/**
	 * Action which turns selected text to lowecase.
	 */
	private Action lowercaseAction = new LocalizableAction("lowercase", "descLowercase", flp) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeString(String::toLowerCase);
		}
	};
	
	/**
	 * Action which inverst case of selected text.
	 */
	private Action invertCaseAction = new LocalizableAction("invertCase", "descInvertCase", flp) {
	
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			changeString(s -> {
				char[] chars = s.toCharArray();
				for (int i = 0; i < chars.length; i++) {
					if (Character.isUpperCase(chars[i])) {
						chars[i] = Character.toLowerCase(chars[i]);
					} else if (Character.isLowerCase(chars[i])) {
						chars[i] = Character.toUpperCase(chars[i]);
					}
				}
				return new String(chars);
			});
		}
	};
	
	/**
	 * Changes the string in text area by calling giving function.
	 * @param func function that transforms string
	 */
	private void changeString(Function<String, String> func) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		Document doc = editor.getDocument();
		int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
		if (len == 0)
			return;
		int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		try {
			String text = func.apply(doc.getText(offset, len));
			
			doc.remove(offset, len);
			doc.insertString(offset, text, null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Action which sorts selected text ascending.
	 */
	private Action sortAscendingAction = new LocalizableAction("ascending", "descAscending", flp) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Locale locale = new Locale(flp.getCurrentLanguage());
			Collator collator = Collator.getInstance(locale);
			
			transformLines(s -> s.sorted((a,b) -> collator.compare(a,b)));
		}
	};
	
	/**
	 * Action which sorts selected text descending.
	 */
	private Action sortDescendingAction = new LocalizableAction("descending", "descDescending", flp) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Locale locale = new Locale(flp.getCurrentLanguage());
			Collator collator = Collator.getInstance(locale);
			
			transformLines(s -> s.sorted((a,b) -> -collator.compare(a,b)));
		}
	};
	
	/**
	 * Action which deletes duplicate lines from selected text.
	 */
	private Action uniqueAction = new LocalizableAction("unique", "descUnique", flp) {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			transformLines(s -> s.distinct());
		}
	};
	
	/**
	 * Transfors a stream of lines into another stream of lines
	 * using given function.
	 * @param func function which transforms stream of lines into different stream of lines
	 */
	public void transformLines(Function<Stream<String>, Stream<String>> func) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		Document doc = editor.getDocument();
		String text = editor.getText();
		
		int fromPos = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
		int toPos = Math.max(editor.getCaret().getDot(), editor.getCaret().getMark());
		
		// Find starting point
		for (int i = fromPos; i > 0; i--) {
			char c = text.charAt(i);
			fromPos--;
			if (c == '\n' || c == '\r') {
				fromPos = i+1;
				break;
			}
		}
		
		// Find end point
		for (int i = toPos, n = text.length(); i < n; i++) {
			char c = text.charAt(i);
			toPos++;
			if (c == '\n' || c == '\r') {
				toPos = i;
				break;
			}
		}
		
		String[] lines = text.substring(fromPos, toPos).split("\\r\\n|[\\n\\x0B\\x0C\\r\\u0085\\u2028\\u2029]");
		Stream<String> stream = Stream.of(lines);
		List<String> list = func.apply(stream).collect(Collectors.toList());
		
		StringBuilder sb = new StringBuilder();
		list.forEach(s -> sb.append(s).append("\n"));
		String sorted = sb.toString();
		try {
			doc.remove(fromPos, toPos-fromPos);
			doc.insertString(fromPos, sorted.substring(0, sorted.length()-1), null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Adds properties to actions.
	 */
	private void createActions() {
		enableOrDisableTextSelection(false);
		pasteTextAction.setEnabled(false);
		
		createBlankDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt S"));
		closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
		cutTextAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		copyTextAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		pasteTextAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		uppercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt U"));
		lowercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt L"));
		invertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt I"));
		sortAscendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt A"));
		sortDescendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt D"));
		uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		
		createBlankDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F4);
		cutTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
		copyTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		pasteTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
		statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		uppercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);
		lowercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
		invertCaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);
		sortAscendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		sortDescendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);
		uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
	}

	/**
	 * Creates the menus.
	 */
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu(flp.getString("menuFile"));
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(createBlankDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.add(new JMenuItem(exitAction));

		JMenu editMenu = new JMenu(flp.getString("menuEdit"));
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(cutTextAction));
		editMenu.add(new JMenuItem(copyTextAction));
		editMenu.add(new JMenuItem(pasteTextAction));

		JMenu toolsMenu = new JMenu(flp.getString("menuTools"));
		menuBar.add(toolsMenu);

		toolsMenu.add(new JMenuItem(statisticsAction));
		
		JMenu changeCaseMenu = new JMenu(flp.getString("menuChangeCase"));
		toolsMenu.add(changeCaseMenu);
		
		changeCaseMenu.add(new JMenuItem(uppercaseAction));
		changeCaseMenu.add(new JMenuItem(lowercaseAction));
		changeCaseMenu.add(new JMenuItem(invertCaseAction));
		
		JMenu sortMenu = new JMenu(flp.getString("menuSort"));
		toolsMenu.add(sortMenu);
		
		sortMenu.add(new JMenuItem(sortAscendingAction));
		sortMenu.add(new JMenuItem(sortDescendingAction));
		
		toolsMenu.add(new JMenuItem(uniqueAction));

		JMenu langMenu = createLanguagesMenu();
		menuBar.add(langMenu);

		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				fileMenu.setText(flp.getString("menuFile"));
				editMenu.setText(flp.getString("menuEdit"));
				toolsMenu.setText(flp.getString("menuTools"));
				langMenu.setText(flp.getString("menuLang"));
				changeCaseMenu.setText(flp.getString("menuChangeCase"));
				sortMenu.setText(flp.getString("menuSort"));
			}
		});

		this.setJMenuBar(menuBar);
	}

	/**
	 * Creates the language menu.
	 * 
	 * @return language menu
	 */
	private JMenu createLanguagesMenu() {
		JMenu languageMenu = new JMenu(flp.getString("menuLang"));

		JMenuItem hr = new JMenuItem(new LocalizableAction("langHr", "langHr", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
		});
		languageMenu.add(hr);

		JMenuItem en = new JMenuItem(new LocalizableAction("langEn", "langEn", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		});
		languageMenu.add(en);

		JMenuItem de = new JMenuItem(new LocalizableAction("langDe", "langDe", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		});
		languageMenu.add(de);

		return languageMenu;
	}

	/**
	 * Creates the toolbars.
	 */
	private void createToolbars() {
		JToolBar fileToolBar = new JToolBar(flp.getString("toolbarFile"));
		fileToolBar.setFloatable(true);

		fileToolBar.add(new JButton(createBlankDocumentAction));
		fileToolBar.add(new JButton(openDocumentAction));
		fileToolBar.add(new JButton(saveDocumentAction));
		fileToolBar.add(new JButton(saveAsDocumentAction));
		fileToolBar.add(new JButton(closeDocumentAction));
		fileToolBar.add(new JButton(exitAction));
		fileToolBar.addSeparator();
		fileToolBar.add(new JButton(cutTextAction));
		fileToolBar.add(new JButton(copyTextAction));
		fileToolBar.add(new JButton(pasteTextAction));
		fileToolBar.addSeparator();
		fileToolBar.add(new JButton(statisticsAction));

		this.getContentPane().add(fileToolBar, BorderLayout.PAGE_START);
	}

	/**
	 * Starting point of program
	 * 
	 * @param args command line arguments // not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			LocalizationProvider.getInstance().setLanguage("en");
			new JNotepadPP().setVisible(true);
		});
	}
}
