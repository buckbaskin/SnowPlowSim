package locam;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
	
	public ArrayList<Feature> features;
	
	public ArrayList<Sensor> sensors;

	public Map() {
		features = new ArrayList<Feature>();
	}
	
	public HashMap<Sensor, HashMap<Feature,Double>> observe() {
		HashMap<Sensor, HashMap<Feature,Double>> obs = new HashMap<Sensor, HashMap<Feature,Double>>();
		for (Sensor s : sensors) {
			obs.put(s, observe_sensor(s));
		}
		return obs;
	}
	
	public HashMap<Feature, Double> observe_sensor(Sensor s) {
		HashMap<Feature,Double> observations = new HashMap<Feature,Double>();
		for(Feature f : features) {
			observations.put(f, new Double(s.observe_heading(f)));
		}
		return observations;
	}

}
