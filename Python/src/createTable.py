__author__ = "group"
import math

class State:
    def __init__(self, hand=(1, 1, 1, 1), turn=0, parent=None, loop=(), key=(1, 1, 1, 1, 0), childs=()):
        self.hands = hand
        self.turn = turn #p1 or p2
        self.parent = parent
        self.loop = loop
        self.key = key
        self.childs = childs

def tap(state, action):
    newHands = list(state.hands)
    if newHands[action[1]] == 0:
        print("Invalid Move")
        return state
    newHands[action[1]] += newHands[action[0]]
    if newHands[action[1]] >= 5:
        newHands[action[1]] = newHands[action[1]] % 5

    newState = State(hand=tuple(newHands), turn=(state.turn + 1) % 2, parent=state)
    newState.key = key_maker(newState)

    return newState


def generate_moves(state):
    p1 = [0, 1]
    p2 = [2, 3]
    moves = []
    if not state.turn:  # state[4] =0 for player 0 turn
        for i in p1:
            for j in p2:
                if state.hands[i] != 0 and state.hands[j] != 0:
                    moves.append((i, j))
    else:
        for i in p2:
            for j in p1:
                if state.hands[i] != 0 and state.hands[j] != 0:
                    moves.append((i, j))
    return moves


def create_States_set(state, state_set):
    if state not in state_set:
        state_set.add(state)
        moves = generate_moves(state)
        for move in moves:
            newState = tap(state, move)
            create_States_set(newState, state_set)


def createStates_dict(state, state_dict):
    #state_dict[key_maker(state)] = state
    if state.key not in state_dict:
        moves = generate_moves(state)
        for move in moves:
            newState = tap(state, move)
            if newState in state_dict:
                loo = list(state.loop)
                state.loop = tuple(loo.append(newState))
            # unique children code, doesn't work in current implementation
            # key_list =[]
            # child_list = list(state.childs)
            # for child in child_list:
            #     key_list.append(child.key)
            # if key_maker(newState) not in key_list:
            x = list(state.childs)
            x.append(newState)
            x.append(move)
            state.childs = tuple(x)
            state_dict[state.key] = state
            createStates_dict(newState, state_dict)


# def createTables(initial_state):
#     if goal_test(initial_state):
#         return True
#     moves = generate_moves(initial_state)


def goal_test(state):
    hands = list(state.hands)
    return sum(hands[0:2]) == 0 or sum(hands[2:4]) == 0


def key_maker(state):
    x = list(state.hands)
    x.append(state.turn)
    return tuple(x)
    # return tuple(list(state.hands).append(state.turn))


def print_child(child):
    for i in child:
        if type(i) == State:
            print(i.key)
        else:
            print(i)


def minimax(state):
    depth = 0
    action = None

    child_states = state.childs
    it = iter(child_states)
    options = []
    actions = []
    for x in it:
        child_state = x
        action = next(it)
        options.append(minimax_min(child_state,depth+1))
        actions.append(action)
    return actions[options.index(max(options))]


def minimax_max(state, depth):
    if(goal_test(state)):
        return -math.exp(depth)

    v= -math.inf
    child_states = state.childs
    it = iter(child_states)
    for x in it:
        child_state = x
        action = next(it) # action is here to move the iteration around
        v = max(v,minimax_min(child_state,depth+1))
    return v


def minimax_min(state, depth):
    if(goal_test(state)):
        return math.exp(depth)

    v= math.inf
    child_states = state.childs
    it = iter(child_states)
    for x in it:
        child_state = x
        action = next(it) # action is here to move the iteration around
        v = min(v,minimax_max(child_state,depth+1))
    return v

def printHandAndAction(state, best_move_dict):
    print(state.key, best_move_dict[state.key])
    it = iter(state.childs)
    for iters_state in it:
        child_state = iters_state
        action = next(it) # action is here to move the iteration around
        if action == best_move_dict[state.key]:
            printHandAndAction(child_state, best_move_dict)

def generate_best_moves(state_dict):
    best_move_dict = {}
    for key in state_dict.keys():
        current_key = state_dict[key].key
        best_move_dict[current_key] = minimax(state_dict[current_key])
    return best_move_dict

def main():
    # Player 1 hands, player 2 hands, player move,
    initial_state = State()
    # initial_state.hands = (1, 1, 1, 1)
    # initial_state.turn = 0
    # initial_state.parent = None
    # initial_state.loop = None
    # initial_state.key = key_maker(initial_state.hands, initial_state.key)

    #state_set = set()

    # createStates_set(initial_state,state_set)
    state_dict = {}
    createStates_dict(initial_state, state_dict)
    state_dict[initial_state.key] = initial_state
    # print(state_dict[(1,1,1,1,0)])
    #print(len(state_dict.keys()))
    # print(state_set)
    #print_child((1, 1, 1, 1, 0), state_dict)
    # for i in state_dict.keys():
    #     print (i)
    #

    # # goal_test(initial_state)
    # num_goals = 0
    # for i in state_set:
    #     if goal_test(i):
    #         #print(i)
    #         num_goals += 1
    # print(num_goals)
    #test_state = state_dict[(1,1,1,1,0)]
    #print(test_state.parent.turn)
   #print(test_state.parent.hands)
    #print_child(test_state.childs)
    best_move_dict = generate_best_moves(state_dict)

    # print(best_move_dict[initial_state])
    #print(type(best_move_dict))
    printHandAndAction(initial_state, best_move_dict)

    #print(minimax(test_state))


if __name__ == '__main__':
    main()



