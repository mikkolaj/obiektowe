package agh.cs.lab1;
import java.util.ArrayList;

public class OptionsParser {
    public static MoveDirection[] parse (String[] args) {
        ArrayList<MoveDirection> directions = new ArrayList<MoveDirection>();
        for (String dir : args) {
            if (dir == "f" || dir == "forward") {
                directions.add(MoveDirection.FORWARD);
                continue;
            }
            if (dir == "b" || dir == "backward") {
                directions.add(MoveDirection.BACKWARD);
                continue;
            }
            if (dir == "l" || dir == "left") {
                directions.add(MoveDirection.LEFT);
                continue;
            }
            if (dir == "r" || dir == "right") {
                directions.add(MoveDirection.RIGHT);
            }
        }
        return directions.toArray(new MoveDirection[directions.size()]);
    }
}
