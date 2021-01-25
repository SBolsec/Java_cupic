package hr.fer.oprpp1.p11.gui;

import javax.swing.table.AbstractTableModel;

import hr.fer.oprpp1.p11.model.Mjesec;

public class ModelTabliceMjesecaUGodini extends AbstractTableModel {
	
	private static final long serialVersionUID = 8808734293697050665L;
	
	private Mjesec[] mjeseci = {
			new Mjesec(1, "Siječanj", 31, true),
			new Mjesec(2, "Veljača", 28, false),
			new Mjesec(3, "Ožujak", 31, false),
			new Mjesec(4, "Travanj", 30, false),
			new Mjesec(5, "Svibanj", 31, false),
			new Mjesec(6, "Lipanj", 30, false),
			new Mjesec(7, "Srpanj", 31, true),
			new Mjesec(8, "Kolovoz", 31, true),
			new Mjesec(9, "Rujan", 30, true),
			new Mjesec(10, "Listopad", 31, false),
			new Mjesec(11, "Studeni", 30, false),
			new Mjesec(12, "Prosinac", 31, true)
	};

	@Override
	public int getRowCount() {
		return mjeseci.length;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}
	
	@Override
	public String getColumnName(int column) {
		switch(column) {
		case 0: return "Broj u godini";
		case 1: return "Naziv";
		case 2: return "Broj dana u mjesecu";
		case 3: return "Poželjan za godišnji";
		}
		throw new IllegalArgumentException("Tražen nepostojeći stupac: " + column);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
		case 0: return Integer.class;
		case 1: return String.class;
		case 2: return Integer.class;
		case 3: return Boolean.class;
		}
		throw new IllegalArgumentException("Tražen nepostojeći stupac: " + columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1 || columnIndex == 3;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return switch (columnIndex) {
		case 0 -> mjeseci[rowIndex].getRedniBrojUGodini();
		case 1 -> mjeseci[rowIndex].getNaziv();
		case 2 -> mjeseci[rowIndex].getBrojDana();
		case 3 -> mjeseci[rowIndex].isPozeljanZaGodisnji();
		default ->
			throw new IllegalArgumentException("Unexpected value: " + columnIndex);
		};
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch(columnIndex) {
		case 1: {
			String novaVrijednost = (String) aValue;
			mjeseci[rowIndex].setNaziv(novaVrijednost);
			break;
		}
		case 3: {
			Boolean novaVrijednost = (Boolean) aValue;
			mjeseci[rowIndex].setPozeljanZaGodisnji(novaVrijednost);
			break;
		}
		default: return;
		}
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}
