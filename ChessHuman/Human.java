import Chess.ChessBoard;
import Chess.ChessMove;
import Chess.ChessPiece;
import Chess.ChessPosition;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MyCanvas extends JComponent {
    int width, height;
    int sqrWdth, sqrHght;
    Color gris = new Color(230,230,230);
    Color myWhite = new Color(220, 220, 220);

    int state[][] = new int[8][8];
    int turn = 0;
    double t1, t2;
    boolean gameOver = false;
    int theMe;
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

    public MyCanvas(int w, int h, int _me) {
        //System.out.println("MyCanvas");
        width = w;
        height = h;

        sqrWdth = (w - 60) / 8;
        sqrHght = (h - 168) / 8;

        theMe = _me;

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

    public void initializeState() {
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

    public void printState() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(pieceMap.get(state[i][j]));
                System.out.print(" ");
                if ((j + 1) % 8 == 0) {
                    System.out.println();
                }
            }
        }
    }

    public void updateState(int nState[][], int nTurn, double nt1, double nt2, int nwinner) {
        int i, j;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                state[i][j] = nState[i][j];
            }
        }

        turn = nTurn;
        winner = nwinner;

        //System.out.println("**********Turn: " + turn);

        t1 = nt1;
        t2 = nt2;

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
        int i, j;
        int x, y;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                int drawRow = (theMe == 1) ? (7 - i) : i;
                int drawCol = (theMe == 1) ? j : (7 - j);

                if (state[i][j] == -6) {  // black
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(blackPawn, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == 6) { // white
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(whitePawn, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == -5) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(blackKing, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == 5) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(whiteKing, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == -4) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(blackQueen, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == 4) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(whiteQueen, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == -3) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(blackBishop, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == 3) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(whiteBishop, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == -2) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(blackKnight, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == 2) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(whiteKnight, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == -1) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
                    g2.drawImage(blackRook, x, y, sqrWdth, sqrHght, null);
                }
                else if (state[i][j] == 1) {
                    x = 30 + sqrWdth*drawCol;
                    y = 34 + sqrHght * drawRow;
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

        int xanchor = 180;

        g.setColor(Color.black);
        String whiteStr = "" + countWhite;

        if (theMe == 1)
            g.drawString("White (you):", xanchor-45, height - 58);
        else
            g.drawString("White:", xanchor-45, height - 58);
        if (countBlack < 10)
            g.drawString(whiteStr, xanchor + 110, height - 58);
        else
            g.drawString(whiteStr, xanchor + 104, height - 58);

        int min = (int)(t1 / 60);
        int sec = (int)(t1+0.5) % 60;
        System.out.println("t1 = " + t1 + "; sec = " + sec);
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

        if (theMe == 2)
            g.drawString("Black (you):", xanchor-45, height - 32);
        else
            g.drawString("Black:", xanchor-45, height - 32);
        String blackStr = "" + countBlack;
        if (countBlack < 10)
            g.drawString(blackStr, xanchor + 110, height - 32);
        else
            g.drawString(blackStr, xanchor + 104, height - 32);

        if (!gameOver) {
            if ((turn == 1) || ((turn == -1) && (theMe == 2)))
                g.fillOval(xanchor-55, height - 67, 6, 6);
            else
                g.fillOval(xanchor-55, height - 41, 6, 6);
        }

        min = (int)(t2 / 60);
        sec = (int)(t2+0.5) % 60;
        System.out.println("t2 = " + t2 + "; sec = " + sec);
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
            if (winner == 2) {
                g.setColor(Color.red);
                g.drawRect(115, height - 76, 316, 24);
                g.drawRect(116, height - 75, 314, 22);
            }
            else if (winner == 1) {
                g.setColor(Color.red);
                g.drawRect(115, height - 50, 316, 24);
                g.drawRect(116, height - 49, 314, 22);
            }
        }
    }
}


