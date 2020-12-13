package agh.cs.lab1;//package agh.cs.lab1;
//
//import java.util.Iterator;
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//public class MapBoundary implements IPositionChangeObserver {
      // TODO dobry pomysł to IWorldMapElement zamiast Object
//    private final SortedSet<Object> yObjects = new TreeSet<>((obj1, obj2) -> {
//        Vector2d pos1 = getObjPos(obj1);
//        Vector2d pos2 = getObjPos(obj2);
//        return getOrder(pos1.y, pos2.y, pos1.x, pos2.x, obj1);
//    });
//
//    private final SortedSet<Object> xObjects = new TreeSet<>((obj1, obj2) -> {
//        Vector2d pos1 = getObjPos(obj1);
//        Vector2d pos2 = getObjPos(obj2);
//        return getOrder(pos1.x, pos2.x, pos1.y, pos2.y, obj1);
//    });
//
//    private int getOrder(int obj1MainPos, int obj2MainPos,  int obj1SecPos, int obj2SecPos, Object obj1) {
//        if (obj1MainPos < obj2MainPos || (obj1MainPos == obj2MainPos && obj1SecPos < obj2SecPos) ||
//                obj1MainPos == obj2MainPos && obj1SecPos == obj2SecPos && obj1 instanceof Animal){
//            return -1;
//        } else {
//            return 1;
//        }
//    }
//
//    private Vector2d getObjPos(Object obj) {
//        if (obj instanceof Animal) {
//            return ((Animal) obj).getPosition();
//        } else {
//            return ((Grass) obj).getPosition();
//        }
//    }
//
//    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
//        if (!oldPosition.equals(newPosition)) {
//            Iterator<Object> it = this.xObjects.iterator();
//            Object current;
//            while (it.hasNext()) {
//                current = it.next();
//                if (current instanceof Animal && ((Animal) current).getPosition() == newPosition) {
//                    this.xObjects.remove(current);
//                    this.yObjects.remove(current);
//                    this.xObjects.add(current);
//                    this.yObjects.remove(current);
//                    break;
//                }
//            }
//        }
//    }
//
//    public Vector2d getLowerLeft() {
//        Vector2d pos1 = this.getObjPos((this.xObjects.first()));
//        Vector2d pos2 = this.getObjPos((this.yObjects.first()));
//        return pos1.lowerLeft(pos2);
//    }
//
//    public Vector2d getUpperRight() {
//        Vector2d pos1 = this.getObjPos((this.xObjects.last()));
//        Vector2d pos2 = this.getObjPos((this.yObjects.last()));
//        return pos1.upperRight(pos2);
//    }
//
      // TODO przeciążenie metody na animala i trawę
//    public void addObject(Object obj) {
//        this.xObjects.add(obj);
//        this.yObjects.add(obj);
//    }
//}
