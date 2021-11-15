package com.walkover.tablut.client;

import com.walkover.tablut.domain.*;
import com.walkover.tablut.evaluator.KingDistanceMetric;
import com.walkover.tablut.exceptions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WalkoverPlayer {
    public static void main(String[] args) throws IOException, ClassNotFoundException, PawnException, DiagonalException, ClimbingException, ActionException, CitadelException, StopException, OccupitedException, BoardException, ClimbingCitadelException, ThroneException {
        WalkoverClient.main(args);

    }

}
