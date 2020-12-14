package hr.fer.zemris.tecaj.swing.p03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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

public class Prozor2 extends JFrame {

	private static final long serialVersionUID = 1L;

	public Prozor2() {
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
		
		MojModelListe<Integer> modelListe = new MojModelListe<>();
		modelListe.add(52);
		modelListe.add(78);
		modelListe.add(34);
		modelListe.add(111);
		
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
			modelListe.add(slucajni);
		});
	}
	
	private static class ModelListePrirodnihBrojeva implements ListModel<Integer> {
		private int n;
		
		public ModelListePrirodnihBrojeva(int n) {
			super();
			this.n = n;
		}
		
		
		@Override
		public int getSize() {
			return n;
		}

		@Override
		public Integer getElementAt(int index) {
			return index + 1;
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			// TODO Auto-generated method stub
			
		}
		
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
	
	private static class MojModelListe<E> implements ListModel<E> {

		private List<E> mojiPodatci = new ArrayList<>();
		private List<ListDataListener> promatraci = new ArrayList<>();
		
		public void add(E element) {
			mojiPodatci.add(element);
			System.out.println("Dodan je broj " + element);
			
			ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, mojiPodatci.size()-1, mojiPodatci.size());
			for (ListDataListener l : promatraci) {
				System.out.println("Obavijestavam nekoga...");
				l.intervalAdded(e);
			}
		}
		
		@Override
		public int getSize() {
			return mojiPodatci.size();
		}

		@Override
		public E getElementAt(int index) {
			return mojiPodatci.get(index);
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			System.out.println("Netko se na mene zakvacio");
			promatraci.add(l);
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			promatraci.remove(l);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{
			new Prozor2().setVisible(true);
		});

//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				Prozor1 prozor = new Prozor1();
//				prozor.setVisible(true);
//			}
//		});
	}
}
