package simpleSimulator;

import java.util.Scanner;

public class SimpleSimulator 
{
	double[][] snow_model;
	int robot_x;
	int robot_y;
	SimpleController c;
	double max_snow = 61.0; //assume starts with 3 each
	double blade_height = 30;
	double initial_snow;
	int x_start; //start of snow
	int y_start; //start of snow
	int snow_height; //starting snow area
	int snow_width; //starting snow area

	int obstacle_x = -1;
	int obstacle_y = -1;

	boolean hit_obstacle;

	double max_depth;

	SimpleVisualSim v;

	public SimpleSimulator(int x_size, int y_size)
	{
		snow_model = new double[x_size][y_size];
		v = new SimpleVisualSim(this);
		max_depth = 0;
	}

	public void set_snow(double depth, int x_start, int y_start, int height, int width)
	{
		this.x_start = x_start;
		this.y_start = y_start;
		this.initial_snow = depth;
		this.snow_height = height;
		this.snow_width = width;

		for(int i = 0; i < snow_model.length; i++)
		{
			for(int j = 0; j < snow_model[i].length; j++)
			{
				snow_model[i][j] = 0.0;
			}
		}
		System.out.println("begin snow setup");
		for(int i = Math.max(x_start,0); i < x_start+width && i < snow_model.length; i++)
		{
			for(int j = Math.max(y_start,0); j < y_start+height && j < snow_model[i].length; j++)
			{
				snow_model[i][j] = depth;
			}
		}
		System.out.println("Snow setup complete");
	}
	public void set_robot(int x_start, int y_start, SimpleController controller)
	{
		robot_x = x_start;
		robot_y = y_start;
		c = controller;
	}
	public void place_obstacle(int x_start, int y_start, int height, int width)
	{
		//place the obstacle in the given box
		obstacle_x = (int) (Math.random()*width + x_start);
		obstacle_y = (int) (Math.random()*height + y_start);
		snow_model[obstacle_x][obstacle_y] = 0.0;

	}

