package com.fus2048.fusion2048.controller;

import com.fus2048.fusion2048.model.GameState;
import com.fus2048.fusion2048.service.GameService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.fus2048.fusion2048.repository.UserRepository;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
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

