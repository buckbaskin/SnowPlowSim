package simulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VisualSim extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6708652923467522296L;

	public Simulator to_draw;
	JPanel pj = new JPanel();

	int max_x;
	int max_y;

	int scale = 30;

	public VisualSim(Simulator s)
	{
		to_draw = s;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.black);
		max_x = 2*scale+to_draw.snow_model.length*scale;
		max_y = 2*scale+to_draw.snow_model[0].length*scale+scale;
		this.setSize(max_x, max_y);
		this.setResizable(false);
		pj.setBackground(Color.black);
		this.add(pj);
		this.setVisible(true);
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		for(int i = 0; i < to_draw.snow_model.length; i++)
		{
			for(int j = 0; j < to_draw.snow_model[i].length; j++)
			{
				g2.setColor(Color.white);
				if(i == to_draw.obstacle_x && j == to_draw.obstacle_y) g2.setColor(Color.red);
				g2.fillRect(i*scale+scale+1, max_y-(j*scale+2*scale-1), scale-2, scale-2);
				g2.setColor(Color.black);
				double snow_fraction = 360.0*(to_draw.snow_model[i][j]/to_draw.max_snow);

				//System.out.println("Snow Fraction: "+((int)(snow_fraction)));
				if(snow_fraction!=0 && snow_fraction <.1)
				{
					//System.out.println("i:"+i+" j:"+j+" model:"+to_draw.snow_model[i][j]+" -> "+snow_fraction);
				}
				g2.fillArc(i*scale+scale+5, max_y-(j*scale+2*scale-5), 20, 20, (int) (snow_fraction), (int) (360-snow_fraction));
			}
		}
		//System.out.println("Robotx: "+to_draw.robot_x+" roboty: "+to_draw.robot_y);

		g2.setColor(Color.green);
		g2.fillRect(to_draw.robot_x*scale+scale, max_y-(to_draw.robot_y*scale+2*scale), scale, scale);

		g2.setColor(Color.orange);
		g2.drawString("Score: "+to_draw.evaluation_function(false), 2*scale, 2*scale);

		for(int i = 0; i < to_draw.snow_model.length; i++)
		{
			for(int j = 0; j < to_draw.snow_model[i].length; j++)
			{
				if(to_draw.snow_model[i][j] > .1)
				{
					g2.setColor(new Color(149,0,223));
					//System.out.println("Draw orange "+(to_draw.snow_model[i][j]+"    ").substring(0,4));
					g2.drawString((to_draw.snow_model[i][j]+"    ").substring(0,4), i*scale+scale+1, max_y-(j*scale+scale-1)-2);
				}
			}
		}
	}
}
