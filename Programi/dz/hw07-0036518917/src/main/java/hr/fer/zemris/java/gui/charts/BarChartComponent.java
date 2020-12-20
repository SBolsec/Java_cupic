package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JComponent;

/**
 * Draws a bar chart based on the model it contains.
 * @author sbolsec
 *
 */
public class BarChartComponent extends JComponent {
	/** Generated serial version UID **/
	private static final long serialVersionUID = -2290688286165650352L;
	
	/** Reference to BarChart **/
	private final BarChart barChart;
	/** Window in which to draw **/
	private Rectangle window;
	/** Default font **/
	private Font defaultFont;
	/** Bold font for numbers **/
	private Font boldFont;
	/** Font metrics **/
	private FontMetrics metrics;
	/** Font metrics for bold font **/
	private FontMetrics boldFontMetrics;
	/** Width of the widest number on the y axis **/
	private int numWidth;
	/** Gap from edge of window **/
	private final int gapToWindow = 20;
	/** Gap from axis description to numbers **/
	private final int gapToNum = 20;
	/** Gap from numbers to axis **/
	private final int gapToAxis = 15;
	/** Color of axis lines **/
	private final Color axisColor = Color.GRAY;
	/** Color of grid lines **/
	private final Color gridColor = Color.ORANGE;
	/** Color of the bars **/
	private final Color barColor = new Color(244,119,72,255);
	/** Color of bar shadow **/
	private final Color shadowColor = new Color(197,195,195,255);
	/** Length the lines go over the axis **/
	private final int extraLines = 5;
	/** Gap between neighbouring bars **/
	private final int gapBetweenBars = 1;
	/** Extra to add to axis lines for arrows **/
	private final int arrowExtra = 10;
	
	/**
	 * Constructor which sets the BarChart
	 * @param barChart BarChart to be used by component
	 * @throws NullPointerException if given argument is null
	 */
	public BarChartComponent(BarChart barChart) {
		if (barChart == null) throw new NullPointerException("BarChart can not be null!");
		this.barChart = barChart;
	}

	@Override
	public void paintComponent(Graphics g) {
		window = this.getBounds();
		defaultFont = g.getFont();
		metrics = g.getFontMetrics(g.getFont());
		boldFont = new Font("default", Font.BOLD, 12);
		boldFontMetrics = g.getFontMetrics(boldFont);
		numWidth = boldFontMetrics.stringWidth(Integer.toString(barChart.getMaxY()));
		
		drawDescription(g);
		drawAxisGridBars(g);
	}
	
	/**
	 * Draws the descriptions of the axis.
	 * @param g graphic used to draw with
	 */
	private void drawDescription(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		int height = metrics.getHeight();
		int gap = gapToWindow + height + gapToNum + numWidth + gapToAxis;
		
		// x-axis
		int xDescWidth = metrics.stringWidth(barChart.getxDesc());
		int xLeftGap = window.x + gap;
		int x = xLeftGap + ((window.width - xLeftGap - xDescWidth - gapToWindow) / 2);
		int y = window.height - gapToWindow - metrics.getDescent();
		g2d.drawString(barChart.getxDesc(), x, y);
		
		// y-axis
		// get a reference of the affine transform of the original coordinate system
		AffineTransform defaultAt = g2d.getTransform();
		 
		AffineTransform at = AffineTransform.getQuadrantRotateInstance(3);
		g2d.setTransform(at);
		
		//int yDescWidth = metrics.stringWidth(barChart.getyDesc());
		int yDownGap = window.y + gap;
		x = yDownGap + ((window.height - yDownGap - height - gapToWindow) / 2);
		y = window.x + gapToWindow + metrics.getAscent();
		g2d.drawString(barChart.getyDesc(), -x, y);
		 
		// restore the original coordinate system
		g2d.setTransform(defaultAt);
	}
	
