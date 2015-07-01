package locam;

public class Sensor extends Feature {
	
	public double heading;

	public Sensor(int i, double x, double y, double heading) {
		super(i, x, y);
		this.heading = heading;
	}
	
	public double observe_heading(Feature observed) {
		if(this == observed) {
			return -4*Math.PI;
		}
		if(observed.x - x <= .0005 && observed.y - y <= .0005) {
			return -4*Math.PI;
		}
		return Math.atan2(observed.x - x, observed.y-y) - heading;
	}

}
