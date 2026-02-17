package com.fus2048.fusion2048.controller;

import com.fus2048.fusion2048.model.GameState;
import com.fus2048.fusion2048.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public GameState getGameState() {
        return gameService.getGameState();
    }

    @PostMapping("/move")
    public GameState move(@RequestParam String dir) {
        return gameService.makeMove(dir);
    }

    @PostMapping("/restart")
    public GameState restart() {
        return gameService.restartGame();
    }

    @GetMapping("/secure-test")
    public String secureTest() {
        return "You accessed secure endpoint!";
    }
}

