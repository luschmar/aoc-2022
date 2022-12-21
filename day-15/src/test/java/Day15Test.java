import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test {
    @ParameterizedTest
    @AocFileSource(inputs = {@AocInputMapping(input = "test.txt", solution = "26"), @AocInputMapping(input = "input.txt", solution = "5878678")})
    void part1(Stream<String> input, String solution) {
        var inputList = input.map(SensorBeaconPair::new).toList();

        var b = new TunnelNetwork(inputList);
        if (b.maxX < 100) {
            var matrix = b.fillInSB();
            b.printMap(matrix);
        }
        var result = 0;
        if (b.maxX < 100) {
            result = b.countCannotExist(10);
        } else {
            result = b.countCannotExist(2000000);
        }


        assertEquals(Integer.parseInt(solution), result);
    }

    class SensorBeaconPair {
        int sx;
        int sy;
        int bx;
        int by;

        SensorBeaconPair(String input) {
            this.sx = Integer.parseInt(input.split("=")[1].substring(0, input.split("=")[1].indexOf(",")));
            this.sy = Integer.parseInt(input.split("=")[2].substring(0, input.split("=")[2].indexOf(":")));
            this.bx = Integer.parseInt(input.split("=")[3].substring(0, input.split("=")[3].indexOf(",")));
            this.by = Integer.parseInt(input.split("=")[4]);
        }

        public boolean checkSensor(int x, int y) {
            return sx == x && sy == y;
        }

        boolean checkBeacon(int x, int y) {
            return bx == x && by == y;
        }

        int manhattenDistance() {
            return abs(sx - bx) + abs(sy - by);
        }

        int minX() {
            return Math.min(Math.min(sx, bx), sx - manhattenDistance());
        }

        int minY() {
            return Math.min(Math.min(sy, by), sy - manhattenDistance());
        }

        int maxX() {
            return Math.max(Math.max(sx, bx), sx + manhattenDistance());
        }

        int maxY() {
            return Math.max(Math.max(sy, by), sy + manhattenDistance());
        }

        public boolean checkRange(int x, int y) {

                if (x == bx && y == by) {
                    return false;
                }
                return sx + manhattenDistance() >= x && sx - manhattenDistance() <= x && sy + manhattenDistance() >= y && sy - manhattenDistance() <= y && Math.abs(x - sx) + Math.abs(y - sy) <= manhattenDistance();
        }

        public boolean checkOuter(int x, int y) {
            if(sx == 8 && sy == 7) {

                if (x == bx && y == by) {
                    return false;
                }
                return sx - manhattenDistance()-1 <= x &&
                        sx + manhattenDistance()+1 >= x &&
                        sy + manhattenDistance()+1 >= y &&
                        sy - manhattenDistance()-1 <= y &&
                        (Math.abs(x - sx) + Math.abs(y - sy) == manhattenDistance()+1 ||
                                Math.abs(x - sx) + Math.abs(y - sy) == manhattenDistance()+2);
            }
            return false;
        }

        public Stream<Beacon> getOuterRing(int max) {
            var maxX = Math.max(0, Math.min(sx + manhattenDistance()+1,max));
            var maxY = Math.max(0, Math.min(sy + manhattenDistance()+1,max));

            return IntStream.range(0, maxX).mapToObj( x ->
                    IntStream.range(0, maxY).filter(y -> checkOuter(x, y)).mapToObj(y ->
                        new Beacon(x, y)
                    )
            ).flatMap(a -> a);
        }

        public List<Beacon> getCandidates(int max) {
            return List.of(new Beacon(sx+manhattenDistance()+1, sy),
                    new Beacon(sx-manhattenDistance()-1, sy),
                    new Beacon(sx, sy+manhattenDistance()+1),
                    new Beacon(sx, sy-manhattenDistance()-1));
        }

    }

    @ParameterizedTest
    @CsvSource(value = {"Sensor at x=2, y=18: closest beacon is at x=-2, y=15;2;18;-2;15"}, delimiter = ';')
    void test(String str, int sx, int sy, int bx, int by) {
        var sbp = new SensorBeaconPair(str);

        assertEquals(sx, sbp.sx);
        assertEquals(sy, sbp.sy);
        assertEquals(bx, sbp.bx);
        assertEquals(by, sbp.by);
    }

    class TunnelNetwork {
        private final int minX;
        private final int maxX;
        private final int minY;
        private final int maxY;


        private final List<SensorBeaconPair> sensorBeaconPairList;

        TunnelNetwork(List<SensorBeaconPair> sbp) {
            sensorBeaconPairList = sbp;

            minX = sbp.stream().mapToInt(SensorBeaconPair::minX).min().getAsInt();
            maxX = sbp.stream().mapToInt(SensorBeaconPair::maxX).max().getAsInt() + 1;
            minY = sbp.stream().mapToInt(SensorBeaconPair::minY).min().getAsInt();
            maxY = sbp.stream().mapToInt(SensorBeaconPair::maxY).max().getAsInt() + 1;
        }

        char[][] fillInSB() {
            char[][] map = new char[abs(minX) + abs(maxX)][abs(minY) + abs(maxY)];
            IntStream.range(0, map.length).forEach(x -> {
                IntStream.range(0, map[x].length).forEach(y -> {
                    if (sensorBeaconPairList.stream().anyMatch(s -> s.checkSensor(x + minX, y + minY))) {
                        map[x][y] = 'S';
                    } else if (sensorBeaconPairList.stream().anyMatch(s -> s.checkBeacon(x + minX, y + minY))) {
                        map[x][y] = 'B';
                    } else if (sensorBeaconPairList.stream().anyMatch(s -> s.checkRange(x + minX, y + minY))) {
                        map[x][y] = '#';
                    } else if (sensorBeaconPairList.stream().anyMatch(s -> s.checkOuter(x + minX, y + minY))) {
                        map[x][y] = 'O';
                    }
                });
            });
            return map;
        }

        public int countCannotExist(int y) {
            return IntStream.range(minX, maxX).parallel().map(x -> sensorBeaconPairList.stream().anyMatch(s -> s.checkRange(x, y)) ? 1 : 0).sum();
        }

        public Beacon findCoordinateNotCovered(int searchX, int searchY) {
           System.out.println("searchraum: "+sensorBeaconPairList.stream().parallel().flatMap(sb -> sb.getCandidates(searchX).stream()).distinct().count());

            return sensorBeaconPairList.stream().parallel().flatMap(sb -> sb.getCandidates(searchX).stream()).distinct()
                    .filter(b -> b.x > 0 && b.x < searchX && b.y > 0 && b.y < searchY)
                    .filter(b -> sensorBeaconPairList.stream().parallel().allMatch(s -> !s.checkRange(b.x, b.y) && !s.checkBeacon(b.x, b.y))).findAny().get();
        }

        void printMap(char[][] map) {
            IntStream.range(-2, map[0].length).forEach(mY -> {
                IntStream.range(0, map.length).forEach(mX -> {
                    if (mY == -1 || mY == -2) {
                        if (mX != 0 && mY == -1 && mX % 5 == 0) {
                            System.out.print(mX % 10 == 0 ? "0" : "5");
                        } else if (mX != 0 && mY == -2 && mX % 5 == 0) {
                            System.out.print("X");
                        } else {
                            System.out.print(" ");
                        }
                    } else if (map[mX][mY] == '\0') {
                        System.out.print(".");
                    } else {
                        System.out.print(map[mX][mY]);
                    }
                });
                System.out.printf("%4s%n", mY + minY);
            });
        }
    }

    record Beacon(int x, int y) {
    }

    @ParameterizedTest
    @AocFileSource(inputs = {@AocInputMapping(input = "test.txt", solution = "56000011"),
            @AocInputMapping(input = "input.txt", solution = "1")
    })
    void part2(Stream<String> input, String solution) {
        var inputList = input.map(SensorBeaconPair::new).toList();
        var b = new TunnelNetwork(inputList);
        var searchArea = 0;
        if (b.maxX < 100) {
            searchArea = 20;
        } else {
            searchArea = 4000000;
        }
        var result = b.findCoordinateNotCovered(searchArea, searchArea);

        assertEquals(Integer.parseInt(solution), result.x * 4000000 + result.y);
    }

}