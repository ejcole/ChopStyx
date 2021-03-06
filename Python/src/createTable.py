__author__ = "group"

import math
import pandas as pd
from src.AugmentedMinimax import *


class State:
    def __init__(self, hand=(1, 1, 1, 1), turn=0, parent=None, loop=(), key=(1, 1, 1, 1, 0), childs=(), hand_count=2):
        self.hands = hand
        self.turn = turn #p1 or p2
        self.parent = parent
        self.loop = loop
        self.key = key
        self.childs = childs
        self.hand_count = hand_count

    def get_actions(self):
        p1 = list(range(self.hand_count))
        p2 = list(range(self.hand_count, 2 * self.hand_count))
        moves = []
        if not self.turn:  # state[4] =0 for player 0 turn
            for i in p1:
                for j in p2:
                    if self.hands[i] != 0 and self.hands[j] != 0:
                        moves.append((i, j))
        else:
            for i in p2:
                for j in p1:
                    if self.hands[i] != 0 and self.hands[j] != 0:
                        moves.append((i, j))
        return moves

    def result(self, action):
        newHands = list(self.hands)
        if newHands[action[1]] == 0 or newHands[action[0]] == 0:
            print("Invalid Move")
            return self
        newHands[action[1]] += newHands[action[0]]
        if newHands[action[1]] >= 5:
            newHands[action[1]] %= 5
        newState = State(hand=tuple(newHands), turn=(self.turn + 1) % 2, parent=self)
        newState.key = key_maker(newState)
        return newState

    def is_terminal(self):
        hands = list(self.hands)
        return sum(hands[0: self.hand_count]) == 0 or sum(hands[self.hand_count: 2 * self.hand_count]) == 0

    def utility(self, depth):
        if self.turn == 0:
            return -math.exp(-depth)
        else:
            return math.exp(-depth)


def createStates_dict(state, state_dict):
    # state_dict[key_maker(state)] = state
    if state.key not in state_dict:
        moves = state.get_actions()
        for move in moves:
            newState = state.result(move)
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


def key_maker(state):
    x = list(state.hands)
    x.append(state.turn)
    return tuple(x)


def print_child(child):
    for i in child:
        if type(i) == State:
            print(i.key)
        else:
            print(i)


def write_csv(state_dict):
    # best_move_dict = generate_best_moves(state_dict)
    df = pd.DataFrame.from_dict(state_dict, orient='index')
    # print(df)
    df.to_csv("Best_Moves_wrong")

    f = open('bestmoves.csv', 'w')
    f.write("state,action\n")
    for key in state_dict.keys():
        s = str(key)
        m = str(state_dict[key])
        f.write(s[1:-1].replace(',', '') + "," + m[1:-1].replace(',', '') + '\n')
    f.close()


def play_game(state, best_moves_dict):
    while not state.is_terminal():
        move = best_moves_dict[state.key]
        newstate = state.result(move)
        print(move)
        print(newstate.hands)
        tapper = int(input("Enter Tapper: "))
        tapee = int(input("Enter Tapee: "))
        state = (newstate.is_terminal((tapper, tapee)))
        print(state.hands)


def main():
    # Player 1 hands, player 2 hands, player move,
    initial_state = State()
    state_dict = {}
    createStates_dict(initial_state, state_dict)
    state_dict[initial_state.key] = initial_state
    # print("num reachable states", len(state_dict.keys()))
    minimaxer = AugmentedMinimax()
    bestmove = {}
    for tups in state_dict.items():
        state = tups[1]
        state_key = tups[0]
        if state.turn == 1:
            bestmove[state_key] = minimaxer.minimax_start_min_player(state)
        else:
            bestmove[state_key] = minimaxer.minimax_start_max_player(state)

    write_csv(bestmove)


if __name__ == '__main__':
    main()

# old code

