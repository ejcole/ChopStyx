package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import algorithms.MinimaxExploredVisitedAugmented;
import algorithms.abstracts.Action;

public class GamePlayer2H
{
	public static void main(String[] args) throws FileNotFoundException
	{
		 playAgainstRandomAi();
//		humanVsAi(args);
	}

	public static void playAgainstRandomAi() throws FileNotFoundException
	{
		String fileName = "PathSetExploredSetMinimax_2h.csv";
		HashMap<StickState2H, Action> loadedMap = CSVMoveWriter.convert2HandFileToMap(fileName);

		int winsAI = 0;
		int winsRandom = 0;

		boolean letRandomGoFirst = true;
		boolean letBothAgentsGetFirstTurns = true;

		int numRepeats = 50;

		for (StickState2H state : loadedMap.keySet())
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

		System.out.println("AI to Random Win Ratio" + winsAI / (double) winsRandom);

	}

	private static int playRandomAiGame(HashMap<StickState2H, Action> loadedMap, StickState2H state, boolean randomGoesFirst, int maxStateVisits)
	{
		// avoid infinite loops by terminating the game when the same state has been reached a
		// specific number of time
		HashMap<StickState2H, Integer> statesVisited = new HashMap<>();

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
				state = (StickState2H) state.result(state, actions[moveIndex]);
			}
			else
			{
				Action action = loadedMap.get(state);
				state = (StickState2H) state.result(state, action);
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

	public static void humanVsAi(String[] args) throws FileNotFoundException
	{
		String fileName = "PathSetExploredSetMinimax_2h.csv";

		// check if different CSV cmd argument was provided
		if (args.length > 0)
		{
			File file = new File(args[0]);
			if (file.exists())
			{
				String[] components = args[0].split(".");
				if (components[components.length - 1] == ".csv")
				{
					fileName = args[0];
				}
			}
		}

		Scanner kb = new Scanner(System.in);
		boolean playAgain = true;
		do
		{
			System.out.println("Play first? y/n");
			boolean aiFirst = kb.nextLine().toLowerCase().charAt(0) == 'n';

			System.out.println("Match Starting");
			// HashMap<StickState2H, Action> loadedMap =
			// CSVMoveWriter.convert2HandFileToMap(fileName);
			HashMap<StickState2H, Action> loadedMap = buildTable();
			StickState2H state = new StickState2H();

			//state = new StickState2H(0,1,1,3,0);
			
			System.out.println(state);
			while (true)
			{
				if (!aiFirst)
				{

					if (state.isTerminal())
					{
						System.out.println("You lose. :(");
						break;
					}
					System.out.println("Your Turn");
					System.out.println("type LR for attack right with your left hand, you must give valid moves; this is a simple implementation)");
					state.printState();
					System.out.println();
					String move = kb.nextLine();

					if (move.toLowerCase().charAt(0) == 'l')
					{
						if (move.toLowerCase().charAt(1) == 'l')
							state = (StickState2H) state.result(state, ActionSticks2H.LEFT_ATK_LEFT);
						else
							state = (StickState2H) state.result(state, ActionSticks2H.LEFT_ATK_RIGHT);
					}
					else
					{
						if (move.toLowerCase().charAt(1) == 'l')
							state = (StickState2H) state.result(state, ActionSticks2H.RIGHT_ATK_LEFT);
						else
							state = (StickState2H) state.result(state, ActionSticks2H.RIGHT_ATK_RIGHT);
					}
					System.out.println(state.toString() + '\n');
					if (state.isTerminal())
					{
						System.out.println("You Win. :)");
						break;
					}
				}
				// just don't skip for the remainder of the game
				aiFirst = false;

				Action action = loadedMap.get(state);
				System.out.println("AI move:" + action.toString());
				state = (StickState2H) state.result(state, action);
				System.out.println(state.toString() + '\n');

			}
			System.out.println("Play again? y/n");
			String again = kb.nextLine();

			playAgain = again.toLowerCase().charAt(0) == 'y';

		} while (playAgain);

		kb.close();
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
