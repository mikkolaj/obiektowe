package agh.cs.lab1;

import java.util.HashSet;
import java.util.Set;

public class AnimalObserver {
    private final Animal observedAnimal;
    private int daysPassed = 0;
    private int dayOfDeath = -1;
    private final int duration;
    private final Set<Animal> children = new HashSet<>();
    private final Set<Animal> otherDescendants = new HashSet<>();

    public AnimalObserver(Animal animal, int days) {
        this.observedAnimal = animal;
        this.duration = days;
    }

    public int getNumberOfChildren() {
        return this.children.size();
    }

    public int getNumberOfAllDescendants() {
        return this.children.size() + this.otherDescendants.size();
    }

    public int getDayOfDeath() {
        return this.dayOfDeath;
    }

    public void animalsReproduced(Animal strongerAnimal, Animal weakerAnimal, Animal child) {
        if (strongerAnimal.equals(this.observedAnimal) || weakerAnimal.equals(this.observedAnimal)) {
            this.children.add(child);
        } else if (this.children.contains(strongerAnimal) || this.children.contains(weakerAnimal) ||
                this.otherDescendants.contains(strongerAnimal) || this.otherDescendants.contains(weakerAnimal)) {
            this.otherDescendants.add(child);
        }
    }

    public void animalDied(Animal animal) {
        if (animal.equals(this.observedAnimal)) {
            this.dayOfDeath = this.daysPassed;
        }
        this.children.remove(animal);
        this.otherDescendants.remove(animal);
    }

    public void dayHasPassed() {
        this.daysPassed += 1;
    }

    public boolean isFinished() {
        return this.daysPassed >= this.duration;
    }
}
