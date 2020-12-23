package hr.fer.oprpp1.hw08.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.oprpp1.hw08.jnotepadpp.document.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.document.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.document.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.swing.LJFrame;
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

	/**
	 * Constructor.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocation(0, 0);
		setSize(900, 600);
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
				int selectedIndex = tabbedPane.getSelectedIndex();

				if (selectedIndex == -1) {
					setTitle("JNotepad++");
					enableOrDisableActions(false);
					return;
				}

				enableOrDisableActions(true);
				Path path = model.getDocument(selectedIndex).getFilePath();
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
				}
			}
		});
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());
		
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

		add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		createActions();
		createMenus();
		createToolbars();
	}

	/**
	 * Enables or disables actions based on given boolean. Used when there are no
	 * open tabs to disable certain actions.
	 * 
	 * @param status boolean which indicates whether the actions will be enabled or
	 *               disabled
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
	 * Adds properties to actions.
	 */
	private void createActions() {
		createBlankDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0);
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_1);
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_2);
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_3);
		closeDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_4);
		exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_5);
		cutTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_6);
		copyTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_7);
		pasteTextAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_8);
		statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_9);
		
		//TODO keyboard shortcuts
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

		JMenu langMenu = createLanguagesMenu();
		menuBar.add(langMenu);

		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				fileMenu.setText(flp.getString("menuFile"));
				editMenu.setText(flp.getString("menuEdit"));
				toolsMenu.setText(flp.getString("menuTools"));
				langMenu.setText(flp.getString("menuLang"));
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
		JToolBar toolBar = new JToolBar(flp.getString("toolbar"));
		toolBar.setFloatable(true);

		toolBar.add(new JButton(createBlankDocumentAction));
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.add(new JButton(saveAsDocumentAction));
		toolBar.add(new JButton(closeDocumentAction));
		toolBar.add(new JButton(exitAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(cutTextAction));
		toolBar.add(new JButton(copyTextAction));
		toolBar.add(new JButton(pasteTextAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(statisticsAction));

		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
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
