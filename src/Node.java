import java.util.Arrays;
import java.util.List;

public class Node {
    public int id;
    public int lifeTime;
    public int destination;
    public String msg;
    public List<Integer> neighbors;

    @Override
    public String toString() {
        return "id = " + id + "\n" +
                "lifeTime = " + lifeTime + "\n" +
                "destination = " + destination + "\n" +
                "msg = " + msg + "\n" +
                "neighbors = " + Arrays.toString(neighbors.toArray());

    }
}
