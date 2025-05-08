package Chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private TeamColor color;
    private PieceType type;

    // add main method
    public static void main(String[] args) {
        int state[][] = {
                {0, 0, 3, 4, 5, 3, 2, 1},
                {-6, 0, 6, 6, 6, 6, 6, 6},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {-6,-6,-6,-6,-6,-6,-6,-6},
                {-1,-2,-3,-4,-5,-3,-2,-1}
        };
        ChessBoard myBoard = new ChessBoard(state);
        int me = 2;
        ChessMove validMoves[] = new ChessMove[100];
        int numValidMoves = 0;

        int i, j;

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

        for (ChessMove move : validMoves) {
            if (move != null) {
                System.out.println(move);
            }
        }
    }

    public ChessPiece(ChessPiece.TeamColor color, ChessPiece.PieceType type) {
        this.color = color;
        this.type = type;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public TeamColor getTeamColor() {
        if(this.type == null) {
            return null;
        }
        else{
            return this.color;
        }
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        if(this.type == null) {
            return null;
        }
        else{
            return this.type;
        }
    }

    private boolean isValidMove(ChessPiece destinationPiece, ChessPosition destination) {
        int rank = destination.getRow();
        int file = destination.getColumn();

        if(rank >= 1 && rank <= 8 && file >= 1 && file <= 8) {
            return destinationPiece == null || destinationPiece.getTeamColor() != this.color;
        }

        return false;
    }

    private boolean isInBounds(int rank, int file) {
        return rank >= 1 && rank <= 8 && file >= 1 && file <= 8;
    }

    private void addMove(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessPosition destination) {
        int rank = destination.getRow();

        if(this.type == ChessPiece.PieceType.PAWN) {
            if(rank == 1 || rank == 8) {
                validMoves.add(new ChessMove(myPosition, destination, PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, destination, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, destination, PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, destination, PieceType.ROOK));
            }
            else validMoves.add(new ChessMove(myPosition, destination, null));
        }
        else validMoves.add(new ChessMove(myPosition, destination, null));
    }

    private boolean isWhite() {
        return this.color == ChessPiece.TeamColor.WHITE;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        int currentRank = myPosition.getRow();
        int currentFile = myPosition.getColumn();

        ChessPiece destinationPiece;
        ChessPosition destination;
        ChessPiece currentPiece = board.getPiece(myPosition);
        PieceType currentType = currentPiece.getPieceType();

        switch(currentType) {
            case PAWN:
                // Move pawn forward
                destination = new ChessPosition(isWhite() ? currentRank + 1 : currentRank - 1, currentFile);
                destinationPiece = board.getPiece(destination);
                if(isValidMove(destinationPiece, destination) && destinationPiece == null) {
                    addMove(validMoves, myPosition, destination);
                    if(currentRank == (isWhite() ? 2 : 7)) {
                        destination = new ChessPosition(isWhite() ? currentRank + 2 : currentRank - 2, currentFile);
                        destinationPiece = board.getPiece(destination);
                        if(isValidMove(destinationPiece, destination) && destinationPiece == null) {
                            addMove(validMoves, myPosition, destination);
                        }
                    }
                }

                // Capture with pawn
                for(int i = -1; i <= 1; i = i + 2) {
                    if(isInBounds(isWhite() ? currentRank + 1 : currentRank - 1, currentFile + i)) {
                        destination = new ChessPosition(isWhite() ? currentRank + 1 : currentRank - 1, currentFile + i);
                        destinationPiece = board.getPiece(destination);
                        if(isValidMove(destinationPiece, destination) && destinationPiece != null) {
                            addMove(validMoves, myPosition, destination);
                        }
                    }
                }
                break;

            case KING:
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        if(isInBounds(currentRank + i, currentFile + j) && !(i == 0 && j == 0)) {
                            destination = new ChessPosition(currentRank + i, currentFile + j);
                            destinationPiece = board.getPiece(destination);
                            if(isValidMove(destinationPiece, destination)) {
                                addMove(validMoves, myPosition, destination);
                            }
                        }
                    }
                }
                break;

            case QUEEN:
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        for(int k = 1; k <= 7; k++) {
                            int newRank = currentRank + k*i;
                            int newFile = currentFile + k*j;

                            if((i == 0 && j == 0) || !isInBounds(newRank, newFile)) break;
                            destination = new ChessPosition(newRank, newFile);
                            destinationPiece = board.getPiece(destination);
                            if(isValidMove(destinationPiece, destination)) {
                                addMove(validMoves, myPosition, destination);
                                if(destinationPiece != null) break;
                            }
                            else break;
                        }
                    }
                }
                break;

            case BISHOP:
                for(int i = -1; i <= 1; i = i + 2) {
                    for(int j = -1; j <= 1; j = j + 2) {
                        for(int k = 1; k <= 7; k++) {
                            int newRank = currentRank + k*i;
                            int newFile = currentFile + k*j;

                            if(!isInBounds(newRank, newFile)) break;
                            destination = new ChessPosition(newRank, newFile);
                            destinationPiece = board.getPiece(destination);
                            if(isValidMove(destinationPiece, destination)) {
                                addMove(validMoves, myPosition, destination);
                                if(destinationPiece != null) break;
                            }
                            else break;
                        }
                    }
                }
                break;

            case ROOK:
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        for(int k = 1; k <= 7; k++) {
                            int newRank = currentRank + k*i;
                            int newFile = currentFile + k*j;

                            if((Math.abs(i) == 1 && Math.abs(j) == 1) || (i == 0 && j == 0) || !isInBounds(newRank, newFile)) break;
                            destination = new ChessPosition(newRank, newFile);
                            destinationPiece = board.getPiece(destination);
                            if(isValidMove(destinationPiece, destination)) {
                                addMove(validMoves, myPosition, destination);
                                if(destinationPiece != null) break;
                            }
                            else break;
                        }
                    }
                }
                break;

            case KNIGHT:
                for(int i = -2; i <= 2; i++) {
                    for(int j = -2; j <= 2; j++) {
                        int newRank = currentRank + i;
                        int newFile = currentFile + j;

                        if(((Math.abs(i) == 1 && Math.abs(j) == 2) || (Math.abs(i) == 2 && Math.abs(j) == 1)) && isInBounds(newRank, newFile)) {
                            destination = new ChessPosition(newRank, newFile);
                            destinationPiece = board.getPiece(destination);
                            if (isValidMove(destinationPiece, destination)) {
                                addMove(validMoves, myPosition, destination);
                            }
                        }
                    }
                }
                break;
        }

        return validMoves;
    }

    private ChessPiece testMove(ChessMove move, ChessBoard board) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition destination = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(startPosition);
        ChessPiece destinationPiece = board.getPiece(destination);
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();

        if(promotionPiece != null) {
            currentPiece = new ChessPiece(currentPiece.getTeamColor(), promotionPiece);
        }

        board.addPiece(destination, currentPiece);
        board.addPiece(startPosition, null);

        return destinationPiece;
    }

    private void undoMove(ChessMove move, ChessPiece capturedPiece, ChessBoard board) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition destination = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(destination);
        board.addPiece(startPosition, currentPiece);
        board.addPiece(destination, capturedPiece);
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition[][] currentPosition = new ChessPosition[8][8];
        ChessPiece[][] currentPiece = new ChessPiece[8][8];
        ChessPosition kingPosition = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition[i][j] = new ChessPosition(i + 1, j + 1);
                currentPiece[i][j] = board.getPiece(currentPosition[i][j]);
                if(currentPiece[i][j] == null) {
                    continue;
                }
                if (currentPiece[i][j].getPieceType() == ChessPiece.PieceType.KING && currentPiece[i][j].getTeamColor() == teamColor) {
                    kingPosition = currentPosition[i][j];
                }
            }
        }
        Collection<ChessMove> validMoves;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(currentPiece[i][j] == null) {
                    continue;
                }
                validMoves = currentPiece[i][j].pieceMoves(board, currentPosition[i][j]);
                if (validMoves == null) {
                    continue;
                }
                if (validMoves.contains(new ChessMove(currentPosition[i][j], kingPosition, null))) {
                    return true;
                }
                else if (validMoves.contains(new ChessMove(currentPosition[i][j], kingPosition, ChessPiece.PieceType.QUEEN))) {
                    return true;
                }
                else if (validMoves.contains(new ChessMove(currentPosition[i][j], kingPosition, ChessPiece.PieceType.ROOK))) {
                    return true;
                }
                else if (validMoves.contains(new ChessMove(currentPosition[i][j], kingPosition, ChessPiece.PieceType.BISHOP))) {
                    return true;
                }
                else if (validMoves.contains(new ChessMove(currentPosition[i][j], kingPosition, ChessPiece.PieceType.KNIGHT))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition, ChessBoard board) {
        ChessPiece currentPiece = board.getPiece(startPosition);
        if (currentPiece == null) {
            return null;
        }
        TeamColor currentColor = currentPiece.getTeamColor();
        Collection<ChessMove> potentiallyValidMoves = currentPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPiece capturedPiece;
        for (ChessMove move : potentiallyValidMoves) {
            capturedPiece = this.testMove(move, board);
            if(!this.isInCheck(currentColor, board)) {
                validMoves.add(move);
            }
            this.undoMove(move, capturedPiece, board);
        }
//        if (currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
//            for (int i = 0; i < 8; i++) {
//                for (int j = 0; j < 8; j++) {
//                    ChessPosition destination = new ChessPosition(i + 1, j + 1);
//                    if (board.getPiece(destination) == null) {
//                        continue;
//                    }
//                    ChessPiece piece = board.getPiece(destination);
//                    if (piece.getTeamColor() != currentColor) {
//                        continue;
//                    }
//                    Collection<ChessMove> moves = piece.pieceMoves(board, destination);
//                    for (ChessMove move : moves) {
//                        capturedPiece = this.testMove(move, board);
//                        if(!this.isInCheck(currentColor, board)) {
//                            validMoves.add(new ChessMove(startPosition, destination, null));
//                        }
//                        this.undoMove(move, capturedPiece, board);
//                    }
//                }
//            }
//        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        switch(type) {
            case PAWN:
                sb.append(color.equals(ChessPiece.TeamColor.WHITE) ? "P" : "p");
                break;

            case KING:
                sb.append(color.equals(ChessPiece.TeamColor.WHITE) ? "K" : "k");
                break;

            case QUEEN:
                sb.append(color.equals(ChessPiece.TeamColor.WHITE) ? "Q" : "q");
                break;

            case ROOK:
                sb.append(color.equals(ChessPiece.TeamColor.WHITE) ? "R" : "r");
                break;

            case BISHOP:
                sb.append(color.equals(ChessPiece.TeamColor.WHITE) ? "B" : "b");
                break;

            case KNIGHT:
                sb.append(color.equals(ChessPiece.TeamColor.WHITE) ? "N" : "n");
                break;

            default:
                break;
        }

        return sb.toString();
    }
}
