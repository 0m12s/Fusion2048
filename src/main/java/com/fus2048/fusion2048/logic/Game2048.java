package com.fus2048.fusion2048.logic;

import lombok.Getter;

import java.util.Random;

public class Game2048 {

    private static final int SIZE = 4;

    @Getter
    private int[][] board;

    @Getter
    private int score;

    private boolean moved = false;
    private final Random random = new Random();

    public Game2048() {
        this.board = new int[SIZE][SIZE];
        this.score = 0;
        addRandomTile();
        addRandomTile();
    }

    private Game2048(int[][] board, int score) {
        this.board = board;
        this.score = score;
    }

    public void move(String direction) {
        moved = false;

        switch (direction.toUpperCase()) {
            case "UP":
                moveUp();
                break;
            case "DOWN":
                moveDown();
                break;
            case "LEFT":
                moveLeft();
                break;
            case "RIGHT":
                moveRight();
                break;
        }

        if (moved) {
            addRandomTile();
        }
    }

    public boolean isGameOver() {

        for (int[] row : board)
            for (int val : row)
                if (val == 0) return false;

        Game2048 copy = new Game2048(copyBoard(), score);

        copy.move("UP");
        if (copy.moved) return false;

        copy = new Game2048(copyBoard(), score);
        copy.move("DOWN");
        if (copy.moved) return false;

        copy = new Game2048(copyBoard(), score);
        copy.move("LEFT");
        if (copy.moved) return false;

        copy = new Game2048(copyBoard(), score);
        copy.move("RIGHT");

        return !copy.moved;
    }

    public boolean hasWon() {
        for (int[] row : board)
            for (int val : row)
                if (val == 2048) return true;
        return false;
    }

    public void restart() {
        board = new int[SIZE][SIZE];
        score = 0;
        addRandomTile();
        addRandomTile();
    }

    private void addRandomTile() {
        int row, col;
        do {
            row = random.nextInt(SIZE);
            col = random.nextInt(SIZE);
        } while (board[row][col] != 0);

        board[row][col] = random.nextInt(10) < 9 ? 2 : 4;
    }

    private void moveLeft() {
        for (int i = 0; i < SIZE; i++) {
            int[] row = compress(board[i]);
            row = merge(row);
            board[i] = compress(row);
        }
    }

    private void moveRight() {
        for (int i = 0; i < SIZE; i++) {
            int[] reversed = reverse(board[i]);
            reversed = compress(reversed);
            reversed = merge(reversed);
            board[i] = reverse(compress(reversed));
        }
    }

    private void moveUp() {
        for (int col = 0; col < SIZE; col++) {
            int[] column = new int[SIZE];
            for (int row = 0; row < SIZE; row++) {
                column[row] = board[row][col];
            }
            column = compress(column);
            column = merge(column);
            column = compress(column);
            for (int row = 0; row < SIZE; row++) {
                board[row][col] = column[row];
            }
        }
    }

    private void moveDown() {
        for (int col = 0; col < SIZE; col++) {
            int[] column = new int[SIZE];
            for (int row = 0; row < SIZE; row++) {
                column[row] = board[row][col];
            }
            column = reverse(column);
            column = compress(column);
            column = merge(column);
            column = compress(column);
            column = reverse(column);
            for (int row = 0; row < SIZE; row++) {
                board[row][col] = column[row];
            }
        }
    }

    private int[] compress(int[] row) {
        int[] newRow = new int[SIZE];
        int index = 0;

        for (int val : row) {
            if (val != 0) {
                newRow[index++] = val;
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (row[i] != newRow[i]) {
                moved = true;
                break;
            }
        }

        return newRow;
    }

    private int[] merge(int[] row) {
        for (int i = 0; i < SIZE - 1; i++) {
            if (row[i] != 0 && row[i] == row[i + 1]) {
                row[i] *= 2;
                row[i + 1] = 0;
                score += row[i];
                moved = true;
            }
        }
        return row;
    }

    private int[] reverse(int[] row) {
        int[] newRow = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            newRow[i] = row[SIZE - 1 - i];
        }
        return newRow;
    }

    private int[][] copyBoard() {
        int[][] newBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);
        }
        return newBoard;
    }
}
