package application;

import java.util.HashMap;

import algorithms.MinimaxSetBased;
import algorithms.abstracts.Action;

public class Main
{
	private static Action action;

	public static void main(String[] args)
	{
		StickState2H.generateAllStates();
		HashMap<StickState2H, StickState2H> stateMap = StickState2H.getStateMap();
		HashMap<StickState2H, Action> stateToMoveMap = new HashMap<>();

		for (StickState2H state : stateMap.keySet())
		{
			Action action = null;
			if (!state.isTerminal())
			{
				if (state.move == StickState2H.MAX_TURN)
				{
					action = MinimaxSetBased.minimaxDecisionMax(state);
					// action = Minimax.minimaxDecisionMax(state);
				}
				else
				{
					action = MinimaxSetBased.minimaxDecisionMin(state);
					// action = Minimax.minimaxDecisionMin(state);
				}
				stateToMoveMap.put(state, action);
			}
		}

		for (StickState2H state : stateToMoveMap.keySet())
		{
			action = stateToMoveMap.get(state);
			System.out.println(state.toString() + (action != null ? action.toString() : "null"));
		}
	}
}
