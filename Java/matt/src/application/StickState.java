package application;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class StickState extends State
{
	int[] maxer;
	int[] opponent;
	
	public StickState()
	{
		maxer = new int[]{5, 5};
		opponent = new int[]{5, 5};
	}
	
	public StickState(StickState toCopy)
	{
		this.maxer = toCopy.maxer.clone();
		this.opponent = toCopy.opponent.clone();
	}
	
	@Override
	public boolean equals(Object state)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTerminal()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int utility()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Action[] getActions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State result(State state, Action action)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
