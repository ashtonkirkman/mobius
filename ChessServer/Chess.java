
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.io.*;


class MyCanvas extends JComponent {
    int width, height;
    int sqrWdth, sqrHght;
    Color gris = new Color(230,230,230);
    Color myWhite = new Color(220, 220, 220);

    int state[][] = new int[8][8];
    int turn = 0;
    double t1, t2;
    boolean gameOver = false;
    int winner;

    public MyCanvas(int w, int h) {
        //System.out.println("MyCanvas");
        width = w;
        height = h;

        sqrWdth = (w - 60) / 8;
        sqrHght = (h - 168) / 8;
    }

    public void gameOver() {
        gameOver = true;
        System.out.println("Game Over");
        repaint();
    }


    public void updateState(int nState[][], int nTurn, double nt1, double nt2, int nwinner) {
        int i, j;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                state[i][j] = nState[i][j];
            }
        }

        turn = nTurn;
        t1 = nt1;
        t2 = nt2;
        winner = nwinner;

        //System.out.println("should repaint");

        repaint();
    }

    void drawAlphabetBar(Graphics g, int h) {
        g.setColor(myWhite);
        g.fillRect (6, h, width-12, 20);

        g.setFont(new Font("Courier", 1, 18));
        g.setColor(Color.black);

        int baseline = h + 16;

        g.drawString("a", 25 + (sqrWdth / 2), baseline);
        g.drawString("b", 25 + (sqrWdth / 2) + sqrWdth*1, baseline);
        g.drawString("c", 25 + (sqrWdth / 2) + sqrWdth*2, baseline);
        g.drawString("d", 25 + (sqrWdth / 2) + sqrWdth*3, baseline);
        g.drawString("e", 25 + (sqrWdth / 2) + sqrWdth*4, baseline);
        g.drawString("f", 25 + (sqrWdth / 2) + sqrWdth*5, baseline);
        g.drawString("g", 25 + (sqrWdth / 2) + sqrWdth*6, baseline);
        g.drawString("h", 25 + (sqrWdth / 2) + sqrWdth*7, baseline);
    }

    void drawNumberBar(Graphics g, int w) {

        g.setColor(myWhite);
        g.fillRect (w, 34, 20, sqrHght*8);

        g.setFont(new Font("Courier", 1, 18));
        g.setColor(Color.black);

        int baseline = w + 5;
        g.drawString("8", baseline, 34 + 6 + (sqrHght / 2));
        g.drawString("7", baseline, 34 + 6 + (sqrHght / 2) + sqrHght*1);
        g.drawString("6", baseline, 34 + 6 + (sqrHght / 2) + sqrHght*2);
        g.drawString("5", baseline, 34 + 6 + (sqrHght / 2) + sqrHght*3);
        g.drawString("4", baseline, 34 + 6 + (sqrHght / 2) + sqrHght*4);
        g.drawString("3", baseline, 34 + 6 + (sqrHght / 2) + sqrHght*5);
        g.drawString("2", baseline, 34 + 6 + (sqrHght / 2) + sqrHght*6);
        g.drawString("1", baseline, 34 + 6 + (sqrHght / 2) + sqrHght*7);
    }

    void drawPieces(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Synchronously load the image once
        try {
            BufferedImage whiteKing = ImageIO.read(new File("ChessServer\\wt-king.png"));
            BufferedImage blackKing = ImageIO.read(new File("ChessServer\\bk-king.png"));
            BufferedImage whitePawn = ImageIO.read(new File("ChessServer\\wt-pawn.png"));
            BufferedImage blackPawn = ImageIO.read(new File("ChessServer\\bk-pawn.png"));
            BufferedImage whiteQueen = ImageIO.read(new File("ChessServer\\wt-queen.png"));
            BufferedImage blackQueen = ImageIO.read(new File("ChessServer\\bk-queen.png"));
            BufferedImage whiteRook = ImageIO.read(new File("ChessServer\\wt-rook.png"));
            BufferedImage blackRook = ImageIO.read(new File("ChessServer\\bk-rook.png"));
            BufferedImage whiteBishop = ImageIO.read(new File("ChessServer\\wt-bishop.png"));
            BufferedImage blackBishop = ImageIO.read(new File("ChessServer\\bk-bishop.png"));
            BufferedImage whiteKnight = ImageIO.read(new File("ChessServer\\wt-knight.png"));
            BufferedImage blackKnight = ImageIO.read(new File("ChessServer\\bk-knight.png"));

            // white pawns
            for (int col = 0; col < 8; col++) {
                int x = 30 + col * sqrWdth; // offset to align with board
                int y = 34 + (7 - 1) * sqrHght; // row 1 for white pawns (flipped board indexing)
                g2.drawImage(whitePawn, x, y, sqrWdth, sqrHght, null);
            }
            // black pawns
            for (int col = 0; col < 8; col++) {
                int x = 30 + col * sqrWdth; // offset to align with board
                int y = 34 + sqrHght; // row 7 for black pawns (flipped board indexing)
                g2.drawImage(blackPawn, x, y, sqrWdth, sqrHght, null);
            }

            // white pieces
            g2.drawImage(whiteRook, 30, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(whiteKnight, 30 + 1 * sqrWdth, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(whiteBishop, 30 + 2 * sqrWdth, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(whiteQueen, 30 + 3 * sqrWdth, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(whiteKing, 30 + 4 * sqrWdth, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(whiteBishop, 30 + 5 * sqrWdth, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(whiteKnight, 30 + 6 * sqrWdth, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(whiteRook, 30 + 7 * sqrWdth, 34 + (7 - 0) * sqrHght, sqrWdth, sqrHght, null);

            // black pieces
            g2.drawImage(blackRook, 30, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(blackKnight, 30 + 1 * sqrWdth, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(blackBishop, 30 + 2 * sqrWdth, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(blackQueen, 30 + 3 * sqrWdth, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(blackKing, 30 + 4 * sqrWdth, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(blackBishop, 30 + 5 * sqrWdth, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(blackKnight, 30 + 6 * sqrWdth, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);
            g2.drawImage(blackRook, 30 + 7 * sqrWdth, 34 + (7 - 7) * sqrHght, sqrWdth, sqrHght, null);

        } catch (IOException e) {
            System.err.println("Failed to load pawn image: " + e.getMessage());
        }
    }


    public void paint(Graphics g) {
        //System.out.println("here");

        Color myDarkGray = new Color(100, 100, 100);

        drawAlphabetBar(g, 10);
        drawAlphabetBar(g, height - 130);

        drawNumberBar(g, 6);
        drawNumberBar(g, width-26);

        Color lightSquareColor = new Color(238, 238, 210);
        g.setColor(lightSquareColor);
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i+j) % 2 != 0) {
                    g.fillRect(30 + sqrWdth*j, 34 + sqrHght * (7-i), sqrWdth, sqrHght);
                }
            }
        }

        Color darkSquareColor = new Color(118, 150, 86);
        g.setColor(darkSquareColor);
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i+j) % 2 == 0) {
                    g.fillRect(30 + sqrWdth*j, 34 + sqrHght * (7-i), sqrWdth, sqrHght);
                }
            }
        }

        drawPieces(g);

        int i, j;
        int x, y;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (state[i][j] == 1) {  // black
                    g.setColor(Color.black);
                    x = 30 + 6 + sqrWdth*j;
                    y = 34 + 6 + sqrHght * (7-i);
                    g.fillOval(x, y, sqrWdth-12, sqrHght-12);
                }
                else if (state[i][j] == 2) { // white
                    g.setColor(gris);
                    x = 30 + 6 + sqrWdth*j;
                    y = 34 + 6 + sqrHght * (7-i);
                    g.fillOval(x, y, sqrWdth-12, sqrHght-12);

                    //g.setColor(bkgroundColor);
                    //g.drawOval(x, y, sqrWdth-8, sqrHght-8);

                }
            }
        }

        int countBlack = 0, countWhite = 0;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (state[i][j] == 1)
                    countBlack ++;
                else if (state[i][j] == 2)
                    countWhite ++;
            }
        }

        int xanchor = 150;

        g.setColor(Color.black);
        String blackStr = "" + countBlack;
        g.drawString("Black:", xanchor, height - 58);
        if (countBlack < 10)
            g.drawString(blackStr, xanchor + 110, height - 58);
        else
            g.drawString(blackStr, xanchor + 104, height - 58);

        int min = (int)(t1 / 60);
        int sec = (int)(t1+0.5) % 60;
        int mili = (int)(t1 - (min*60 + sec)) * 100;
        String minStr, secStr, miliStr;
        if (min < 10)
            minStr = "0" + min;
        else
            minStr = "" + min;
        if (sec < 10)
            secStr = "0" + sec;
        else
            secStr = "" + sec;
        if (mili < 10)
            miliStr = "0" + mili;
        else
            miliStr = "" + mili;

        String t1Str = minStr + ":" + secStr;
        g.drawString(t1Str, xanchor + 180, height - 58);

        g.drawString("White:", xanchor, height - 32);
        String whiteStr = "" + countWhite;
        if (countWhite < 10)
            g.drawString(whiteStr, xanchor + 110, height - 32);
        else
            g.drawString(whiteStr, xanchor + 104, height - 32);

        if (!gameOver) {
            if (turn == 0)
                g.fillOval(xanchor-10, height - 67, 6, 6);
            else
                g.fillOval(xanchor-10, height - 41, 6, 6);
        }

        min = (int)(t2 / 60);
        sec = (int)(t2+0.5) % 60;
        mili = (int)(t2 - (min*60 + sec)) * 100;
        if (min < 10)
            minStr = "0" + min;
        else
            minStr = "" + min;
        if (sec < 10)
            secStr = "0" + sec;
        else
            secStr = "" + sec;
        if (mili < 10)
            miliStr = "0" + mili;
        else
            miliStr = "" + mili;

        String t2Str = minStr + ":" + secStr;
        g.drawString(t2Str, xanchor + 180, height - 32);


        g.setFont(new Font("Courier", 1, 16));
        g.setColor(myDarkGray);
        g.drawString("Score", xanchor+91, height - 80);

        g.drawString("Time", xanchor+188, height - 80);

        if (gameOver) {
            g.setFont(new Font("Courier", 1, 40));
            g.setColor(Color.red);

            g.drawString("Game Over", width / 2 - 109, height / 2 - 16);
        }

        if (winner > 0) {
            if (winner == 1) {
                g.setColor(Color.red);
                g.drawRect(125, height - 76, 280, 24);
                g.drawRect(126, height - 75, 278, 22);
            }
            else if (winner == 2) {
                g.setColor(Color.red);
                g.drawRect(125, height - 50, 280, 24);
                g.drawRect(126, height - 49, 278, 22);
            }
        }
    }
}

