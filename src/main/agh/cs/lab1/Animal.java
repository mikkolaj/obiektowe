package agh.cs.lab1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private final int[] genes = new int[32];
    private final float[] probs = new float[8];
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    final private IWorldMap map;

    public Animal(IWorldMap map, Vector2d initialPosition) {
        Random generator = new Random();
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.energy = 100;
        for (int i = 0; i < 32; i++) {
            this.genes[i] = generator.nextInt(8);
        }
        countProbs();
    }

    // dwie grupy mają być losowane
    public Animal(IWorldMap map, Vector2d initialPosition, Animal strongerAnimal, Animal weakerAnimal) {
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        setGenes(strongerAnimal.genes, weakerAnimal.genes);
        countProbs();
    }

    private void setGenes(int[] strongerGenes, int[] weakerGenes) {
        Random generator = new Random();
        int firstDivider = generator.nextInt(30);
        int secondDivider = firstDivider + 1 + generator.nextInt(30 - firstDivider);
        int weakerPart = generator.nextInt(3);
        if (weakerPart == 0) {
            copyGenes(strongerGenes, weakerGenes, 0, firstDivider, firstDivider + 1, secondDivider, secondDivider + 1, 32);
        } else if (weakerPart == 2) {
            copyGenes(strongerGenes, weakerGenes, secondDivider + 1, 32, 0, firstDivider, firstDivider + 1, secondDivider);
        } else {
            copyGenes(strongerGenes, weakerGenes, firstDivider + 1, secondDivider, 0, firstDivider, secondDivider + 1, 32);
        }

    }

    private void copyGenes(int[] strongerGenes, int[] weakerGenes, int weakerBeg, int weakerEnd, int strongerBeg1, int strongerEnd1, int strongerBeg2, int strongerEnd2) {
        for (int i = weakerBeg; i <= weakerEnd; i++) {
            this.genes[i] = weakerGenes[i];
        }
        for (int i = strongerBeg1; i < strongerEnd1; i++) {
            this.genes[i] = strongerGenes[i];
        }
        for (int i = strongerBeg2; i <= strongerEnd2; i++) {
            this.genes[i] = strongerGenes[i];
        }
    }

    private void countProbs() {
        for (int i : this.genes) {
            this.probs[i] += 1;
        }
        for (int i = 0; i < this.probs.length; i++) {
            this.probs[i] /= 32;
        }
    }

    private int selectTurn() {
        Random generator = new Random();
        int number = generator.nextInt(100);
        float curProb = 0;
        for (int i=0; i<8; i++) {
            curProb += this.probs[i];
            if (curProb * 100 > number) {
                return i;
            }
        }
        return 7;
    }

    public String toString() {
        return this.orientation.toString();
    }

    public void move() {
        int turn = selectTurn();
        System.out.println(turn);
        for(int i=0; i<turn; i++) {
            this.orientation = this.orientation.next();
        }
        Vector2d curMove = this.orientation.toUnitVector();
        Vector2d newPos = this.position.add(curMove);
        if (this.map.canMoveTo(newPos)) {
            Vector2d oldPos = this.position;
            this.position = newPos;
            this.positionChanged(oldPos, newPos);
        }
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(oldPosition, newPosition);
        }
    }

    public int getEnergy() {
        return this.energy;
    }

    public void boostEnergy(int value) {
        if(value >= 0)
            this.energy += value;
    }

    @Override
    public int hashCode() {
        return this.position.hashCode();
    }
}
