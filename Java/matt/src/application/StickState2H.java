package application;

import algorithms.abstracts.Action;
import algorithms.abstracts.State;

public class StickState2H extends State
{
	protected int[] maxer;
	protected int[] opponent;
	protected int move;

	private final static int LEFT = 0;
	private final static int RIGHT = 1;

	public StickState2H()
	{
		maxer = new int[] { 5, 5 };
		opponent = new int[] { 5, 5 };
		move = 0;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isTerminal()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int utility()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Action[] getActions()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State result(State state, Action action)
	{
		// clone the state w/ copy constructor
		StickState2H newState2h = new StickState2H((StickState2H) state);
		ActionSticks2H action2h = (ActionSticks2H) action;

		int[] reciever = null;
		int[] attacker = null;

		// This does not do checking if hand is valid, that should be done outside this function
		if (Player.convertFromInt(newState2h.move) == Player.MAX)
		{
			reciever = newState2h.opponent;
			attacker = newState2h.maxer;
		}
		else
		{
			reciever = newState2h.maxer;
			attacker = newState2h.opponent;
		}

		//update hands with reciever and attackers determined
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

		//update move
		newState2h.move = (newState2h.move + 1) % 2;
		return newState2h;
	}

	private int updateHand(int attackerHand, int recipientHand)
	{
		return (recipientHand + attackerHand) % 6;
	}

	@Override
	public void printState()
	{
		// TODO Auto-generated method stub

	}

	enum Player
	{
		MAX, // think of this as "us"
		MIN;

		public int getValue()
		{
			return this == Player.MAX ? 0 : 1;
		}

		public static Player convertFromInt(int value)
		{
			return value == 0 ? MAX : MIN;
		}
	}
}
