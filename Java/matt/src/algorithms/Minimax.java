package algorithms;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class Minimax
{
	
	public static Action minimaxDecision(State state)
	{
		// function Minimax-Decision(state) returns an action
		// return arg maxa(is element) Actions(s) Min-Value(Result(state, a))
		return null;
	}

	public static int maxValue(State state)
	{
		// function Max-Value(state) returns a utility value
		// if Terminal-Test(state) then return Utility(state)
		// v <- -infinity
		// for each a in Actions(state) do
		// v <-Max(v, Min-Value(Result(s, a)))
		// return v
		return 0;
	}

	public static int minValue(State state)
	{
		// function Min-Value(state) returns a utility value
		// if Terminal-Test(state) then return Utility(state)
		// v <-infinity
		// for each a in Actions(state) do
		// v <-Min(v, Max-Value(Result(s, a))
		// return v
		return 0;
	}

}
