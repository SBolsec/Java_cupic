package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Demo which displays a BarChart.
 * @author sbolsec
 *
 */
public class BarChartDemo extends JFrame {
	/** Generated serial version UID **/
	private static final long serialVersionUID = 8001783544740936988L;

	/** BarChart to be displayed **/
	private BarChart barChart;
	/** Label at the top of the window displaying file from which BarChart was created **/
	private JLabel fileLabel;
	
	/**
	 * Constructor.
	 */
	public BarChartDemo(BarChart barChart, String fileName) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("BarChart Demo");
		setSize(800, 600);
		
		this.barChart = barChart;
		
		initGUI(fileName);
	}
	
	private void initGUI(String fileName) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JPanel north = new JPanel();
		north.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		fileLabel = new JLabel(fileName);
		fileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		north.add(fileLabel);
		cp.add(north, BorderLayout.NORTH);
		
		
		BarChartComponent bar = new BarChartComponent(barChart);
		bar.setBorder(BorderFactory.createLineBorder(Color.GREEN, 1));
		cp.add(bar, BorderLayout.CENTER);
	}
	
	/**
	 * Starting point of demo
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("There must be only one agrument: path to a file containing arguments of BarChart!");
			System.exit(1);
		}
		
		BarChart barChart = parseFile(args[0]);
		
		SwingUtilities.invokeLater(() -> {
			new BarChartDemo(barChart, args[0]).setVisible(true);
		});
	}
	
	/**
	 * Tries to parse the provided file and generate BarChart from it.
	 * @param filePath file which to parse
	 * @return <code>BarChart</code> generated from file
	 * @throws IllegalArgumentException if there was a error
	 */
	public static BarChart parseFile(String filePath) {
		Path path = Path.of(filePath);
		
		if (!Files.exists(path))
			throw new IllegalArgumentException("File does not exist!");
		if (Files.isDirectory(path))
			throw new IllegalArgumentException("Directory was provided!");
		if (!Files.isReadable(path))
			throw new IllegalArgumentException("File is not readable!");
		if (!Files.isRegularFile(path))
			throw new IllegalArgumentException("Path is not a regular file!");
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			throw new IllegalArgumentException("There was an error while reading the file!");
		}
		
		if (lines.size() < 6)
			throw new IllegalArgumentException("File must contain 6 rows, it had: " + lines.size());
		
		List<XYValue> values = null;
		try {
			values = Stream.of(lines.get(2).split(" "))
					.map(v -> new XYValue(Integer.parseInt(v.substring(0, v.indexOf(','))), 
							Integer.parseInt(v.substring(v.indexOf(',')+1))))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new IllegalArgumentException("Creating XYValues failed!");
		}
		
		int minY, maxY, gap;
		try {
			minY = Integer.parseInt(lines.get(3));
			maxY = Integer.parseInt(lines.get(4));
			gap = Integer.parseInt(lines.get(5));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Min y, max y or gap could not be parsed as integr!");
		}
		
		return new BarChart(values, lines.get(0), lines.get(1), minY, maxY, gap);
	}
}
