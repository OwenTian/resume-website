/* PLAYER CLASS: 
 * Represents a player object
 * Combines eval function and minMax functionality
 * Related File: Connect4.java
 * 
 * 
 * minMax function.
 * Owen Tian, spheal@bu.edu
 * Date: 4/26/13
 * CS112 HW 9 minmax function
 * Code for minMax and related functions based off of pseudocode from Lecture 22 slides
 * and EnumerationExample.java.
 */


public class Player {
    private int counter = 0;
    private int counterTracker = 0;
    private static int D = 6; // max depth for search
    private int sandwichCounter = 0;
    private static int MAX_THRESHOLD = Integer.MAX_VALUE;
    private static int MIN_THRESHOLD = Integer.MIN_VALUE;
    
    public int eval(int [][] board) {
        int returnVal = 0;
        // Going through the columns, bottom up left right.
        for (int i = 0; i < board.length; i++) {
            counter = 0;
            counterTracker = 0;           
            for (int j = board.length - 1; j >= 0; j--) {                
                int temp = evalHelper(board, j, i, "col");
                if (temp == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (temp == Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                returnVal += temp;
                
                // Adjusts for mobility for your piece
                // Evaluate this mobility decision later. Is it really wortwhile?
                if (board[j][i] != 0 && (j == board.length - 1 || board[j + 1][i] != 0)) {
                    // Rightward mobility
                    if (i != board.length - 1 && board[j][i + 1] == 0 && (j == board.length - 1 || board[j + 1][i+1] != 0)) {
                        if (board[j][i] == 10) {
                            returnVal += 50;
                        } else if (board[j][i] == 1) {
                            returnVal -= 50;
                        }
                    }
                    // Leftward mobility
                    if (i != 0 && board[j][i - 1] == 0 && (j == board.length - 1 || board[j + 1][i - 1] != 0)) {
                        if (board[j][i] == 10) {
                            returnVal += 50;
                        } else if (board[j][i] == 1) {
                            returnVal -= 50;
                        }
                    }
                    // Diagonal left mobility UP
                    if (j != 0 && i != 0 && board[j - 1][i - 1] == 0 && (board[j][i- 1] != 0)) {
                        if (board[j][i] == 10) {
                            returnVal += 50;
                        } else if (board[j][i] == 1) {
                            returnVal -= 50;
                        }
                    }
                    // Diagonal right mobility UP
                    if (j != 0 && i != board.length - 1 && board[j - 1][i + 1] == 0 && (board[j][i+1] != 0)) {
                        if (board[j][i] == 10) {
                            returnVal += 50;
                        } else if (board[j][i] == 1) {
                            returnVal -= 50;
                        }
                    }
                    // Diagonal left mobility DOWN
                    if (j != board.length - 1 && i != 0 && board[j + 1][i - 1] == 0 && (j == board.length - 2 || board[j+2][i-1] != 0)) {
                        if (board[j][i] == 10) {
                            returnVal += 50;
                        } else if (board[j][i] == 1) {
                            returnVal -= 50;
                        }
                    }
                    // Diagonal left mobility DOWN
                    if (j != board.length - 1 && i != board.length - 1 && board[j + 1][i + 1] == 0 && (j == board.length - 2 || board[j+2][i+1] != 0)) {
                        if (board[j][i] == 10) {
                            returnVal += 50;
                        } else if (board[j][i] == 1) {
                            returnVal -= 50;
                        }
                    }
                    // Updward mobility
                    if (j != 0 && board[j - 1][i] == 0) {
                        if (board[j][i] == 10) {
                            returnVal += 50;
                        } else if (board[j][i] == 1) {
                            returnVal -= 50;
                        }
                    }
                    
                }
                
            }
        }
        
        // Going through the rows, bottom up left right.
        for (int i = board.length - 1; i >= 0; i--) {
            counter = 0;
            counterTracker = 0;
            this.sandwichCounter = 0;
            for (int j = 0; j < board.length; j++) {
                sandwichCounter++;
                int temp = evalHelper(board, i, j, "row");
                if (temp == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (temp == Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                returnVal += temp;
            }
        }
        
        // Going through the downward right slanting diagonals (that looks like this \), starting at the top, 
        // going left-right, moving down. 
        int k = 0;
        int j = 0;
        for (int i = 0; i < board.length/2 + 1; i++) {
            counter = 0;
            counterTracker = 0;            
            for (j = 0, k = i; k < board.length; k++, j++) {
                int temp = evalHelper(board, j, k, "ddiag");
                if (temp == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (temp == Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                returnVal += temp;
            }
        }
        
        // Going through the upward right slanting diagonals (/), starting down, moving up, left to right. 
        for (int i = 0; i < board.length/2 + 1; i++) {
            counter = 0;
            counterTracker = 0;
            for (j = board.length - 1, k = i; i <= j; j--, k++) {
                int temp = evalHelper(board, j, k, "udiag");
                if (temp == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (temp == Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                returnVal += temp;
            }
        }
        
        // Final diagonal check; goes down the left side of the board (column 0) and checks these diagonals
        // as the previous two tests overlook them and this is worth testing.
        
        for (int i = 1; i < board.length/2 + 1; i++) {
            counter = 0;
            counterTracker = 0;
            for (j = 0; j < board.length - i; j++) {
                int temp = evalHelper(board, j + i, j, "ddiag");
                if (temp == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (temp == Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                returnVal += temp;
            }
        }
        
        for (int i = board.length - 2; i > board.length/2 - 1; i--) {
            counter = 0;
            counterTracker = 0;
            for (j = 0; j < board.length - i; j++) {
                int temp = evalHelper(board, i - j, j, "udiag");
                if (temp == Integer.MAX_VALUE) {
                    return Integer.MAX_VALUE;
                } else if (temp == Integer.MIN_VALUE) {
                    return Integer.MIN_VALUE;
                }
                returnVal += temp;
            }
        }
        
        return returnVal;
        
    }
    
    
    private int evalHelper(int[][] board, int left, int right, String type) {
        int returnVal = 0;
        if (counter != 0 && board[left][right] == counterTracker) {
            counter++;
        } else if (counter != 0 && board[left][right] != counterTracker) {
            counter = 0;
        }
        if ((board[left][right] == 10 || board[left][right] == 1) && counter < 1) {
            counter = 1;
            counterTracker = board[left][right];
        }
        // Detects a trap setup formation
        if (counter == 2 && right >= 2 && right <= 6 && type.equals("row")) {
            if (board[left][right -2] == 0 && board[left][right + 1] == 0) {
                if (counterTracker == 1) {
                    returnVal -= 4000;
                } else if (counterTracker == 10) {
                    returnVal += 500;
                }
            }
        }
        // Detects x x _ x and x _ x x situations
        if (sandwichCounter > 3 && type.equals("row")) {
            returnVal += sandwich(board, left, right - 3, right, type);
        }
        if (type.equals("row") && counter == 3) {
            if (right - 3 >= 0 && board[left][right - 3] == 0) {
                if (counterTracker == 10) {
                    returnVal += 1000;
                } else if (counterTracker == 1) {
                    returnVal -= 4000;
                }
            } else if (right - 3 >= 0 && board[left][right - 3] == 10) {
                returnVal += 1000;
            }
            
        } else if (type.equals("ddiag") && counter == 3 && (right == board.length - 1 || left == board.length - 1)) {
            if (left - 3 >= 0 && right - 3 >= 0 && board[left - 3][right - 3] == 0) {
                if (counterTracker == 10) {
                    returnVal += 1000;
                } else if (counterTracker == 1) {
                    returnVal -= 4000;
                }
            }
        } else if (type.equals("udiag") && counter == 3 && (right == board.length - 1 || left == 0)) {
            if (left + 3 <= board.length - 1 && right - 3 >= 0 && board[left + 3][right - 3] == 0) {
                if (counterTracker == 10) {
                    returnVal += 1000;
                } else if (counterTracker == 1) {
                    returnVal -= 4000;
                }
            }
        }
        if (counter == 3 && type.equals("col") && left!= 0 && board[left - 1][right] == 0 && counterTracker == 1) {
            returnVal -= 4000;
        } else if (counter == 3 && type.equals("col") && left!= 0 && board[left - 1][right] == 0 && counterTracker == 10) {
            returnVal += 400;
        }
        if (counter == 3 && type.equals("row") && right != board.length - 1 && board[left][right+1] == 0) {
            if (board[left][right] == 1) {
                returnVal -= 4000;
            } else if (board[left][right] == 10) {
                returnVal += 400;
            }
        }
        // More diag testing 
        if (counter == 3 && type.equals("udiag") && right != board.length - 1 && left != 0 && board[left][right + 1] != 0 && board[left - 1][right + 1] == 0) {
            if (counterTracker == 10) {
                returnVal += 100;
            } else if (counterTracker == 1) {
                returnVal -= 2000;
            }
        }
        
        // end more diag testing
        if (counter >= 4 && counterTracker == 10) {
            return Integer.MAX_VALUE;
        }
        if (counter >= 4 && counterTracker == 1) {
            return Integer.MIN_VALUE;
        }
        return returnVal;
    }
    
    // Tests the sandwich test cases
    private int sandwich(int[][] board, int left, int start, int right, String type) {
        int tempCount10 = 0; 
        int tempCount1 = 0;
        boolean oneNot = false;
        for (int i = start; i <= right; i++) {
            if (board[left][i] == 10) {
                tempCount10++;
            } else if (board[left][i] == 1) {
                tempCount1++;
            }
            if (board[left][i] == 0) {
                oneNot = true;
            }
            if (tempCount10 > 2 && oneNot) {
                return 1000;
            } else if (tempCount1 > 2 && oneNot) {
                return -1500;
            }
        }
        return 0;
    }
    
    public int chooseMove(int[][] board, int player) {
        
        int bestMove = -1;
        int val;
        boolean isMax = true;
        
        int minVal = Integer.MAX_VALUE;
        int maxVal = Integer.MIN_VALUE;
        
        
        if(player == 10) {
            
            isMax = true;
        }
        
        else if(player == 1) {
            
            isMax = false;
        }
        
        for(int i = 0; i < 8; i++) {
            
            if(findMove(board, i) != -1) {
                
                // inserting the new move
                int row = findMove(board, i);
                
                board[row][i] = player;
                
                if(isMax) {
                    
                    val = minMax(board, 1, !isMax, player, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    if(val > maxVal) {
                        
                        bestMove = i;
                        maxVal = val;
                    }
                    
                }
                
                else {
                    
                    val = minMax(board, 1, !isMax, player, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    
                    if(val < minVal) {
                        
                        bestMove = i;
                        minVal = val;
                    }
                    
                }
                
                // deleting the move that was just made
                board[row][i] = 0;
            }
        }
        
        return bestMove;
        
    }
    
    private int minMax(int[][] board, int depth, boolean isMax, int player, int alpha, int beta) {
        
        if(depth == D || this.eval(board) >= MAX_THRESHOLD || this.eval(board) <= MIN_THRESHOLD) {
            
            return this.eval(board);
        }
        
        else if(isMax) {
            
            int val = Integer.MIN_VALUE;
            
            int currentPlayer = player;
            int nextPlayer;
            
            // switching the value of player for the next recursive call
            
            if(player == 10) {
                
                nextPlayer = 1;
            }
            
            else {
                
                nextPlayer = 10;
            }
            
            
            for(int i = 0; i < 8; i++) {
                
                alpha = max(alpha, val);
                
                if(beta <alpha) {
                    
                    break;
                }
                
                // making the next move
                int row = findMove(board, i);
                
                // making sure the row isn't full
                if(row != -1) {
                    
                    board[row][i] = currentPlayer;
                    
                    val = max(val, minMax(board, depth + 1, !isMax, nextPlayer, alpha, beta));
                    
                    // erasing the move
                    board[row][i] = 0;
                    
                }
                
            }
            
            return val;
            
        }
        
        else {
            
            int val = Integer.MAX_VALUE;
            
            int currentPlayer = player;
            int nextPlayer;
            
            // switching the value of player for the next recursive call
            
            if(player == 10) {
                
                nextPlayer = 1;
            }
            
            else {
                
                nextPlayer = 10;
            }
            
            
            for(int i = 0; i < 8; i++) {
                
                beta = min(beta, val);
                
                if(beta < alpha) {
                    
                    break;
                }
                
                // making the next move
                int row = findMove(board, i);
                
                if(row != -1) {
                    
                    
                    board[row][i] = currentPlayer;
                    
                    val = min(val, minMax(board, depth + 1, !isMax, nextPlayer, alpha, beta));
                    
                    // erasing the move
                    board[row][i] = 0;
                    
                }
                
            }
            
            return val;
        }
        
    }
    
    // this method takes the column of a move and returns the row of the next available slot.
    // returns -1 if column is full.
    
    private int findMove(int[][] board, int column) {
        
        for(int i = 7; i >= 0; i--) {
            
            if(board[i][column] == 0) {
                
                return i;
            }
        }
        
        return -1;
    }
    
    private static int max(int a, int b) {
        
        if(a > b) {
            
            return a;
        }
        
        else if(b > a) {
            
            return b;
        }
        
        else {
            
            return a;
        }
    }
    
    private static int min(int a, int b) {
        
        if(a < b) {
            
            return a;
        }
        
        else if(b < a) {
            
            return b;
        }
        
        else {
            
            return a;
        }
    }
    
    public static void main(String args[]) {
        Player p1 = new Player();
        /*
        // Eval unit tests
        int[][] tester1 = {{10, 0, 0},{10,10, 0},{10,10,10}};
        
        System.out.println(p1.eval(tester1));
        int[][] tester2 = {{0, 0, 10},{0,10,10},{10,10,10}};
        System.out.println(p1.eval(tester2));
        int[][] tester3 = {{0, 0, 0, 0},{0, 0, 0, 10},{0,0,10,10},{0,10,10,10}};
        System.out.println(p1.eval(tester3));
        int[][] tester4 = {{0, 0, 0, 0},{0, 0, 0, 1},{0,0,1,1},{0,1,1,1}};
        System.out.println(p1.eval(tester4));
        int[][] tester5 = {{0, 0, 0, 0},{0, 0, 0, 0},{0, 0, 0, 0},{10, 10, 10, 0}};
        System.out.println(p1.eval(tester5));
        int[][] tester6 = {{10, 0, 0, 0},{0,10,0,0},{0,0,10,0},{0,0,0,10}};
        System.out.println(p1.eval(tester6));
        int[][] tester7 = {{0, 0, 0, 10},{0,0,10,0},{0,10,0,0},{10,0,0,0}};
        System.out.println(p1.eval(tester7));
        
        
        int[][] tester8 = {{0, 0, 10, 10, 10, 10, 10, 0},
            {0, 10, 0, 10, 0, 0, 0, 0},
            {10, 10, 10, 0, 10, 0, 10, 10},
            {10, 0, 10, 10, 10, 0, 10, 10},
            {10, 0, 10, 10, 10, 0, 10, 10},
            {0, 10, 0, 10, 0, 0, 0, 0},
            {0, 0, 10, 10, 0, 0 , 10, 10},
            {10, 10, 10, 10, 10, 10, 0, 10, 10}};
        System.out.println("Speed test:");
        for (int i = 2; i < 40320; i++) {
            int temp = p1.eval(tester8);
        }        
        System.out.println("done");
        
        // Tests on 8x8 boards.
        int[][] tester9 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {10, 0, 0, 0, 0, 0, 0, 0},
            {10, 0, 0, 0, 10, 0, 0, 0},
            {10, 0, 0, 0, 10, 0, 0, 0},
            {10, 0, 0, 10, 10, 10, 0, 0},
            {10, 0, 10, 10, 10, 10 , 0, 0},
            {10, 10, 10, 10, 10, 10, 0, 10, 10}};
        System.out.println("Should be a positive win:");
        System.out.println(p1.eval(tester9));
        
        
        System.out.println("Should be 0:");
        int[][] tester10 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {10, 1, 10, 1, 10, 1, 10, 1}};
        System.out.println(p1.eval(tester10));
        
        System.out.println("Should be an opponent win:");
        int[][] tester11 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 10, 0, 0, 0},
            {0, 0, 1, 10, 10, 10, 0, 0},
            {10, 1, 10, 10, 1, 10, 10, 10}};
        System.out.println(p1.eval(tester11));
        
        System.out.println("Should be an opponent win:");
        int[][] tester12 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 10, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 10, 0, 0, 10, 0, 0},
            {10, 10, 10, 0, 10, 10, 0, 10}};
        System.out.println(p1.eval(tester12));
        
        System.out.println("Should be a win on the part of the player:");
        int[][] tester13 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 10, 0, 0, 0, 0},
            {10, 0, 10, 0, 0, 0, 0, 0},
            {0, 10, 0, 0, 0, 0, 0, 0},
            {10, 0, 10, 0, 10, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 10, 0, 0, 10, 0, 0},
            {10, 10, 10, -0, 10, 10, 0, 10}};
        System.out.println(p1.eval(tester13));
        
        int[][] tester14 = {{0, 0, 0, 0},{0, 0, 0, 0},{0, 0, 0, 0}, {0, 10, 10, 10}};
        System.out.println(p1.eval(tester14));
        int[][] tester15 = {{0, 0, 0, 0},{0, 10, 0, 0},{0, 0, 10, 0}, {0, 0, 0, 10}};
        System.out.println(p1.eval(tester15));
        int[][] tester16 = {{0, 0, 0, 10},{0, 0, 10, 0},{0, 10, 0, 0}, {0, 0, 0, 0}};
        System.out.println(p1.eval(tester16));
        
        System.out.println("Should be a win on the part of the player:");
        int[][] tester17 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 10, 0, 0, 0, 0},
            {0, 0, 10, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 10, 0, 0, 10, 0, 0, 0},
            {0, 0, 10, 0, 0, 0, 0, 0},
            {0, 0, 0, 10, 0, 10, 0, 0},
            {10, 0, 0, 0,10, 0, 0, 10}};
        System.out.println(p1.eval(tester17));
        
        System.out.println("Should be a win on the part of the opponent:");
        int[][] tester18 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 10, 0, 0, 0, 0},
            {0, 0, 10, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 10, 0, 0, 10, 1, 0, 0},
            {0, 0, 10, 0, 1, 10, 0, 0},
            {0, 0, 1, 1, 1, 10, 0, 0},
            {10, 0, 1, 10,10, 1, 1, 10}};
        System.out.println(p1.eval(tester18));
        
        int[][] tester19 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 10, 0}, 
            {10, 1, 1, 1, 10, 10, 0, 0}};
        System.out.println(p1.eval(tester19));
        
        int[][] tester20 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 10, 0, 0}, 
            {0, 1, 1, 1, 10, 10, 10, 0}};
        System.out.println(p1.eval(tester20));
        
        
        // Testing sandwich on the ttwo types it shoudl detect for column vectors. 
        int[][] tester21 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 1, 1, 0, 1, 0, 0, 0}};
        System.out.println(p1.eval(tester21));
        
        int[][] tester22 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 1, 0, 1, 1, 0, 0, 0}};
        System.out.println(p1.eval(tester22));
        
        int[][] tester23 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 10, 10, 10, 1, 0, 1, 1}};
        System.out.println(p1.eval(tester23));
        
        int[][] tester24 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 10, 0, 0},
            {0, 0, 0, 0, 10, 10, 0, 0}, 
            {0, 0, 0, 0, 10, 10, 0, 0}, 
            {0, 1, 10, 0, 10, 1, 0, 0}, 
            {1, 10, 1, 1, 1, 10, 10, 0}, 
            {1, 10, 1, 1, 1, 10, 1, 0}};
        System.out.println(p1.eval(tester24));
        
        int[][] tester25 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 1, 0, 1, 10, 0}, 
            {0, 10, 0, 10, 10, 10, 1, 0}, 
            {0, 1, 0, 10, 1, 1, 10, 0}, 
            {0, 10, 0, 1, 10, 1, 1, 0}};
        System.out.println(p1.eval(tester25));
        
        int[][] tester26 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 10, 0, 0, 0}, 
            {0, 0, 10, 0, 1, 1, 0, 0}};
            System.out.println(p1.eval(tester26));
            */
        
        int[][] tester27 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 10, 10, 10, 1, 1, 1, 0}};
        System.out.println(p1.eval(tester27));
        
        int[][] tester28 = {{0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 0, 0, 0, 0, 0}, 
            {0, 0, 10, 0, 0, 0, 0, 0}, 
            {0, 0, 0, 1, 1, 0, 0, 0}, 
            {0, 10, 10, 1, 10, 1, 0, 0}};
        System.out.println(p1.eval(tester28));
        // MinMax unit tests
        
        // diagonal test
        /*
        int[][] test1 =
        {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0},
            {10, 10, 1, 0, 0, 0, 0, 0},
            {1, 1, 10, 1, 0, 0, 0, 0},
            {1, 10, 10, 10, 1, 0, 0, 0}};
        
        System.out.println("---winning with diagonal test, correct answer: 0---");
        System.out.println("pre-test 1");
        System.out.println(p1.chooseMove(test1, 1));
        System.out.println("post-test 1");
        
        
        // empty case test
        
        int[][] test2 =
        {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}};
        
        
        System.out.println("---empty case test, correct answer: idk---");
        System.out.println("pre-test 2");
        System.out.println(p1.chooseMove(test2, 1));
        System.out.println("post-test 2");
        
        // blocking the opponent
        
        int[][] test3 =
        {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {10, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 0, 0, 0, 0},
            {1, 10, 0, 0, 0, 0, 0, 0},
            {10, 10, 10, 0, 0, 0, 0, 0},
            {1, 1, 1, 10, 1, 0, 0, 0}};
        
        System.out.println("---blocking opponent test, correct answer: 3---");
        System.out.println("pre-test 3");
        System.out.println(p1.chooseMove(test3, 1));
        System.out.println("post-test 3");
        
        int[][] test4 =
        {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 1, 10, 10, 10, 0}};
        
        
        System.out.println("---blocking vs. winning test, correct answer: 1---");
        System.out.println("pre-test 4");
        System.out.println(p1.chooseMove(test4, 1));
        System.out.println("post-test 4");
        
        int[][] test5 =
        {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 10, 0, 0, 1, 0, 0},
            {0, 1, 10, 0, 0, 10, 1, 0},
            {10, 1, 10, 1, 1, 10, 0, 1}};
        
        
        System.out.println("---random test---");
        System.out.println("pre-test 5");
        System.out.println(p1.chooseMove(test5, 1));
        System.out.println("post-test 5");
        
        // testing a full board
        
        int[][] test6 =
        {{10, 10, 10, 10, 1, 10, 10, 10},
            {10, 10, 10, 10, 10, 10, 10, 10},
            {10, 10, 10, 10, 10, 10, 10, 10},
            {10, 10, 10, 10, 10, 10, 10, 10},
            {10, 10, 1, 10, 10, 10, 10, 10},
            {10, 10, 10, 10, 10, 1, 10, 10},
            {10, 1, 10, 10, 10, 10, 1, 10},
            {10, 1, 10, 1, 1, 10, 10, 1}};
        
        
        System.out.println("---full board test---");
        System.out.println("pre-test 6");
        System.out.println(p1.chooseMove(test6, 1));
        System.out.println("post-test 6");
        
        int[][] test7 =
        {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {10, 10, 1, 10, 1, 10, 10, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 10, 1, 1, 1}};
        
        
        System.out.println("---impossible board test---");
        System.out.println("pre-test 7");
        System.out.println(p1.chooseMove(test7, 10));
        System.out.println("post-test 7");
        
        int[][] test8 = {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {10, 1, 10, 1, 10, 1, 10, 1}};
        
        System.out.println("---random board test #2---");
        System.out.println("pre-test 8");
        System.out.println(p1.chooseMove(test8, 1));
        System.out.println("post-test 8");
        
        int[][] test9 =
        {{0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {10, 10, 10, 10, 10, 10, 10, 1},
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 10, 1, 1, 1}};
        
        System.out.println("pre-test 9");
        System.out.println(p1.chooseMove(test9, 1));
        System.out.println("post-test 9");
        
        
        int[][] test10 =
        {{0, 0, 0, 0, 0, 0, 0, 10},
            {0, 0, 0, 0, 0, 10, 0, 0},
            {0, 10, 0, 1, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 10, 1},
            {10, 1, 10, 10, 1, 10, 1, 1},
            {1, 10, 1, 1, 10, 1, 1, 10}};
        System.out.println("pre-test 10");
        System.out.println(p1.chooseMove(test10, 1));
        System.out.println("post-test 10");
        */
    }
    
}