package com.fus2048.fusion2048.service;

import com.fus2048.fusion2048.logic.Game2048;
import com.fus2048.fusion2048.model.GameState;
import com.fus2048.fusion2048.entity.User;
import com.fus2048.fusion2048.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameService {

    private final UserRepository userRepository;

    //per user game instances
    private final Map<String, Game2048> userGames = new ConcurrentHashMap<>();

    //current logged-in username
    private String getCurrentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return auth.getName();
    }

    // game for user
    private Game2048 getUserGame() {
        String username = getCurrentUsername();
        return userGames.computeIfAbsent(username, u -> new Game2048());
    }

    // game state
    public GameState getGameState() {
        Game2048 game = getUserGame();

        String username = getCurrentUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return new GameState(game.getBoard(), game.getScore(), game.isGameOver(), game.hasWon(), user.getBestScore());
    }


    //Make move
    public GameState makeMove(String direction) {
        Game2048 game = getUserGame();
        game.move(direction);

        GameState state = getGameState();

        // Save best score
        saveScoreIfHigher(state.getScore());

        return state;
    }

    //Restart
    public GameState restartGame() {
        Game2048 game = getUserGame();
        game.restart();
        return getGameState();
    }

    // Save Best Score
    private void saveScoreIfHigher(int newScore) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBestScore() == null || newScore > user.getBestScore()) {
            user.setBestScore(newScore);
            userRepository.save(user);
        }
    }
}
