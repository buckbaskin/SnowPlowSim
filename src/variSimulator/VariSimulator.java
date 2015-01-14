package variSimulator;

import java.awt.Point;
import java.awt.Polygon;
import java.util.Random;

public class VariSimulator {
	//simulation info
	int visual_resolution; //pixels per meter to display
	int model_resolution; //snow blocks per meter (min 2, max 100)

	//robot and controller info
	Robot r;
	Controller c;
	//used for odometry frame relative to map frame
	double robot_start_x;
	double robot_start_y;

	//snow info
	byte[][] snow_model;
	private int snow_x_start;
	private int snow_y_start;
	private byte initial_snow;
	private int snow_height;
	private int snow_width;

	//obstacle
	double obstacle_x;
	double obstacle_y;
	boolean hit_obstacle;

	public VariSimulator(int vis_res, int model_res,
			double r_x, double r_y, double r_l, double r_w, double r_h,
			byte snow_depth, int x_start, int y_start, int height, int width)
	{
		visual_resolution = vis_res; //pixels per meter to display
		model_resolution = (int) Math.min(Math.max(model_res,2),100); //half meter to centimeter resolution

		set_robot(r_l,r_w,r_x,r_y,r_h);
		set_snow(snow_depth, x_start, y_start, height, width);
	}
	public VariSimulator()
	{
		this(30,10,
				2,2,1.1,1,Math.PI*.5,
				(byte) 10,3,3,10,1);
	}
	private void set_robot(double length, double width, 
			double x, double y, double heading)
	{
		r = new Robot(length, width, this);
		r.simulator_update(x, y, heading, 0,0);
		robot_start_x = x;
		robot_start_y = y;
	}
	public Robot set_controller(Controller c)
	{
		this.c = c;
		return r;
	}
	public void set_field(byte depth, int x_start, int y_start, int height, int width)
	{
		//set snow, obstacle
		set_snow(depth, x_start, y_start, height, width);
		set_obstacle();
	}
	private void set_snow(byte depth, int x_start, int y_start, int height, int width)
	{
		this.snow_x_start = x_start;
		this.snow_y_start = y_start;
		this.initial_snow = depth;
		this.snow_height = height;
		this.snow_width = width;

		for(int i = 0; i < snow_model.length; i++)
		{
			for(int j = 0; j < snow_model[i].length; j++)
			{
				snow_model[i][j] = 0;
			}
		}
		for(int i = Math.max(x_start*model_resolution,0);
		i < x_start*model_resolution+width*model_resolution && i < snow_model.length; i++)
		{
			for(int j = Math.max(y_start*model_resolution,0);
			j < y_start*model_resolution+height*model_resolution && j < snow_model[i].length; j++)
			{
				snow_model[i][j] = depth;
			}
		}
	}
	private void set_obstacle()
	{
		//do nothing for now
		//will remove snow and place obstacle (-1 in snow model?)

	}

