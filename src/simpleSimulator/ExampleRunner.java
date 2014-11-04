package simpleSimulator;

import java.util.Scanner;

public class ExampleRunner {

	public static void main(String[] args)
	{
		int field_x = 7;
		int field_y = 15;

		int snow_start_x = 2;
		int snow_start_y = 3;
		int snow_width = 3;
		int snow_height = 10;
		double snow_depth = 10.0;

		int robot_start_x = 3;
		int robot_start_y = 2;

		SimpleController c = new DoubleTController(field_x, field_y, 
				snow_start_x, snow_start_y, snow_height, snow_width, snow_depth,
				robot_start_x, robot_start_y);

		SimpleSimulator s = new SimpleSimulator(field_x, field_y);

		for(int i = 0; i < 1; i++)
		{
			s.set_snow(snow_depth, snow_start_x, snow_start_y, snow_height, snow_width);
			s.place_obstacle(snow_start_x, snow_start_y, snow_height, snow_width);
			s.set_robot(robot_start_x, robot_start_y, c);

			//										  s.run(max iterations,pause in ms)
			System.out.println(" -----> :T: scored: "+s.run(108,200)+" <----- ");
		}
		Scanner scan = new Scanner(System.in);
		scan.next();
		System.exit(0);
	}

}
