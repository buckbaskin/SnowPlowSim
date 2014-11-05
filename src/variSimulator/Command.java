package variSimulator;

public class Command {
	double linear_velocity; //meters/sec
	double angular_velocity; //radians/sec
	
	public Command(double linear, double angular)
	{
		linear_velocity = linear;
		angular_velocity = angular;
	}
}
