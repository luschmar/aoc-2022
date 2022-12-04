import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", solution = "21"),
            @AocInputMapping(input = "input.txt", solution = "1688"),
    })
    void part1(Stream<String> input, String solution) {
        var treeArray = input.map(String::toCharArray).toArray(char[][]::new);

        var visibleArray = checkEdgeVisible(treeArray);

        var result = Arrays.stream(visibleArray)
                .flatMap(s -> IntStream.range(0, s.length)
                        .mapToObj(i -> s[i])).filter(a -> a).count();
        assertEquals(Integer.parseInt(solution), result);
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", solution = "21"),
            @AocInputMapping(input = "input.txt", solution = "1688"),
    })
    void part1_string(Stream<String> input, String solution) {
        var horizontal = input.toList();
        var vertical = IntStream.range(0, horizontal.get(0).length())
                .mapToObj(x-> horizontal.stream().map(s -> s.charAt(x))
                        .map(String::valueOf).collect(joining())).toList();

        int c = 0;
        for(int i = 0; i < horizontal.size();i++) {
            for(int j = 0; j < vertical.size();j++) {
                var height = horizontal.get(i).charAt(j);
                if(lookString(i, height, vertical.get(j)) ||
                        lookString(j, height, horizontal.get(i)))
                {
                    c++;
                }
            }
        }

        assertEquals(Integer.parseInt(solution), c);
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", solution = "21"),
            @AocInputMapping(input = "input.txt", solution = "1688"),
    })
    void part1_int(Stream<String> input, String solution) {
        var horizontal = input.map(String::chars).map(IntStream::toArray).toList();
        var vertical = IntStream.range(0, horizontal.get(0).length)
                .mapToObj(x-> horizontal.stream().mapToInt(s -> s[x])).map(IntStream::toArray).toList();

        int c = 0;
        for(int i = 0; i < horizontal.size();i++) {
            for(int j = 0; j < vertical.size();j++) {
                var height = horizontal.get(i)[j];
                if(lookInt(i, height, vertical.get(j)) ||
                        lookInt(j, height, horizontal.get(i)))
                {
                    c++;
                }
            }
        }

        assertEquals(Integer.parseInt(solution), c);
    }

    private boolean lookString(int pos, int height, String view) {
        var north = view.substring(0, pos);
        var south = view.substring(pos+1);

        var n = north.chars().filter(f -> height <= f).count();
        var s = south.chars().filter(f -> height <= f).count();

        return 0 == n || 0 == s;
    }

    private boolean lookInt(int pos, int height, int[] view) {
        var north = Arrays.stream(view, 0, pos).filter(f -> height <= f).count();
        var south = Arrays.stream(view, pos + 1, view.length).filter(f -> height <= f).count();

        return 0 == north || 0 == south;
    }


    private boolean[][] checkEdgeVisible(char[][] treeArray) {
        var visibleArray = new boolean[treeArray.length][treeArray[0].length];
        for (int i = 0; i < treeArray.length; i++) {
            for (int j = 0; j < treeArray[i].length; j++) {
                if (tryWalkWest(i, j, treeArray) ||
                        tryWalkEast(i, j, treeArray) ||
                        tryWalkSouth(i, j, treeArray) ||
                        tryWalkNorth(i, j, treeArray)) {
                    visibleArray[i][j] = true;
                }
            }
        }
        return visibleArray;
    }

    private boolean tryWalkWest(int x, int y, char[][] treeArray) {
        var height = treeArray[x][y];
        for (int i = x - 1; i >= 0; i--) {
            if (height <= treeArray[i][y]) {
                return false;
            }
        }
        return true;
    }

    private boolean tryWalkEast(int x, int y, char[][] treeArray) {
        var height = treeArray[x][y];
        for (int i = x + 1; i < treeArray.length; i++) {
            if (height <= treeArray[i][y]) {
                return false;
            }
        }
        return true;
    }

    private boolean tryWalkSouth(int x, int y, char[][] treeArray) {
        var height = treeArray[x][y];
        for (int i = y - 1; i >= 0; i--) {
            if (height <= treeArray[x][i]) {
                return false;
            }
        }
        return true;
    }

    private boolean tryWalkNorth(int x, int y, char[][] treeArray) {
        var height = treeArray[x][y];
        for (int i = y + 1; i < treeArray[x].length; i++) {
            if (height <= treeArray[x][i]) {
                return false;
            }
        }
        return true;
    }


    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "test.txt", solution = "8"),
            @AocInputMapping(input = "input.txt", solution = "410400")
    })
    void part2(Stream<String> input, String solution) {
        var treeArray = input.map(String::toCharArray).toArray(char[][]::new);

        var scenicScore = calculateScenicScore(treeArray);

        var result = Arrays.stream(scenicScore)
                .flatMapToLong(Arrays::stream).max().getAsLong();
        assertEquals(Long.parseLong(solution), result);
    }


    private long[][] calculateScenicScore(char[][] treeArray) {
        var scenicScores = new long[treeArray.length][treeArray[0].length];
        for (int i = 0; i < treeArray.length; i++) {
            for (int j = 0; j < treeArray[i].length; j++) {
                var west = westTrees(i, j, treeArray).size();
                var east = eastTrees(i, j, treeArray).size();
                var south = southTrees(i, j, treeArray).size();
                var north = northTrees(i, j, treeArray).size();

                scenicScores[i][j] = (long) west * east * south * north;
            }
        }
        return scenicScores;
    }

    private List<Character> northTrees(int x, int y, char[][] treeArray) {
        var trees = new ArrayList<Character>();
        var height = treeArray[x][y];
        for (int i = y + 1; i < treeArray[x].length; i++) {
            trees.add(treeArray[x][i]);
            if (height <= treeArray[x][i]) {
                return trees;
            }
        }
        return trees;
    }

    private List<Character> southTrees(int x, int y, char[][] treeArray) {
        var trees = new ArrayList<Character>();
        var height = treeArray[x][y];
        for (int i = y - 1; i >= 0; i--) {
            trees.add(treeArray[x][i]);
            if (height <= treeArray[x][i]) {
                return trees;
            }
        }
        return trees;
    }

    private List<Character> eastTrees(int x, int y, char[][] treeArray) {
        var trees = new ArrayList<Character>();
        var height = treeArray[x][y];
        for (int i = x + 1; i < treeArray.length; i++) {
            trees.add(treeArray[i][y]);
            if (height <= treeArray[i][y]) {
                return trees;
            }
        }
        return trees;
    }

    private List<Character> westTrees(int x, int y, char[][] treeArray) {
        var trees = new ArrayList<Character>();
        var height = treeArray[x][y];
        for (int i = x - 1; i >= 0; i--) {
            trees.add(treeArray[i][y]);
            if (height <= treeArray[i][y]) {
                return trees;
            }
        }
        return trees;
    }
}