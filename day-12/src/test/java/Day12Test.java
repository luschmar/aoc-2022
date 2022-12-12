import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", solution = "31"),
            @AocInputMapping(input = "input.txt", solution = "408")
    })
    void part1(Stream<String> input, String solution) {
        var inputList = input.toList();

        var m = new ElfMap(inputList);
        var startIndex = m.getStartIndex();
        var result = dijkstrasShortestPath(m, startIndex);

        var a = result[m.getEndIndex()];

        assertEquals(Integer.parseInt(solution), a);
    }

    public static int getClosestVertex(int[] distance, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minIdx = -1;
        for (int i = 0; i < distance.length; i++) {
            if (distance[i] < min && !visited[i]) {
                min = distance[i];
                minIdx = i;
            }
        }
        return minIdx == -1 ? Integer.MAX_VALUE : minIdx;
    }

    public static int[] dijkstrasShortestPath(ElfMap m, int src) {
        //final shortest distance array
        int[] distance = new int[m.numOfvertices];
        //array to tell whether shortest distance of vertex has been found
        boolean[] visited = new boolean[m.numOfvertices];

        //initializing the arrays
        for (int i = 0; i < m.numOfvertices; i++) {
            distance[i] = Integer.MAX_VALUE;//initial distance is infinite
            visited[i] = false;//shortest distance for any node has not been found yet
        }
        distance[src] = 0;

        //printMatrix(m);
        for (int i = 0; i < m.numOfvertices; i++) {
            int closestVertex = getClosestVertex(distance, visited); //get the closest node
            //if closest node is infinite distance away, it means that no other node can be reached. So
            if (closestVertex == Integer.MAX_VALUE) {
                return distance;
            }

            visited[closestVertex] = true;
            for (int j = 0; j < m.numOfvertices; j++) {
                if (!visited[j])//shortest distance of the node j should not have been finalized
                {

                    if (m.adjMatrix[closestVertex][j] != 0) {

                        int d = distance[closestVertex] + m.adjMatrix[closestVertex][j];
                        if (d < distance[j]) { //distance via closestVertex is less than the initial distance
                            //System.out.println(m.getValueAt(closestVertex)+" -> "+m.getValueAt(j)+"   = "+m.adjMatrix[closestVertex][j] );
                            distance[j] = d;
                        }
                    }
                }
            }

            //printState(distance);

        }
        return distance;
    }

    private static void printMatrix(ElfMap map) {
        for(int i = 0; i < 40; i++) {
            for(int j = 0; j < 40; j++) {
                System.out.print(map.adjMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printState(int[] distance) {
        for(int i = 0; i < distance.length; i++) {
            if(distance[i] != Integer.MAX_VALUE) {
                System.out.print(String.format("%5s", distance[i]));
            }
            else {
                System.out.print(String.format("%5s", "-"));
            }
            if((i+1) % 8 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    class ElfMap {
        private final List<String> input;
        public int numOfvertices;
        public int[][] adjMatrix;

        ElfMap(List<String> input) {
            this.input = input;
            this.numOfvertices = input.size() * input.get(0).length();
            this.adjMatrix = new int[numOfvertices][numOfvertices];

            for (int i = 0; i < input.size(); i++) {
                for (int j = 0; j < input.get(0).length(); j++) {
                    var height = getValueAt(i, j);
                    // add north node
                    if (i + 1 < input.size()) {
                        var heightEast = getValueAt(i + 1, j);
                        if (height+1 >= heightEast) {
                            //System.out.println(height +"+1 >= "+ heightEast);
                            adjMatrix[getIndexOf(i, j)][getIndexOf(i + 1, j)] = 1;
                        }
                    }
                    if (i - 1 >= 0) {
                        var heightWest = getValueAt(i - 1, j);
                        if (height+1 >= heightWest) {
                            //System.out.println(height +"+1 >= "+ heightWest);
                            adjMatrix[getIndexOf(i, j)][getIndexOf(i - 1, j)] = 1;
                        }
                    }
                    // east
                    if (j + 1 < input.get(i).length()) {
                        var heightSouth = getValueAt(i, j + 1);
                        if (height+1 >= heightSouth) {
                            //System.out.println(height +"+1 >= "+ heightSouth);
                            adjMatrix[getIndexOf(i, j)][getIndexOf(i, j + 1)] = 1;
                        }
                    }
                    // west
                    if (j - 1 >= 0) {
                        var heightNorth = getValueAt(i, j - 1);
                        if (height+1 >= heightNorth) {
                            //System.out.println(height +"+1 >= "+ heightNorth);
                            adjMatrix[getIndexOf(i, j)][getIndexOf(i, j - 1)] = 1;
                        }
                    }
                }
            }
        }

        int getStartIndex() {
            for (int i = 0; i < input.size(); i++) {
                for (int j = 0; j < input.get(0).length(); j++) {
                    if (input.get(i).charAt(j) == 'S') {
                        return getIndexOf(i, j);
                    }
                }
            }
            return 0;
        }

        int[] getStartIndexes() {
            ArrayList<Integer> starts = new ArrayList<>();
            for (int i = 0; i < input.size(); i++) {
                for (int j = 0; j < input.get(0).length(); j++) {
                    if (input.get(i).charAt(j) == 'S' || input.get(i).charAt(j) == 'a') {
                        starts.add(getIndexOf(i, j));
                    }
                }
            }
            return starts.stream().mapToInt(Integer::intValue).toArray();
        }

        int getEndIndex() {
            for (int i = 0; i < input.size(); i++) {
                for (int j = 0; j < input.get(0).length(); j++) {
                    if (input.get(i).charAt(j) == 'E') {
                        return getIndexOf(i, j);
                    }
                }
            }
            return 0;
        }

        private int getIndexOf(int x, int y) {
            return (input.get(0).length() * x) + y;
        }

        private char getValueAt(int x, int y) {
            var val = input.get(x).charAt(y);
            if (val == 'S') {
                return 'a';
            }
            if (val == 'E') {
                return 'z'+1;
            }
            return val;
        }

        public char getValueAt(int j) {
            return input.get(j/input.get(0).length()).charAt(j%input.get(0).length());
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", solution = "29"),
            @AocInputMapping(input = "input.txt", solution = "399")
    })
    void part2(Stream<String> input, String solution) {
        var inputList = input.toList();

        var m = new ElfMap(inputList);
        var startIndex = m.getStartIndexes();
       var result = Arrays.stream(startIndex).parallel().map(i -> dijkstrasShortestPath(m, i)[m.getEndIndex()]).min();


        assertEquals(Integer.parseInt(solution), result.getAsInt());
    }

}