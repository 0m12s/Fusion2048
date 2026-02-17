const API_BASE = "https://fusion2048.onrender.com";
const GAME_API = `${API_BASE}/game`;

let gameState = null;
let overlayVisible = false;
let isLoginMode = true;

/* ================= AUTH ================= */

function showLogin() {
  document.getElementById("loginModal").style.display = "flex";
}

function hideLogin() {
  document.getElementById("loginModal").style.display = "none";
}

function getToken() {
  return localStorage.getItem("token");
}

function authHeaders() {
  const token = getToken();
  if (!token) return {};
  return {
    "Content-Type": "application/json",
    Authorization: "Bearer " + token,
  };
}

function logout() {
  localStorage.removeItem("token");
  location.reload();
}

function clearAuthFields() {
  document.getElementById("usernameInput").value = "";
  document.getElementById("passwordInput").value = "";
  document.getElementById("loginError").innerText = "";
}

async function handleAuth() {
  const username = document.getElementById("usernameInput").value.trim();
  const password = document.getElementById("passwordInput").value.trim();

  if (!username || !password) {
    document.getElementById("loginError").innerText =
      "Username and password required";
    return;
  }

  const endpoint = isLoginMode ? "login" : "register";

  try {
    const res = await fetch(`${API_BASE}/auth/${endpoint}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (!res.ok) throw new Error();

    if (isLoginMode) {
      const data = await res.json();
      const token = data.token;
      localStorage.setItem("token", token);
      console.log("TOKEN STORED:", localStorage.getItem("token"));

      clearAuthFields();
      hideLogin();
      showUsername();
      await loadGame();
    } else {
      clearAuthFields();
      document.getElementById("loginError").innerText =
        "Registration successful. Please login.";
      toggleMode();
    }
  } catch {
    document.getElementById("loginError").innerText = isLoginMode
      ? "Invalid credentials"
      : "Username already exists";
  }
}

/* ================= LOAD GAME ================= */

async function loadGame() {
  try {
    const response = await fetch(GAME_API, {
      headers: authHeaders(),
    });

    if (!response.ok) {
      throw new Error("Unauthorized");
    }

    gameState = await response.json();
    hideOverlay();
    render();
  } catch (err) {
    console.log("Token invalid or expired");
    logout();
  }
}

/* ================= RESTART ================= */

async function restartGame() {
  try {
    const response = await fetch(`${GAME_API}/restart`, {
      method: "POST",
      headers: authHeaders(),
    });

    if (!response.ok) throw new Error();

    gameState = await response.json();
    hideOverlay();
    render();
  } catch {
    logout();
  }
}

/* ================= MOVE ================= */

async function makeMove(direction) {
  if (!gameState || gameState.gameOver || overlayVisible) return;

  const previousBoard = JSON.stringify(gameState.board);

  try {
    const response = await fetch(`${GAME_API}/move?dir=${direction}`, {
      method: "POST",
      headers: authHeaders(),
    });

    if (!response.ok) throw new Error();

    gameState = await response.json();

    const newBoard = JSON.stringify(gameState.board);

    if (previousBoard !== newBoard) {
      const moveSound = document.getElementById("moveSound");
      if (moveSound) {
        moveSound.currentTime = 0;
        moveSound.play();
      }
    }

    render();
  } catch {
    logout();
  }
}

/* ================= SAVE SCORE ================= */

async function saveScore() {
  try {
    await fetch(`${GAME_API}/save`, {
      method: "POST",
      headers: authHeaders(),
      body: JSON.stringify(gameState),
    });
  } catch {
    console.error("Score save failed");
  }
}

/* ================= RENDER ================= */

function render() {
  const boardDiv = document.getElementById("board");
  boardDiv.innerHTML = "";

  gameState.board.forEach((row) => {
    row.forEach((cell) => {
      const tile = document.createElement("div");
      tile.className = "tile";

      if (cell !== 0) {
        tile.textContent = cell;
        tile.classList.add(`tile-${cell}`);
      }

      boardDiv.appendChild(tile);
    });
  });

  document.getElementById("score").textContent = gameState.score;

  document.getElementById("best").textContent = gameState.bestScore || 0;

  if (gameState.gameOver && !overlayVisible) {
    showOverlay("lose");
    saveScore();
  } else if (gameState.won && !overlayVisible) {
    showOverlay("win");
    saveScore();
  }
}

/* ================= OVERLAY ================= */

function showOverlay(type) {
  overlayVisible = true;

  const overlay = document.getElementById("gameOverlay");
  const textElement = document.getElementById("overlayText");
  const boardContainer = document.querySelector(".board-container");

  overlay.classList.add("active");
  boardContainer.classList.add("overlay-active");

  textElement.textContent = type === "win" ? "🎉 YOU WIN!" : "GAME OVER💀";

  if (type === "win") {
    document.getElementById("winSound")?.play();
  } else {
    document.getElementById("loseSound")?.play();
    boardContainer.classList.add("shake");
    setTimeout(() => {
      boardContainer.classList.remove("shake");
    }, 500);
  }
}

function hideOverlay() {
  document.getElementById("gameOverlay").classList.remove("active");
  document.querySelector(".board-container").classList.remove("overlay-active");
  overlayVisible = false;
}

/* ================= USER DISPLAY ================= */

function showUsername() {
  const token = getToken();
  if (!token) return;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const username = payload.sub;

    document.getElementById("userDisplay").innerText = "👤 " + username;
  } catch {
    logout();
  }
}

/* ================= MODE SWITCH ================= */

function toggleMode() {
  isLoginMode = !isLoginMode;

  document.getElementById("loginTitle").innerText = isLoginMode
    ? "Sign In"
    : "Sign Up";

  document.getElementById("authButton").innerText = isLoginMode
    ? "Login"
    : "Register";

  document.querySelector(".login-switch").innerText = isLoginMode
    ? "Don't have an account? Sign Up"
    : "Already have an account? Sign In";

  document.getElementById("loginError").innerText = "";
}

/* ================= KEYBOARD ================= */

document.addEventListener("keydown", (event) => {
  if (!gameState || gameState.gameOver || overlayVisible) return;

  const map = {
    ArrowUp: "UP",
    ArrowDown: "DOWN",
    ArrowLeft: "LEFT",
    ArrowRight: "RIGHT",
  };

  if (map[event.key]) {
    event.preventDefault();
    makeMove(map[event.key]);
  }
});

/* ================= INIT ================= */

window.onload = async function () {
  const token = getToken();

  if (!token) {
    showLogin();
  } else {
    hideLogin();
    showUsername();
    await loadGame();
  }
};

function logout() {
  localStorage.removeItem("token");
  location.reload();
}

