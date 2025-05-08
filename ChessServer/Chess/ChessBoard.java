package Chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private static final int boardSize = 8;
    private ChessPiece[][] boardPieces = new ChessPiece[boardSize][boardSize];  // dataType[][] arrayName = new dataType[rows][columns];

    public ChessBoard(int state[][]) {
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if(state[i][j] == 0) {
                    boardPieces[i][j] = null;
                }
                else if(Math.abs(state[i][j]) == 1) {
                    if (state[i][j] < 0) {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
                    } else {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
                    }
                }
                else if(Math.abs(state[i][j]) == 2) {
                    if (state[i][j] < 0) {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
                    } else {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
                    }
                }
                else if (Math.abs(state[i][j]) == 3) {
                    if (state[i][j] < 0) {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
                    } else {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
                    }
                }
                else if (Math.abs(state[i][j]) == 4) {
                    if (state[i][j] < 0) {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                    } else {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                    }
                }
                else if (Math.abs(state[i][j]) == 5) {
                    if (state[i][j] < 0) {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.BLACK, ChessPiece.PieceType.KING);
                    } else {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.WHITE, ChessPiece.PieceType.KING);
                    }
                }
                else if (Math.abs(state[i][j]) == 6) {
                    if (state[i][j] < 0) {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                    } else {
                        boardPieces[i][j] = new ChessPiece(ChessPiece.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                    }
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();

        boardPieces[row-1][col-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        return boardPieces[row-1][col-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
//        if (!(o instanceof ChessBoard that)) return false;
        if (!(o instanceof ChessBoard)) return false;
        ChessBoard that = (ChessBoard) o;
        for(int i = 0; i < boardSize; i++) {
            for(int j = 0; j < boardSize; j++) {
                if(this.boardPieces[i][j] == null && that.boardPieces[i][j] != null) {
                    return false;
                }
                else if(this.boardPieces[i][j] != null && !this.boardPieces[i][j].equals(that.boardPieces[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardPieces);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = boardSize - 1; i >= 0; i--) {
            sb.append("|");
            for (int j = 0; j < boardSize; j++) {
                ChessPiece currentPiece = this.getPiece(new ChessPosition(i+1, j+1));
                if (currentPiece != null) {
                    sb.append(currentPiece).append("|");
                }
                else {
                    sb.append(" |");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
