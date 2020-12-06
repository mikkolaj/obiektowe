package agh.cs.lab1;
//import java.util.ArrayList;
//
//public class OptionsParser {
//    public static MoveDirection[] parse (String[] args) {
//        ArrayList<MoveDirection> directions = new ArrayList<MoveDirection>();
//        for (String dir : args) {
//                if (dir.equals("f") || dir.equals("forward")) {
//                    directions.add(MoveDirection.FORWARD);
//                } else if (dir.equals("b") || dir.equals("backward")) {
//                    directions.add(MoveDirection.BACKWARD);
//                } else if (dir.equals("l") || dir.equals("left")) {
//                    directions.add(MoveDirection.LEFT);
//                } else if (dir.equals("r") || dir.equals("right")) {
//                    directions.add(MoveDirection.RIGHT);
//                } else {
//                    throw new IllegalArgumentException(dir + " is not legal move specification");
//                }
//        }
//        return directions.toArray(new MoveDirection[directions.size()]);
//    }
//}
