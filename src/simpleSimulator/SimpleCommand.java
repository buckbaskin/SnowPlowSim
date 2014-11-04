package simpleSimulator;

public class SimpleCommand {
	
	/*
	 * a and b   -> up
	 * a and b'  -> right
	 * a' and b' -> down
	 * a' and b  -> left
	 */
	
	boolean a;
	boolean b;
	boolean plow_straight;
	boolean plow_left;
	String done;
	
	public SimpleCommand()
	{
		this(true,true,true,true);
	}
	public SimpleCommand(boolean left, boolean right, boolean straight, boolean p_left)
	{
		a = left;
		b = right;
		plow_straight = straight;
		plow_left = p_left;
		done = null;
	}
	public SimpleCommand(String s)
	{
		String[] split = s.split("|");
		if(split[0].equals("up"))
		{
			a = true;
			b = true;
		}
		if(split[0].equals("down"))
		{
			a = false;
			b = false;
		}
		if(split[0].equals("left"));
		{
			a = false;
			b = true;
		}
		if(split[0].equals("right"))
		{
			a = true;
			b = false;
		}
		if(split[1].equals("left"))
		{
			plow_left = true;
			plow_straight = false;
		}
		else if(split[1].equals("right"))
		{
			plow_left = false;
			plow_straight = false;
		}
		else
		{
			plow_straight = true;
			plow_left = false;
		}
		try{
			done = split[3];
		}
		catch(IndexOutOfBoundsException ioobe)
		{
			done = null;
		}
		
	}
	
	public boolean[] command()
	{
		boolean[] output = new boolean[4];
		output[0] = a;
		output[1] = b;
		output[2] = plow_straight;
		output[3] = plow_left;
		return output;
	}
	
}
