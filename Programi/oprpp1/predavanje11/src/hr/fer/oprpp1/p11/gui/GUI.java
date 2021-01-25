package hr.fer.oprpp1.p11.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.TableRowSorter;

public class GUI extends JFrame {

	private static final long serialVersionUID = 9221969213729238461L;
	
	private JTable tablica;
	private TableRowSorter<ModelTabliceMjesecaUGodini> sorter;

	public GUI() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500,500);
		
		initGUI();
	}

	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel gornji = new JPanel(new GridLayout(1,0));
		JRadioButton btn1 = new JRadioButton("Prikaži sve");
		JRadioButton btn2 = new JRadioButton("Prikaži poželjne");
		JRadioButton btn3 = new JRadioButton("Prikaži nepoželjne");
		gornji.add(btn1);
		gornji.add(btn2);
		gornji.add(btn3);
		btn1.setActionCommand("sve");
		btn2.setActionCommand("p");
		btn3.setActionCommand("n");
		
		ButtonGroup grupa = new ButtonGroup();
		grupa.add(btn1);
		grupa.add(btn2);
		grupa.add(btn3);
		
		ItemListener zajednici = e -> {
			azurirajStatusFiltriranja((JRadioButton) e.getSource());
		};
		
		btn1.addItemListener(zajednici);
		btn2.addItemListener(zajednici);
		btn3.addItemListener(zajednici);
		
		btn1.setSelected(true);
		
		ModelTabliceMjesecaUGodini model = new ModelTabliceMjesecaUGodini();
		tablica = new JTable(model);
		sorter = new TableRowSorter<>(model);
		tablica.setRowSorter(sorter);
		
		JScrollPane jsp = new JScrollPane(tablica);
		jsp.setPreferredSize(tablica.getPreferredSize());
		
		cp.add(gornji, BorderLayout.PAGE_START);
		cp.add(jsp, BorderLayout.CENTER);
		
		tablica.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int indeksRetkaVizualno = tablica.getSelectedRow();
				int indeksRetkaUModelu = tablica.convertRowIndexToModel(indeksRetkaVizualno);
				System.out.println("Kliknuta tablica; sel redak (vizualno) = " + indeksRetkaVizualno + " što je u modelu redak = " + indeksRetkaUModelu);
				System.out.println(model.getValueAt(indeksRetkaUModelu, 1));
			}
		});
	}
	
	private void azurirajStatusFiltriranja(JRadioButton btn) {
		if (!btn.isSelected() || sorter == null) return;
		switch(btn.getActionCommand()) {
		case "sve": sorter.setRowFilter(null); return;
		case "p": sorter.setRowFilter(new FilterPozeljnihMjeseci(true)); return;
		case "n": sorter.setRowFilter(new FilterPozeljnihMjeseci(false)); return;
		}
	}

	private static class FilterPozeljnihMjeseci extends RowFilter<ModelTabliceMjesecaUGodini, Integer> {
		private boolean ukljuciPozeljne;
		
		public FilterPozeljnihMjeseci(boolean ukljuciPozeljne) {
			super();
			this.ukljuciPozeljne = ukljuciPozeljne;
		}
		
		@Override
		public boolean include(Entry<? extends ModelTabliceMjesecaUGodini, ? extends Integer> entry) {
			boolean p = (Boolean) entry.getValue(3);
			
			return ukljuciPozeljne && p || !ukljuciPozeljne && !p;
		}
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() ->
				new GUI().setVisible(true)
		);
	}
}
