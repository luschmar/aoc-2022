import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {
	@ParameterizedTest
	@AocFileSource(inputs = { @AocInputMapping(input = "test.txt", solution = "13"),
	 @AocInputMapping(input = "input.txt", solution = "6209")
	})
	void part1(Stream<String> input, String solution) {
		var inputList = input.toList();

		var r = new Rope(2);
		var set = new HashSet<String>();
		for (var s : inputList) {
			var split = s.split(" ");
			var dir = Dir.valueOf(split[0]);
			var steps = Integer.parseInt(split[1]);
			System.out.println(s);

			for (int i = 0; i < steps; i++) {
				r.move(dir, set);
			}

		}

		assertEquals(Integer.parseInt(solution), set.size());
	}

	class Rope {
		int[] x;
		int[] y;
		int lenght;

		public Rope(int l) {
			lenght = l;
			x = new int[lenght];
			y = new int[lenght];
		}

		void move(Dir dir, Set<String> visitedCoordinates) {
			switch (dir) {
			case U -> {
				x[0]++;
				moveTail();
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}
			case D -> {
				x[0]--;
				moveTail();
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}
			case L -> {
				y[0]--;
				moveTail();
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}
			case R -> {
				y[0]++;
				moveTail();
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}

			default -> throw new IllegalArgumentException("Unexpected value: " + dir);
			}
			printState();
		}

		private void moveTail() {
			System.out.println();
			
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}
			for (int i = lenght-1; i > 0; i--) {
				// do nothing
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
			}

			for (int i = lenght-1; i > 0; i--) {
				if (!isInRange(x[i-1], y[i-1], x[i], y[i])) {
					tryFollowDirection(i-1);
				}
				// do nothing
			}
			
			
		}
		
		private void printState() {
			var minX = Arrays.stream(x).min().getAsInt();
			var maxX = Arrays.stream(x).max().getAsInt();
			var minY = Arrays.stream(y).min().getAsInt();
			var maxY = Arrays.stream(y).max().getAsInt();
			
			
			IntStream.range(-20, 20).forEach(i -> {
				IntStream.range(-20, 20).forEach(j -> {
					var inv = i *-1;
					if(inv == 0 && j == 0) {
						System.out.print("s");
						return;
					}
					if(IntStream.range(0, lenght).anyMatch(a -> x[a] == inv &&  y[a] == j )) {
						var p = IntStream.range(0, lenght).filter(a -> x[a] == inv &&  y[a] == j ).findFirst().getAsInt();
						System.out.print(""+p);
					}else {
						System.out.print(".");
					}
				});
				System.out.println();
			});;
			System.out.println();
		}

		private void tryFollowDirection(int index) {
			var north = new Candidate(x[index]+1, y[index], calculateDistance(x[index]+1, y[index], x[index+1], y[index+1]));
			var south = new Candidate(x[index]-1, y[index], calculateDistance(x[index]-1, y[index], x[index+1], y[index+1]));
			var west = new Candidate(x[index], y[index]-1, calculateDistance(x[index], y[index]-1, x[index+1], y[index+1]));
			var east = new Candidate(x[index], y[index]+1, calculateDistance(x[index], y[index]+1, x[index+1], y[index+1]));

			var pos = Stream.of(north, south, west, east).min(Comparator.comparing(Candidate::distance)).get();
			x[index+1] = pos.x;
			y[index+1] = pos.y;
		}

		private double calculateDistance(int x1, int y1, int x2, int y2) {
			 double ac = Math.abs(y2 - y1);
			    double cb = Math.abs(x2 - x1);
			    return Math.hypot(ac, cb);
		}

		private void tryFollowDirectionBuggy(Day09Test.Dir dir, int i) {
			System.out.println(i);
			switch (dir) {
			case U -> {
				if (y[i + 1] == y[i]) {
					System.out.println("move up "+(i+1));
					x[i + 1]++;
				} else if (isInRange(x[i], y[i], x[i + 1] + 1, y[i + 1] + 1)) {
					System.out.println("move up/right "+(i+1));
					x[i + 1]++;
					y[i + 1]++;
				} else if (isInRange(x[i], y[i], x[i + 1] + 1, y[i + 1] - 1)) {
					System.out.println("move up/left "+(i+1));
					x[i + 1]++;
					y[i + 1]--;
				}
			}
			case D -> {
				if (y[i + 1] == y[i]) {
					System.out.println("move down "+(i+1));
					x[i + 1]--;
				} else if (isInRange(x[i], y[i], x[i + 1] - 1, y[i + 1] - 1)) {
					System.out.println("move down/left "+(i+1));
					x[i + 1]--;
					y[i + 1]--;
				} else if (isInRange(x[i], y[i], x[i + 1] - 1, y[i + 1] + 1)) {
					System.out.println("move down/right "+(i+1));
					x[i + 1]--;
					y[i + 1]++;
				}
			}
			case L -> {
				if (x[i + 1] == x[i]) {
					System.out.println("move left "+(i+1));
					y[i + 1]--;
				} else if (isInRange(x[i], y[i], x[i + 1] - 1, y[i + 1] - 1)) {
					System.out.println("move left/down "+(i+1));
					x[i + 1]--;
					y[i + 1]--;
				} else if (isInRange(x[i], y[i], x[i + 1] + 1, y[i + 1] - 1)) {
					System.out.println("move left/up "+(i+1));
					y[i + 1]--;
					x[i + 1]++;
				}

			}
			case R -> {
				if (x[i + 1] == x[i]) {
					System.out.println("move right "+(i+1));
					y[i + 1]++;
					return;
				} else if (isInRange(x[i], y[i], x[i + 1] + 1, y[i + 1] + 1)) {
					System.out.println("move right/up "+(i+1));
					x[i + 1]++;
					y[i + 1]++;
					return;
				} else if (isInRange(x[i], y[i], x[i + 1] - 1, y[i + 1] + 1)) {
					System.out.println("move right/down "+(i+1));
					y[i + 1]++;
					x[i + 1]--;
				}
			}

			default -> throw new IllegalArgumentException("Unexpected value: " + dir);
			}			
		}
	}

	enum Dir {
		U, D, L, R
	}
	
	record Candidate(int x, int y, double distance) {}

	private boolean isInRange(int x1, int y1, int x2, int y2) {
		// Check do nothing
		var range = IntStream.rangeClosed(-1,  1).anyMatch(i -> {
			return IntStream.rangeClosed(-1, 1).anyMatch(j -> {
				if (x1 == x2 + i && y1 == y2 + j) {
					return true;
				}
				return false;
			});
		});
		
		if(range) {
			System.out.println(x1+":"+y1+" in range "+x2+":"+y2);

			return true;
		}
		System.out.println(x1+":"+y1+" not in range "+x2+":"+y2);
		return false;
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			 @AocInputMapping(input = "test.txt", solution = "1"),
			@AocInputMapping(input = "test2.txt", solution = "36"),
	@AocInputMapping(input = "input.txt", solution = "1")
	})
	void part2(Stream<String> input, String solution) {
		var inputList = input.toList();

		var r = new Rope(10);
		var set = new HashSet<String>();
		for (var s : inputList) {
			var split = s.split(" ");

			var dir = Dir.valueOf(split[0]);
			var steps = Integer.parseInt(split[1]);
			System.out.println(s);

			for (int i = 0; i < steps; i++) {
				r.move(dir, set);
			}
			r.printState();
		}

		assertEquals(Integer.parseInt(solution), set.size());
	}

}