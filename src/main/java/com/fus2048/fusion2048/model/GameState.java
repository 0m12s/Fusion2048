package com.fus2048.fusion2048.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameState {
    private int[][] board;
    private int score;
    private boolean gameOver;
    private boolean won;
    private Integer bestScore;
}
