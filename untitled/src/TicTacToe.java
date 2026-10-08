import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Консольные крестики-нолики 3x3.
 * Режимы: игрок против игрока или игрок против компьютера.
 */
public class TicTacToe {

    private static final int SIZE = 3;
    private static final char EMPTY = ' ';
    private static final char X = 'X';
    private static final char O = 'O';

    private static final int[][] LINES = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // строки
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // столбцы
            {0, 4, 8}, {2, 4, 6}             // диагонали
    };

    private final char[] board = new char[SIZE * SIZE];
    private final Scanner in = new Scanner(System.in);
    private final Random random = new Random();

    public static void main(String[] args) {
        new TicTacToe().run();
    }

    private void run() {
        System.out.println("=== Крестики-нолики ===");
        boolean vsComputer = askMode();

        do {
            playRound(vsComputer);
        } while (askYesNo("Сыграть ещё? (д/н): "));

        System.out.println("Пока!");
    }

    private void playRound(boolean vsComputer) {
        java.util.Arrays.fill(board, EMPTY);
        char current = X;

        while (true) {
            printBoard();

            int move = (vsComputer && current == O) ? computerMove() : humanMove(current);
            board[move] = current;

            if (vsComputer && current == O) {
                System.out.println("Компьютер ходит в клетку " + (move + 1));
            }

            if (isWinner(current)) {
                printBoard();
                System.out.println("Победил " + current + "!");
                return;
            }
            if (isFull()) {
                printBoard();
                System.out.println("Ничья!");
                return;
            }
            current = (current == X) ? O : X;
        }
    }

    private boolean askMode() {
        while (true) {
            System.out.println("Выберите режим:");
            System.out.println("  1 - игрок против игрока");
            System.out.println("  2 - игрок против компьютера");
            System.out.print("> ");
            String line = in.nextLine().trim();
            if (line.equals("1")) return false;
            if (line.equals("2")) return true;
            System.out.println("Введите 1 или 2.");
        }
    }

    private boolean askYesNo(String prompt) {
        System.out.print(prompt);
        String line = in.nextLine().trim().toLowerCase();
        return line.startsWith("д") || line.startsWith("y");
    }

    private int humanMove(char player) {
        while (true) {
            System.out.print("Ход " + player + " (1-9): ");
            String line = in.nextLine().trim();
            int cell;
            try {
                cell = Integer.parseInt(line) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Нужно число от 1 до 9.");
                continue;
            }
            if (cell < 0 || cell >= board.length) {
                System.out.println("Нужно число от 1 до 9.");
            } else if (board[cell] != EMPTY) {
                System.out.println("Клетка занята.");
            } else {
                return cell;
            }
        }
    }

    /** Простой ИИ: выиграть, иначе помешать, иначе центр, угол или любая клетка. */
    private int computerMove() {
        int move = findWinningMove(O);
        if (move >= 0) return move;

        move = findWinningMove(X);
        if (move >= 0) return move;

        if (board[4] == EMPTY) return 4;

        List<Integer> corners = freeCells(new int[]{0, 2, 6, 8});
        if (!corners.isEmpty()) return corners.get(random.nextInt(corners.size()));

        List<Integer> rest = freeCells(new int[]{1, 3, 5, 7});
        return rest.get(random.nextInt(rest.size()));
    }

    private int findWinningMove(char player) {
        for (int[] line : LINES) {
            int count = 0;
            int empty = -1;
            for (int cell : line) {
                if (board[cell] == player) count++;
                else if (board[cell] == EMPTY) empty = cell;
            }
            if (count == 2 && empty >= 0) return empty;
        }
        return -1;
    }

    private List<Integer> freeCells(int[] cells) {
        List<Integer> result = new ArrayList<>();
        for (int cell : cells) {
            if (board[cell] == EMPTY) result.add(cell);
        }
        return result;
    }

    private boolean isWinner(char player) {
        for (int[] line : LINES) {
            if (board[line[0]] == player && board[line[1]] == player && board[line[2]] == player) {
                return true;
            }
        }
        return false;
    }

    private boolean isFull() {
        for (char c : board) {
            if (c == EMPTY) return false;
        }
        return true;
    }

    private void printBoard() {
        System.out.println();
        for (int row = 0; row < SIZE; row++) {
            StringBuilder sb = new StringBuilder(" ");
            for (int col = 0; col < SIZE; col++) {
                int i = row * SIZE + col;
                // Пустые клетки показываем номером, чтобы было понятно, куда ходить
                sb.append(board[i] == EMPTY ? Character.forDigit(i + 1, 10) : board[i]);
                if (col < SIZE - 1) sb.append(" | ");
            }
            System.out.println(sb);
            if (row < SIZE - 1) System.out.println("---+---+---");
        }
        System.out.println();
    }
}
