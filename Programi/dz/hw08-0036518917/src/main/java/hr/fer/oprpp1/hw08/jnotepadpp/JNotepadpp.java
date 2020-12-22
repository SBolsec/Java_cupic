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
import javax.swing.Icon;
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
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import hr.fer.oprpp1.hw08.jnotepadpp.document.DefaultMultipleDocumentModel;
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
public class JNotepadpp extends LJFrame {

	/** Generated serial version UID **/
	private static final long serialVersionUID = 2115546573199433581L;
	/** Provides localization **/
	private final FormLocalizationProvider flp = this.getFormLocalizationProvider();
	/** Model for working with multiple documents, also adds support for tabs **/
	private DefaultMultipleDocumentModel model = new DefaultMultipleDocumentModel();
	/** Clipboard used by cut, copy and paste **/
	private String clipboard = "";

	/**
	 * Constructor.
	 */
	public JNotepadpp() {
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

		model.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				int selectedIndex = tabbedPane.getSelectedIndex();

				if (selectedIndex == -1) {
					setTitle("JNotepad++");
					return;
				}

				Path path = model.getDocument(selectedIndex).getFilePath();
				String file = "";
				if (path != null)
					file = path.toString();
				setTitle((path != null ? file : "(" + flp.getString("unnamedName") + ")") + "  -  JNotepad++");
			}
		});

		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
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

		model.createNewDocument();
		JScrollPane scrollPane = new JScrollPane(model.getCurrentDocument().getTextComponent());
		String title = flp.getString("unnamedName");
		Icon icon = null;
		String tip = flp.getString("unnamedTooltip");
		model.addTab(title, icon, scrollPane, tip);
		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				SingleDocumentModel doc = model.getDocument(0);
				boolean isNull = doc.getFilePath() == null;
				model.setTitleAt(0, isNull ? flp.getString("unnamedName") : doc.getFilePath().getFileName().toString());
				model.setToolTipTextAt(0,
						isNull ? flp.getString("unnamedTooltip") : doc.getFilePath().toAbsolutePath().toString());
			}
		});

		add(model, BorderLayout.CENTER);
		model.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		createActions();
		createMenus();
		createToolbars();
	}

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

				int result = JOptionPane.showOptionDialog(JNotepadpp.this, file + " " + message, title,
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

				switch (result) {
				case JOptionPane.CLOSED_OPTION:
					return;
				case 0:
					if (path == null) {
						SingleDocumentModel doc = m;
						Path openedFilePath;
						JFileChooser jfc = new JFileChooser();
						jfc.setDialogTitle("Save document as");
						if (jfc.showSaveDialog(JNotepadpp.this) != JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(JNotepadpp.this, "Ništa nije snimljeno.", "Upozorenje",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
						openedFilePath = jfc.getSelectedFile().toPath();

						try {
							model.saveDocument(doc, openedFilePath);
							
							int index = model.getSelectedIndex();
							doc = model.getDocument(index);
							model.setTitleAt(index, doc.getFilePath().getFileName().toString());
							model.setToolTipTextAt(index, doc.getFilePath().toString());
							setTitle(doc.getFilePath().toString() + "  -  JNotepad++");
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(JNotepadpp.this,
									"Pogreška prilikom zapisivanja datoteke " + openedFilePath.toFile().getAbsolutePath()
											+ ".\nPažnja: nije jasno u kojem je stanju datoteka na disku!",
									"Pogreška", JOptionPane.ERROR_MESSAGE);
							return;
						}
						JOptionPane.showMessageDialog(JNotepadpp.this, "Datoteka je snimljena.", "Informacija",
								JOptionPane.INFORMATION_MESSAGE);
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

	private Action createBlankDocumentAction = new LocalizableAction("newDocument", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			model.createNewDocument();
			JScrollPane scrollPane = new JScrollPane(model.getCurrentDocument().getTextComponent());
			String title = flp.getString("unnamedName");
			Icon icon = null;
			String tip = flp.getString("unnamedTooltip");
			model.addTab(title, icon, scrollPane, tip);
			flp.addLocalizationListener(new ILocalizationListener() {
				@Override
				public void localizationChanged() {
					int index = model.getTabCount() - 1;
					model.setTitleAt(index, flp.getString("unnamedName"));
					model.setToolTipTextAt(index, flp.getString("unnamedTooltip"));
				}
			});
			model.setSelectedIndex(model.getNumberOfDocuments() - 1);
		}
	};

	private Action openDocumentAction = new LocalizableAction("openDocument", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle(flp.getString("openFile"));
			if (fc.showOpenDialog(JNotepadpp.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(JNotepadpp.this,
						"Datoteka " + fileName.getAbsolutePath() + " ne postoji!", "Pogreška",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				model.loadDocument(filePath);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JNotepadpp.this,
						"Pogreška prilikom čitanja datoteke " + fileName.getAbsolutePath() + ".", "Pogreška",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	};

	private Action saveDocumentAction = new LocalizableAction("saveDocument", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = model.getCurrentDocument();
			Path openedFilePath = doc.getFilePath();
			if (openedFilePath == null) {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save document");
				if (jfc.showSaveDialog(JNotepadpp.this) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(JNotepadpp.this, "Ništa nije snimljeno.", "Upozorenje",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				openedFilePath = jfc.getSelectedFile().toPath();
			}

			try {
				model.saveDocument(doc, openedFilePath);
				
				int index = model.getSelectedIndex();
				doc = model.getDocument(index);
				model.setTitleAt(index, doc.getFilePath().getFileName().toString());
				model.setToolTipTextAt(index, doc.getFilePath().toString());
				setTitle(doc.getFilePath().toString() + "  -  JNotepad++");
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(JNotepadpp.this,
						"Pogreška prilikom zapisivanja datoteke " + openedFilePath.toFile().getAbsolutePath()
								+ ".\nPažnja: nije jasno u kojem je stanju datoteka na disku!",
						"Pogreška", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(JNotepadpp.this, "Datoteka je snimljena.", "Informacija",
					JOptionPane.INFORMATION_MESSAGE);

		}
	};

	private Action saveAsDocumentAction = new LocalizableAction("saveAsDocument", flp) {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			SingleDocumentModel doc = model.getCurrentDocument();
			Path openedFilePath;
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Save document as");
			if (jfc.showSaveDialog(JNotepadpp.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepadpp.this, "Ništa nije snimljeno.", "Upozorenje",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			openedFilePath = jfc.getSelectedFile().toPath();

			try {
				model.saveDocument(doc, openedFilePath);
				
				int index = model.getSelectedIndex();
				doc = model.getDocument(index);
				model.setTitleAt(index, doc.getFilePath().getFileName().toString());
				model.setToolTipTextAt(index, doc.getFilePath().toString());
				setTitle(doc.getFilePath().toString() + "  -  JNotepad++");
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(JNotepadpp.this,
						"Pogreška prilikom zapisivanja datoteke " + openedFilePath.toFile().getAbsolutePath()
								+ ".\nPažnja: nije jasno u kojem je stanju datoteka na disku!",
						"Pogreška", JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(JNotepadpp.this, "Datoteka je snimljena.", "Informacija",
					JOptionPane.INFORMATION_MESSAGE);

		}
	};

	private Action closeDocument = new LocalizableAction("closeDocument", flp) {

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

				int result = JOptionPane.showOptionDialog(JNotepadpp.this, file + " " + message, title,
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);

				switch (result) {
				case JOptionPane.CLOSED_OPTION:
					return;
				case 0:
					if (path == null)
						path = Path.of(""); // TODO pitaj kroz dialog
					model.saveDocument(m, path);
					break;
				case 1:
					break;
				case 2:
					return;
				}
			}

			model.closeDocument(m);
		}
	};

	private Action cutText = new LocalizableAction("cutText", flp) {

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

	private Action copyText = new LocalizableAction("copyText", flp) {

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

	private Action pasteText = new LocalizableAction("pasteText", flp) {

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

	private Action statisticsAction = new LocalizableAction("stats", flp) {

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

			JOptionPane.showMessageDialog(JNotepadpp.this, message, flp.getString("stats"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	};

	/**
	 * Adds properties to actions.
	 */
	private void createActions() {
		createBlankDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control n"));
		createBlankDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0);
		createBlankDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descCreate"));

		openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control o"));
		openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_1);
		openDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descOpen"));

		saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control s"));
		saveDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_2);
		saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descSave"));

		saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift s"));
		saveAsDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_3);
		saveAsDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descSaveAs"));

		closeDocument.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control x"));
		closeDocument.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_4);
		closeDocument.putValue(Action.SHORT_DESCRIPTION, flp.getString("descClose"));

		cutText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control w"));
		cutText.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_5);
		cutText.putValue(Action.SHORT_DESCRIPTION, flp.getString("descCut"));

		copyText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control c"));
		copyText.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_6);
		copyText.putValue(Action.SHORT_DESCRIPTION, flp.getString("descCopy"));

		pasteText.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control v"));
		pasteText.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_7);
		pasteText.putValue(Action.SHORT_DESCRIPTION, flp.getString("descPaste"));

		statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control i"));
		statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_8);
		statisticsAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descStats"));

		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				createBlankDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descCreate"));
				openDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descOpen"));
				saveDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descSave"));
				saveAsDocumentAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descSaveAs"));
				closeDocument.putValue(Action.SHORT_DESCRIPTION, flp.getString("descClose"));
				cutText.putValue(Action.SHORT_DESCRIPTION, flp.getString("descCut"));
				copyText.putValue(Action.SHORT_DESCRIPTION, flp.getString("descCopy"));
				pasteText.putValue(Action.SHORT_DESCRIPTION, flp.getString("descPaste"));
				statisticsAction.putValue(Action.SHORT_DESCRIPTION, flp.getString("descCut"));
			}
		});
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
		fileMenu.add(new JMenuItem(closeDocument));

		JMenu editMenu = new JMenu(flp.getString("menuEdit"));
		menuBar.add(editMenu);

		editMenu.add(new JMenuItem(cutText));
		editMenu.add(new JMenuItem(copyText));
		editMenu.add(new JMenuItem(pasteText));

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

		JMenuItem hr = new JMenuItem(new LocalizableAction("langHr", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
		});
		languageMenu.add(hr);

		JMenuItem en = new JMenuItem(new LocalizableAction("langEn", flp) {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		});
		languageMenu.add(en);

		JMenuItem de = new JMenuItem(new LocalizableAction("langDe", flp) {

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
		toolBar.add(new JButton(closeDocument));
		toolBar.addSeparator();
		toolBar.add(new JButton(cutText));
		toolBar.add(new JButton(copyText));
		toolBar.add(new JButton(pasteText));
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
			new JNotepadpp().setVisible(true);
		});
	}
}