	public double run(double step_length, int iterations, int sleeptime)
	{//TODO fill out
		for(int i = 0; i < iterations; i++)
		{
			step(step_length);
		}
		return evaluation_function(true);
	}
	private void step(double step_length)
	{   
		/*
		 * MOVE THE ROBOT
		 */
		Command c = get_next_command();
		double dx = step_length*c.linear_velocity*Math.cos(r.heading);
		double dy = step_length*c.linear_velocity*Math.cos(r.heading);
		double dh = step_length*c.angular_velocity;
		r.simulator_update(r.x+dx, r.y+dy, r.heading+dh, c.linear_velocity, c.angular_velocity);

		/*
		 * CALCULATE SNOW PILE COLLISIONS
		 */
		//check for new snow pile collisions
		Polygon robot = r.get_polygon();
		//location in display space
		int disp_min_x = robot.xpoints[0];
		int disp_max_x = robot.xpoints[0];
		int disp_min_y = robot.ypoints[0];
		int disp_max_y = robot.ypoints[0];
		for(int i = 0; i < robot.npoints; i++)
		{
			disp_min_x = Math.min(robot.xpoints[i], disp_min_x);
			disp_min_y = Math.min(robot.ypoints[i], disp_min_y);
			disp_max_x = Math.max(robot.xpoints[i], disp_max_x);
			disp_max_y = Math.max(robot.ypoints[i], disp_max_y);
		}
		int mod_min_x = disp_min_x/visual_resolution*model_resolution;
		int mod_min_y = disp_min_y/visual_resolution*model_resolution;
		int mod_max_x = disp_max_x/visual_resolution*model_resolution;
		int mod_max_y = disp_max_y/visual_resolution*model_resolution;

		//check for piles in display space by iterating through display space
		for(int i = mod_min_x; i <= mod_max_x; i+=1)
		{
			for(int j = mod_min_y; j <= mod_max_y; j+=1)
			{
				//stepping through in model resolution space

				int disp_x = i/model_resolution*visual_resolution; //in display space
				int disp_y = j/model_resolution*visual_resolution; //in display space
				if(robot.contains(new Point(disp_x,disp_y)))
				{
					if(snow_model[i][j] >=0) //model space
					{
						//move collided snow_pile
						//8 box
						double robot_direction = 0;
						double[] p = new double[8];
						p[0] = Math.max(0, Math.cos(Math.PI/4*0-robot_direction));
						p[1] = Math.max(0, Math.cos(Math.PI/4*1-robot_direction));
						p[2] = Math.max(0, Math.cos(Math.PI/4*2-robot_direction));
						p[3] = Math.max(0, Math.cos(Math.PI/4*3-robot_direction));
						p[4] = Math.max(0, Math.cos(Math.PI/4*4-robot_direction));
						p[5] = Math.max(0, Math.cos(Math.PI/4*5-robot_direction));
						p[6] = Math.max(0, Math.cos(Math.PI/4*6-robot_direction));
						p[7] = Math.max(0, Math.cos(Math.PI/4*7-robot_direction));

						double sum = 0;
						for(double d : p)
						{
							sum += d;
						}
						for(int k = 0; k < p.length; k++)
						{
							p[k] = p[k]/sum; //normalize
						}
						double select = Math.random();
						for(int k = 0; k < p.length; k--)
						{
							if (p[k] > select && select >= 0)
							{
								select = -1.0;
								//select
								try
								{
									if(k==0)
									{
										snow_model[i+1][j] += snow_model[i][j];
									}
									else if(k==1)
									{
										snow_model[i+1][j+1] += snow_model[i][j];
									}
									else if(k==2)
									{
										snow_model[i][j+1] += snow_model[i][j];
									}
									else if(k==3)
									{
										snow_model[i-1][j+1] += snow_model[i][j];
									}
									else if(k==4)
									{
										snow_model[i-1][j] += snow_model[i][j];
									}
									else if(k==5)
									{
										snow_model[i-1][j-1] += snow_model[i][j];
									}
									else if(k==6)
									{
										snow_model[i][j-1] += snow_model[i][j];
									}
									else // k==7
									{
										snow_model[i+1][j-1] += snow_model[i][j];
									}
								}
								catch(ArrayIndexOutOfBoundsException e)
								{
									//pass
								}
							}
							else
							{
								select = select - p[k];
							}
						}
						snow_model[i][j] = 0;
						//then do local smooth, not into robot

					}
				}
			}
		}
		/*
		 * SMOOTH OUT SNOW MODEL
		 */
		//smooth out snow_pile stacks (stacks should not be more than x taller than their neighbors
		//TODO
		//full smooth (call infrequently)
		//local robot smooth (call repeatedly)
	}
	private void local_smooth(double robot_x, double robot_y)
	{
		int model_radius = 1*model_resolution;
	}

	private Command get_next_command() {
		return c.get_command();
	}
	public double evaluation_function(boolean print_report) 
	{ //TODO check
		//in model space
		double snow_count = 0.0;
		double starting_snow = 0.0;
		for(int i = Math.max(snow_x_start*model_resolution,0);
		i < snow_x_start*model_resolution+snow_width*model_resolution && i < snow_model.length; i++)
		{
			for(int j = Math.max(snow_y_start*model_resolution,0);
			j < snow_y_start*model_resolution+snow_height*model_resolution && j < snow_model[i].length; j++)
			{
				//ignore obstacle
				if(i != (int) obstacle_x && j != (int) obstacle_y)
				{
					snow_count+= snow_model[i][j];
					starting_snow+= initial_snow;
				}
			}
		}
		//if(print_report) System.out.println("Snow in space: "+snow_count);
		for(int i = 0;  i < snow_model.length; i++)
		{
			for( int j = 0; j < snow_y_start; j++)
			{
				snow_count += snow_model[i][j];
			}
		}
		for(int i = 0;  i < snow_model.length; i++)
		{
			for( int j = snow_y_start+snow_height; j < snow_model[i].length; j++)
			{
				snow_count += snow_model[i][j];
			}
		}
		//if(print_report) System.out.println("Snow including penalty areas: "+snow_count);
		if(print_report)
		{
			System.out.println("_____ Evaluation Report ______");
			System.out.println("snow_count   "+snow_count);
			System.out.println("---------- = -------");
			System.out.println("total_snow   "+starting_snow);
			//System.out.println("max snow depth = "+max_depth);
		}
		double adjust = 1;
		if(hit_obstacle){
			if(print_report) System.out.println("* Note: Robot hit obstacle");
			adjust = .8;
		}
		/*if(max_depth > blade_height)
		{
			//adjust = adjust*(blade_height/max_depth);
		}*/
		return 100*(1-snow_count/starting_snow)*adjust;	
	}
}
