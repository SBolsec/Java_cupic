package hr.fer.oprpp1.hw08.local;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.oprpp1.hw08.local.swing.LocalizableAction;

public class Prozor extends SomeFrame {

	private static final long serialVersionUID = 1L;
	
	private JButton gumb;
	
	public Prozor() throws HeadlessException {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(0,0);
		setTitle("Demo");
		
		initGUI();
		
		pack();
	
		flp.addLocalizationListener(new ILocalizationListener() {
			@Override
			public void localizationChanged() {
				gumb.setText(LocalizationProvider.getInstance().getString("login"));
			}
		});
	}
	
	private void initGUI() {
		getContentPane().setLayout(new BorderLayout());

		
		gumb = new JButton(
				new LocalizableAction("login", flp) {

					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
					}
				}
		);
		getContentPane().add(gumb, BorderLayout.CENTER);
		
		gumb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Napravi prijavu...
			}
		});
		
		createActions();
		createMenus();
	}
	
	private void createActions() {
		
	}
	
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu languageMenu = new JMenu("Languages");
		menuBar.add(languageMenu);
		
		JMenuItem hr = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("hr");
			}
		});
		hr.setText("hr");
		languageMenu.add(hr);
		JMenuItem en = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("en");
			}
		});
		en.setText("en");
		languageMenu.add(en);
		JMenuItem de = new JMenuItem(new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalizationProvider.getInstance().setLanguage("de");
			}
		});
		de.setText("de");
		languageMenu.add(de);
		
		this.setJMenuBar(menuBar);
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Očekivao sam oznaku jezika kao argument!");
			System.err.println("Zadajte kao parametar hr ili en.");
			System.exit(-1);
		}
		final String jezik = args[0];
		SwingUtilities.invokeLater(() -> {
			LocalizationProvider.getInstance().setLanguage(jezik);
			new Prozor().setVisible(true);
		});
	}
}
