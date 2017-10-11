package algorithms;

import java.util.HashSet;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class MinimaxSetBased
{
	public static HashSet<State> visited;
	public static Action minimaxDecisionMax(State state)
	{
		// function MINIMAX-DECISION(state) returns an action
		// return argmax a in ACTIONS(s) MIN-VALUE(RESULT(state, a))
		visited = new HashSet<State>();
		visited.add(state);
		Action[] actions = state.getActions();
		
		Pair<Action, Double> bestMove = new Pair<>(null, Double.NEGATIVE_INFINITY);
		for(Action action : actions)
		{
			double value = minValue(state.result(state, action),  0);
			if(value > bestMove.utility)
				bestMove.setData(action, value);
		}
		return bestMove.action;
	}
	public static Action minimaxDecisionMin(State state)
	{
		visited = new HashSet<State>();
		visited.add(state);
		Action[] actions = state.getActions();
		
		Pair<Action, Double> bestMove = new Pair<>(null, Double.POSITIVE_INFINITY);
		for(Action action : actions)
		{
			double value = maxValue(state.result(state, action),  0);
			if(value < bestMove.utility)
				bestMove.setData(action, value);
		}
		return bestMove.action;
	}

	public static double maxValue(State state, int depth)
	{
		// function Max-Value(state) returns a utility value
		// if Terminal-Test(state) then return Utility(state)
		// v <-- -infinity
		// for each a in Actions(state) do
		// v <-Max(v, Min-Value(Result(s, a)))
		// return v
		
		//return 0 for loop.
		if(visited.contains(state)) 
			return 0;
		visited.add(state);
		
		if(state.isTerminal()) return state.utility(depth);
		Action[] actions = state.getActions();
		
		Pair<Action, Double> bestMove = new Pair<>(null, Double.NEGATIVE_INFINITY);
		for(Action action : actions)
		{
			//System.out.println(state.result(state, action).toString() + " " + action.toString());
			double value = minValue(state.result(state, action), depth + 1);
			if(value > bestMove.utility)
				bestMove.setData(action, value);
		}
		
		return bestMove.utility;
	}

	public static double minValue(State state, int depth)
	{
		
		// function Min-Value(state) returns a utility value
		// if Terminal-Test(state) then return Utility(state)
		// v <-- infinity
		// for each a in Actions(state) do
		// v <-Min(v, Max-Value(Result(s, a))
		// return v
		
		//return 0 for loop.
		if(visited.contains(state))
			return 0;
		visited.add(state);
		
		
		if(state.isTerminal()) return state.utility(depth);
		Action[] actions = state.getActions();
		
		Pair<Action, Double> bestMove = new Pair<>(null, Double.POSITIVE_INFINITY);
		for(Action action : actions)
		{
			//System.out.println(state.result(state, action).toString() + " " + action.toString());
			double value = maxValue(state.result(state, action), depth + 1);
			if(value < bestMove.utility)
				bestMove.setData(action, value);
		}
		
		return bestMove.utility;
	}
}
