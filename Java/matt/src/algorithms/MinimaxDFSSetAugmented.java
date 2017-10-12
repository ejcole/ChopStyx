package algorithms;

import java.util.HashSet;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;
import application.Utils;

public class MinimaxDFSSetAugmented
{
	public static final int MAX = 0;
	public static final int MIN = 1;
	
	public static HashSet<State> pathVisited;
	public static HashSet<State> explored;
	public static Action minimaxDecisionMax(State state)
	{
		pathVisited = new HashSet<State>();
		pathVisited.add(state);
		explored = new HashSet<>();
		explored.add(state);
		
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
		pathVisited = new HashSet<State>();
		pathVisited.add(state);
		explored = new HashSet<>();
		explored.add(state);
		
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
	
	//Probably not a good idea to have this many if checks within a function that gets called this much
	public static double findMinMaxValues(State state, int depth, int player)
	{
		if (pathVisited.contains(state)) return 0;
		pathVisited.add(state);

		if (state.isTerminal()) return state.utility(depth);

		Pair<Action, Double> bestMove = player == MAX ? new Pair<>(null, Double.NEGATIVE_INFINITY) : new Pair<>(null, Double.POSITIVE_INFINITY) ;
		if (!explored.contains(state))
		{
			for (Action action : state.getActions())
			{
				//Utils.printTabs(depth); 
				// System.out.println("PARENT: " + state.toString() + " CHILD:" +
				// state.result(state, action).toString() + " " + action.toString());
				double value = findMinMaxValues(state.result(state, action), depth + 1, (player + 1) % 2);
				if(player == MAX)
					if (value > bestMove.utility) 
						bestMove.setData(action, value);
				else
					if (value < bestMove.utility)
						bestMove.setData(action, value);
						
			}
		}
		else
		{
			//we have explored this state and it is not on the current path, it should have values associated with it!
		}
		
		explored.add(state);
		
		// remove state for DFS augmented set
		pathVisited.remove(state);
		//Utils.printTabs(depth);
		// System.out.println("REMOVED:" + state.toString());
		return bestMove.utility;
	}

	public static double maxValue(State state, int depth)
	{
		if(pathVisited.contains(state)) 
			return 0;
		pathVisited.add(state);
		
		if(state.isTerminal()) return state.utility(depth);
		Action[] actions = state.getActions();
		
		Pair<Action, Double> bestMove = new Pair<>(null, Double.NEGATIVE_INFINITY);
		for(Action action : actions)
		{
			Utils.printTabs(depth);
			//System.out.println("PARENT: " + state.toString() + " CHILD:" + state.result(state, action).toString() + " " + action.toString());
			double value = minValue(state.result(state, action), depth + 1);
			if(value > bestMove.utility)
				bestMove.setData(action, value);
		}
		
		//remove state for DFS augmented set
		pathVisited.remove(state);
		Utils.printTabs(depth);
		//System.out.println("REMOVED:" + state.toString());
		return bestMove.utility;
	}

	public static double minValue(State state, int depth)
	{
		//return 0 for loop.
		if(pathVisited.contains(state))
			return 0;
		pathVisited.add(state);
		
		if(state.isTerminal()) return state.utility(depth);
		Action[] actions = state.getActions();
		
		Pair<Action, Double> bestMove = new Pair<>(null, Double.POSITIVE_INFINITY);
		for(Action action : actions)
		{
			Utils.printTabs(depth);
			//System.out.println("PARENT: " + state.toString() + " " + state.result(state, action).toString() + " " + action.toString());
			double value = maxValue(state.result(state, action), depth + 1);
			if(value < bestMove.utility)
				bestMove.setData(action, value);
		}
		//remove state for DFS augmented set
		pathVisited.remove(state);
		Utils.printTabs(depth);
		//System.out.println("REMOVED:" + state.toString());
		return bestMove.utility;
	}
}
