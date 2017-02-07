import java.awt.Container;

import javax.swing.JFrame;

public class PlaneFrame extends JFrame
{
	public PlaneFrame() 
	{
		setTitle("Plane Shoot!");

		GamePanel panel = new GamePanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);

		pack();
	}
	public static void main(String[] args) 
	{
		PlaneFrame e1 = new PlaneFrame();

		e1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		e1.setVisible(true);
	}
}

