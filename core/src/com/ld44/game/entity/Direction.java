package com.ld44.game.entity;

import java.util.Random;

public enum Direction {

    UP, DOWN, LEFT, RIGHT, NONE
    ;

    public static Direction getRandomDirection() {
        Direction[] possibleDirections = {UP, DOWN, LEFT, RIGHT};

        return possibleDirections[new Random().nextInt(possibleDirections.length)];
    }

}
