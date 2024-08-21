package net.minecraft.client.player;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;

public final class MovementInputFromOptions extends MovementInput {
    private boolean[] movementKeyStates = new boolean[6]; // Cover forward, back, left, right, jump, and sprint
    private GameSettings gameSettings;
    private long lastForwardPressTime = 0L; // Track the last time forward was pressed
    private final long doubleTapThreshold = 200L; // Threshold for double-tap detection in milliseconds
    public static boolean isSprinting = false; // Public to let the player update logic access it
    private boolean forwardWasReleased = false; // Track if forward was released after the first press

    public static float maxStamina = 10.0f; // Max stamina time in seconds
    public static float staminaRemaining = maxStamina; // Start with full stamina
    public static float speedMultiplier;
    private float staminaRate = 0.1f; // Stamina change rate (per tick)
    private boolean isExhausted = false; // Tracks if stamina is fully depleted
    private boolean canSprint = true; // Controls whether sprinting is allowed
    private long lastStaminaUpdateTime = 0L; // Last time stamina was updated
    private Minecraft mc;

    private boolean hasInitializedStamina = false; // Flag to ensure max stamina is set only once
    private int lastHealth = -1; // Track player's health to detect changes

    public MovementInputFromOptions(GameSettings gameSettings, Minecraft mc) {
        this.gameSettings = gameSettings;
        this.mc = mc;
        initializeStamina(); // Ensure stamina is set on game start
    }

    private void initializeStamina() {
        // Set stamina to max when the player first enters the game
        if (!hasInitializedStamina) {
            staminaRemaining = maxStamina;
            lastHealth = this.mc.thePlayer.health; // Initialize health tracking
            hasInitializedStamina = true;
        }
    }

    @Override
    public final void checkKeyForMovementInput(int keyCode, boolean isPressed) {
        int keyIndex = -1;

        if (keyCode == this.gameSettings.keyBindForward.keyCode) {
            keyIndex = 0;
        } else if (keyCode == this.gameSettings.keyBindBack.keyCode) {
            keyIndex = 1;
        } else if (keyCode == this.gameSettings.keyBindLeft.keyCode) {
            keyIndex = 2;
        } else if (keyCode == this.gameSettings.keyBindRight.keyCode) {
            keyIndex = 3;
        } else if (keyCode == this.gameSettings.keyBindJump.keyCode) {
            keyIndex = 4;
        } else if (keyCode == this.gameSettings.keyBindSprint.keyCode) {
            keyIndex = 5;
        }

        if (keyIndex >= 0) {
            this.movementKeyStates[keyIndex] = isPressed;

            // Handle sprint key press directly
            if (keyIndex == 5 && isPressed && canSprint) { // Sprint key (Control) is pressed and sprinting is allowed
                startSprinting();
            }

            // Handle double-tap detection for sprinting
            if (keyIndex == 0 && isPressed && canSprint) { // Forward key pressed and sprinting is allowed
                long currentTime = System.currentTimeMillis();

                if (forwardWasReleased && (currentTime - lastForwardPressTime < this.doubleTapThreshold)) {
                    startSprinting(); // Start sprinting on valid double-tap
                    forwardWasReleased = false; // Reset release tracking for future taps
                }

                lastForwardPressTime = currentTime;
            } else if (keyIndex == 0 && !isPressed) {
                forwardWasReleased = true; // Forward key was released
            }

            // Stop sprinting if both the forward key and sprint key are no longer held
            if ((keyIndex == 0 && !isPressed) || (keyIndex == 5 && !isPressed)) {
                stopSprinting();
            }
        }
    }

    private void startSprinting() {
        int playerHealth = this.mc.thePlayer.health; // Access the player's health

        // Only allow sprinting if stamina is available, sprinting is allowed, and health is above 3 hearts (6 health points)
        if (staminaRemaining > 0 && canSprint && playerHealth > 6) {
            isSprinting = true;
        }
    }

    private void stopSprinting() {
        isSprinting = false;
    }

    private void updateMaxStamina() {
        // Max stamina is based on health, with lower health reducing available stamina
        int playerHealth = this.mc.thePlayer.health; // Access the player's health

        // Calculate the difference in health and adjust stamina accordingly
        if (playerHealth < lastHealth) {
            int lostHealth = lastHealth - playerHealth;
            float timeLost = lostHealth * 1.0f; // Each lost heart removes a fixed time (1 second per heart)
            staminaRemaining = Math.max(0, staminaRemaining - timeLost); // Remove the corresponding stamina
        }

        maxStamina = playerHealth * 1.0f; // Each heart gives 1.0 seconds of stamina
        staminaRemaining = Math.min(staminaRemaining, maxStamina); // Cap remaining stamina at the new max
        lastHealth = playerHealth; // Update last health for future comparisons

        // Disable sprinting if health falls below 3 hearts (less than or equal to 6 health points)
        if (playerHealth <= 6) {
            canSprint = false;
            stopSprinting(); // Stop sprinting immediately if health is too low
        } else if (!isExhausted) {
            canSprint = true; // Re-enable sprinting if not exhausted
        }
    }

    @Override
    public final void updatePlayerMoveState() {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        long currentTime = System.currentTimeMillis();

        boolean isMoving = this.movementKeyStates[0] || this.movementKeyStates[1] || this.movementKeyStates[2] || this.movementKeyStates[3];

        // Initialize stamina only once when the game starts
        if (!hasInitializedStamina) {
            initializeStamina();
        }

        // Dynamically update stamina based on health if it changes
        updateMaxStamina();

        // Update stamina only if the player is sprinting and moving
        if (isSprinting && isMoving && currentTime - lastStaminaUpdateTime >= 50) {
            staminaRemaining -= staminaRate; // Deplete stamina
            if (staminaRemaining <= 0) {
                staminaRemaining = 0;
                stopSprinting(); // Stop sprinting when stamina runs out
                isExhausted = true; // Mark as exhausted
                canSprint = false; // Disable sprinting until stamina fully regenerates
            }
            lastStaminaUpdateTime = currentTime; // Update the last stamina update time
        }

        // Handle stamina regeneration if the player is not sprinting or not moving
        if ((!isSprinting || !isMoving) && currentTime - lastStaminaUpdateTime >= 50) {
            if (isExhausted) {
                staminaRemaining += staminaRate * 0.5f; // Slow regeneration when exhausted
                if (staminaRemaining >= maxStamina) {
                    staminaRemaining = maxStamina;
                    isExhausted = false; // Stop marking as exhausted once fully regenerated
                    canSprint = true; // Re-enable sprinting after full stamina regeneration
                }
            } else if (staminaRemaining < maxStamina) {
                staminaRemaining = Math.min(staminaRemaining + staminaRate, maxStamina); // Regenerate normally when not sprinting
            }
            lastStaminaUpdateTime = currentTime; // Update the last stamina update time
        }

        // Update movement inputs
        if (this.movementKeyStates[0]) { // Move forward
            this.moveForward = 1.0F; // Move forward at normal speed
        }

        if (this.movementKeyStates[1]) { // Move back
            this.moveForward = -1.0F;
        }

        if (this.movementKeyStates[2]) { // Move left
            this.moveStrafe = 1.0F;
        }

        if (this.movementKeyStates[3]) { // Move right
            this.moveStrafe = -1.0F;
        }

        this.jump = this.movementKeyStates[4];
    }
}
