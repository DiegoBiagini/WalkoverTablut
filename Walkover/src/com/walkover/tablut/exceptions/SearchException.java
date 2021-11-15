package com.walkover.tablut.exceptions;


import com.walkover.tablut.search.WalkoverSearch;

public class SearchException extends Exception {

    private static final long serialVersionUID = 1L;

    public SearchException(String message, WalkoverSearch a)
    {
        super(message);
    }

}