package application;

import java.util.ArrayList;
import java.util.HashMap;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class StickState2H extends State
{
	private static HashMap<StickState2H, StickState2H> allStates;
	private static HashMap<StickState2H, StickState2H> depthStateTest;

	public static HashMap<StickState2H, StickState2H> getStateMap()
	{
		return allStates;
	}

	/** Constants for indexing into arrays. The left hand is index 0; the right hand is index 1 */
	public final static int LEFT = 0;
	public final static int RIGHT = 1;
	public final static int MAX_TURN = 0;
	public final static int MIN_TURN = 1;
	public final static int HAND_OUT = 0;

	/** Player 1 */
	protected int[] maxer;

	/** Player 2 */
	protected int[] opponent;

	/** The current turn, 0 is player1 and 1 is opponent(player2) */
	protected int move;

	/** Special Variables for loop detection */
	protected boolean loopState = false;
	protected boolean terminalStateOfLoop = false;
	protected Integer encounteredDepth = null;

	/**
	 * Construct the start state.
	 */
	public StickState2H()
	{
		maxer = new int[]
		{ 1, 1 };
		opponent = new int[]
		{ 1, 1 };
		// move = Player.convertFromEnum(Player.MAX);
		move = MAX_TURN;
	}

	public StickState2H(StickState2H toCopy)
	{
		this.maxer = toCopy.maxer.clone();
		this.opponent = toCopy.opponent.clone();
		this.move = toCopy.move;
	}

	@Override
	public boolean equals(Object state)
	{
		StickState2H other = (StickState2H) (state);
		if (this.move != other.move) return false;
		for (int i = 0; i < maxer.length; i++)
		{
			if (other.maxer[i] != this.maxer[i]) return false;
		}
		for (int i = 0; i < opponent.length; i++)
		{
			if (other.opponent[i] != this.opponent[i]) return false;
		}
		if (!compareEncounteredDepths(other)) return false;

		// loopstate SHOULD NOT compared for proper state enumeration
		return true;
	}

	public boolean compareEncounteredDepths(StickState2H other)
	{
		// if one is not null, then the other must be non-null and of equivalent value
		if (this.encounteredDepth != null || other.encounteredDepth != null)
		{
			// if either is null, then there is a difference since we know one of previous is not
			// null
			if (this.encounteredDepth == null || other.encounteredDepth == null) return false;

			// they are both non-null, therefore they should be equal.
			if (!this.encounteredDepth.equals(other.encounteredDepth)) return false;

		}
		// if one is null, then both should be null
		if (this.encounteredDepth == null || other.encounteredDepth == null)
		{
			// both should be null, therefore both should be equivalent
			if (this.encounteredDepth != other.encounteredDepth) return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int primeNumber = 23;

		// +1 to ensure 0 is not multiplied against
		int hornerHash = move + 1;
		for (int i = 0; i < maxer.length; i++)
		{
			hornerHash = (hornerHash * primeNumber) + maxer[i];
		}
		for (int i = 0; i < opponent.length; i++)
		{
			hornerHash = (hornerHash * primeNumber) + opponent[i];
		}
		if (encounteredDepth != null) hornerHash = (hornerHash * primeNumber) + encounteredDepth;
		return hornerHash;
	}

	@Override
	public boolean isTerminal()
	{
		//@formatter:off
		return (maxer[RIGHT] == HAND_OUT && maxer[LEFT] == HAND_OUT) 
				|| (opponent[RIGHT] == HAND_OUT && opponent[LEFT] == HAND_OUT); 
				//|| loopState;
		//@formatter:on

	}

	@Override
	public double utility(int depth)
	{
		if (maxer[RIGHT] == HAND_OUT && maxer[LEFT] == HAND_OUT)
		{
			return -Math.pow(Math.E, -depth);
		}
		else if (opponent[RIGHT] == HAND_OUT && opponent[LEFT] == HAND_OUT)
		{
			return Math.pow(Math.E, -depth);
		}
		else if (loopState)
		{
			// loop states are favor of neither, however we prefer them to infinite loops
			// not sure what to do about ordering,
			return 0;
		}
		else
		{
			throw new RuntimeException("Utility should not be called on non-terminal state for the game of chop sticks\n there is a small state space");
		}
	}

	@Override
	public Action[] getActions()
	{
		// PREPARE ATTACKER AND RECIEVER BASED ON TURN
		int[] atkGiver = null;
		int[] atkReceiver = null;
		if (move == MAX_TURN)
		{
			atkGiver = maxer;
			atkReceiver = opponent;
		}
		else
		{
			atkGiver = opponent;
			atkReceiver = maxer;
		}

		// ADD VALID MOVES
		ArrayList<Action> actions = new ArrayList<>();
		if (atkGiver[RIGHT] != HAND_OUT)
		{
			if (atkReceiver[RIGHT] != HAND_OUT) actions.add(ActionSticks2H.RIGHT_ATK_RIGHT);
			if (atkReceiver[LEFT] != HAND_OUT) actions.add(ActionSticks2H.RIGHT_ATK_LEFT);
		}
		if (atkGiver[LEFT] != HAND_OUT)
		{
			if (atkReceiver[RIGHT] != HAND_OUT) actions.add(ActionSticks2H.LEFT_ATK_RIGHT);
			if (atkReceiver[LEFT] != HAND_OUT) actions.add(ActionSticks2H.LEFT_ATK_LEFT);
		}

		return actions.toArray(new Action[actions.size()]);
	}

	@Override
	public State result(State state, Action action)
	{
		// clone the state w/ copy constructor
		StickState2H newState2h = new StickState2H((StickState2H) state);
		ActionSticks2H action2h = (ActionSticks2H) action;

		int[] reciever = null;
		int[] attacker = null;

		// if (Player.convertFromInt(newState2h.move) == Player.MAX)
		// This does not do checking if hand is valid, that should be done outside this function
		if (move == MAX_TURN)
		{
			reciever = newState2h.opponent;
			attacker = newState2h.maxer;
		}
		else
		{
			reciever = newState2h.maxer;
			attacker = newState2h.opponent;
		}

		// update hands with receiver and attackers determined
		switch (action2h)
		{
		case LEFT_ATK_LEFT:
			reciever[LEFT] = updateHand(attacker[LEFT], reciever[LEFT]);
			break;
		case LEFT_ATK_RIGHT:
			reciever[RIGHT] = updateHand(attacker[LEFT], reciever[RIGHT]);
			break;
		case RIGHT_ATK_LEFT:
			reciever[LEFT] = updateHand(attacker[RIGHT], reciever[LEFT]);
			break;
		case RIGHT_ATK_RIGHT:
			reciever[RIGHT] = updateHand(attacker[RIGHT], reciever[RIGHT]);
			break;
		default:
			throw new RuntimeException("invalid action for given state");
		}

		// update move; if move is 0, then next move is 1. if move is 1, next move is 0 (2%2 == 0).
		newState2h.move = (newState2h.move + 1) % 2;
		return newState2h;
	}

	private int updateHand(int attackerHand, int recipientHand)
	{
		return (recipientHand + attackerHand) % 5;
	}

	@Override
	public void printState()
	{
		System.out.print(toString());
	}

	public String toString()
	{
//		return String.format("%d %d | %d %d : %d loopstate:%b", maxer[LEFT], maxer[RIGHT], opponent[LEFT], opponent[RIGHT], move, loopState);
		 return String.format("%d %d | %d %d : %d", maxer[LEFT], maxer[RIGHT], opponent[LEFT], opponent[RIGHT], move);

	}

	public static void generateAllStates()
	{
		allStates = new HashMap<>();
		depthStateTest = new HashMap<>();

		StickState2H startState = new StickState2H();
		allStates.put(startState, startState);
		depthStateTest.put(startState, startState);

		addChildStates(startState);
		// depth first search
		// addChildStatesDFS(startState);
		// addChildStates_AdvLoopDetect(startState);
	}

	private static void addChildStates(StickState2H baseState)
	{
		// base case
		if (baseState.isTerminal()) return;

		Action[] actions = baseState.getActions();
		for (Action actionRaw : actions)
		{
			ActionSticks2H action = (ActionSticks2H) actionRaw;
			StickState2H child = (StickState2H) baseState.result((State) baseState, action);

			if (allStates.get(child) == null)
			{
				depthStateTest.put(child, child);
				allStates.put(child, child);

				// recursively depth first find states, this will find loops for a given path
				addChildStates(child);

				// remove from depthFirst
				depthStateTest.remove(child);
			}
		}
	}

	@SuppressWarnings("unused")
	private static void addChildStatesDFS(StickState2H baseState)
	{
		// base case
		if (baseState.isTerminal()) return;

		Action[] actions = baseState.getActions();
		for (Action actionRaw : actions)
		{
			ActionSticks2H action = (ActionSticks2H) actionRaw;
			StickState2H child = (StickState2H) baseState.result((State) baseState, action);

			// if we haven't encountered it in our depth first search
			if (depthStateTest.get(child) == null)
			{
				// ensure that some other branch did not generate this state, because we will
				// already have values for its children
				if (allStates.get(child) == null)
				{
					// add to depth first set catch loops
					depthStateTest.put(child, child);
					allStates.put(child, child);

					// recursively depth first find states, this will find loops for a given path
					addChildStatesDFS(child);

					// remove from depthFirst
					depthStateTest.remove(child);
				}
			}
			else
			{
				// depth search has already found this state... mark the state as infinite loop.
				// (note: if depth first found this, it should be in all states)
				StickState2H previous = allStates.get(child);

				// note that loopState should not factor into equality nor hash functions
				previous.loopState = true;
			}
		}
	}

	@SuppressWarnings("unused")
	private static boolean addChildStates_AdvLoopDetect(StickState2H baseState)
	{
		// base case
		if (baseState.isTerminal()) { return baseState.loopState; }

		ArrayList<StickState2H> children = new ArrayList<>();

		Action[] actions = baseState.getActions();
		for (Action actionRaw : actions)
		{
			ActionSticks2H action = (ActionSticks2H) actionRaw;
			StickState2H child = (StickState2H) baseState.result((State) baseState, action);
			children.add(child);

			// if we haven't encountered it in our depth first search
			if (depthStateTest.get(child) == null)
			{
				// ensure that some other branch did not generate this state, because we will
				// already have values for its children
				if (allStates.get(child) == null)
				{
					// add to depth first set catch loops
					depthStateTest.put(child, child);
					allStates.put(child, child);

					// recursively depth first find states, this will find loops for a given path
					boolean wasLoopNode = addChildStates_AdvLoopDetect(child);

					// overwrite state in allStates to include whether this was a loopstate
					// we still need this to be in all states for the recursion to work (see the if
					// statement two scopes up)
					child.loopState = wasLoopNode;
					allStates.put(child, child);

					// remove from depthFirst
					depthStateTest.remove(child);
				}
			}
			else
			{
				// depth search has already found this state... mark the state as infinite loop.
				// (note: if depth first found this, it should be in all states)
				StickState2H previousEncounter = allStates.get(child);

				// note that loopState should not factor into equality nor hash functions
				previousEncounter.loopState = true;
				previousEncounter.terminalStateOfLoop = true;
			}
		}

		for (StickState2H child : children)
		{
			if (child.loopState)
			{
				baseState.loopState = true;
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args)
	{
		StickState2H.generateAllStates();

		HashMap<StickState2H, StickState2H> stateMap = StickState2H.getStateMap();
		System.out.println(stateMap.size());

		// HashSet<String> findDupliate = new HashSet<>();
		// ArrayList<String> sortedList = new ArrayList<>();
		for (StickState2H state : stateMap.keySet())
		{
			state.printState();
			// if(findDupliate.contains(state.toString()))
			// {
			// System.out.println("Found duplicate");
			// }
			// else
			// {
			// findDupliate.add(state.toString());
			// sortedList.add(state.toString());
			// }
		}
		// System.out.println(findDupliate.size());
		// sortedList.sort(new Comparator<String>() {
		// @Override
		// public int compare(String o1, String o2) {
		// return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
		// }
		// });
		//
		// for(String x : sortedList)
		// System.out.print(x);
	}

	public String getFileString()
	{
		return "" + maxer[0] + " "+ maxer[1] + " " + opponent[0] + " " + opponent[1] + " " +  move;
	}

	public static StickState2H parseString(String string)
	{
		String[] stateStrElements = string.split(" ");
		StickState2H state = new StickState2H();
		
		state.maxer[0] = Integer.parseInt(stateStrElements[0]);
		state.maxer[1] = Integer.parseInt(stateStrElements[1]);
		state.opponent[0] = Integer.parseInt(stateStrElements[2]);
		state.opponent[1] = Integer.parseInt(stateStrElements[3]);
		state.move = Integer.parseInt(stateStrElements[4]);
		
		return state;
	}

	// enum Player
	// {
	// MAX, // think of this as "us"
	// MIN; // think of this as the "opponent"
	//
	// public int getValue()
	// {
	// return this == Player.MAX ? 0 : 1;
	// }
	//
	// public static Player convertFromInt(int value)
	// {
	// return value == 0 ? MAX : MIN;
	// }
	//
	// public static int convertFromEnum(Player value)
	// {
	// return value == Player.MAX ? 0 : 1;
	// }
	// }
}
