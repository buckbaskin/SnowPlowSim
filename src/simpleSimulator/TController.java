package simpleSimulator;

public class TController extends SimpleController 
{
	int next_move;
	public TController(int field_x, int field_y, int snow_start_x,
			int snow_start_y, int snow_height, int snow_width,
			double snow_depth, int start_x, int start_y) {
		super(field_x, field_y, snow_start_x, snow_start_y, snow_height, snow_width,
				snow_depth, start_x, start_y);
		next_move = 0;
	}
	
	/*
	 * a and b   -> up
	 * a and b'  -> right
	 * a' and b' -> down
	 * a' and b  -> left
	 */
	
	public SimpleCommand request_next_move(boolean last_valid)
	{
		SimpleCommand c = new SimpleCommand();
		if(next_move % 5 ==0)
		{ //go forward, plow left
			c.a = true;
			c.b = true;
			c.plow_left = true;
			if(next_move % 10 == 0)
				c.plow_left = false;
			c.plow_straight = false;
		}
		else if(next_move % 5 ==1)
		{ //go left, plow right (no left/down spill)
			c.a = false;
			c.b = true;
			c.plow_left = false;
			c.plow_straight = false;
		}
		else if(next_move % 5 ==2)
		{ //go right, plow left (no right/down spill)
			c.a = true;
			c.b = false;
			c.plow_left = true;
			c.plow_straight = false;
		}
		else if(next_move % 5 ==3)
		{ //go right, plow left (no right/down spill)
			c.a = true;
			c.b = false;
			c.plow_left = true;
			c.plow_straight = false;
		}
		else if(next_move % 5 ==4)
		{ //go left, plow right (no left/down spill)
			c.a = false;
			c.b = true;
			c.plow_left = false;
			c.plow_straight = false;
		}
		next_move++;
		
		if(next_move >= 61)
		{
			c.done = ":T: Reached end of run.";
		}
		return c;
	}
	public void reset()
	{
		next_move = 0;
	}
	
}
