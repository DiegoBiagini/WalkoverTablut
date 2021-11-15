package com.walkover.tablut.domain;

import java.util.Objects;

public class Coordinate {
    public int r;
    public int c;

    public Coordinate(int r, int c){
        this.r = r;
        this.c = c;
    }

    public Coordinate(){
        this.r = -1;
        this.c = -1;
    }

    public Coordinate(String c_string){
        if(c_string.length() != 2){
            this.r = -1;
            this.c = -1;
        }
        this.r = (int)(c_string.charAt(1)) - 49;
        this.c = (int)(c_string.charAt(0)) - 97;
    }

    public String getHumanCoordinate(){
        String ret;
        char col = (char) (c + 97);
        ret = col + "" + (r + 1);
        return ret;
    }

    public int getManhattanDist(Coordinate c2){
        return(Math.abs(r-c2.r) + Math.abs(c-c2.c));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return r == that.r && c == that.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + r +
                ", y=" + c +
                '}';
    }
}
