package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import algorithms.MinimaxExploredVisitedAugmented;
import algorithms.abstracts.Action;

public class CsvBattler2H
{
	public static void main(String[] args) throws FileNotFoundException
	{
		// playAgainstRandomAi();
		// humanVsAi(args);
		battleTwoCsvs(args);
	}

	public static void battleTwoCsvs(String[] args) throws FileNotFoundException
	{

		String fileName1 = null;
		String fileName2 = null;

		// check if different CSV cmd argument was provided
		if (args.length > 0)
		{
			File file = new File(args[0]);
			if (file.exists())
			{
				String[] components = args[0].split("\\.");
				if (components[components.length - 1].equals("csv"))
				{
					fileName1 = args[0];
				}
			}
			else
			{
				throw new RuntimeException("csv for arg 1 doesn't exist");
			}

			file = new File(args[1]);
			if (file.exists())
			{
				String[] components = args[1].split("\\.");
				if (components[components.length - 1].equals("csv"))
				{
					fileName2 = args[1];
				}
			}
			else
			{
				throw new RuntimeException("csv for arg 2 doesn't exist");
			}
		}
		else
		{
			throw new RuntimeException("cmd arguments did not contain two values");
		}
		HashMap<StickState2H, Action> loadedMapFirst = CSVMoveWriter.convert2HandFileToMap(fileName1);
		//HashMap<StickState2H, Action> loadedMapFirst = CSVMoveWriter.convert2HandFileToMap(fileName2); //make one csvs fight self other 
		HashMap<StickState2H, Action> loadedMapSecond = CSVMoveWriter.convert2HandFileToMap(fileName2);

		int winsAI = 0;
		int winsRandom = 0;

		boolean letRandomGoFirst = true;
		boolean letBothAgentsGetFirstTurns = true;

		int numRepeats = 50;

		for (StickState2H state : loadedMapFirst.keySet())
		{
			if (state.isTerminal())
			{
				// we will still get states that win in a single move
				continue;
			}

			// do 10 games with this state.
			for (int i = 0; i < numRepeats; ++i)
			{
				// random AI shouldn't get stuck in a loop
				int result = playRandomAiGame(loadedMapFirst, loadedMapSecond, state, letRandomGoFirst, 10);
				if (result == 0)
				{
					// do nothing, it was a tie
				}
				if (result > 0)
				{
					winsRandom++;
				}
				else
				{
					winsAI++;
				}
			}
			if (letBothAgentsGetFirstTurns)
			{
				for (int i = 0; i < numRepeats; ++i)
				{
					// random AI shouldn't get stuck in a loop
					int result = playRandomAiGame(loadedMapFirst, loadedMapSecond, state, !letRandomGoFirst, 10);
					if (result == 0)
					{
						// do nothing, it was a tie
					}
					if (result > 0)
					{
						winsRandom++;
					}
					else
					{
						winsAI++;
					}
				}
			}
		}
		System.out.println("random wins: " + winsRandom);
		System.out.println("AI wins: " + winsAI);

		System.out.println("AI to Random Win Ratio" + winsAI / (double) winsRandom);
	}

	private static int playRandomAiGame(HashMap<StickState2H, Action> loadedMapFirst, HashMap<StickState2H, Action> loadedMapSecond, StickState2H inState, boolean randomGoesFirst, int maxStateVisits)
	{
		// avoid infinite loops by terminating the game when the same state has been reached a
		// specific number of time
		HashMap<StickState2H, Integer> statesVisited = new HashMap<>();

		Random rng = new Random();
		int randomMoveNumber = randomGoesFirst ? 0 : 1;

		StickState2H state = inState;
		
		// infinitely
		for (int i = 0; true; i = (i + 1) % 2)
		{
			// conduct move
			if (i == randomMoveNumber)
			{
				Action action = loadedMapSecond.get(state);
				StickState2H tempstate = (StickState2H) state.result(state, action);
				state = tempstate;
			}
			else
			{
				Action action = loadedMapFirst.get(state);
				StickState2H tempstate = (StickState2H) state.result(state, action);
				state = tempstate;
			}

			// loop check
			Integer numVisits = statesVisited.get(state);
			if (numVisits != null)
			{
				numVisits++;
				if (numVisits >= maxStateVisits)
					return 0;
				else
					statesVisited.put(state, numVisits);
			}
			else
			{
				statesVisited.put(state, 1);
			}

			// terminal check
			if (state.isTerminal())
			{
				if (state.move == randomMoveNumber)
				{
					// the loser gets to move in a terminal state...
					return 1;
				}
				else
				{
					return -1;
				}
			}
		}
	}


	public static HashMap<StickState2H, Action> buildTable()
	{
		Action action = null;
		StickState2H.generateAllStates();
		HashMap<StickState2H, StickState2H> stateMap = StickState2H.getStateMap();
		HashMap<StickState2H, Action> stateToMoveMap = new HashMap<>();

		for (StickState2H state : stateMap.keySet())
		{
			if (!state.isTerminal())
			{
				if (state.move == StickState2H.MAX_TURN)
					action = MinimaxExploredVisitedAugmented.minimaxDecisionMax(state);
				else
					action = MinimaxExploredVisitedAugmented.minimaxDecisionMin(state);
				stateToMoveMap.put(state, action);
			}
		}
		return stateToMoveMap;
	}

}
