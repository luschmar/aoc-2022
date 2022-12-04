import org.junit.jupiter.params.ParameterizedTest;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
				moveTail(Dir.U);
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}
			case D -> {
				x[0]--;
				moveTail(Dir.D);
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}
			case L -> {
				y[0]--;
				moveTail(Dir.L);
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}
			case R -> {
				y[0]++;
				moveTail(Dir.R);
				visitedCoordinates.add(x[lenght - 1] + ":" + y[lenght - 1]);
			}

			default -> throw new IllegalArgumentException("Unexpected value: " + dir);
			}
		}

		private void moveTail(Dir dir) {
			for (int i = 0; i < (lenght-1); i++) {
				// do nothing
				if (!isInRange(x[i], y[i], x[i + 1], y[i + 1])) {
					tryFollowDirection(dir, i);
				}
			}
		}
		
		private void tryFollowDirectionWtf(Day09Test.Dir dir, int index) {
			IntStream.range(-1,  2).forEach(i -> {
				IntStream.range(-1, 2).forEach(j -> {
					// can dock north?
					if (x[index]+1 == x[index+1]+i && y[index] == y[index+1]+j) {
						x[index+1] = x[index]+i;
						y[index+1] = y[index+1]+j;
					}
					// can dock south?
					if (x[index]-1 == x[index+1]+i && y[index] == y[index+1]+j) {
						x[index+1] = x[index]-1;
						y[index+1] = y[index+1]+j;
					}
					// can dock east?
					if (x[index] == x[index+1]+i && y[index]+1 == y[index+1]+j) {
						x[index+1] = x[index+1]+i;
						y[index+1] = y[index]+1;
					}
					// can dock west?
					if (x[index] == x[index+1]+i && y[index]-1 == y[index+1]+j) {
						x[index+1] = x[index+1]+i;
						y[index+1] = y[index]-1;
					}
				});
			});
		}

		private void tryFollowDirection(Day09Test.Dir dir, int i) {
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

	private boolean isInRange(int x1, int y1, int x2, int y2) {
		// Check do nothing
		return IntStream.range(-1,  2).anyMatch(i -> {
			return IntStream.range(-1, 2).anyMatch(j -> {
				if (x1 == x2 + i && y1 == y2 + j) {
					return true;
				}
				return false;
			});
		});
	}

	@ParameterizedTest
	@AocFileSource(inputs = {
			 @AocInputMapping(input = "test.txt", solution = "1"),
			@AocInputMapping(input = "test2.txt", solution = "36"),
	// @AocInputMapping(input = "input.txt", solution = "1")
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

		}

		assertEquals(Integer.parseInt(solution), set.size());
	}

}