	public double run(int iterations, int sleeptime)
	{
		c.reset();
		hit_obstacle = false;
		boolean successful_last_move = true;
		for (int i = 0; i < iterations; i++){
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("Iteration: "+i);
			boolean successful_move = step(successful_last_move);
			v.repaint();
			if(!successful_move && !successful_last_move && c.request_next_move(false).done ==null){
				System.out.println("Robot Action Error. May result from trying to move out of bounds.");
				System.out.println("Robot:  x="+robot_x+" y="+robot_y);
				System.out.println("Bounds: x=0,"+snow_model.length+" y=0,"+snow_model[0].length);
				break;
			}
			if(!successful_move && !successful_last_move)
			{
				break;
			}
			successful_last_move = successful_move;
		}
		return evaluation_function(true);
	}
	private boolean step(boolean last_valid)
	{ //returns true if successful, else false
		if(robot_x == obstacle_x && robot_y == obstacle_y)
		{
			hit_obstacle = true;
		}
		SimpleCommand comm = c.request_next_move(last_valid);
		if(!last_valid && comm.done == null) 
		{
			System.out.println("Move Warning: Unsuccesful Last Move");
		}
		if (comm.done != null)
		{
			System.out.println(comm.done);
			return false;
		}
		try
		{
			if(comm.a && comm.b)
			{ //move up
				double[] snow_shift = snow_function(snow_model[robot_x][robot_y+1],comm.plow_straight,comm.plow_left);

				try{
					snow_model[robot_x-1][robot_y+1] += snow_shift[0]; /*left*/ 
					snow_model[robot_x][robot_y+2] += snow_shift[1]; /*forward*/
					snow_model[robot_x+1][robot_y+1] += snow_shift[2]; /*right*/ 
				}
				catch(IndexOutOfBoundsException err)
				{
					//System.out.println("Snow pushed off map. Duly noted");
				}
				snow_model[robot_x][robot_y+1] = snow_shift[3]; /*in place*/
				//**Note if in place throws an out of bounds error, then the move is illegal

				robot_y += 1;
			}
			else if(comm.a)
			{ //move right
				double[] snow_shift = snow_function(snow_model[robot_x+1][robot_y],comm.plow_straight,comm.plow_left);

				try{
					snow_model[robot_x+1][robot_y+1] += snow_shift[0]; //left
					snow_model[robot_x+2][robot_y] += snow_shift[1]; //forward
					snow_model[robot_x+1][robot_y-1] += snow_shift[2]; //right
				}
				catch(IndexOutOfBoundsException err)
				{
					//System.out.println("Snow pushed off map. Duly noted");
				}
				snow_model[robot_x+1][robot_y] = snow_shift[3]; //in place


				robot_x += 1;
			}
			else if(comm.b)
			{ //move left
				double[] snow_shift = snow_function(snow_model[robot_x-1][robot_y],comm.plow_straight,comm.plow_left);

				try{
					snow_model[robot_x-1][robot_y-1] += snow_shift[0]; //left
					snow_model[robot_x-2][robot_y] += snow_shift[1]; //forward
					snow_model[robot_x-1][robot_y+1] += snow_shift[2]; //right
				}
				catch(IndexOutOfBoundsException err)
				{
					//System.out.println("Snow pushed off map. Duly noted");
				}
				snow_model[robot_x-1][robot_y] = snow_shift[3]; //in place

				robot_x += -1;
			}
			else
			{ //move down
				double[] snow_shift = snow_function(snow_model[robot_x][robot_y-1],comm.plow_straight,comm.plow_left);

				try{
					snow_model[robot_x+1][robot_y-1] += snow_shift[0]; //left
					snow_model[robot_x][robot_y-2] += snow_shift[1]; //forward
					snow_model[robot_x-1][robot_y-1] += snow_shift[2]; //right
				}
				catch(IndexOutOfBoundsException err)
				{
					//System.out.println("Snow pushed off map. Duly noted");
				}
				snow_model[robot_x][robot_y-1] = snow_shift[3]; //in place

				robot_y += -1;
			}
			//check max snow level
			for(int i = 0; i < snow_model.length; i++)
			{
				for(int j = 0; j < snow_model[i].length; j++)
				{
					if(snow_model[i][j] > max_depth)
					{
						max_depth = snow_model[i][j];
					}
				}
			}

			return true;
		}
		catch(IndexOutOfBoundsException error)
		{
			System.out.println("INVALID MOVE "+comm.a+"-"+comm.b);
			return false;
		}
	}
	private double[] snow_function(double snow_to_move, boolean plow_straight, boolean plow_left)
	{
		double to_move = snow_to_move;
		double extra = Math.max(0, snow_to_move-blade_height);
		to_move = snow_to_move - extra;
		double[] shifted_snow = new double[4]; // left , forward , right , in place
		//assumes no snow left behind
		if(plow_straight)
		{
			shifted_snow[0] = .1*to_move;
			shifted_snow[1] = .8*to_move;
			shifted_snow[2] = .1*to_move;
			shifted_snow[3] = 0*to_move+extra;
		}
		else if(plow_left)
		{
			shifted_snow[0] = .7*to_move;
			shifted_snow[1] = .3*to_move;
			shifted_snow[2] = 0*to_move;
			shifted_snow[3] = 0*to_move+extra;
		}
		else
		{ //plow right
			shifted_snow[0] = 0*to_move;
			shifted_snow[1] = .3*to_move;
			shifted_snow[2] = .7*to_move;
			shifted_snow[3] = 0*to_move+extra;
		}
		for(int i = 0; i < 4; i++)
		{
			if(shifted_snow[i]<.1) shifted_snow[i] = 0;
		}
		return shifted_snow;
	}
	public double evaluation_function(boolean print_report)
	{
		double snow_count = 0.0;
		double starting_snow = 0.0;
		for(int i = Math.max(x_start,0); i < x_start+snow_width && i < snow_model.length; i++)
		{
			for(int j = Math.max(y_start,0); j < y_start+snow_height && j < snow_model[i].length; j++)
			{
				if(i != obstacle_x && j != obstacle_y)
				{
					snow_count+= snow_model[i][j];
					starting_snow+= initial_snow;
				}
			}
		}
		//if(print_report) System.out.println("Snow in space: "+snow_count);
		for(int i = 0;  i < snow_model.length; i++)
		{
			for( int j = 0; j < y_start; j++)
			{
				snow_count += snow_model[i][j];
			}
		}
		for(int i = 0;  i < snow_model.length; i++)
		{
			for( int j = y_start+snow_height; j < snow_model[i].length; j++)
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
			System.out.println("max snow depth = "+max_depth);
		}
		double adjust = 1;
		if(hit_obstacle){
			if(print_report) System.out.println("* Note: Robot hit obstacle");
			adjust = .8;
		}
		if(max_depth > blade_height)
		{
			//adjust = adjust*(blade_height/max_depth);
		}
		return 100*(1-snow_count/starting_snow)*adjust;//*initial_snow/max_depth;
	}
}