//package agh.cs.lab1;
//
//class RectangularMap extends AbstractWorldMap {
//    private final Vector2d lowerLeft;
//    private final Vector2d upperRight;
//
//    public RectangularMap (int width, int height) {
//        this.lowerLeft = new Vector2d(0, 0);
//        this.upperRight = new Vector2d(width - 1, height - 1);
//    }
//
//    @Override
//    public boolean canMoveTo(Vector2d position) {
//        return insideMap(position) && super.canMoveTo(position);
//    }
//
//    private boolean insideMap(Vector2d position) {
//        return position.follows(this.lowerLeft) && position.precedes(this.upperRight);
//    }
//
//    @Override
//    public boolean isOccupied(Vector2d position) {
//        return this.insideMap(position) && super.isOccupied(position);
//    }
//}
