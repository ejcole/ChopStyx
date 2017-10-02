package algorithms;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class Minimax
{
	public static Action minimaxDecision(State state, TransitionModel transModel)
	{
		// function MINIMAX-DECISION(state) returns an action
		// return argmax a in ACTIONS(s) MIN-VALUE(RESULT(state, a))
		Action[] actions = transModel.getPossibleActions();
		
		Pair<Action, Integer> bestMove = new Pair<>(null, Integer.MIN_VALUE);
		for(Action action : actions)
		{
			int value = minValue(state.result(state, action));
			if(value > bestMove.utility)
				bestMove.setData(action, value);
		}
		return bestMove.action;
	}

	public static int maxValue(State state)
	{
		// function Max-Value(state) returns a utility value
		// if Terminal-Test(state) then return Utility(state)
		// v <-- -infinity
		// for each a in Actions(state) do
		// v <-Max(v, Min-Value(Result(s, a)))
		// return v
		
		if(state.isTerminal()) return state.utility();
		Action[] actions = state.getActions();
		
		Pair<Action, Integer> bestMove = new Pair<>(null, Integer.MIN_VALUE);
		for(Action action : actions)
		{
			int value = maxValue(state.result(state, action));
			if(value < bestMove.utility)
				bestMove.setData(action, value);
		}
		
		return bestMove.utility;
	}

	public static int minValue(State state)
	{
		
		// function Min-Value(state) returns a utility value
		// if Terminal-Test(state) then return Utility(state)
		// v <-- infinity
		// for each a in Actions(state) do
		// v <-Min(v, Max-Value(Result(s, a))
		// return v
		if(state.isTerminal()) return state.utility();
		Action[] actions = state.getActions();
		
		Pair<Action, Integer> bestMove = new Pair<>(null, Integer.MAX_VALUE);
		for(Action action : actions)
		{
			int value = maxValue(state.result(state, action));
			if(value < bestMove.utility)
				bestMove.setData(action, value);
		}
		
		return bestMove.utility;
	}
}

class Pair<K, V>
{
	public K action = null;
	public V utility = null;
	
	public Pair(K newAction, V newUtility)
	{
		this.action = newAction;
		this.utility = newUtility;
	}
	
	public void setData(K newAction, V newUtility)
	{
		this.action = newAction;
		this.utility = newUtility;
	}
}
