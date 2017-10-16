package algorithms;

import java.util.HashMap;
import java.util.HashSet;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class MinimaxExploredVisitedAugmented
{
	public static final int MAX = 0;
	public static final int MIN = 1;

	public static HashSet<State> pathVisited;
	public static HashMap<State, Integer> maxExplored;
	public static HashMap<State, Integer> minExplored;

	public static Action minimaxDecisionMax(State state)
	{
		pathVisited = new HashSet<State>();
		pathVisited.add(state);

		// use previous explored maps for efficiency
		maxExplored = maxExplored == null ? new HashMap<>() : maxExplored;
		minExplored = minExplored == null ? new HashMap<>() : minExplored;

		Action[] actions = state.getActions();

		Pair<Action, Double> bestMove = new Pair<>(actions[0], Double.NEGATIVE_INFINITY);
		for (Action action : actions)
		{
			int childDepth = minValue(state.result(state, action), 1);
			double childUtility = shortCutUtility(childDepth, 0);
			if (childUtility > bestMove.utility)
			{
				bestMove.setData(action, childUtility);
			}
		}
		return bestMove.action;
	}

	public static Action minimaxDecisionMin(State state)
	{
		pathVisited = new HashSet<State>();
		pathVisited.add(state);

		// use previous explored maps for efficiency
		maxExplored = maxExplored == null ? new HashMap<>() : maxExplored;
		minExplored = minExplored == null ? new HashMap<>() : minExplored;

		Action[] actions = state.getActions();
		Pair<Action, Double> bestMove = new Pair<>(actions[0], Double.POSITIVE_INFINITY);
		for (Action action : actions)
		{
			int childDepth = maxValue(state.result(state, action), 1);
			double childUtility = shortCutUtility(childDepth, 0);
			if (childUtility < bestMove.utility)
			{
				bestMove.setData(action, childUtility);
			}
		}
		return bestMove.action;
	}

	public static int maxValue(State state, int depth)
	{
		if (state.isTerminal())
		{
			// 1 represents 1 node depth to terminal state
			double utility = state.utility(depth);// < 0 ? -depth : depth;
			if (utility == 0)
				return 0;
			else if (utility > 0)
				return 1;
			else
				return -1;
		}
		
		if (pathVisited.contains(state)) {
			return 0;
		}
		pathVisited.add(state);

		Pair<Action, Double> bestMove = new Pair<>(null, Double.NEGATIVE_INFINITY);
		int bestDepth = Integer.MIN_VALUE;
		if (!maxExplored.containsKey(state))
		{
			Action[] actions = state.getActions();
			for (Action action : actions)
			{
				State result = state.result(state, action);
				int childDepth = minValue(result, depth + 1);
				double childUtility = shortCutUtility(childDepth, depth);
				if (childUtility > bestMove.utility)
				{
					bestMove.setData(action, childUtility);
					bestDepth = childDepth;
				}
			}
			// update this state with depth to win or lose
			maxExplored.put(state, adjustedDepthToTerminalState(1, bestDepth));
		}
		else
		{
			// this state has already been explored, just look up previous value
			int previousDepth = maxExplored.get(state);
			bestDepth = previousDepth;
		}

		pathVisited.remove(state);

		return adjustedDepthToTerminalState(1, bestDepth);
	}

	public static int minValue(State state, int depth)
	{
		if (state.isTerminal())
		{
			// 1 represents 1 node depth to terminal state
			double utility = state.utility(depth);// < 0 ? -depth : depth;
			if (utility == 0)
				return 0;
			else if (utility > 0)
				return 1;
			else
				return -1;
		}
		
		if (pathVisited.contains(state)) {
			return 0;
		}
		pathVisited.add(state);

		Pair<Action, Double> bestMove = new Pair<>(null, Double.POSITIVE_INFINITY);
		int bestDepth = Integer.MAX_VALUE;
		if (!minExplored.containsKey(state))
		{
			Action[] actions = state.getActions();
			for (Action action : actions)
			{
				State result = state.result(state, action);
				int childDepth = maxValue(result, depth + 1);
				double childUtility = shortCutUtility(childDepth, depth);
				if (childUtility < bestMove.utility)
				{
					bestMove.setData(action, childUtility);
					bestDepth = childDepth;
				}
			}
			// update this state with depth to win or lose
			minExplored.put(state, adjustedDepthToTerminalState(1, bestDepth));
		}
		else
		{
			// this state has already been explored, just look up previous value
			int previousDepth = minExplored.get(state);
			bestDepth = previousDepth;
		}

		pathVisited.remove(state);
		return adjustedDepthToTerminalState(1, bestDepth);
	}

	private static Double shortCutUtility(int previousDepth, int curentDepth)
	{
		if (previousDepth == 0)
		{
			return 0.0;
		}
		else if (previousDepth > 0)
		{
			return Math.pow(Math.E, -(curentDepth + previousDepth));
		}
		else // previousDepth < 0
		{
			return -Math.pow(Math.E, -(curentDepth + -previousDepth));
		}
	}

	private static int adjustedDepthToTerminalState(int currentDepth, int depthToTerminalState)
	{
		if (depthToTerminalState == 0)
		{
			// 0 depths suggest loop state
			return 0;
		}
		else if (depthToTerminalState > 0)
		{
			// positive depths suggest max player win
			return depthToTerminalState + currentDepth;
		}
		else
		{
			// negative depths suggest min player win
			return depthToTerminalState + -currentDepth;
		}
	}
}
