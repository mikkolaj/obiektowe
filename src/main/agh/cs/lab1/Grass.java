package agh.cs.lab1;

public class Grass {
    Vector2d position;  // to powinno być prywatne i może być finalne

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
