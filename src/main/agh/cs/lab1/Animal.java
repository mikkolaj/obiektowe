package agh.cs.lab1;

import java.util.*;

public class Animal {
    private static final int GENOTYPE_LENGTH = 32;
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private final List<Integer> genes = new ArrayList<>(GENOTYPE_LENGTH);
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    final private GrassField map;
    private int daysLived = 0;
    private int children = 0;

    public Animal(GrassField map, Vector2d initialPosition, int startEnergy) {
        Random generator = new Random();
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.energy = startEnergy;
        for (int i = 0; i < GENOTYPE_LENGTH; i++) {
            this.genes.add(generator.nextInt(8));
        }
        this.fixGenes(generator);
        this.map.fieldChanged(initialPosition);
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
        strongerAnimal.children += 1;
        weakerAnimal.children += 1;
        this.energy = strongerEnergy + weakerEnergy;
        this.map.fieldChanged(initialPosition);
    }

    private void fixGenes(Random generator) {
        for (int i = 0; i < 8; i++) {
            if (!this.genes.contains(i)) {
                int position;
                do {
                    position = generator.nextInt(GENOTYPE_LENGTH);
                } while (Collections.frequency(this.genes, this.genes.get(position)) < 2);
                this.genes.remove(position);
                this.genes.add(position, i);
            }
        }
        this.genes.sort(Integer::compareTo);
    }

    private void setGenes(List<Integer> strongerGenes, List<Integer> weakerGenes) {
        Random generator = new Random();
        int firstDivider = generator.nextInt(GENOTYPE_LENGTH - 2);
        int secondDivider;
        if (firstDivider == GENOTYPE_LENGTH - 3) {
            secondDivider = GENOTYPE_LENGTH - 2;
        } else {
            secondDivider = firstDivider + 1 + generator.nextInt(GENOTYPE_LENGTH - 3 - firstDivider);
        }
        int weakerPart = generator.nextInt(3);
        if (weakerPart == 0) {
            copyGenes(strongerGenes, weakerGenes, 0, firstDivider, firstDivider + 1, secondDivider, secondDivider + 1, GENOTYPE_LENGTH - 1);
        } else if (weakerPart == 2) {
            copyGenes(strongerGenes, weakerGenes, secondDivider + 1, GENOTYPE_LENGTH - 1, 0, firstDivider, firstDivider + 1, secondDivider);
        } else {
            copyGenes(strongerGenes, weakerGenes, firstDivider + 1, secondDivider, 0, firstDivider, secondDivider + 1, GENOTYPE_LENGTH - 1);
        }
    }

    private void copyGenes(List<Integer> strongerGenes, List<Integer> weakerGenes, int weakerBeg, int weakerEnd, int strongerBeg1, int strongerEnd1, int strongerBeg2, int strongerEnd2) {
        for (int i = weakerBeg; i <= weakerEnd; i++) {
            this.genes.add(weakerGenes.get(i));
        }
        for (int i = strongerBeg1; i <= strongerEnd1; i++) {
            this.genes.add(strongerGenes.get(i));
        }
        for (int i = strongerBeg2; i <= strongerEnd2; i++) {
            this.genes.add(strongerGenes.get(i));
        }
    }

    private int selectTurn() {
        Random generator = new Random();
        return this.genes.get(generator.nextInt(GENOTYPE_LENGTH));
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
            this.daysLived += 1;
            Vector2d oldPos = this.position;
            this.position = newPos;
            this.energy -= this.map.moveEnergy;
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
        if (value >= 0)
            this.energy += value;
    }

    public List<Integer> getGenes() {
        return this.genes;
    }

    public int getChildren() {
        return this.children;
    }

    public int getDaysLived() {
        return this.daysLived;
    }

    @Override
    public int hashCode() {
        return this.position.hashCode();
    }
}
