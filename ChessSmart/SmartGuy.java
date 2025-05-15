import Chess.ChessBoard;
import Chess.ChessMove;
import Chess.ChessPiece;
import Chess.ChessPosition;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;

class SmartGuy {

    public Socket s;
    public BufferedReader sin;
    public PrintWriter sout;
    Random generator = new Random();

    double t1, t2;
    int me;
    int boardState;
    public int state[][] = new int[8][8]; // state[0][0] is the bottom left corner of the board (on the GUI)
    int turn = -1;
    int round;
    int nodes = 0;
    private static final int MAX_QUIESCENCE_DEPTH = 4;

    ChessMove validMoves[] = new ChessMove[100];
    int numValidMoves;
    Evaluation eval = new Evaluation();

    public static class Result {
        public int value;
        public int index;

        public Result(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    static final Map<Integer, Character> pieceMap = new HashMap<>();

    static {
        pieceMap.put(0, '-');
        pieceMap.put(1, 'R');
        pieceMap.put(2, 'H');
        pieceMap.put(3, 'B');
        pieceMap.put(4, 'Q');
        pieceMap.put(5, 'K');
        pieceMap.put(6, 'P');

        pieceMap.put(-1, 'r');
        pieceMap.put(-2, 'h');
        pieceMap.put(-3, 'b');
        pieceMap.put(-4, 'q');
        pieceMap.put(-5, 'k');
        pieceMap.put(-6, 'p');
    }

    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},         {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };


    // main function that (1) establishes a connection with the server, and then plays whenever it is this player's turn
    public SmartGuy(int _me, String host) {
        me = _me;
        initClient(host);
        int myMove;

        eval.initTables();

        while (true) {
            readMessage();

            if (turn == me) {
                getGlobalValidMoves(state);

                myMove = move();
                //myMove = generator.nextInt(numValidMoves);        // select a move randomly
                getGlobalValidMoves(state);
                String sel = validMoves[myMove].getEndPosition().getRow() - 1 + "\n" +
                        (validMoves[myMove].getEndPosition().getColumn() - 1) + "\n" +
                        (validMoves[myMove].getStartPosition().getRow() - 1) + "\n" +
                        (validMoves[myMove].getStartPosition().getColumn() - 1);

                sout.println(sel);
//                System.out.println(sel);
            }
        }
    }

