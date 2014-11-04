package simpleSimulator;

public class SimpleControllerTemplate extends SimpleController{

	public SimpleControllerTemplate(int field_x, int field_y, 
			int snow_start_x, int snow_start_y, int snow_height, int snow_width,
			double snow_depth, 
			int start_x, int start_y) {
		super(field_x, field_y, snow_start_x, snow_start_y, snow_height, snow_width,
				snow_depth, start_x, start_y);
		/*
		 * Variable information:
		 * 
		 * field_x, field_y <- maximum bounds for the driving field
		 * 
		 * snow_start_x, snow_start_y <- the lower left corner of the snowfield
		 * snow_height, snow_width <- the size of the snowfield
		 * snow_depth <- the depth of the snow
		 * 
		 * start_x, start_y <- robot start coordinates
		 */
		
		//insert save information here, for instance storing the boundary information to
		// ensure that it will not try to move out of the field
		
		
	}
	
	public SimpleCommand request_next_move(boolean last_valid)
	{
		SimpleCommand c = new SimpleCommand();
		//add controll logic here for step to step commands.
		// c.a and c.b set the movement patten.
		/*
		 * c.a and c.b   -> up
		 * c.a and c.b'  -> right
		 * c.a' and c.b' -> down
		 * c.a' and c.b  -> left
		 */
		
		//c.plow_straight is a selector for moving the plow or not.
		// if c.plow_straight = false
		/*
		 * the plow favors the left if plow_left = true
		 * otherwise the plow favors the robot's right
		 */
		
		return c;
	}
	
	public void reset()
	{
		//anything that needs to happen between runs goes here.
	}

}
