import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.math.*;
import java.text.*;

public class Player {
    int me;
    int thePort = 3333;
    ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter sout;
    BufferedReader sin;

    int validMoves[] = new int[64];
    int numValidMoves;

    Player(int _me, int minutos) {
        me = _me;

        // get a connection
        getConnection(thePort+me, minutos);
    }

    private void getConnection(int port, int minutos) {
        System.out.println("Set up the connections:" + port);

        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            sout = new PrintWriter(clientSocket.getOutputStream(), true);
            sin = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Connection for player " + me + " set.");

            sout.println(me + " " + minutos);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    private void getValidMoves(int round, int state[][]) {
        int i, j;
        numValidMoves = 0;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                validMoves[numValidMoves] = (i*8) + j;
                if (numValidMoves < 63) {
                    numValidMoves++;
                }
            }
        }
//        if (round < 4) {
//            if (state[3][3] == 0) {
//                validMoves[numValidMoves] = 3*8 + 3;
//                numValidMoves ++;
//            }
//            if (state[3][4] == 0) {
//                validMoves[numValidMoves] = 3*8 + 4;
//                numValidMoves ++;
//            }
//            if (state[4][3] == 0) {
//                validMoves[numValidMoves] = 4*8 + 3;
//                numValidMoves ++;
//            }
//            if (state[4][4] == 0) {
//                validMoves[numValidMoves] = 4*8 + 4;
//                numValidMoves ++;
//            }
//            //System.out.println("Valid Moves:");
//            //for (i = 0; i < numValidMoves; i++) {
//            //    System.out.println(validMoves[i] / 8 + ", " + validMoves[i] % 8);
//            //}
//        }
//        else {
//            //System.out.println("Valid Moves:");
//            for (i = 0; i < 8; i++) {
//                for (j = 0; j < 8; j++) {
//                    if (state[i][j] == 0) {
//                        if (couldBe(state, i, j)) {
//                            validMoves[numValidMoves] = i*8 + j;
//                            numValidMoves ++;
//                            //System.out.println(i + ", " + j);
//                        }
//                    }
//                }
//            }
//        }
    }

    public int[] takeTurn(int round, int state[][], double t1, double t2, PrintWriter prnt) {
        // first, check to see if this player has any valid moves
        getValidMoves(round, state);

        int i, j;
        prnt.println("Valid moves for " + me + ":");
        for (i = 0; i < numValidMoves; i++) {
            prnt.println((validMoves[i] / 8) + ", " + (validMoves[i] % 8));
        }
        prnt.println();

        if (numValidMoves == 0) {
            int mueva[] = new int[2];
            mueva[0] = mueva[1] = -1;

            //System.out.println("Player " + me + " has no valid moves");

            return mueva;
        }

        //System.out.println("Turn for " + me + "; Round " + round);

        int mueva[] = new int[4];
        try {
            // tell the player the world state
            boolean valid = false;
            int targetRow = -1, targetCol = -1, fromRow = -1, fromCol = -1;
            while (!valid) {
                String status = me + "\n" + round + "\n" + t1 + "\n" + t2 + "\n";
                for (i = 0; i < 8; i++) {
                    for (j = 0; j < 8; j++) {
                        status += state[i][j] + "\n";
                    }
                }
                sout.println(status);

                // receive the players move
                targetRow = Integer.parseInt(sin.readLine().trim());
                targetCol = Integer.parseInt(sin.readLine().trim());
                fromRow = Integer.parseInt(sin.readLine().trim());
                fromCol = Integer.parseInt(sin.readLine().trim());

                // check to see whether the move is a valid move
                for (i = 0; i < numValidMoves; i++) {
                    if ((targetRow == (validMoves[i] / 8)) && (targetCol == (validMoves[i] % 8))) {
                        valid = true;
                        break;
                    }
                }
            }

            // return the move
            mueva[0] = targetRow;
            mueva[1] = targetCol;
            mueva[2] = fromRow;
            mueva[3] = fromCol;
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        return mueva;
    }

    public void update(int round, int state[][], double t1, double t2) {
        String status = (1-me) + "\n" + round + "\n" + t1 + "\n" + t2 + "\n";
        int i, j;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                status += state[i][j] + "\n";
            }
        }
        sout.println(status);
        System.out.println("Sent status update");
    }

    public void gameOver(int state[][]) {
        sout.println("-999");
    }

    public void finale(int winner, int state[][], double t1, double t2) {
        String status = winner + "\n" + t1 + "\n" + t2 + "\n";
        int i, j;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                status += state[i][j] + "\n";
            }
        }
        sout.println(status);
    }
}