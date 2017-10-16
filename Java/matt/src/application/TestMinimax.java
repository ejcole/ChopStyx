package application;

import java.awt.Dialog.ModalExclusionType;
import java.io.FileNotFoundException;
import java.util.HashMap;

import algorithms.MinimaxExploredVisitedAugmented;
import algorithms.abstracts.Action;

public class TestMinimax
{
	private static Action action;
	public static int LEFT = 0;
	public static int RIGHT = 1;
	

	public static void main(String[] args)
	{
		StickState2H.generateAllStates();
		HashMap<StickState2H, StickState2H> stateMap = StickState2H.getStateMap();
		HashMap<StickState2H, Action> stateToMoveMap = new HashMap<>();

		StickState2H testState = new StickState2H();
		//2 1 | 1 2 : 0
		testState.maxer[LEFT] = 2; testState.maxer[RIGHT] = 1;testState.opponent[LEFT] = 1; testState.opponent[RIGHT] = 2; testState.move = 0;
		
		//0 1 | 1 3 : 0
		testState = new StickState2H(0,1,1,3,0);
		
		StickState2H state = testState;
		if (!state.isTerminal())
		{
			if (state.move == StickState2H.MAX_TURN)
			{
				action = MinimaxExploredVisitedAugmented.minimaxDecisionMax(state);

			}
			else
			{
				action = MinimaxExploredVisitedAugmented.minimaxDecisionMin(state);

			}
			stateToMoveMap.put(state, action);
		}

//		for (StickState2H state : stateToMoveMap.keySet())
//		{
//			action = stateToMoveMap.get(state);
//			System.out.println(state.toString() + (action != null ? action.toString() : "null"));
//		}
//		String fileName = "PathSetExploredSetMinimax_2h.csv";
//		CSVMoveWriter.writeToCSV_2Hand(stateToMoveMap, fileName);
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
