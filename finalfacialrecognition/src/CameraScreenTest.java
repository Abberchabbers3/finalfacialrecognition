import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.github.sarxos.webcam.Webcam;

public class CameraScreenTest {
	Image j = null;
	public static void main(String[] args) {
		CameraScreenTest cs = new CameraScreenTest();
		cs.initialize();
	}
	private void initialize() {
		final Webcam webcam = Webcam.getDefault();
		webcam.open();
		JFrame window = new JFrame();
		JPanel panel = new JPanel() {
			@Override 
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(j, 0, 0, 500, 250, this);
			}
		};
		Dimension d = new Dimension(500,450);
		window.setPreferredSize(d);
		Timer imagetimer = new Timer((int) 0,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				j = webcam.getImage();
				panel.repaint();
			}		
			});
		
		imagetimer.start();
		
			JButton button = new JButton(new AbstractAction("Take Image") {

			//private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String name = String.format("frcam-%d.jpg", System.currentTimeMillis());
					ImageIO.write(webcam.getImage(), "JPG", new File(name));
					FaceDetector.detectface(webcam.getImage());
					System.out.format("File %s has been saved\n", name);
				} 
				catch (IOException t) {
					t.printStackTrace();
				}
			}
		});
		DetectEyeTrial trial = new DetectEyeTrial(webcam.getImage());
		trial.detectWhite(webcam.getImage());
		panel.setLayout(null);
		button.setBounds(0, 250, 500, 200);
		panel.add(button);
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}

}