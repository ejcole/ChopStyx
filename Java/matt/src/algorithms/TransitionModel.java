package algorithms;

import algorithms.abstracts.Action;

public interface TransitionModel
{
	Action[] getPossibleActions();
}