class Human extends JFrame {
    Color bkgroundColor = new Color(200,160,120);
    MyCanvas canvas;
    boolean gameOver = false;

    public Socket s;
    public BufferedReader sin;
    public PrintWriter sout;

    int winner = -1;

    double t1, t2;
    int theMe;
    int boardState;
    int state[][] = new int[8][8];
    int turn = -1;
    int round;
    int nMouseX = -1;
    int nMouseY = -1;
    int firstClickX = -1;
    int firstClickY = -1;
    boolean waitingForSecondClick = false;

    ChessMove validMoves[] = new ChessMove[100];
    int numValidMoves;

    public Human(int _me, String host) {
        final int width = 540;//620;
        final int height = 648;//728;

        theMe = _me;

        setSize(width,height);//400 width and 500 height
        getContentPane().setBackground(bkgroundColor);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, width, height);
        canvas = new MyCanvas(width, height, theMe);
        getContentPane().add(canvas);

        setVisible(true);

        setTitle("Mere Human");

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                int msX = me.getPoint().x - 31;
                int msY = me.getPoint().y - (34+24);

                int ymouse = ((height - 168) - msY) / ((height - 168) / 8);
                int xmouse = msX / ((width - 60) / 8);

                if ((ymouse >= 0) && (ymouse < 8) && (xmouse >= 0) && (xmouse < 8)) {
                    if (!waitingForSecondClick) {
                        // First click: select piece
                        firstClickX = xmouse;
                        firstClickY = ymouse;
                        waitingForSecondClick = true;
                    } else {
                        // Second click: destination
                        nMouseX = xmouse;
                        nMouseY = ymouse;
                        waitingForSecondClick = false;
                    }
                }
            }
        });

        Random generator = new Random();
        initClient(host);
        canvas.initializeState();

        int myMove;

        while (true) {
            //System.out.println("Read");
            readMessage();

            if (gameOver)
                break;

            canvas.printState();
            canvas.updateState(state, turn, t1, t2, winner);

            if (turn == theMe) {

                getValidMoves(state);

                nMouseX = -1;
                waitingForSecondClick = false;
                while (nMouseX == -1 || waitingForSecondClick) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                }
                if (theMe == 2) {
                    nMouseY = 7 - nMouseY;
                    nMouseX = 7 - nMouseX;
                    firstClickY = 7 - firstClickY;
                    firstClickX = 7 - firstClickX;
                }
                String sel = nMouseY + "\n" + nMouseX + "\n" + firstClickY + "\n" + firstClickX;

                sout.println(sel);
            }
        }

        readFinale();
        canvas.updateState(state, -1, t1, t2, winner);
    }

    private void getValidMoves(int state[][]) {
        int i, j;
        numValidMoves = 0;

        ChessBoard board = new ChessBoard(state);
        ChessPiece currentPiece;
        ChessPiece.TeamColor myColor = theMe == 1 ? ChessPiece.TeamColor.WHITE : ChessPiece.TeamColor.BLACK;
        Collection<ChessMove> pieceMoves;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                currentPiece = board.getPiece(new ChessPosition(i+1, j+1));
                if (currentPiece == null || currentPiece.getTeamColor() != myColor) {
                    continue;
                }
                // check to see if the piece can move
                pieceMoves = currentPiece.validMoves(new ChessPosition(i+1, j+1), board);
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
                gameOver = true;
                canvas.gameOver();
                return;
            }

            System.out.println("Turn: " + turn);
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

    public void readFinale() {
        int i, j;
        String status;
        try {
            winner = Integer.parseInt(sin.readLine());
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
        int portNumber = 3333+theMe;

        System.out.println(portNumber);

        try {
            s = new Socket(host, portNumber);
            sout = new PrintWriter(s.getOutputStream(), true);
            sin = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String info = sin.readLine();
            System.out.println(info);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        new Human(Integer.parseInt(args[1]), args[0]);
    }

}
