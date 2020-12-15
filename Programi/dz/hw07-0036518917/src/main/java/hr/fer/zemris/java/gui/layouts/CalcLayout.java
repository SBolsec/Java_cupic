package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Function;

/**
 * Layout that has 5 rows and 7 columns and it suited
 * for a simple calculator.
 * @author sbolsec
 *
 */
public class CalcLayout implements LayoutManager2 {
	/** Gap between rows and columns **/
	private int gap;
	/** Components in this layout **/
	private Map<Component, RCPosition> components = new HashMap<>();
	
	/**
	 * Default constructor, sets gap between rows and columns to 0.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * Constructor which initializes the gap between rows and columns.
	 * @param gap
	 */
	public CalcLayout(int gap) {
		super();
		this.gap = gap;
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition p = null;
		
		if (constraints instanceof RCPosition) {
			p = (RCPosition) constraints;
		} else if (constraints instanceof String) {
			try {
				p = RCPosition.parse(constraints.toString());
			} catch (NullPointerException | IllegalArgumentException e) {
				throw new IllegalArgumentException("String could not be parsed as RCPosition, it was: " + constraints.toString());
			}
		} else {
			throw new IllegalArgumentException("Constraints were neither a string nor RCPosition!");
		}
		
		if (p == null) 
			throw new NullPointerException("RCPosition can not be null!");
		if (comp == null)
			throw new NullPointerException("Component can not be null!");
		
		int r = p.getRow();
		int c = p.getColumn();
		
		
		if (r < 1 || r > 5)
			throw new CalcLayoutException("Row was invalid, it must be between 1 and 5, it was: " + r + ".");
		if (c < 1 || c > 7)
			throw new CalcLayoutException("Column was invalid, it must be between 1 and 7, it was: " + c + ".");
		if (r == 1 && (c > 1 && c < 6))
			throw new CalcLayoutException("Row was 1, so column can not be between 2 and 5, it was: " + c + ".");
		if (components.containsValue(p) || components.containsKey(comp))
			throw new CalcLayoutException("This position was already used! It was: (" + r + ", " + c + ").");
		
		components.put(comp, p);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		components.remove(comp); 
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calculateWidthAndHeight(c -> c.getPreferredSize(), parent.getInsets());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateWidthAndHeight(c -> c.getMinimumSize(), parent.getInsets());
	}
	
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateWidthAndHeight(c -> c.getMaximumSize(), target.getInsets());
	}
	
	private Dimension calculateWidthAndHeight(Function<Component, Dimension> function, Insets ins) {
		double width = 0, height;
		
		boolean first = true;
		for (Map.Entry<Component, RCPosition> entry : components.entrySet()) {
			double newWidth = function.apply(entry.getKey()).getWidth();
			RCPosition e = entry.getValue();
			if (e.getRow() == 1 && e.getColumn() == 1) {
				newWidth = (newWidth - (4*gap)) / 5.;
			}
			if (first) {
				width = newWidth;
				first = false;
				continue;
			}
			if (newWidth > width) {
				width = newWidth;
			}
		}
		width = width*7 + gap*6;
		
		OptionalDouble maxHeight = components.keySet().stream().mapToDouble(c -> function.apply(c).getHeight()).max();
		height = maxHeight.getAsDouble() * 5 + gap*4;
		
		
		width += ins.left + ins.right;
		height += ins.top + ins.bottom;
		return new Dimension((int) width, (int) height);
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}
	
	@Override
	public void layoutContainer(Container parent) {
		Insets ins = parent.getInsets();
		Dimension dim = parent.getSize();
		double dx = (dim.getWidth() - 6*gap) / 7.;
		double dy = (dim.getHeight() - 4*gap) / 5.;
		int remainder = (int) ((dim.getWidth() - 6*gap) % 7);
		
		class Pomocna {
			public Component c;
			public int x;
			public int y;
			public int w;
			public int h;
			
			public Pomocna(Component c, int x, int y, int w, int h) {
				super();
				this.c = c;
				this.x = x;
				this.y = y;
				this.w = w;
				this.h = h;
			}
		}
		
		Pomocna[][] polje = new Pomocna[5][7];
		
		// Calculating temporary data
		for (Map.Entry<Component, RCPosition> entry : components.entrySet()) {
			RCPosition p = entry.getValue();
			Component c = entry.getKey();
			double x, y;
			double w = dx;
			
			int px = p.getRow();
			int py = p.getColumn();
			
			if (px == 1 && py == 1) {
				x = ins.left;
				w = dx*5 + gap*4;
				
				if (remainder <= 4) w += 3;
				else if (remainder == 5) w += 4;
				else if (remainder == 6) w += 5;
			} else {
				if (px == 1 && py == 6) {
					if (remainder >= 4) w += 1;
				}
				x = ins.left + (py-1)*dx + (py-2)*gap;
			}
			
			y = ins.top + (px-1)*dy + (px-2)*gap;
			polje[px-1][py-1] = new Pomocna(c, (int) x, (int) y, (int) w, (int) dy);
		}
		
		// Uniform increase
		for (int i = 1; i < 5; i++) {
			int count = (int) ((dim.getWidth() - 6*gap) % 7);
			int j = 0;
			while (count > 0) {
				count--;
				Pomocna p = polje[i][j];
				if (p != null) {
					p.w = p.w+1;
				}
				j += 2;
				if (j > 6)
					j = 1;
			}
		}
		
		// Setting bounds
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				Pomocna p = polje[i][j];
				if (p != null) {
					p.c.setBounds(p.x, p.y, p.w, p.h);
				}
			}
		}
	}

	@Override
	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub
		
	}

}
