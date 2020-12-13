package agh.cs.lab1;

import java.util.*;

public class Animal {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private final int[] genes = new int[32];
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    final private GrassField map;

    public Animal(GrassField map, Vector2d initialPosition, int startEnergy) {
        Random generator = new Random();
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.energy = startEnergy;
        for (int i = 0; i < 32; i++) {
            this.genes[i] = generator.nextInt(8);
        }
        this.fixGenes(generator);
    }

    // dwie grupy mają być losowane
    public Animal(GrassField map, Vector2d initialPosition, Animal strongerAnimal, Animal weakerAnimal) {
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.setGenes(strongerAnimal.genes, weakerAnimal.genes);
        this.fixGenes(new Random());
        int strongerEnergy = (int) Math.ceil(1.0 * strongerAnimal.energy / 4);
        int weakerEnergy = (int) Math.ceil(1.0 * weakerAnimal.energy / 4);
        strongerAnimal.energy -= strongerEnergy;
        weakerAnimal.energy -= weakerEnergy;
        this.energy = strongerEnergy + weakerEnergy;
    }

    private void fixGenes(Random generator) {
        List<Integer> listOfGenes = new ArrayList<Integer>(this.genes.length);
        for (int i : this.genes) {
            listOfGenes.add(i);
        }
        for (int i = 0; i < 8; i++) {
            if (!listOfGenes.contains(i)) {
                int position;
                do {
                    position = generator.nextInt(32);
                } while (Collections.frequency(listOfGenes, listOfGenes.get(position)) < 2);
                listOfGenes.remove(position);
                listOfGenes.add(i, position);
            }
        }
        for (int i = 0; i < 32; i++) {
            this.genes[i] = listOfGenes.get(i);
        }
        Arrays.sort(this.genes);
    }

    private void setGenes(int[] strongerGenes, int[] weakerGenes) {
        Random generator = new Random();
        int firstDivider = generator.nextInt(30);
        int secondDivider;
        if (firstDivider == 29) {
            secondDivider = 30;
        } else {
            secondDivider = firstDivider + 1 + generator.nextInt(29 - firstDivider);
        }
        int weakerPart = generator.nextInt(3);
        if (weakerPart == 0) {
            copyGenes(strongerGenes, weakerGenes, 0, firstDivider, firstDivider + 1, secondDivider, secondDivider + 1, 31);
        } else if (weakerPart == 2) {
            copyGenes(strongerGenes, weakerGenes, secondDivider + 1, 31, 0, firstDivider, firstDivider + 1, secondDivider);
        } else {
            copyGenes(strongerGenes, weakerGenes, firstDivider + 1, secondDivider, 0, firstDivider, secondDivider + 1, 31);
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

    private int selectTurn() {
        Random generator = new Random();
        return this.genes[generator.nextInt(32)];
    }

    public String toString() {
        return this.orientation.toString();
    }

    public void move() {
        int turn = selectTurn();
        for (int i = 0; i < turn; i++) {
            this.orientation = this.orientation.next();
        }
        Vector2d curMove = this.orientation.toUnitVector();
        Vector2d newPos = this.map.wrapPositions(this.position.add(curMove));
        if (this.map.canMoveTo(newPos)) {
            Vector2d oldPos = this.position;
            this.position = newPos;
            this.positionChanged(oldPos, newPos);
            this.energy -= this.map.moveEnergy;
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
        if (value >= 0)
            this.energy += value;
    }

    public int[] getGenes() {
        return this.genes;
    }

    @Override
    public int hashCode() {
        return this.position.hashCode();
    }
}