    private int move() {
        int depth;
//        if (round <= 2) {
//            depth = 2;
//        } else if (round <= 6) {
//            depth = 4;
//        } else if (round <= 20) {
//            depth = 5;
//        } else if (round <= 40) {
//            depth = 6;
//        } else if (round <= 80) {
//            depth = 7;
//        } else {
//            depth = 8;
//        }
        depth = 4;

        boolean maximizing = me == 1;

        Result result = minimax(state, depth, me, maximizing, Integer.MIN_VALUE, Integer.MAX_VALUE);

        System.out.println("Nodes: " + nodes);
        return result.index;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    private Result minimax(int[][] board, int depth, int currentPlayer, boolean maximizing, int alpha, int beta) {
        if (depth == 0) {
//            ChessBoard printBoard = new ChessBoard(board);
//            System.out.println(printBoard);
//            return new Result(heuristic(board), 0);
            return new Result(quiescence(board, maximizing, currentPlayer, alpha, beta, 0), 0);
        }

        List<ChessMove> moves = getValidMoves(currentPlayer, board, false);
        if (moves.isEmpty()) {
//            ChessBoard printBoard = new ChessBoard(board);
//            System.out.println(printBoard);
//            return new Result(heuristic(board), 0);
            return new Result(quiescence(board, maximizing, currentPlayer, alpha, beta, 0), 0);
        }

        nodes++;
        int bestValue = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestIndex = 0;

        for (int i = 0; i < moves.size(); i++) {
            ChessMove move = moves.get(i);
            int[][] newBoard = getNewState(board, move, currentPlayer);
            Result result = minimax(newBoard, depth - 1, currentPlayer == 2 ? 1 : 2, !maximizing, alpha, beta);

            if (maximizing) {
                if (result.value > bestValue) {
                    bestValue = result.value;
                    bestIndex = i;
                }
                alpha = Math.max(alpha, bestValue);
            } else {
                if (result.value < bestValue) {
                    bestValue = result.value;
                    bestIndex = i;
                }
                beta = Math.min(beta, bestValue);
            }

            if (beta <= alpha) {
                break;
            }
        }

        return new Result(bestValue, bestIndex);
    }

    private int quiescence(int[][] board, boolean maximizing, int currentPlayer, int alpha, int beta, int depth) {
        if (depth >= MAX_QUIESCENCE_DEPTH) {
            return heuristic(board);
        }

        int evaluation = heuristic(board);

//        if (evaluation >= beta) {
//            return beta;
//        }
//
//        if (evaluation > alpha) {
//            alpha = evaluation;
//        }

        if (maximizing) {
            if (evaluation >= beta) return beta;
            if (evaluation > alpha) alpha = evaluation;
        } else {
            if (evaluation <= alpha) return alpha;
            if (evaluation < beta) beta = evaluation;
        }

        List<ChessMove> moves = getValidMoves(currentPlayer, board, true);
        if (moves.isEmpty()) {
            return evaluation;
        }

        int bestValue = maximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (ChessMove move : moves) {
            int[][] newBoard = getNewState(board, move, currentPlayer);
            int score = quiescence(newBoard, !maximizing, currentPlayer == 2 ? 1 : 2, alpha, beta, depth + 1);

            if (maximizing) {
                if (score > bestValue) {
                    bestValue = score;
                }
                alpha = Math.max(alpha, bestValue);
            } else {
                if (score < bestValue) {
                    bestValue = score;
                }
                beta = Math.min(beta, bestValue);
            }

            if (beta <= alpha) {
                break;
            }
        }

        return bestValue;

    }

    private int heuristic(int[][] state) {
//        return getMaterialDisparity(board);
        int evaluation = eval.eval(state);

//        System.out.println(evaluation);
        return evaluation;
    }

    private double getMaterialDisparity(int[][] board) {
        int i, j;

        int countBlack = 0, countWhite = 0;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (board[i][j] == 1)
                    countWhite += 500;
                else if (board[i][j] == 2) {
                    countWhite += 320;
                } else if (board[i][j] == 3) {
                    countWhite += 330;
                } else if (board[i][j] == 4) {
                    countWhite += 900;
                } else if (board[i][j] == 5) {
                    countWhite += 20000;
                } else if (board[i][j] == 6) {
                    countWhite += 100;
                } else if (board[i][j] == -1) {
                    countBlack += 500;
                } else if (board[i][j] == -2) {
                    countBlack += 320;
                } else if (board[i][j] == -3) {
                    countBlack += 330;
                } else if (board[i][j] == -4) {
                    countBlack += 900;
                } else if (board[i][j] == -5) {
                    countBlack += 20000;
                } else if (board[i][j] == -6) {
                    countBlack += 100;
                }
            }
        }

        return countWhite - countBlack;
    }