public class Chess extends JFrame {
    Color bkgroundColor = new Color(200,160,120);
    static MyCanvas canvas;
    static int state[][] = new int[8][8];

    static String fnombre;
    static FileWriter wrte;
    static PrintWriter prnt;
    static int winner = -1;


    public Chess() {
        int width = 540;//620;
        int height = 648;//728;

        setSize(width,height);//400 width and 500 height
        getContentPane().setBackground(bkgroundColor);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, width, height);
        canvas = new MyCanvas(width, height);
        getContentPane().add(canvas);

        setVisible(true);
        setTitle("Chess -- Server");

        try {
            fnombre = "GameLog.txt";
            wrte = new FileWriter(fnombre, false);
            prnt = new PrintWriter(wrte);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void checkDirection(int row, int col, int incx, int incy, int turn) {
        int sequence[] = new int[7];
        int seqLen;
        int i, r, c;

        seqLen = 0;
        for (i = 1; i < 8; i++) {
            r = row+incy*i;
            c = col+incx*i;

            if ((r < 0) || (r > 7) || (c < 0) || (c > 7))
                break;

            sequence[seqLen] = state[r][c];
            seqLen++;
        }

        int count = 0;
        for (i = 0; i < seqLen; i++) {
            if (turn == 0) {
                if (sequence[i] == 2)
                    count ++;
                else {
                    if ((sequence[i] == 1) && (count > 0))
                        count = 20;
                    break;
                }
            }
            else {
                if (sequence[i] == 1)
                    count ++;
                else {
                    if ((sequence[i] == 2) && (count > 0))
                        count = 20;
                    break;
                }
            }
        }

        if (count > 10) {
            if (turn == 0) {
                i = 1;
                r = row+incy*i;
                c = col+incx*i;
                while (state[r][c] == 2) {
                    state[r][c] = 1;
                    i++;
                    r = row+incy*i;
                    c = col+incx*i;
                }
            }
            else {
                i = 1;
                r = row+incy*i;
                c = col+incx*i;
                while (state[r][c] == 1) {
                    state[r][c] = 2;
                    i++;
                    r = row+incy*i;
                    c = col+incx*i;
                }
            }
        }
    }

    public static void changeColors(int row, int col, int turn) {
        int incx, incy;

        for (incx = -1; incx < 2; incx++) {
            for (incy = -1; incy < 2; incy++) {
                if ((incx == 0) && (incy == 0))
                    continue;

                checkDirection(row, col, incx, incy, turn);
            }
        }
    }

    public static void printState() {
        int i, j;

        for (i = 7; i >= 0; i--) {
            for (j = 0; j < 8; j++) {
                prnt.print(state[i][j]);
            }
            prnt.println();
        }
        prnt.println();
    }

    public static void playGame(int minutos) {
        int i, j;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                state[i][j] = 0;
            }
        }

