import math
class AugmentedMinimax(object):

    max_explored = None
    min_explored = None

    def utility_from_depth(self, child_solution_depth):
        """
        negative depth = min wins; eg -5 means the min player wins in 5 moves 
        positive depth = max win;  eg +3 means the max player will win in 3 moves
        0 depth = this means a loop will be taken in this direction; which is better than losing
        """
        pass
    
    
    def minimax_start_max_player(self, start_state):
        """ Start the minimax algorithm as the max player """
        
        path_visited = set(tuple(start_state))
    
        #recycle previous discovered dictionaries because they will give us a speed up
        max_explore = max_explored if max_explored != None else {}
        min_explore = min_explored if min_explored != None else {}
        
        actions = start_state.get_actions()
        
        #start with a non-null move
        best_move = actions[0]
        best_move_utilty = math.inf
        
        for action in actions:
            child_solution_depth = self.min_recurse(state.result(action), 1, path_visited)
            child_utility = self.utility_from_depth(child_solution_depth)
            if child_utility > best_move_utilty:
                best_move = action
                best_move_utilty = child_utility
                
        return best_move
            
    def minimax_start_min_player(self, start_state):
        """ start the minimax algorithm as the min player """
        pass
    
    
    def min_recurse(self, state, depth, path_visited):
        """ find the best move for the min player to take """
        pass
    
    
    def max_recurse(self, state, depth, path_visited):
        """ find the best move for the max player to take """
        pass

















def main():
    print("hello world")

if __name__ == '__main__':
    main()

