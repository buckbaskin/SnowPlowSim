package locam.features;

public class EstimatedFeature{

	double est_x;
	double est_y;
	int id;
	
	public EstimatedFeature(int i, double x, double y) {
		est_x = x;
		est_y = y;
		id = i;
	}
	
	public double x() {
		return est_x;
	}
	public double x(double x) {
		est_x = x;
		return est_x;
	}
	
	public double y() {
		return est_y;
	}
	public double y(double y) {
		est_y = y;
		return est_y;
	}

}