        double t1 = minutos * 60.0, t2 = minutos * 60.0;

        canvas.updateState(state, 0, t1, t2, winner);

        System.out.println("Set up the players");

        Player p1 = new Player(1, minutos);
        Player p2 = new Player(2, minutos);

        System.out.println("Going to play a " + minutos + "-minute game");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        int round = 0;
        int turn = 0;
        int mueva[] = new int[2];
        int nocount = 0;
        long sTime, eTime;
        while (true) {
            //System.out.println("Round: " + round);
            prnt.println("\nRound: " + round);
            printState();

            //System.out.println("Game isn't over yet.");

            sTime = System.nanoTime();

            if (turn == 0) {
                mueva = p1.takeTurn(round, state, t1, t2, prnt);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                eTime = System.nanoTime() - sTime;
                t1 -= (eTime / 1000000000.0);

                if (t1 <= 0.0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                    p1.gameOver(state);
                    p2.gameOver(state);
                    canvas.gameOver();

                    break;
                }
            }
            else {
                mueva = p2.takeTurn(round, state, t1, t2, prnt);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                eTime = System.nanoTime() - sTime;
                t2 -= (eTime / 1000000000.0);

                if (t2 <= 0.0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                    p1.gameOver(state);
                    p2.gameOver(state);
                    canvas.gameOver();

                    break;
                }
            }

            System.out.println("\nBlack: " + t1 + "\nWhite: " + t2);

            if (mueva[0] != -1) {
                //System.out.println("Move: " + mueva[0] + ", " + mueva[1]);
                prnt.println("Player " + (turn+1) + ": " + mueva[0] + ", " + mueva[1]);

                state[mueva[0]][mueva[1]] = turn+1;

                changeColors(mueva[0], mueva[1], turn);

                prnt.println("\nAfter move by Player " + (turn+1));
                printState();

                canvas.updateState(state, 1-turn, t1, t2, winner);

                round = round + 1;

                p1.update(round, state, t1, t2);
                p2.update(round, state, t1, t2);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                nocount = 0;
            }
            else {
                prnt.println("Player " + (turn+1) + " can't move");
                nocount ++;
            }

            if (nocount == 2) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }

