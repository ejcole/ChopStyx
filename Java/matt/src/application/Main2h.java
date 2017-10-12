package application;

import java.io.FileNotFoundException;
import java.util.HashMap;

import algorithms.MinimaxExploredVisitedAugmented;
import algorithms.abstracts.Action;

public class Main2h
{
	private static Action action;

	public static void main(String[] args) throws FileNotFoundException
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
					// action = MinimaxSetBased.minimaxDecisionMax(state); 
					//action = MinimaxDFSSetAugmented.minimaxDecisionMax(state);
					action = MinimaxExploredVisitedAugmented.minimaxDecisionMax(state);

				}
				else
				{
					// action = MinimaxSetBased.minimaxDecisionMin(state);
					//action = MinimaxDFSSetAugmented.minimaxDecisionMin(state);
					action = MinimaxExploredVisitedAugmented.minimaxDecisionMin(state);

				}
				stateToMoveMap.put(state, action);
			}
		}

		for (StickState2H state : stateToMoveMap.keySet())
		{
			action = stateToMoveMap.get(state);
			System.out.println(state.toString() + (action != null ? action.toString() : "null"));
		}
		String fileName = "PathSetExploredSetMinimax_2h.csv";
		CSVMoveWriter.writeToCSV_2Hand(stateToMoveMap, fileName);
		//testReading2hFile(fileName);
	}
	
	public static void testReading2hFile(String fileName) throws FileNotFoundException
	{
		HashMap<StickState2H, Action> loadedMap = CSVMoveWriter.convert2HandFileToMap(fileName);
		for (StickState2H state : loadedMap.keySet())
		{
			action = loadedMap.get(state);
			System.out.println(state.toString() + (action != null ? action.toString() : "null"));
		}
	}
}
