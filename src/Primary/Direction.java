package Primary;

import javafx.scene.paint.Color;

public enum Direction {
    NS ("NS"),
    EW ("EW"),
    N ("N"),
    E ("E"),
    S ("S"),
    W ("W");



    private final String state;

    Direction(String state) {
        this.state = state;
    }
}
