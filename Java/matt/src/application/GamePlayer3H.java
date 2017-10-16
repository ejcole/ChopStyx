package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;

import algorithms.abstracts.Action;

public class GamePlayer3H
{
	public static void main(String[] args) throws FileNotFoundException
	{
		playAgainstRandomAi();
	}

	public static void playAgainstRandomAi() throws FileNotFoundException
	{
		String fileName = "PathSetExploredSetMinimax_3h.csv";
		HashMap<StickState3H, Action> loadedMap = CSVMoveWriter.convert3HandFileToMap(fileName);

		int winsAI = 0;
		int winsRandom = 0;

		boolean letRandomGoFirst = true;
		boolean letBothAgentsGetFirstTurns = true;
		// boolean letRandomGoFirst = false;
		// boolean letBothAgentsGetFirstTurns = false;

		int numRepeats = 50;

		for (StickState3H state : loadedMap.keySet())
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
				int result = playRandomAiGame(loadedMap, state, letRandomGoFirst, 10);
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
					int result = playRandomAiGame(loadedMap, state, !letRandomGoFirst, 10);
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

		System.out.println("AI to Random Win Ratio " + winsAI / (double) winsRandom);

	}

	private static int playRandomAiGame(HashMap<StickState3H, Action> loadedMap, StickState3H state, boolean randomGoesFirst, int maxStateVisits)
	{
		// avoid infinite loops by terminating the game when the same state has been reached a
		// specific number of time
		HashMap<StickState3H, Integer> statesVisited = new HashMap<>();

		Random rng = new Random();
		int randomMoveNumber = randomGoesFirst ? 0 : 1;

		// infinitely
		for (int i = 0; true; i = (i + 1) % 2)
		{
			// conduct move
			if (i == randomMoveNumber)
			{
				Action[] actions = state.getActions();
				int moveIndex = rng.nextInt(actions.length);
				state = (StickState3H) state.result(state, actions[moveIndex]);
			}
			else
			{
				Action action = loadedMap.get(state);
				state = (StickState3H) state.result(state, action);
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
	
	public static void humanVsAi(String[] args)
	{
		String fileName = "PathSetExploredSetMinimax_3h.csv";
		
		//check if different CSV cmd argument was provided
		if(args.length > 0)
		{
			File file = new File(args[0]);
			if(file.exists())
			{
				String[] components = args[0].split(".");
				if(components[components.length-1] == ".csv")
				{
					fileName = args[0];
				}
			}
		}
		
		
		
		
	}
}
