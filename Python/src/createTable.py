__author__ = "group"
from ErikIdeas import tap


def generate_moves(state):
    p1 =[0,1]
    p2 =[2,3]
    moves = []
    if not state[4]: #state[4] =0 for player 0 turn
        for i in p1:
            for j in p2:
                if(state[i] != 0 and state[j] != 0):
                    moves.append((i,j))
    else:
        for i in p2:
            for j in p1:
                if( state[i] != 0 and state[j] != 0):
                    moves.append((i,j))
    return moves

def createTables(initial_state):
    if goal_test(initial_state):
        return True
    moves = generate_moves(initial_state)

def createStates(state,state_set):
    if state not in state_set:
        state_set.add(state)
        moves = generate_moves(state)
        for move in moves:
            newState = tap(state, move)
            createStates(newState, state_set)

def goal_test(state):
    state = list(state[0:4])
    if(sum(state[0:2]) == 0  or sum(state[2:4]) == 0):
        return True
    return False

def main():
    #Player 1 hands, player 2 hands, player move, depth
    initial_state = (1,1,1,1,0)
    state_set = set()
    createStates(initial_state,state_set)
    print(len(state_set))
    #print(state_set)
    goal_test(initial_state)
    num_goals = 0
    for i in state_set:
        if goal_test(i):
            #print(i)
            num_goals += 1
    print(num_goals)

if __name__ == '__main__':
    main()
