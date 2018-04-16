import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String... args) {
        Node node = parseInput(args);
        System.out.println(node);
    }

    private static Node parseInput(String... args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Input must contain node id, duration, and destination node");
        }

        Node node = new Node();
        node.id = Integer.parseInt(args[0]);
        node.lifeTime = Integer.parseInt(args[1]);
        node.destination = Integer.parseInt(args[2]);

        if (args.length > 3) {
            List<Integer> neighbors = new ArrayList<>();
            if (args.length == 4) {
                neighbors.add(Integer.parseInt(args[3]));
                node.neighbors = neighbors;
            } else {
                node.msg = args[3];
                int neighbor = 4;
                while (neighbor < args.length) {
                    neighbors.add(Integer.parseInt(args[neighbor++]));
                }
            }
        }

        return node;
    }
}
