package algorithms.abstracts;

public abstract class State
{
	public abstract boolean equals(Object state);
	public abstract int hashCode();
	public abstract void printState();
	public abstract boolean isTerminal();
	public abstract double utility(int depth);
	public abstract Action[] getActions();
	public abstract State result(State state, Action action);
}
