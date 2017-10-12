package application;

import java.io.FileNotFoundException;
import java.util.HashMap;

import algorithms.MinimaxExploredVisitedAugmented;
import algorithms.abstracts.Action;

public class Main3h
{
	private static Action action;

	public static void main(String[] args) throws FileNotFoundException
	{
		StickState3H.generateAllStates();
		HashMap<StickState3H, StickState3H> stateMap = StickState3H.getStateMap();
		HashMap<StickState3H, Action> stateToMoveMap = new HashMap<>();

		for (StickState3H state : stateMap.keySet())
		{
			Action action = null;
			if (!state.isTerminal())
			{
				if (state.move == StickState2H.MAX_TURN)
					action = MinimaxExploredVisitedAugmented.minimaxDecisionMax(state);
				else
					action = MinimaxExploredVisitedAugmented.minimaxDecisionMin(state);
				stateToMoveMap.put(state, action);
			}
		}

		for (StickState3H state : stateToMoveMap.keySet())
		{
			action = stateToMoveMap.get(state);
			System.out.println(state.toString() + (action != null ? action.toString() : "null"));
		}
		String fileName = "PathSetExploredSetMinimax_3h.csv";
		CSVMoveWriter.writeToCSV_3Hand(stateToMoveMap, fileName);
		//testReading2hFile(fileName);
	}
	
	public static void testReading2hFile(String fileName) throws FileNotFoundException
	{
		HashMap<StickState3H, Action> loadedMap = CSVMoveWriter.convert3HandFileToMap(fileName);
		for (StickState3H state : loadedMap.keySet())
		{
			action = loadedMap.get(state);
			System.out.println(state.toString() + (action != null ? action.toString() : "null"));
		}
	}
}
