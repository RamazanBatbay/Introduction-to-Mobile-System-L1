package com.example.mazegame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    // Screens ViewGroups
    private View layoutStart;
    private View layoutGame;
    private View layoutResult;

    // Game Navigation Buttons
    private Button btnNavUp;
    private Button btnNavDown;
    private Button btnNavLeft;
    private Button btnNavRight;

    // Game Status Indicators & Text
    private TextView textCoordinates;
    private TextView textDebugMask;
    private TextView doorUpIndicator;
    private TextView doorDownIndicator;
    private TextView doorLeftIndicator;
    private TextView doorRightIndicator;

    // Maze Definition
    private final int[][] maze = {
        {10,  8, 10,  9},
        {28,  1,  0, 12},
        {12, 10,  9, 13},
        { 6,  5,  6,  5}
    };

    // Game State variables
    private int startRow = 1;
    private int startCol = 0;
    private int currentRow;
    private int currentCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Views
        layoutStart = findViewById(R.id.layout_start);
        layoutGame = findViewById(R.id.layout_game);
        layoutResult = findViewById(R.id.layout_result);

        // Navigation Buttons
        btnNavUp = findViewById(R.id.btn_nav_up);
        btnNavDown = findViewById(R.id.btn_nav_down);
        btnNavLeft = findViewById(R.id.btn_nav_left);
        btnNavRight = findViewById(R.id.btn_nav_right);

        // Text & Door Indicators
        textCoordinates = findViewById(R.id.text_coordinates);
        textDebugMask = findViewById(R.id.text_debug_mask);
        doorUpIndicator = findViewById(R.id.door_up_indicator);
        doorDownIndicator = findViewById(R.id.door_down_indicator);
        doorLeftIndicator = findViewById(R.id.door_left_indicator);
        doorRightIndicator = findViewById(R.id.door_right_indicator);

        // Start screen button setup
        Button btnStartGame = findViewById(R.id.btn_start_game);
        btnStartGame.setOnClickListener(v -> startGame());

        // Result screen buttons setup
        Button btnRestart = findViewById(R.id.btn_restart);
        btnRestart.setOnClickListener(v -> restartGame());

        Button btnBackToMenu = findViewById(R.id.btn_back_to_menu);
        btnBackToMenu.setOnClickListener(v -> backToMenu());

        // Setup Nav Click Listeners
        btnNavUp.setOnClickListener(v -> movePlayer(-1, 0));
        btnNavDown.setOnClickListener(v -> movePlayer(1, 0));
        btnNavLeft.setOnClickListener(v -> movePlayer(0, -1));
        btnNavRight.setOnClickListener(v -> movePlayer(0, 1));

        // Locate Start Cell dynamically based on bit 16
        findStartRoom();
    }

    /**
     * Scans the 2D maze array to locate the room with the special start marker (bit 16).
     * If multiple rooms exist, the first encountered is preferred. If none exist, falls back to (1, 0).
     */
    private void findStartRoom() {
        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[r].length; c++) {
                if ((maze[r][c] & 16) != 0) {
                    startRow = r;
                    startCol = c;
                    return;
                }
            }
        }
    }

    /**
     * Initializes the player coordinates to the starting room and transitions to the game screen.
     */
    private void startGame() {
        currentRow = startRow;
        currentCol = startCol;
        layoutStart.setVisibility(View.GONE);
        layoutGame.setVisibility(View.VISIBLE);
        layoutResult.setVisibility(View.GONE);
        updateRoomUI();
    }

    /**
     * Resets the player back to the starting cell and transitions from the Win screen back to play.
     */
    private void restartGame() {
        startGame();
    }

    /**
     * Returns the player back to the start main menu layout.
     */
    private void backToMenu() {
        layoutStart.setVisibility(View.VISIBLE);
        layoutGame.setVisibility(View.GONE);
        layoutResult.setVisibility(View.GONE);
    }

    /**
     * Moves the player position if the move is validated, checking if they reached the win condition.
     */
    private void movePlayer(int rowDelta, int colDelta) {
        int targetRow = currentRow + rowDelta;
        int targetCol = currentCol + colDelta;

        if (isValidMove(rowDelta, colDelta)) {
            currentRow = targetRow;
            currentCol = targetCol;

            // Check Win Condition (Value 0)
            if (maze[currentRow][currentCol] == 0) {
                showWinScreen();
            } else {
                updateRoomUI();
            }
        }
    }

    /**
     * Validates whether a move in a specific direction is allowed by the room bitmask and grid boundary checks.
     */
    private boolean isValidMove(int rowDelta, int colDelta) {
        int val = maze[currentRow][currentCol];

        // Grid boundaries check
        int targetRow = currentRow + rowDelta;
        int targetCol = currentCol + colDelta;
        if (targetRow < 0 || targetRow >= maze.length || targetCol < 0 || targetCol >= maze[targetRow].length) {
            return false;
        }

        // Bitmask checks (ignore bit 16)
        if (rowDelta == -1 && colDelta == 0) {
            return (val & 4) != 0; // UP
        } else if (rowDelta == 1 && colDelta == 0) {
            return (val & 8) != 0; // DOWN
        } else if (rowDelta == 0 && colDelta == -1) {
            return (val & 1) != 0; // LEFT
        } else if (rowDelta == 0 && colDelta == 1) {
            return (val & 2) != 0; // RIGHT
        }

        return false;
    }

    /**
     * Transitions UI to show the Result Screen.
     */
    private void showWinScreen() {
        layoutStart.setVisibility(View.GONE);
        layoutGame.setVisibility(View.GONE);
        layoutResult.setVisibility(View.VISIBLE);
    }

    /**
     * Dynamically updates texts, indicators, and buttons based on player coordinates and door parameters.
     */
    private void updateRoomUI() {
        int currentVal = maze[currentRow][currentCol];

        // Update coordinate text
        textCoordinates.setText(getString(R.string.room_coordinates, currentRow, currentCol));

        // Update debug details
        String binaryStr = Integer.toBinaryString(currentVal);
        // Pad binary representation to 5 bits for aesthetic quality
        while (binaryStr.length() < 5) {
            binaryStr = "0" + binaryStr;
        }
        textDebugMask.setText(getString(R.string.room_value_debug, currentVal, binaryStr));

        // Evaluate and style directional navigation buttons
        styleNavButton(btnNavUp, isValidMove(-1, 0));
        styleNavButton(btnNavDown, isValidMove(1, 0));
        styleNavButton(btnNavLeft, isValidMove(0, -1));
        styleNavButton(btnNavRight, isValidMove(0, 1));

        // Update visual door indicators (UP, DOWN, LEFT, RIGHT checks, ignoring boundaries for door icons)
        styleDoorIndicator(doorUpIndicator, (currentVal & 4) != 0);
        styleDoorIndicator(doorDownIndicator, (currentVal & 8) != 0);
        styleDoorIndicator(doorLeftIndicator, (currentVal & 1) != 0);
        styleDoorIndicator(doorRightIndicator, (currentVal & 2) != 0);
    }

    /**
     * Helper to apply modern active or disabled styling to navigation button states.
     */
    private void styleNavButton(Button button, boolean isAvailable) {
        button.setEnabled(isAvailable);
        if (isAvailable) {
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_available));
            button.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_unavailable));
            button.setTextColor(ContextCompat.getColor(this, R.color.disabled_text));
        }
    }

    /**
     * Helper to apply visual colors to door status indicators in the central room view.
     */
    private void styleDoorIndicator(TextView view, boolean isAvailable) {
        if (isAvailable) {
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.accent_green));
            view.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(this, R.color.disabled_gray));
            view.setTextColor(ContextCompat.getColor(this, R.color.disabled_text));
        }
    }
}
