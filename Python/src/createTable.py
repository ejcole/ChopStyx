__author__ = "group"


class State:
    # hands = ()
    # turn = 0
    # parent = None
    # loop = ()
    # key = ()

    def __init__(self, hand=(1, 1, 1, 1), turn=0, parent=None, loop=(), key=(1, 1, 1, 1, 0)):
        self.hands = hand
        self.turn = turn #p1 or p2
        self.parent = parent
        self.loop = loop
        self.key = key
    #
    # def key_maker(self):
    #     return tuple(list(hands).append(turn))


def tap(state, action):
    newHands = list(state.hands)
    if newHands[action[1]] == 0:
        print("Invalid Move")
        return state
    newHands[action[1]] += newHands[action[0]]
    if newHands[action[1]] >= 5 :
        newHands[action[1]] = newHands[action[1]] % 5

    newState = State(hand=tuple(newHands), turn=(state.turn + 1) % 2, parent=state)
    newState.key = key_maker(newState)

    return newState


def generate_moves(state):
    p1 = [0, 1]
    p2 = [2, 3]
    moves = []
    if not state.turn: #state[4] =0 for player 0 turn
        for i in p1:
            for j in p2:
                if state.hands[i] != 0 and state.hands[j] != 0:
                    moves.append((i,j))
    else:
        for i in p2:
            for j in p1:
                if state.hands[i] != 0 and state.hands[j] != 0:
                    moves.append((i,j))
    return moves


def createStates_set(state,state_set):
    if state not in state_set:
        state_set.add(state)
        moves = generate_moves(state)
        for move in moves:
            newState = tap(state, move)
            createStates_set(newState, state_set)


def createStates_dict(state, state_dict):
    if state.key not in state_dict:
        moves = generate_moves(state)
        for move in moves:
            newState = tap(state, move)
            if newState in state_dict:
                loo = list(state.loop)
                state.loop = tuple(loo.append(newState))
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



def main():
    #Player 1 hands, player 2 hands, player move,
    initial_state = State()
    # initial_state.hands = (1, 1, 1, 1)
    # initial_state.turn = 0
    # initial_state.parent = None
    # initial_state.loop = None
    # initial_state.key = key_maker(initial_state.hands, initial_state.key)

    state_set = set()
    state_dict = {}
    # createStates_set(initial_state,state_set)
    createStates_dict(initial_state, state_dict)
    print(len(state_dict))
    #print(state_set)





    # # goal_test(initial_state)
    # num_goals = 0
    # for i in state_set:
    #     if goal_test(i):
    #         #print(i)
    #         num_goals += 1
    # print(num_goals)

if __name__ == '__main__':
    main()