# main()
    # print(minimax(test_state))
    # play_game(initial_state,best_moves_dict=generate_best_moves(state_dict))
    # initial_state.hands = (1,1,1,1,1,1)
    # initial_state.turn=0
    # initial_state.hand_count =3
    # print(generate_moves_v(initial_state))
    # best_move_dict = generate_best_moves(state_dict)
    # write_csv(state_dict)
    # printHandAndAction(initial_state, best_move_dict,state_dict)
    # test = state_dict[((4,2,2,4,0))]
    # printHandAndAction(test, best_move_dict,state_dict)
    # initial_state.hands = (1, 1, 1, 1)
    # initial_state.turn = 0
    # initial_state.parent = None
    # initial_state.loop = None
    # initial_state.key = key_maker(initial_state.hands, initial_state.key)
    # initial_state.hand_count = 3
    # initial_state.hands = (1,1,1,1,1,1)
    # initial_state.key = key_maker(initial_state)

# def printHandAndAction(state, best_move_dict,state_dict):
#     print(state.key, best_move_dict[state.key])
#     it = iter(state.childs)
#
#     for iters_state in it:
#         #print(iters_state.hands)
#         child_state = iters_state
#         action = next(it) # action is here to move the iteration around
#         #print(action)
#         if action == best_move_dict[state.key]:
#             #print(action)
#             #print(child_state.key)
#             if(child_state.is_terminal()):
#                 print(child_state.key)
#                 if child_state.turn == 0:
#                     print("Player 1 wins")
#                 else:
#                     print("Player 0 wins")
#                 return
#             child_state = state_dict[child_state.key]
#             printHandAndAction(child_state, best_move_dict,state_dict)

# def minimax(state):
#     depth = 0
#     action = None
#     player = state.turn
#
#     child_states = state.childs
#     it = iter(child_states)
#     options = []
#     actions = []
#     for x in it:
#         child_state = x
#         action = next(it)
#         if(player == 0):
#             options.append(minimax_min(child_state,depth+1))
#             actions.append(action)
#         else:
#             options.append(minimax_max(child_state,depth+1))
#             actions.append(action)
#     if(player == 0):
#         return actions[options.index(max(options))]
#     else:
#         return actions[options.index(min(options))]


# def minimax_max(state, depth):
#     if(state.is_terminal()):
#         return -math.exp(depth)
#
#     v= -math.inf
#     child_states = state.childs
#     it = iter(child_states)
#     for x in it:
#         child_state = x
#         action = next(it) # action is here to move the iteration around
#         v = max(v,minimax_min(child_state,depth+1))
#     return v


# def minimax_min(state, depth):
#     if(state.is_terminal()):
#         return math.exp(depth)
#
#     v= math.inf
#     child_states = state.childs
#     it = iter(child_states)
#     for x in it:
#         child_state = x
#         action = next(it) # action is here to move the iteration around
#         v = min(v,minimax_max(child_state,depth+1))
#     return v

# def generate_best_moves(state_dict):
#     best_move_dict = {}
#     for key in state_dict.keys():
#         current_key = key
#         best_move_dict[current_key] = minimax(state_dict[current_key])
#     return best_move_dict

# def generate_moves(state):
#     p1 = [0, 1]
#     p2 = [2, 3]
#     moves = []
#     if not state.turn:  # state[4] =0 for player 0 turn
#         for i in p1:
#             for j in p2:
#                 if state.hands[i] != 0 and state.hands[j] != 0:
#                     moves.append((i, j))
#     else:
#         for i in p2:
#             for j in p1:
#                 if state.hands[i] != 0 and state.hands[j] != 0:
#                     moves.append((i, j))
#     return moves

# def create_States_set(state, state_set):
#     if state not in state_set:
#         state_set.add(state)
#         moves = state.get_actions()
#         for move in moves:
#             #newState = tap(state, move) #think this was missed in the refactor?
#             newState = state.result(move)
#             create_States_set(newState, state_set)
