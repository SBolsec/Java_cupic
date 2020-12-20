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
	private final int gap;
	/** Components in this layout **/
	private final Map<Component, RCPosition> components;
	
	/**
	 * Default constructor, sets gap between rows and columns to 0.
	 */
	public CalcLayout() {
		this(0);
	}
	
	/**
	 * Constructor which initializes the gap between rows and columns.
	 * @param gap gap between rows and columns
	 */
	public CalcLayout(int gap) {
		super();
		this.gap = gap;
		components = new HashMap<>();
	}
	
	/**
	 * Throws <code>UnsupportedOperationException</code>
	 * @throws UnsupportedOperationException always
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @throws CalcLayoutException if arguments violated a constraint of this layout
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition p;
		
		if (comp == null)
			throw new NullPointerException("Component can not be null!");
		
		if (constraints instanceof RCPosition) {
			p = (RCPosition) constraints;
		} else if (constraints instanceof String) {
			try {
				p = RCPosition.parse(constraints.toString());
			} catch (NullPointerException | IllegalArgumentException e) {
				throw new CalcLayoutException("String could not be parsed as RCPosition, it was: " + constraints.toString());
			}
		} else {
			throw new IllegalArgumentException("Constraints were neither a string nor RCPosition!");
		}

		if (p == null)
			throw new NullPointerException("RCPosition can not be null!");

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
		return calculateWidthAndHeight(Component::getPreferredSize, parent.getInsets());
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calculateWidthAndHeight(Component::getMinimumSize, parent.getInsets());
	}
	
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calculateWidthAndHeight(Component::getMaximumSize, target.getInsets());
	}
	
	/**
	 * Calculates dimension based on applying given function on each component.
	 * @param function function that does the calculation
	 * @param ins insets
	 * @return dimension based on function calculations
	 */
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
		/** Helper class **/
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
		Insets ins = parent.getInsets();
		Dimension dim = parent.getSize();
		double dx = (dim.getWidth() - 6*gap) / 7.;
		double dy = (dim.getHeight() - 4*gap) / 5.;
		int colRemainder = (int) ((dim.getWidth() - 6*gap) % 7);
		
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
				w = dx*5 + gap*4 + 1;
			} else {
				if (px == 1 && py == 6) {
					if (colRemainder >= 4) w += 1;
				}
				x = ins.left + (py-1)*dx + (py-1)*gap;
			}
			y = ins.top + (px-1)*dy + (px-1)*gap;
			
			polje[px-1][py-1] = new Pomocna(c, (int) x, (int) y, (int) w, (int) dy);
		}
		
		// Uniform increase in columns
		for (int i = 1; i < 5; i++) {
			int count = (int) ((dim.getWidth() - 6*gap) % 7);
			int j = 0;
			while (count > 0) {
				count--;
				Pomocna p = polje[i][j];
				if (p != null) {
					p.w += 1;
				}
				j += 2;
				if (j > 6)
					j = 1;
			}
		}
		
		// Fixing the gaps in columns
		if (polje[0][0] != null && polje[0][5] != null) {
			int actualGap = polje[0][5].x - (polje[0][0].x + polje[0][0].w);
			if (actualGap != gap) {
				int toAdd = gap - actualGap;
				polje[0][5].x += toAdd;
			}
		}
		if (polje[0][5] != null && polje[0][6] != null) {
			int actualGap = polje[0][6].x - (polje[0][5].x + polje[0][5].w);
			if (actualGap != gap) {
				int toAdd = gap - actualGap;
				polje[0][6].x += toAdd;
			}
		}
		for (int i = 1; i < 5; i++) { // All other rows
			for (int j = 1; j < 7; j++) {
				if (polje[i][j] != null && polje[i][j-1] != null) {
					int actualGap = polje[i][j].x - (polje[i][j-1].x + polje[i][j-1].w);
					if (actualGap != gap) {
						int toAdd = gap - actualGap;
						polje[i][j].x += toAdd;
					}
				}
			}
		}
		
		// Uniform increase in rows
		boolean doneFirst = false;
		for (int i = 0; i < 7; i++) {
			int count = (int) ((dim.getHeight() - 4*gap) % 5);
			int j = 0;
			
			while (count > 0) {
				if (j >= 4)
					j = 1;
				count--;
				if (j == 0 && i < 5) {
					if (doneFirst) {
						j += 2;
						continue;
					}
					Pomocna p = polje[0][0];
					if (p != null) {
						p.h += 1;
					}
					doneFirst = true;
					j += 2;
					continue;
				}
				Pomocna p = polje[j][i];
				if (p != null) {
					p.h += 1;
				}
				j += 2;
			}
		}
		
		// Fixing the gaps in rows
		for (int i = 1; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				int x = j;
				if (i == 1 && j < 5) {
					x = 0;
				}
				if (polje[i][j] != null && polje[i-1][x] != null) {
					int actualGap = polje[i][j].y - polje[i-1][x].y - polje[i-1][x].h;
					if (actualGap != gap) {
						int toAdd = gap - actualGap;
						polje[i][j].y += toAdd;
					}
				}
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
		// do nothing?
	}

}
