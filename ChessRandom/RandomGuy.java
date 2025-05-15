import Chess.ChessBoard;
import Chess.ChessMove;
import Chess.ChessPiece;
import Chess.ChessPosition;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

class RandomGuy {

    public Socket s;
    public BufferedReader sin;
    public PrintWriter sout;
    Random generator = new Random();

    double t1, t2;
    int me;
    int boardState;
    int[][] state = new int[8][8]; // state[0][0] is the bottom left corner of the board (on the GUI)
    int turn = -1;
    int round;

    ChessMove[] validMoves = new ChessMove[100];
    int numValidMoves;

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
    public RandomGuy(int _me, String host) {
        me = _me;
        initClient(host);

        int myMove;

        while (true) {
            readMessage();

            if (turn == me) {
                getValidMoves(state);
                myMove = move();
                //myMove = generator.nextInt(numValidMoves);        // select a move randomly
                getValidMoves(state);
                String sel = validMoves[myMove].getEndPosition().getRow() - 1 + "\n" +
                        (validMoves[myMove].getEndPosition().getColumn() - 1) + "\n" +
                        (validMoves[myMove].getStartPosition().getRow() - 1) + "\n" +
                        (validMoves[myMove].getStartPosition().getColumn() - 1);

                sout.println(sel);
            }
        }
        //while (turn == me) {
        //    System.out.println("My turn");

        //readMessage();
        //}
    }

    // You should modify this function
    // validMoves is a list of valid locations that you could place your "stone" on this turn
    // Note that "state" is a global variable 2D list that shows the state of the game
    private int move() {
        // just move randomly for now
        int myMove;

        myMove = generator.nextInt(numValidMoves);
        return myMove;
    }

    private void getValidMoves(int[][] state) {
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
                pieceMoves = currentPiece.validMoves(new ChessPosition(i+1, j+1), board, false);
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
    // call: java RandomGuy [ipaddress] [player_number]
    //   ipaddress is the ipaddress on the computer the server was launched on.  Enter "localhost" if it is on the same computer
    //   player_number is 1 (for the black player) and 2 (for the white player)
    public static void main(String args[]) {
        new RandomGuy(Integer.parseInt(args[1]), args[0]);
    }
}
