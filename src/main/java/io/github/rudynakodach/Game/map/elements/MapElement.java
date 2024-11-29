package io.github.rudynakodach.Game.map.elements;

import org.jetbrains.annotations.Range;

public class MapElement {

    private final @Range(from = 0, to = 1) float r;
    private final @Range(from = 0, to = 1) float g;
    private final @Range(from = 0, to = 1) float b;

    public MapElement(@Range(from = 0, to = 1) float r, @Range(from = 0, to = 1) float g, @Range(from = 0, to = 1) float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }
}
