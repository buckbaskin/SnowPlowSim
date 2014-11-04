package simpleSimulator;

import java.util.Random;

public class RandomController extends SimpleController {

	Random r;
	int robot_x;
	int robot_y;
	
	int field_x;
	int field_y;
	
	public RandomController(int field_x, int field_y, 
			int snow_start_x, int snow_start_y, int snow_height, int snow_width, double snow_depth,
			int start_x, int start_y) {
		super(field_x, field_y, 
				snow_start_x, snow_start_y, snow_height, snow_width, snow_depth, 
				start_x, start_y);
		r = new Random();
		robot_x = start_x;
		robot_y = start_y;
		this.field_x = field_x;
		this.field_y = field_y;
	}
	
	public SimpleCommand request_next_move(boolean valid_last_move)
	{
		if (!valid_last_move)
		{
			System.out.println("\"Checking\" next move");

			System.out.println("robox: "+robot_x+" vs. fieldx: "+field_x+
					"\nroboy: "+robot_y+" vs. fieldy: "+field_y);
		}
		SimpleCommand c = new SimpleCommand();
		/*
		 * a and b   -> up
		 * a and b'  -> right
		 * a' and b' -> down
		 * a' and b  -> left
		 */
		if(robot_x == 0)
		{
			if(robot_y ==0)
			{ //left, down are an invalid moves
				c.a = true;
				c.b = r.nextBoolean();
				
			}
			else if(robot_y >= field_y-1)
			{ //left, up are an invalid moves
				c.a = r.nextBoolean(); 
				c.b = false;
			}
			else
			{ //left is an invalid move
				c.a = r.nextBoolean(); c.b = r.nextBoolean();
				while(!c.a && c.b)
				{
					c.a = r.nextBoolean(); c.b = r.nextBoolean();
				}
			}
		}
		else if(robot_x >= field_x-1)
		{
			if(robot_y ==0)
			{ //right, down are an invalid moves
				c.b = true;
				c.a = r.nextBoolean();
			}
			else if(robot_y >= field_y-1)
			{ //right, up are an invalid moves TODO
				c.a = false; c.b = r.nextBoolean();
			}
			else
			{ //right is an invalid move TODO
				c.a = r.nextBoolean(); c.b = r.nextBoolean();
				while(c.a && !c.b)
				{
					c.a = r.nextBoolean(); c.b = r.nextBoolean();
				}
			}
		}
		else if(robot_y ==0)
		{
			//down is invalid
			c.a = r.nextBoolean(); c.b = r.nextBoolean();
			while(!c.a && !c.b)
			{
				c.a = r.nextBoolean(); c.b = r.nextBoolean();
			}
		}
		else if(robot_y >= field_y-1)
		{
			//up is invalid
			c.a = r.nextBoolean(); c.b = r.nextBoolean();
			while(c.a && c.b)
			{
				c.a = r.nextBoolean(); c.b = r.nextBoolean();
			}
		}
		
		else
		{ //else any move possible
			c.a = r.nextBoolean(); c.b = r.nextBoolean();
		}
		
		c.plow_left = r.nextBoolean();
		c.plow_straight = r.nextBoolean();
		
		/*
		 * a and b   -> up
		 * a and b'  -> right
		 * a' and b' -> down
		 * a' and b  -> left
		 */
		if(c.a && c.b)
		{
			robot_y++;
		}
		else if(c.a)
		{
			robot_x++;
		}
		else if(c.b)
		{
			robot_x--;
		}
		else
		{
			robot_y--;
		}
		
		return c;
	}
	
}
