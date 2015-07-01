package locam;

import java.util.ArrayList;
import java.util.HashMap;

import locam.features.EstimatedFeature;
import locam.features.KnownFeature;

public class Concept {
	
	public static void main(String[] args) {
		
		Map world = new Map();
		Concept c = new Concept(1,0);
		
		world.features.add(new Feature(1, 0,0));
		world.features.add(new Feature(2, 0,1));
		world.features.add(new Feature(3, 1,0));
		world.features.add(new Feature(4, 1,1));
		
		world.sensors.add(new Sensor(5, 0,2,0));
		world.sensors.add(new Sensor(6, 2,0,0));
		world.sensors.add(new Sensor(7, 0,-2,0));
		world.sensors.add(new Sensor(8, -2,0,0));
	}
	
	ArrayList<KnownFeature> known_features = new ArrayList<KnownFeature>();
	ArrayList<EstimatedFeature> unknown_features = new ArrayList<EstimatedFeature>();
	ArrayList<Sensor> known_sensors = new ArrayList<Sensor>();
	ArrayList<Sensor> unknown_sensors = new ArrayList<Sensor>();
	
	int unk_feat, unk_sens;
	
	public Concept(int unk_feature, int unk_sensor) {
		unk_feat = unk_feature;
		unk_sens = unk_sensor;
	}
	
	public void observe(HashMap<Sensor, HashMap<Feature,Double>> obs) {
		// TODO
		// from a list of observations:
		// right now, just going known sensors to find unknown feature
		// for known sensors:
		//     
	}
	
	
	
}
