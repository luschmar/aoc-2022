import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "24"),
			@AocInputMapping(input = "input.txt", solution = "964")
			})
	void part1(Stream<String> input, String solution) {
		var stoneLineList = input.map(s -> s.split(" -> ")).flatMap(seg -> IntStream.range(1, seg.length)
				.mapToObj(i -> new StoneLine(
						Integer.parseInt(seg[i - 1 ].split(",")[0]),
						Integer.parseInt(seg[i - 1 ].split(",")[1]),
						Integer.parseInt(seg[i].split(",")[0]),
						Integer.parseInt(seg[i].split(",")[1])
						))).toList();

		var cave = new ButtomlessCave(stoneLineList);
		var sand = cave.simulateSand();
		//cave.printMatrix();

		assertEquals(Integer.parseInt(solution), sand);
	}

	class ButtomlessCave {
		private final char[][] caveMatrix;

		public ButtomlessCave(List<StoneLine> stoneLineList) {
			caveMatrix = new char[maxX(stoneLineList)+2][maxY(stoneLineList)+2];

			fillStone(stoneLineList);
			//printMatrix();
		}

		int simulateSand() {
			var sandCorns = new ArrayDeque<SandCorn>();
			simulate(sandCorns);

			// because ... whatever...
			return sandCorns.size()-1;
		}

		private void simulate(ArrayDeque<SandCorn> sandCorns) {
			while(takeNext(sandCorns)) {
				var s = sandCorns.peek();
				while (move(s)) {
				}
				//printMatrix();
			}
		}

		private boolean takeNext(ArrayDeque<SandCorn> sandCorns) {
			var s = sandCorns.peek();
			if(s != null && caveMatrix[s.x][s.y+1] == 'A') {
				return false;
			}
			sandCorns.push(new SandCorn(500, 0));
			return true;
		}

		private boolean move(SandCorn s) {
			// fall down
			if(caveMatrix[s.x][s.y+1] == '.') {
				s.y++;
				return true;
			}
			// try move right
			if(caveMatrix[s.x-1][s.y+1] == '.') {
				s.y++;
				s.x--;
				return true;
			}
			// try move right
			if(caveMatrix[s.x+1][s.y+1] == '.') {
				s.y++;
				s.x++;
				return true;
			}
			caveMatrix[s.x][s.y] = 'o';
			return false;
		}


		private void printMatrix() {
			IntStream.range(0, caveMatrix.length).forEach(
					i -> {
						System.out.print(String.format("%4s", i));
						IntStream.range(0, caveMatrix[i].length).forEach(
								j -> {
									System.out.print(caveMatrix[i][j]);
								}
						);
						System.out.println();
					}
			);
		}

		private void fillStone(List<StoneLine> stoneLineList) {
			IntStream.range(0, caveMatrix.length).forEach(
					i -> {
						IntStream.range(0, caveMatrix[i].length).forEach(
								j -> {
									caveMatrix[i][j] = '.';
									if (stoneLineList.stream().anyMatch(a -> isPointOnLine(i, j, a))) {
										caveMatrix[i][j] = '#';
									}
								}
						);
					}
			);
			// fill abbys
			IntStream.range(0, caveMatrix.length).forEach(
					i -> {
						caveMatrix[i][caveMatrix[0].length - 1] = 'A';
					}
			);
		}

		private boolean isPointOnLine(int x, int y, StoneLine a) {
			if(a.x1 == a.x2 && x == a.x1 && y >= a.y1 && y <= a.y2) {
				return true;
			}
			if(a.y1 == a.y2 && y == a.y1 && x <= a.x1 && x >= a.x2) {
				return true;
			}

			return false;
		}
	}

	class SandCorn {
		int x;
		int y;

		SandCorn(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	static int maxX(List<StoneLine> stoneLineList) {
		return stoneLineList.stream().flatMap(a -> Stream.of(a.x1, a.x2)).mapToInt(Integer::intValue).max().getAsInt();
	}

	static int maxY(List<StoneLine> stoneLineList) {
		return stoneLineList.stream().flatMap(a -> Stream.of(a.y1, a.y2)).mapToInt(Integer::intValue).max().getAsInt();
	}

	class StoneLine {
		int x1;
		int x2;
		int y1;
		int y2;

		StoneLine(int x1, int y1, int x2, int y2) {
			this.x1 = Math.max(x1, x2);
			this.x2 = Math.min(x1, x2);
			this.y1 = Math.min(y1, y2);
			this.y2 = Math.max(y1, y2);
		}
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "93"),
			@AocInputMapping(input = "input.txt", solution = "32041")
			})
	void part2(Stream<String> input, String solution) {
		var stoneLineList = input.map(s -> s.split(" -> ")).flatMap(seg -> IntStream.range(1, seg.length)
				.mapToObj(i -> new StoneLine(
						Integer.parseInt(seg[i - 1 ].split(",")[0]),
						Integer.parseInt(seg[i - 1 ].split(",")[1]),
						Integer.parseInt(seg[i].split(",")[0]),
						Integer.parseInt(seg[i].split(",")[1])
				))).toList();

		var cave = new FlooredCave(stoneLineList);
		var sand = cave.simulateSand();
		//cave.printMatrix();

		assertEquals(Integer.parseInt(solution), sand);
	}



	class FlooredCave {
		private final char[][] caveMatrix;

		public FlooredCave(List<StoneLine> stoneLineList) {
			caveMatrix = new char[maxX(stoneLineList)+maxY(stoneLineList)][maxY(stoneLineList)+3];

			fillStone(stoneLineList);
			//printMatrix();
		}

		int simulateSand() {
			var sandCorns = new ArrayDeque<SandCorn>();
			simulate(sandCorns);

			// because ... whatever...
			return sandCorns.size();
		}

		private void simulate(ArrayDeque<SandCorn> sandCorns) {
			while(takeNext(sandCorns)) {
				var s = sandCorns.peek();
				while (move(s)) {
				}
				//printMatrix();
			}
		}

		private boolean takeNext(ArrayDeque<SandCorn> sandCorns) {
			var s = sandCorns.peek();
			if(s != null && s.x == 500 && s.y == 0) {
				return false;
			}
			sandCorns.push(new SandCorn(500, 0));
			return true;
		}

		private boolean move(SandCorn s) {
			// fall down
			if(caveMatrix[s.x][s.y+1] == '.') {
				s.y++;
				return true;
			}
			// try move right
			if(caveMatrix[s.x-1][s.y+1] == '.') {
				s.y++;
				s.x--;
				return true;
			}
			// try move right
			if(caveMatrix[s.x+1][s.y+1] == '.') {
				s.y++;
				s.x++;
				return true;
			}
			caveMatrix[s.x][s.y] = 'o';
			return false;
		}


		private void printMatrix() {
			IntStream.range(0, caveMatrix.length).forEach(
					i -> {
						System.out.print(String.format("%4s", i));
						IntStream.range(0, caveMatrix[i].length).forEach(
								j -> {
									System.out.print(caveMatrix[i][j]);
								}
						);
						System.out.println();
					}
			);
		}

		private void fillStone(List<StoneLine> stoneLineList) {
			IntStream.range(0, caveMatrix.length).forEach(
					i -> {
						IntStream.range(0, caveMatrix[i].length).forEach(
								j -> {
									caveMatrix[i][j] = '.';
									if (stoneLineList.stream().anyMatch(a -> isPointOnLine(i, j, a))) {
										caveMatrix[i][j] = '#';
									}
								}
						);
					}
			);
			// fill abbys
			IntStream.range(0, caveMatrix.length).forEach(
					i -> {
						caveMatrix[i][caveMatrix[0].length - 1] = '#';
					}
			);
		}

		private boolean isPointOnLine(int x, int y, StoneLine a) {
			if(a.x1 == a.x2 && x == a.x1 && y >= a.y1 && y <= a.y2) {
				return true;
			}
			if(a.y1 == a.y2 && y == a.y1 && x <= a.x1 && x >= a.x2) {
				return true;
			}

			return false;
		}
	}
}