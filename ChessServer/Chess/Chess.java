package Chess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


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

    BufferedImage whiteKing;
    BufferedImage blackKing;
    BufferedImage whitePawn;
    BufferedImage blackPawn;
    BufferedImage whiteQueen;
    BufferedImage blackQueen;
    BufferedImage whiteRook;
    BufferedImage blackRook;
    BufferedImage whiteBishop;
    BufferedImage blackBishop;
    BufferedImage whiteKnight;
    BufferedImage blackKnight;

    public MyCanvas(int w, int h) {
        //System.out.println("MyCanvas");
        width = w;
        height = h;

        sqrWdth = (w - 60) / 8;
        sqrHght = (h - 168) / 8;

        try {
            whiteKing = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\wt-king.png"));
            blackKing = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\bk-king.png"));
            whitePawn = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\wt-pawn.png"));
            blackPawn = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\bk-pawn.png"));
            whiteQueen = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\wt-queen.png"));
            blackQueen = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\bk-queen.png"));
            whiteRook = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\wt-rook.png"));
            blackRook = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\bk-rook.png"));
            whiteBishop = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\wt-bishop.png"));
            blackBishop = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\bk-bishop.png"));
            whiteKnight = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\wt-knight.png"));
            blackKnight = ImageIO.read(new File("C:\\Users\\kirkm\\BYU Winter 2025\\Intro to AI\\projects\\Chess\\resources\\bk-knight.png"));
        } catch (IOException e) {
            System.err.println("Failed to load pawn image: " + e.getMessage());
        }
    }

    public void gameOver() {
        gameOver = true;
        System.out.println("Game Over");
        repaint();
    }

    public void printState() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
//                System.out.print(pieceMap.get(state[i][j]));
                System.out.print(state[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
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

        int i, j;
        int x, y;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (state[i][j] == -6) {  // black
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(blackPawn, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == 6) { // white
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(whitePawn, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == -5) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(blackKing, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == 5) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(whiteKing, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == -4) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(blackQueen, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == 4) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(whiteQueen, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == -3) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(blackBishop, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == 3) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(whiteBishop, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == -2) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(blackKnight, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == 2) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(whiteKnight, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == -1) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(blackRook, x, y, sqrWdth, sqrHght, null);
                } else if (state[i][j] == 1) {
                    x = 30 + sqrWdth * j;
                    y = 34 + sqrHght * (7 - i);
                    g2.drawImage(whiteRook, x, y, sqrWdth, sqrHght, null);
                }
            }
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

        int countBlack = 0, countWhite = 0;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                if (state[i][j] == 1)
                    countWhite += 5;
                else if (state[i][j] == 2) {
                    countWhite += 3;
                }
                else if (state[i][j] == 3) {
                    countWhite += 3;
                }
                else if (state[i][j] == 4) {
                    countWhite += 9;
                }
                else if (state[i][j] == 6) {
                    countWhite += 1;
                }
                else if (state[i][j] == -1) {
                    countBlack += 5;
                }
                else if (state[i][j] == -2) {
                    countBlack += 3;
                }
                else if (state[i][j] == -3) {
                    countBlack += 3;
                }
                else if (state[i][j] == -4) {
                    countBlack += 9;
                }
                else if (state[i][j] == -6) {
                    countBlack += 1;
                }
            }
        }

        if (countBlack > countWhite) {
            countBlack = countBlack - countWhite;
            countWhite = 0;
        } else if (countBlack < countWhite) {
            countWhite = countWhite - countBlack;
            countBlack = 0;
        } else {
            countWhite = 0;
            countBlack = 0;
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
            fnombre = "ChessServer\\Chess\\GameLog.txt";
            wrte = new FileWriter(fnombre, false);
            prnt = new PrintWriter(wrte);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void initializeState() {
        state[0][0] = 1; // white rook
        state[0][1] = 2; // white knight
        state[0][2] = 3; // white bishop
        state[0][3] = 4; // white queen
        state[0][4] = 5; // white king
        state[0][5] = 3; // white bishop
        state[0][6] = 2; // white knight
        state[0][7] = 1; // white rook
        for (int i = 0; i < 8; i++) {
            state[1][i] = 6; // white pawns
        }

        state[7][0] = -1; // black rook
        state[7][1] = -2; // black knight
        state[7][2] = -3; // black bishop
        state[7][3] = -4; // black queen
        state[7][4] = -5; // black king
        state[7][5] = -3; // black bishop
        state[7][6] = -2; // black knight
        state[7][7] = -1; // black rook
        for (int i = 0; i < 8; i++) {
            state[6][i] = -6; // black pawns
        }
    }

    public static void playGame(int minutos) {
        int i, j;
//        for (i = 0; i < 8; i++) {
//            for (j = 0; j < 8; j++) {
//                state[i][j] = 0;
//            }
//        }

        double t1 = minutos * 60.0, t2 = minutos * 60.0;


        initializeState();
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
                prnt.println("Player " + (turn+1) + ": " + mueva[0] + ", " + mueva[1]);

                // mueva[0] = toRow
                // mueva[1] = toCol
                // mueva[2] = fromRow
                // mueva[3] = fromCol
                // mueva[4] = promotion piece
                boolean castling = (state[mueva[2]][mueva[3]] == 5 || state[mueva[2]][mueva[3]] == -5) && (Math.abs(mueva[3] - mueva[1]) > 1);
                System.out.println(state[mueva[2]][mueva[3]]);
                System.out.println(castling);
                if (castling) {
                    if (mueva[1] == 6) {
                        if (mueva[0] == 0) {
                            state[0][5] = 1;
                            state[0][7] = 0;
                        }
                        else if (mueva[0] == 7) {
                            state[7][5] = -1;
                            state[7][7] = 0;
                        }
                    } else if (mueva[1] == 2) {
                        if (mueva[0] == 0) {
                            state[0][3] = 1;
                            state[0][0] = 0;
                        }
                        else if (mueva[0] == 7) {
                            state[7][3] = -1;
                            state[7][0] = 0;
                        }
                    }
                }
                if (mueva[4] != 0) {
                    state[mueva[0]][mueva[1]] = mueva[4];
                    state[mueva[2]][mueva[3]] = 0;
                }
                else {
                    state[mueva[0]][mueva[1]] = state[mueva[2]][mueva[3]];
                    state[mueva[2]][mueva[3]] = 0;
                }

                prnt.println("\nAfter move by Player " + (turn+1));

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
                winner = 1 - turn;
                nocount ++;
            }

            if (nocount == 1) {
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
        }

        System.out.println("Game Over!");

        if (t1 <= 0.0) {
            winner = 2;
        }
        else if (t2 <= 0.0) {
            winner = 1;
        }

        int countBlack = 1;
        int countWhite = 1;

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