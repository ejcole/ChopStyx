package application.simple;


public class State2h
{
	protected int[] maxer;
	protected int[] opponent;
	protected int move;
	
	public State2h()
	{
		maxer = new int[]{5, 5};
		opponent = new int[]{5, 5};
		move = 0;
	}
	
	public State2h(State2h toCopy)
	{
		this.maxer = toCopy.maxer.clone();
		this.opponent = toCopy.opponent.clone();
		this.move = toCopy.move;
	}
	
	
}
