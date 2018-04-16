import java.util.ArrayList;

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
        node.neighbors = new ArrayList<>();

        if (args.length > 3) {
            int neighbor = 3;
            if (args[3].charAt(0) == '"') {
                node.msg = args[3];
                neighbor++;
            }
            while (neighbor < args.length) {
                node.neighbors.add(Integer.parseInt(args[neighbor++]));
            }
        }

        return node;
    }
}
