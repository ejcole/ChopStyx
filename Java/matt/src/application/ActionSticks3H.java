package application;

import algorithms.abstracts.Action;

public enum ActionSticks3H implements Action
{
	LEFT_ATK_RIGHT, LEFT_ATK_MIDDLE, LEFT_ATK_LEFT, RIGHT_ATK_RIGHT, RIGHT_ATK_MIDDLE, RIGHT_ATK_LEFT, MIDDLE_ATK_RIGHT, MIDDLE_ATK_MIDDLE, MIDDLE_ATK_LEFT;

	@Override
	public void print()
	{
	}

	public String toString()
	{
		switch (this)
		{
		case LEFT_ATK_LEFT:
			return "L->L";
		case LEFT_ATK_MIDDLE:
			return "L->M";
		case LEFT_ATK_RIGHT:
			return "L->R";
		case RIGHT_ATK_LEFT:
			return "R->L";
		case RIGHT_ATK_MIDDLE:
			return "R->M";
		case RIGHT_ATK_RIGHT:
			return "R->R";
		case MIDDLE_ATK_LEFT:
			return "M->L";
		case MIDDLE_ATK_MIDDLE:
			return "M->M";
		case MIDDLE_ATK_RIGHT:
			return "M->R";
		}
		throw new RuntimeException("invalid state of enum for toString");
	}

	public String getString(int move)
	{
		switch (this)
		{
		case LEFT_ATK_LEFT:
			return move == StickState2H.MAX_TURN ? "0 3" : "3 0";
		case LEFT_ATK_MIDDLE:
			return move == StickState2H.MAX_TURN ? "0 4" : "3 1";
		case LEFT_ATK_RIGHT:
			return move == StickState2H.MAX_TURN ? "0 5" : "3 2";
		case RIGHT_ATK_LEFT:
			return move == StickState2H.MAX_TURN ? "2 3" : "5 0";
		case RIGHT_ATK_MIDDLE:
			return move == StickState2H.MAX_TURN ? "2 4" : "5 1";
		case RIGHT_ATK_RIGHT:
			return move == StickState2H.MAX_TURN ? "2 5" : "5 2";
		case MIDDLE_ATK_LEFT:
			return move == StickState2H.MAX_TURN ? "1 3" : "4 0";
		case MIDDLE_ATK_MIDDLE:
			return move == StickState2H.MAX_TURN ? "1 4" : "4 1";
		case MIDDLE_ATK_RIGHT:
			return move == StickState2H.MAX_TURN ? "1 5" : "4 2";
		}
		throw new RuntimeException("invalid state of enum for toString");
	}

	public static ActionSticks3H parseAction(String moveStr)
	{
		// ugly string comparisons but I'm on a deadline...
		switch (moveStr)
		{
		case "0 3":
		case "3 0":
			return LEFT_ATK_LEFT;
		case "0 4":
		case "3 1":
			return LEFT_ATK_MIDDLE;
		case "0 5":
		case "3 2":
			return LEFT_ATK_RIGHT;
		case "2 3":
		case "5 0":
			return RIGHT_ATK_LEFT;
		case "2 4":
		case "5 1":
			return RIGHT_ATK_MIDDLE;
		case "2 5":
		case "5 2":
			return RIGHT_ATK_RIGHT;
		case "1 3":
		case "4 0":
			return MIDDLE_ATK_LEFT;
		case "1 4":
		case "4 1":
			return MIDDLE_ATK_MIDDLE;
		case "1 5" : 
		case "4 2":
			return MIDDLE_ATK_RIGHT;
		default:
			throw new RuntimeException("unparsable move: " + moveStr);
		}
	}
}
