package hr.fer.zemris.tecaj.swing.p01;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class OtvaranjeProzora {

	// Event Dispatching Thread == EDT
//	public static void main(String[] args) {
//		JFrame frame = new JFrame();
//		
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		frame.setLocation(20, 20);
//		frame.setTitle("Moj prvi prozor u Javi!");
//		frame.setSize(500, 200);
//		
//		frame.setVisible(true);
//	}
	
//	private static Runnable posao = new Runnable() {
//		@Override
//		public void run() {
//			System.out.println("metodu run izvodi: " + Thread.currentThread());
//			JFrame frame = new JFrame();
//			
//			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//			frame.setLocation(20, 20);
//			frame.setTitle("Moj prvi prozor u Javi!");
//			frame.setSize(500, 200);
//			
//			frame.setVisible(true);
//		}
//	};
	
	public static void main(String[] args) {
		System.out.println("metodu main izvodi: " + Thread.currentThread());
		SwingUtilities.invokeLater(() -> {
			System.out.println("metodu run izvodi: " + Thread.currentThread());
			JFrame frame = new JFrame();
			
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setLocation(20, 20);
			frame.setTitle("Moj prvi prozor u Javi!");
			frame.setSize(500, 200);
			
			frame.setVisible(true);
		}); // kaže EDT-u da na "posao" pozove metodu run()
		System.out.println("sljedeca naredba");
	}
}