    private List<ChessMove> getValidMoves(int currentPlayer, int[][] state, boolean onlyCaptures) {
        List<ChessMove> moves = new ArrayList<>();

        ChessBoard board = new ChessBoard(state);
        ChessPiece currentPiece;
        ChessPiece.TeamColor myColor = currentPlayer == 1 ? ChessPiece.TeamColor.WHITE : ChessPiece.TeamColor.BLACK;
        Collection<ChessMove> pieceMoves;

        int i, j;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                currentPiece = board.getPiece(new ChessPosition(i+1, j+1));
                if (currentPiece == null || currentPiece.getTeamColor() != myColor) {
                    continue;
                }
                // check to see if the piece can move
                pieceMoves = currentPiece.validMoves(new ChessPosition(i+1, j+1), board, true);
                if (pieceMoves.isEmpty()) {
                    continue;
                }
                if (onlyCaptures) {
                    for (ChessMove move : pieceMoves) {
                        if (board.getPiece(move.getEndPosition()) != null && board.getPiece(move.getEndPosition()).getTeamColor() != myColor) {
                            moves.add(move);
                        }
                    }
                }
                else {
                    moves.addAll(pieceMoves);
                }
            }
        }

        return moves;
    }

    private void getGlobalValidMoves(int state[][]) {
        int i, j;
        numValidMoves = 0;

        ChessBoard board = new ChessBoard(state);
        ChessPiece currentPiece;
        ChessPiece.TeamColor myColor = me == 1 ? ChessPiece.TeamColor.WHITE : ChessPiece.TeamColor.BLACK;
        Collection<ChessMove> pieceMoves;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                currentPiece = board.getPiece(new ChessPosition(i+1, j+1));
                if (currentPiece == null || currentPiece.getTeamColor() != myColor) {
                    continue;
                }
                // check to see if the piece can move
                pieceMoves = currentPiece.validMoves(new ChessPosition(i+1, j+1), board, true);
                if (pieceMoves.isEmpty()) {
                    continue;
                }
                // add piece moves to the list of valid moves
                for (ChessMove move : pieceMoves) {
                    validMoves[numValidMoves] = move;
                    numValidMoves++;
                }
            }
        }
    }

    private int[][] getNewState(int[][] currentState, ChessMove move, int me) {
        int[][] newState = deepCopy(currentState);

        int startRow = move.getStartPosition().getRow() - 1;
        int startCol = move.getStartPosition().getColumn() - 1;
        int endRow = move.getEndPosition().getRow() - 1;
        int endCol = move.getEndPosition().getColumn() - 1;

        if (currentState[startRow][startCol] == 5 || currentState[startRow][startCol] == -5) {
            if (Math.abs(startCol - endCol) > 1) {
                if (endCol == 6) {
                    if (endRow == 0) {
                        newState[0][5] = 1;
                        newState[0][7] = 0;
                    } else {
                        newState[7][5] = -1;
                        newState[7][7] = 0;
                    }
                } else if (endCol == 2) {
                    if (endRow == 0) {
                        newState[0][3] = 1;
                        newState[0][0] = 0;
                    } else {
                        newState[7][3] = -1;
                        newState[7][0] = 0;
                    }
                }
            }
        }

        if (move.getPromotionPiece() != null) {
            if (me == 1) {
                newState[endRow][endCol] = move.getPromotionPiece() == ChessPiece.PieceType.QUEEN ? 4 : 3;
            } else {
                newState[endRow][endCol] = move.getPromotionPiece() == ChessPiece.PieceType.QUEEN ? -4 : -3;
            }
        } else {
            newState[endRow][endCol] = newState[startRow][startCol];
        }

        newState[startRow][startCol] = 0;

        return newState;
    }

    public void readMessage() {
        int i, j;
        String status;
        try {
            //System.out.println("Ready to read again");
            turn = Integer.parseInt(sin.readLine());

            if (turn == -999) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                System.exit(1);
            }

            //System.out.println("Turn: " + turn);
            round = Integer.parseInt(sin.readLine());
            t1 = Double.parseDouble(sin.readLine());
            t2 = Double.parseDouble(sin.readLine());
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    state[i][j] = Integer.parseInt(sin.readLine());
                }
            }
            sin.readLine();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public void initClient(String host) {
        int portNumber = 3333+me;

        try {
            s = new Socket(host, portNumber);
            sout = new PrintWriter(s.getOutputStream(), true);
            sin = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String info = sin.readLine();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }


    // compile on your machine: javac *.java
    // call: java SmartGuy [ipaddress] [player_number]
    //   ipaddress is the ipaddress on the computer the server was launched on.  Enter "localhost" if it is on the same computer
    //   player_number is 1 (for the black player) and 2 (for the white player)
    public static void main(String[] args) {
        new SmartGuy(Integer.parseInt(args[1]), args[0]);
//        SmartGuy bot = new SmartGuy(1, "localhost");
//        int[][] initialState = {
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {2, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {-1, 0, 0, 0, -5, 0, 0, 0}
//        };
//        ChessBoard board = new ChessBoard(initialState);
//        System.out.println(board);
//        bot.round = 1;
//        int myMove;
//        bot.state = initialState;
////        bot.getValidMoves(bot.round, initialState, false);
//        myMove = bot.move();
//        bot.getGlobalValidMoves(initialState);
//        System.out.println(bot.validMoves[myMove]);
    }
}
