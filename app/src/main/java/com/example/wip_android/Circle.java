package com.example.wip_android;

public class Circle {
    private float x;
    private float y;
    private int radius;

    public Circle(float x, float y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Circle() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circle{" + "x=" + x + ", y=" + y + ", radius=" + radius + '}';
    }
}
