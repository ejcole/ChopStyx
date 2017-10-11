package application;

import algorithms.abstracts.Action;

public enum ActionSticks2H implements Action
{
//	MAXXER_LEFT_ATK_RIGHT,     // 0 = player0_left  
//	MAXXER_LEFT_ATK_LEFT,     // 0 = player0_left
//	MAXXER_RIGHT_ATK_RIGHT,    // 1 = player0_right
//	MAXXER_RIGHT_ATK_LEFT,    // 1 = player0_right
//	OPPONENT_LEFT_ATK_RIGHT,   // 2 = player1_left  
//	OPPONENT_LEFT_ATK_LEFT,   // 2 = player1_left
//	OPPONENT_RIGHT_ATK_RIGHT,   // 3 = player1_right 
//	OPPONENT_RIGHT_ATK_LEFT   // 3 = player1_right
	
	
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
}