                p1.gameOver(state);
                p2.gameOver(state);
                canvas.gameOver();

                break;
            }

            turn = 1 - turn;

            System.out.println("Turn: " + turn);

            int countBlack = 0, countWhite = 0;
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 1)
                        countBlack ++;
                    else if (state[i][j] == 2)
                        countWhite ++;
                }
            }

            System.out.println("\nBlack: " + countBlack + "\nWhite: " + countWhite);
        }

        System.out.println("Game Over!");

        int countBlack = 0, countWhite = 0;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (state[i][j] == 1)
                    countBlack ++;
                else if (state[i][j] == 2)
                    countWhite ++;
            }
        }

        // declare the winner and update all information
        if (t1 <= 0.0) {
            winner = 2;
            t1 = 0.0;

            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 1)
                        state[i][j] = 0;
                }
            }
            countBlack = 0;
        }
        else if (t2 <= 0.0) {
            winner = 1;
            t2 = 0.0;
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 2)
                        state[i][j] = 0;
                }
            }
            countWhite = 0;
        }
        else {
            if (countBlack > countWhite)
                winner = 1;
            else if (countWhite > countBlack)
                winner = 2;

            // give empty squares to the winner
            for (i = 0; i < 8; i++) {
                for (j = 0; j < 8; j++) {
                    if (state[i][j] == 0) {
                        state[i][j] = winner;
                        if (winner == 1)
                            countBlack ++;
                        else
                            countWhite ++;
                    }
                }
            }

        }

        prnt.println("\nBlack: " + countBlack + "\nWhite: " + countWhite + "\n");
        canvas.updateState(state, 1-turn, t1, t2, winner);

        p1.finale(winner, state, t1, t2);
        p2.finale(winner, state, t1, t2);

        try {
            prnt.close();
            wrte.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Chess();

        playGame(Integer.parseInt(args[0]));

    }
}