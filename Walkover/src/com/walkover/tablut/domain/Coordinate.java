package com.walkover.tablut.domain;

import java.util.Objects;

public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinate(){
        this.x = -1;
        this.y = -1;
    }

    public Coordinate(String c_string){
        if(c_string.length() != 2){
            this.x = -1;
            this.y = -1;
        }
        this.x = (int)(c_string.charAt(1)) - 49;
        this.y = (int)(c_string.charAt(0)) - 97;
    }

    public String getHumanCoordinate(){
        String ret;
        char col = (char) (y + 97);
        ret = col + "" + (x + 1);
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
