package hr.fer.zemris.tecaj.swing.p03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.table.TableModel;

public class Prozor3 extends JFrame {

	private static final long serialVersionUID = 1L;

	public Prozor3() {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Prozor1");
		setLocation(20, 20);
		setSize(500, 200);
		initGUI();
	}
	
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		DefaultListModel<Integer> modelListe = new DefaultListModel<>();
		//MojModelListe<Integer> modelListe = new MojModelListe<>();
		modelListe.addElement(52);
		modelListe.addElement(78);
		modelListe.addElement(34);
		modelListe.addElement(111);
		
//		ModelListePrirodnihBrojeva modelListe = new ModelListePrirodnihBrojeva(20);
		
		JBrojElemenata be = new JBrojElemenata(modelListe);
		cp.add(be, BorderLayout.PAGE_START);
		
		JList<Integer> lista1 = new JList<>(modelListe);
		JList<Integer> lista2 = new JList<>(modelListe);
		lista1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		lista2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		lista2.setSelectionModel(lista1.getSelectionModel());
		
		JPanel p = new JPanel(new GridLayout(1, 0));
		p.add(new JScrollPane(lista1));
		p.add(new JScrollPane(lista2));
		
		cp.add(p, BorderLayout.CENTER);
		
		JButton dodavanje = new JButton("Dodaj random cijeli broj!");
		cp.add(dodavanje, BorderLayout.SOUTH);
		
		dodavanje.addActionListener(e -> {
			int slucajni = (int) (Math.random() * 1000 + 0.5);
			modelListe.addElement(slucajni);
		});
	}
	
	static class JBrojElemenata extends JComponent implements ListDataListener {

		private ListModel<?> model;
		
		public JBrojElemenata(ListModel<?> model) {
			super();
			this.model = model;
			model.addListDataListener(this);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(getForeground());
			String broj = String.valueOf(model.getSize());
			
			FontMetrics fm = g.getFontMetrics();
			g.drawString(broj, 3, fm.getAscent());
		}
		
		@Override
		public void intervalAdded(ListDataEvent e) {
			repaint();
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			repaint();
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			repaint();
		}
		
	}
	
	// DefaultListModel
	private static class MojModelListe<E> extends AbstractListModel<E> {
		private List<E> mojiPodatci = new ArrayList<>();
		
		public void addElement(E element) {
			mojiPodatci.add(element);
			System.out.println("Dodan je broj " + element);
			
			fireIntervalAdded(this, mojiPodatci.size()-1, mojiPodatci.size()-1);
		}
		
		@Override
		public int getSize() {
			return mojiPodatci.size();
		}

		@Override
		public E getElementAt(int index) {
			return mojiPodatci.get(index);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new Prozor3().setVisible(true);
		});
	}
}
