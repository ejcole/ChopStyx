import math
class AugmentedMinimax(object):

    max_explored = None
    min_explored = None

    def minimax_start_max_player(self, start_state):
        """ Start the minimax algorithm as the max player """
        path_visited = set(start_state.key)
    
        #recycle previous discovered dictionaries because they will give us a speed up
        self.max_explored = max_explored if max_explored != None else {}
        self.min_explored = min_explored if min_explored != None else {}
        
        actions = start_state.get_actions()
        
        #start with a non-null move
        best_move = actions[0]
        best_move_utilty = -math.inf
        
        for action in actions:
            child_solution_depth = self.min_recurse(state.result(action), 1, path_visited)
            child_utility = self.utility_from_depth(child_solution_depth, 0)
            if child_utility > best_move_utilty:
                best_move = action
                best_move_utilty = child_utility
                
        return best_move
            
    def minimax_start_min_player(self, start_state):
        """ start the minimax algorithm as the min player """
        path_visited = set(start_state.key)
    
        #recycle previous discovered dictionaries because they will give us a speed up
        self.max_explored = max_explored if max_explored != None else {}
        self.min_explored = min_explored if min_explored != None else {}
        
        actions = start_state.get_actions()
        
        #start with a non-null move
        best_move = actions[0]
        best_move_utilty = math.inf
        
        for action in actions:
            child_solution_depth = self.max_recurse(state.result(action), 1, path_visited)
            child_utility = self.utility_from_depth(child_solution_depth, 0)
            if child_utility < best_move_utilty:
                best_move = action
                best_move_utilty = child_utility
                
        return best_move
    
    
    def min_recurse(self, state, depth, path_visited):
        """ find the best move depth for the min player to take """
        if state.key in path_visited:
            return 0
        path_visited.add(state.key)
        
        if state.is_terminal():
            utility = state.utility(depth)
            if utility == 0:
                return 0
            elif utility > 0:
                return 1
            else: #utilty < 0
                return -1
            
        best_move_util = math.inf
        best_move_depth = math.inf #changing from maximum int to infinity sys.maxsize #largest int
        
        if state.key not in self.min_explored:
            for action in state.get_actions():
                child_depth = self.max_recurse(state, depth + 1, path_visited)
                child_util = self.utility_from_depth(child_depth, depth)
                if child_util < best_move_util:
                    best_move_depth = child_depth
                    best_move_util = child_util
                    
            #this node's best move achieves win/lose/loop in previous-best depth + 1
            self.min_explored[state.key] = self.adjust_depth(1, best_move_depth)
        else: #state has been explored
            previous_depth = min_explored[state.key]
            best_move_util = self.utility_from_depth(previous_depth, depth)
            if previous_depth == 0:
                self.min_explored[state.key] = 0
            elif previous_depth > 0:
                self.min_explored[state.key] = previous_depth + 1
            else:
                self.min_explored[state.key] = previous_depth - 1
                
        path_visited.remove(state.key)
        
        return self.utility_from_depth(1, best_move_depth) 
    
    def max_recurse(self, state, depth, path_visited):
        """ find the best move depth for the max player to take """
        if state.key in path_visited:
            return 0
        path_visited.add(state.key)
        
        if state.is_terminal():
            utility = state.utility(depth)
            if utility == 0:
                return 0
            elif utility > 0:
                return 1
            else: #utilty < 0
                return -1
            
        best_move_util = -math.inf
        best_move_depth = -math.inf #java code has smallest int, changing to inf #-sys.maxsize +1 #smallest int (twos complement to calculate)
        
        if state.key not in self.max_explored:
            for action in state.get_actions():
                child_depth = self.min_recurse(state, depth + 1, path_visited)
                child_util = self.utility_from_depth(child_depth, depth)
                if child_util > best_move_util:
                    best_move_depth = child_depth
                    best_move_util = child_util
                    
            #this node's best move achieves win/lose/loop in previous-best depth + 1
            self.max_explored[state.key] = self.adjust_depth(1, best_move_depth)
        else: #state has been explored
            previous_depth = max_explored[state.key]
            best_move_util = self.utility_from_depth(previous_depth, depth)
            if previous_depth == 0:
                self.max_explored[state.key] = 0
            elif previous_depth > 0:
                self.max_explored[state.key] = previous_depth + 1
            else:
                self.max_explored[state.key] = previous_depth - 1
                
        path_visited.remove(state.key)
        
        return self.utility_from_depth(1, best_move_depth) 
    
    def utility_from_depth(self, child_solution_depth, current_depth):
        """
        negative depth = min wins; eg -5 means the min player wins in 5 moves 
        positive depth = max win;  eg +3 means the max player will win in 3 moves
        0 depth = this means a loop will be taken in this direction; which is better than losing
        
        @invariant: current_depth is a positive value
        """
        if child_solution_depth == 0:
            return 0
        elif child_solution_depth > 0:
            return math.pow(math.e, -(current_depth + child_solution_depth))
        else:
            #note: we're intentionally correcting child depth to be positive for calculation
            return -math.pow(math.e, -(current_depth + -child_solution_depth))
        
            
    def adjust_depth(self, current_depth, depth_to_terminal_state):
        """ Adjusts depth to be negative if terminal depth is negative, or positive if terminal depth is positive"""
        if depth_to_terminal_state == 0:
            #depth leads to loop
            return 0;
        elif depth_to_terminal_state > 0:
            #make the depth to terminal state a larger positive value
            return current_depth + depth_to_terminal_state
        else:
            #make the depth to terminal state a larger negative value
            return depth_to_terminal_state - current_depth
