package com.example.shared_blackboard.model;

public class DrawAction {
    private String userId;
    private int x;
    private int y;
    private String color;
    private String toolType;

    // Constructors, getters, and setters
    public DrawAction() {}

    public DrawAction(String userId, int x, int y, String color, String toolType) {
        this.userId = userId;
        this.x = x;
        this.y = y;
        this.color = color;
        this.toolType = toolType;
    }

    // Getters and Setters here (use Lombok @Data if you added Lombok dependency)
}
