package variSimulator;

import java.awt.Point;
import java.awt.Polygon;

public class MathTest {

	public static void main(String[] args)
	{
		Polygon p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(0,10);
		p.addPoint(10, 10);
		p.addPoint(10, 0);
		
		System.out.println("contains (9,9) : "+p.contains(new Point(9,9)));
	}
	
}
