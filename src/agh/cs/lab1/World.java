package agh.cs.lab1;

public class World {
    public static void main(String[] args) {
        System.out.println("Program has started!");
        Direction[] newArgs = changeToEnum(args);
        run(newArgs);
        System.out.println("Program is about to exit.");
    }

    public static void run(Direction[] args) {
        for(int i=0; i<args.length; i++) {
            switch(args[i]) {
                case FORWARD:
                    System.out.print("Pet goes forward");
                    break;
                case BACKWARD:
                    System.out.print("Pet goes backward");
                    break;
                case LEFT:
                    System.out.print("Pet goes left");
                    break;
                case RIGHT:
                    System.out.print("Pet goes right");
                    break;
                default:
                    System.out.println("Wrong arguments!");
                    return;
            }
            if (i == args.length -1) System.out.println();
            else System.out.print(", ");
        }
    }

    public static Direction[] changeToEnum(String[] args) {
        Direction[] result = new Direction[ args.length ];
        for(int i=0; i<args.length; i++) {
            switch(args[i]) {
                case "f":
                    result[i] = Direction.FORWARD;
                    break;
                case "b":
                    result[i] = Direction.BACKWARD;
                    break;
                case "l":
                    result[i] = Direction.LEFT;
                    break;
                case "r":
                    result[i] = Direction.RIGHT;
                    break;
            }
        }
        return result;
    }
}
