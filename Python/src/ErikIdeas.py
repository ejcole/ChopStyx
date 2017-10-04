#state = player0_left player0_right player1_left player1_right
#action = tapper tapped
    # 0 = player0_left
    # 1 = player0_right
    # 2 = player1_left
    # 3 = player1_right
__author__ = "group"

def tap(state,action):
    newState = list(state)
    if newState[action[1]] == 0:
        print("Invalid Move")
        return state
    newState[action[1]] += newState[action[0]]
    if newState[action[1]] >= 5 :
        newState[action[1]] = newState[action[1]] % 5
    if newState[4] == 0:
        newState[4] = 1
    else:
        newState[4] = 0
    return tuple(newState)

def main():
    state = (1,1,1,1,0)
    action = (0,2)
    state = tap(state,action)
    print(state)
    action = (2,1)
    state = tap(state,action)
    print(state)
    action = (1,3)
    state = tap(state,action)
    print(state)
    action = (3,1)
    state = tap(state,action)
    print(state)


if __name__ == '__main__':
    main()
