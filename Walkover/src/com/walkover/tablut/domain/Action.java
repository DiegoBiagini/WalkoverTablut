package com.walkover.tablut.domain;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;

/**
 * this class represents an action of a player
 * 
 * @author A.Piretti
 * 
 */
public class Action implements Serializable {

	private static final long serialVersionUID = 1L;

	private String from;
	private String to;

	private State.Turn turn;

	public Action(Coordinate from, Coordinate to, StateTablut.Turn t){
		this.from = from.getHumanCoordinate();
		this.to = to.getHumanCoordinate();
		this.turn = t;
	}

	public Action(String from, String to, StateTablut.Turn t) throws IOException {
		if (from.length() != 2 || to.length() != 2) {
			throw new InvalidParameterException("the FROM and the TO string must have length=2");
		} else {
			this.from = from;
			this.to = to;
			this.turn = t;
		}
	}

	public String getFrom() {
		return this.from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public StateTablut.Turn getTurn() {
		return turn;
	}

	public void setTurn(StateTablut.Turn turn) {
		this.turn = turn;
	}

	public String toString() {
		return "Turn: " + this.turn + " " + "Pawn from " + from + " to " + to;
	}

	/**
	 * @return means the index of the column where the pawn is moved from
	 */
	public int getColumnFrom() {
		return Character.toLowerCase(this.from.charAt(0)) - 97;
	}

	/**
	 * @return means the index of the column where the pawn is moved to
	 */
	public int getColumnTo() {
		return Character.toLowerCase(this.to.charAt(0)) - 97;
	}

	/**
	 * @return means the index of the row where the pawn is moved from
	 */
	public int getRowFrom() {
		return Integer.parseInt(this.from.charAt(1) + "") - 1;
	}

	/**
	 * @return means the index of the row where the pawn is moved to
	 */
	public int getRowTo() {
		return Integer.parseInt(this.to.charAt(1) + "") - 1;
	}

	public void toCoordinate(Coordinate f, Coordinate t){
		f.r = from.charAt(1) - 49;
		f.c = from.charAt(0) -97;

		t.r = to.charAt(1) - 49;
		t.c = to.charAt(0) -97;
	}

}
