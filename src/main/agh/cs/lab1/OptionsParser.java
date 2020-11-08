package agh.cs.lab1;
import java.util.ArrayList;

public class OptionsParser {
    public static MoveDirection[] parse (String[] args) {
        ArrayList<MoveDirection> directions = new ArrayList<MoveDirection>();
        for (String dir : args) {
            if (dir.equals("f") || dir.equals("forward")) {
                directions.add(MoveDirection.FORWARD);
                continue;
            }
            if (dir.equals("b") || dir.equals("backward")) {
                directions.add(MoveDirection.BACKWARD);
                continue;
            }
            if (dir.equals("l") || dir.equals("left")) {
                directions.add(MoveDirection.LEFT);
                continue;
            }
            if (dir.equals("r") || dir.equals("right")) {
                directions.add(MoveDirection.RIGHT);
            }
        }
        return directions.toArray(new MoveDirection[directions.size()]);
    }
}
