package variSimulator;

import java.awt.Polygon;

public class Robot {
	public double x;
	public double y;
	public double heading;
	public double length;
	public double width;
	
	public double velocity;
	public double angular_velocity;
	
	public VariSimulator sim;
	
	public Robot(double length, double width, VariSimulator simulator)
	{
		this.length = length;
		this.width = width;
		sim = simulator;
	}
	
	public void simulator_update(double x, double y, double heading, double velocity, double angular_velocity)
	{
		this.x = x;
		this.y = y;
		this.heading = heading;
		this.velocity = velocity;
		this.angular_velocity = angular_velocity;
	}
	public Polygon get_polygon()
	{
		Polygon poly = new Polygon();
		//front left
		
		//front right
		
		//back left
		
		//back right
		return poly;
	}
	public void reset()
	{
		
	}
	
}
