import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;

import org.apache.tools.ant.DirectoryScanner;

public class Display{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton render, quit;
	WindAdapter windListener;
	DisplayCanvas ro;
	static String fpi[];
	static int count;
	static int i = 0;
	static int w = 1000;
	static int h = 600;
	private static String path;
	private static String instance;
	JFrame jfrm;
	public Display(String title){
		jfrm = new JFrame(title);
		//contentPane = getContentPane();
		//contentPane.
		jfrm.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		ButtonListener listener = new ButtonListener();
		windListener = new WindAdapter();
		jfrm.addWindowListener((WindowListener)windListener);          
		render = new JButton("Render");
		render.addActionListener(listener);
		buttonPanel.add(render);
		quit = new JButton("Quit");
		quit.addActionListener(listener);
		buttonPanel.add(quit);
		jfrm.add(buttonPanel, BorderLayout.SOUTH);
		JPanel dimensionsPanel = new JPanel();
		dimensionsPanel.setLayout(new FlowLayout());
		jfrm.add(dimensionsPanel, BorderLayout.NORTH);
		ro = new DisplayCanvas(
				);
		jfrm.add(ro, BorderLayout.CENTER);
		jfrm.setSize(w,h+30);
		jfrm.setVisible(true);
	}

	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if (e.getSource() == render){
				jfrm.remove(ro);
				ro = new DisplayCanvas();
				jfrm.add(ro, BorderLayout.CENTER);
				jfrm.setVisible(true);
				jfrm.repaint();

			}
			if (e.getSource() == quit){
				System.exit(0);
			}
		}
	}

	class WindAdapter extends WindowAdapter{

		public void windowClosing(WindowEvent e){  
			System.exit(0);
		}

	}

	class DisplayCanvas extends Canvas { 

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		double mleft, mtop, mright, mbottom;

		public DisplayCanvas(){
		}
		public void paint(Graphics g) { 
			DirectoryScanner scanner = new DirectoryScanner();
//			scanner.setIncludes(new String[]{"*359.jpg"});
			scanner.setIncludes(new String[]{instance});
			scanner.setBasedir(path);
			scanner.setCaseSensitive(false);
			scanner.scan();
			String[] files = scanner.getIncludedFiles();

			for (int j=0; j< files.length;j++){
				fpi = files[j].split("30.");

				for(int i=0;i<count;i++){
					String imageText = null;
					imageText = fpi[0]+i+"."+fpi[1];

					try { 
						Image originalImage = ImageIO.read(new File(imageText)); 
						g.drawImage(originalImage, 0, 0, w, h, null); 

					} catch (Exception e) {System.out.println("cnot find");break;} 


					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String args[]){
		path=args[0];
		count = Integer.parseInt(args[1]);
		w = Integer.parseInt(args[2]);
		h = Integer.parseInt(args[3]);
		instance = args[4]+"30.jpg";
		new Display("Display");

	}

}
