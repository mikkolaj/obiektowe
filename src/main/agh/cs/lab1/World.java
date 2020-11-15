package agh.cs.lab1;


public class World {
    public static void main(String[] args) {
        MoveDirection[] directions = OptionsParser.parse(args);
        AbstractWorldMap map = new GrassField(10);
        Vector2d[] positions = { new Vector2d(2,2), new Vector2d(3,4) };
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        System.out.println(map);

        // 9. Dodanie interfejsu IMapElement, implementowanego przez Grass i Animal nie pomogłoby wiele w obecnej
        //    sytuacji. Te klasy mają wspólną jedynie pozycję i metodę, która ją zwraca. Aby zwierzęta mogły stać na
        //    trawie musielibyśmy dalej mieć dwie hashmapy, żeby klucze (pozycje) się nie pokrywały, więc nie
        //    uprościlibyśmy tym klasy GrassField.

        // 10. Ponownie nie widzę korzyści z wprowadzania AbstractWorldMapElement ze względu na znikome podobieństwo
        //     pomiędzy klasami Grass i Animal.
    }

    public static void run(MoveDirection[] args) {
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

    public static MoveDirection[] changeToEnum(String[] args) {
        MoveDirection[] result = new MoveDirection[ args.length ];
        for(int i=0; i<args.length; i++) {
            switch(args[i]) {
                case "f":
                    result[i] = MoveDirection.FORWARD;
                    break;
                case "b":
                    result[i] = MoveDirection.BACKWARD;
                    break;
                case "l":
                    result[i] = MoveDirection.LEFT;
                    break;
                case "r":
                    result[i] = MoveDirection.RIGHT;
                    break;
            }
        }
        return result;
    }
}