	/**
	 * Draws the x and y axis and the grid including the labels.
	 * @param g graphic used to draw with
	 */
	private void drawAxisGridBars(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		int fontHeight = metrics.getHeight();
		
		// Find starting point (0, 0)
		int x0 = window.x + gapToWindow + fontHeight + gapToNum + numWidth + gapToAxis;
		int y0 = window.height - gapToWindow - fontHeight - gapToNum - fontHeight - gapToAxis;
		
		// Find end point of x-axis (x, 0)
		int x1 = window.width - gapToWindow + arrowExtra;
		// int y1 = y0;
		
		// Find end point of y-axis (0, y)
		//int x2 = x0;
		int y2 = gapToWindow - arrowExtra;
		
		// Draw x-axis and y-axis
		g2d.setColor(axisColor);
		g2d.drawLine(x0 - extraLines, y0, x1, y0);
		g2d.drawLine(x0, y0 + extraLines, x0, y2);
		
		// Draw the numbers and grid lines on the y-axis
		g2d.setFont(boldFont);
		int minY = barChart.getMinY();
		int maxY = barChart.getMaxY();
		int barGap = barChart.getGap();
		int yCount = (maxY - minY) / (barGap == 0 ? 1 : barGap) + 1;
		double h = (double)(y0 - y2 - arrowExtra) / (yCount-1);
		for (int i = 0; i < yCount; i++) {
			// Draw number
			String num = Integer.toString(minY + barGap*i);
			int w = metrics.stringWidth(num);
			int x = x0 - gapToAxis - w;
			int y = (int) (y0 + fontHeight/3. - h*i);
			g2d.setColor(Color.BLACK);
			g2d.drawString(num, x, y);
			
			// Draw grid line
			if (i == 0)
				continue;
			
			y = (int) (y0 - h*i);
			g2d.setColor(axisColor);
			g2d.drawLine(x0 - extraLines, y, x0, y);
			g2d.setColor(gridColor);
			g2d.drawLine(x0, y, x1 + extraLines - arrowExtra, y);
		}
		
		// Draw the numbers on the x-axis
		List<XYValue> values = barChart.getValues();
		int xCount = values.size();
		double w = (double)(x1 - x0 - arrowExtra) / xCount;
		h = ((double)(y0 - y2 - arrowExtra) / (yCount-1)) / 2.;
		for (int i = 0; i <= xCount; i++) {
			if (i != xCount) {
				// Draw numbers
				String num = Integer.toString(values.get(i).getX());
				int strWidth = metrics.stringWidth(num);
				int x = (int) (x0 + w*i + (w - strWidth)/2.);
				int y = y0 + gapToAxis + fontHeight;
				g2d.setColor(Color.BLACK);
				g2d.drawString(num, x, y);
				
				// Draw bar
				int val = values.get(i).getY();
				int xStart = (int) (x0 + w*i) + gapBetweenBars;
				int xEnd = (int) (x0 + w*(i+1)) - gapBetweenBars;
				int yStart = y0 - 1;
				int yEnd = (int) (y0 - h*val);
				g2d.setColor(barColor);
				g2d.fillRect(xStart, yEnd, xEnd-xStart, yStart-yEnd);
				
				// Draw shadow
				xStart = (int) (x0 + w*(i+1) + gapBetweenBars);
				xEnd = xStart + (int) (0.03*w);
				yStart = y0 - 1;
				yEnd = (int) (y0 - h*val + 0.3*h);
				g2d.setColor(shadowColor);
				g2d.fillRect(xStart, yEnd, xEnd-xStart, yStart-yEnd);
			}
			
			// Draw grid line
			if (i == 0)
				continue;
			
			int x = (int) (x0 + w*i);
			g2d.setColor(axisColor);
			g2d.drawLine(x, y0 + extraLines, x, y0);
			g2d.setColor(gridColor);
			g2d.drawLine(x, y0, x, y2 + arrowExtra - extraLines);
		}
		g2d.setFont(defaultFont);
		g2d.setColor(Color.BLACK);
		
		// Draw arrows on ends on axis
		int a = 5;
		int b = 3;
		g2d.setColor(axisColor);
		Polygon yArrow = new Polygon(new int[] {x0, x0-b, x0+b}, new int[] {y2-a, y2, y2} , 3);
		g2d.drawPolygon(yArrow);
		g2d.fill(yArrow);
		Polygon xArrow = new Polygon(new int[] {x1-a, x1-a, x1}, new int[] {y0-b, y0+b, y0}, 3);
		g2d.drawPolygon(xArrow);
		g2d.fill(xArrow);
		g2d.setColor(Color.BLACK);
	}
}
