package application;

import algorithms.abstracts.Action;

public enum ActionSticks2H implements Action
{
	LEFT_ATK_RIGHT,
	LEFT_ATK_LEFT, 
	RIGHT_ATK_RIGHT,
	RIGHT_ATK_LEFT;
	
	@Override
	public void print()
	{
	}
	
	public String toString()
	{
		switch(this)
		{
		case LEFT_ATK_LEFT:
			return "L->L";
		case LEFT_ATK_RIGHT:
			return "L->R";
		case RIGHT_ATK_LEFT:
			return "R->L";
		case RIGHT_ATK_RIGHT:
			return "R->R";
		}
		throw new RuntimeException("invalid state of enum for toString");
	}

	public String getString(int move)
	{
		switch(this)
		{
		case LEFT_ATK_LEFT:
			return move == StickState2H.MAX_TURN ? "0 2" : "2 0";
		case LEFT_ATK_RIGHT:
			return move == StickState2H.MAX_TURN ? "0 3" : "2 1";
		case RIGHT_ATK_LEFT:
			return move == StickState2H.MAX_TURN ? "1 2" : "3 0";
		case RIGHT_ATK_RIGHT:
			return move == StickState2H.MAX_TURN ? "1 3" : "3 1";
		}
		throw new RuntimeException("invalid state of enum for toString");
	}

	public static ActionSticks2H parseAction(String moveStr)
	{
		//ugly string comparisons but I'm on a deadline...
		switch(moveStr)
		{
		case "0 2":
		case "2 0":
			return LEFT_ATK_LEFT;
		case "0 3":
		case "2 1":
			return LEFT_ATK_RIGHT;
		case "1 2":
		case "3 0":
			return RIGHT_ATK_LEFT;
		case "1 3":
		case "3 1":
			return RIGHT_ATK_RIGHT;
		default:
			throw new RuntimeException("unparsable move: " + moveStr);
		}
	}	
}
