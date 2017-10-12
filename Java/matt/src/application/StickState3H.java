package application;

import java.util.ArrayList;
import java.util.HashMap;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class StickState3H extends State
{
	private static HashMap<StickState3H, StickState3H> allStates;
	private static HashMap<StickState3H, StickState3H> depthStateTest;

	public static HashMap<StickState3H, StickState3H> getStateMap()
	{
		return allStates;
	}

	/** Constants for indexing into arrays. The left hand is index 0; the right hand is index 1 */
	public final static int LEFT = 0;
	public final static int MIDDLE = 1;
	public final static int RIGHT = 2;
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
	//protected boolean terminalStateOfLoop = false;
	//protected Integer encounteredDepth = null;

	/**
	 * Construct the start state.
	 */
	public StickState3H()
	{
		maxer = new int[]
		{ 1, 1, 1 };
		opponent = new int[]
		{ 1, 1, 1 };
		move = MAX_TURN;
	}

	public StickState3H(StickState3H toCopy)
	{
		this.maxer = toCopy.maxer.clone();
		this.opponent = toCopy.opponent.clone();
		this.move = toCopy.move;
	}

	@Override
	public boolean equals(Object state)
	{
		StickState3H other = (StickState3H) (state);
		if (this.move != other.move) return false;
		for (int i = 0; i < maxer.length; i++)
		{
			if (other.maxer[i] != this.maxer[i]) return false;
		}
		for (int i = 0; i < opponent.length; i++)
		{
			if (other.opponent[i] != this.opponent[i]) return false;
		}
		//if (!compareEncounteredDepths(other)) return false;

		// loopstate SHOULD NOT compared for proper state enumeration
		return true;
	}

//	public boolean compareEncounteredDepths(StickState3H other)
//	{
//		// if one is not null, then the other must be non-null and of equivalent value
//		if (this.encounteredDepth != null || other.encounteredDepth != null)
//		{
//			// if either is null, then there is a difference since we know one of previous is not
//			// null
//			if (this.encounteredDepth == null || other.encounteredDepth == null) return false;
//
//			// they are both non-null, therefore they should be equal.
//			if (!this.encounteredDepth.equals(other.encounteredDepth)) return false;
//
//		}
//		// if one is null, then both should be null
//		if (this.encounteredDepth == null || other.encounteredDepth == null)
//		{
//			// both should be null, therefore both should be equivalent
//			if (this.encounteredDepth != other.encounteredDepth) return false;
//		}
//		return true;
//	}

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
		//if (encounteredDepth != null) hornerHash = (hornerHash * primeNumber) + encounteredDepth;
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
			if (atkReceiver[RIGHT] != HAND_OUT) actions.add(ActionSticks3H.RIGHT_ATK_RIGHT);
			if (atkReceiver[MIDDLE] != HAND_OUT) actions.add(ActionSticks3H.RIGHT_ATK_MIDDLE);
			if (atkReceiver[LEFT] != HAND_OUT) actions.add(ActionSticks3H.RIGHT_ATK_LEFT);
		}
		if (atkGiver[MIDDLE] != HAND_OUT)
		{
			if (atkReceiver[RIGHT] != HAND_OUT) actions.add(ActionSticks3H.MIDDLE_ATK_RIGHT);
			if (atkReceiver[MIDDLE] != HAND_OUT) actions.add(ActionSticks3H.MIDDLE_ATK_MIDDLE);
			if (atkReceiver[LEFT] != HAND_OUT) actions.add(ActionSticks3H.MIDDLE_ATK_LEFT);
		}
		if (atkGiver[LEFT] != HAND_OUT)
		{
			if (atkReceiver[RIGHT] != HAND_OUT) actions.add(ActionSticks3H.LEFT_ATK_RIGHT);
			if (atkReceiver[MIDDLE] != HAND_OUT) actions.add(ActionSticks3H.LEFT_ATK_MIDDLE);
			if (atkReceiver[LEFT] != HAND_OUT) actions.add(ActionSticks3H.LEFT_ATK_LEFT);
		}

		return actions.toArray(new Action[actions.size()]);
	}

	@Override
	public State result(State state, Action action)
	{
		// clone the state w/ copy constructor
		StickState3H newState2h = new StickState3H((StickState3H) state);
		ActionSticks3H action2h = (ActionSticks3H) action;

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
		case LEFT_ATK_MIDDLE:
			reciever[MIDDLE] = updateHand(attacker[LEFT], reciever[MIDDLE]);
			break;
		case LEFT_ATK_RIGHT:
			reciever[RIGHT] = updateHand(attacker[LEFT], reciever[RIGHT]);
			break;
		case RIGHT_ATK_LEFT:
			reciever[LEFT] = updateHand(attacker[RIGHT], reciever[LEFT]);
			break;
		case RIGHT_ATK_MIDDLE:
			reciever[MIDDLE] = updateHand(attacker[RIGHT], reciever[MIDDLE]);
			break;
		case RIGHT_ATK_RIGHT:
			reciever[RIGHT] = updateHand(attacker[RIGHT], reciever[RIGHT]);
			break;
		case MIDDLE_ATK_LEFT:
			reciever[LEFT] = updateHand(attacker[MIDDLE], reciever[LEFT]);
			break;
		case MIDDLE_ATK_MIDDLE:
			reciever[MIDDLE] = updateHand(attacker[MIDDLE], reciever[MIDDLE]);
			break;
		case MIDDLE_ATK_RIGHT:
			reciever[RIGHT] = updateHand(attacker[MIDDLE], reciever[RIGHT]);
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
		 return String.format("%d %d %d| %d %d %d : %d", maxer[LEFT], maxer[MIDDLE], maxer[RIGHT], opponent[LEFT], opponent[MIDDLE], opponent[RIGHT], move);
	}

	public static void generateAllStates()
	{
		allStates = new HashMap<>();
		depthStateTest = new HashMap<>();

		StickState3H startState = new StickState3H();
		allStates.put(startState, startState);
		depthStateTest.put(startState, startState);

		addChildStates(startState);
	}

	private static void addChildStates(StickState3H baseState)
	{
		// base case
		if (baseState.isTerminal()) return;

		Action[] actions = baseState.getActions();
		for (Action actionRaw : actions)
		{
			ActionSticks3H action = (ActionSticks3H) actionRaw;
			StickState3H child = (StickState3H) baseState.result((State) baseState, action);

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

	
	public static void main(String[] args)
	{
		StickState3H.generateAllStates();

		HashMap<StickState3H, StickState3H> stateMap = StickState3H.getStateMap();
		System.out.println(stateMap.size());

		for (StickState3H state : stateMap.keySet())
		{
			state.printState();
		}
	}

	public String getFileString()
	{
		return "" + maxer[LEFT] + " "+ maxer[MIDDLE] + " " + maxer[RIGHT] + " " + opponent[LEFT] + " " + opponent[MIDDLE] + " "+ opponent[RIGHT] + " " +  move;
	}

	public static StickState3H parseString(String string)
	{
		String[] stateStrElements = string.split(" ");
		StickState3H state = new StickState3H();
		
		state.maxer[LEFT] = Integer.parseInt(stateStrElements[0]);
		state.maxer[MIDDLE] = Integer.parseInt(stateStrElements[1]);
		state.maxer[RIGHT] = Integer.parseInt(stateStrElements[2]);
		state.opponent[LEFT] = Integer.parseInt(stateStrElements[3]);
		state.opponent[MIDDLE] = Integer.parseInt(stateStrElements[4]);
		state.opponent[RIGHT] = Integer.parseInt(stateStrElements[5]);
		state.move = Integer.parseInt(stateStrElements[6]);
		
		return state;
	}
}
