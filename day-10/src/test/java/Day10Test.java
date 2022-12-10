import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {
	@ParameterizedTest
	@AocFileSource(inputs = { @AocInputMapping(input = "test.txt", solution = "13140"),
			@AocInputMapping(input = "input.txt", solution = "14320") })
	void part1(Stream<String> input, String solution) {
		var inputList = input.toList();
		// cycle of interest
		var coi = IntStream.range(0, 6).map(x -> ((x * 40) + 20)).boxed().toList();
		var strengths = new ArrayList<Long>();
		var crt = new StringBuilder();

		var X = 1;
		var cycle = 0;
		for (var instruction : inputList) {
			if ("noop".equals(instruction)) {
				cycle++;
				checkIsOfInterest(cycle, coi, X, strengths, crt);
			} else {
				System.out.println(instruction);
				cycle++;
				checkIsOfInterest(cycle, coi, X, strengths, crt);
				cycle++;
				checkIsOfInterest(cycle, coi, X, strengths, crt);
				X += Integer.parseInt(instruction.split(" ")[1]);
			}
		}

		/**
		 * 
		 * var sums = IntStream.range(0, 6).map(x -> ((x*40)+20)).mapToLong(i ->
		 * IntStream.range(0, i).map(a -> inputList[a]).sum() ).toArray();
		 * 
		 * var result = IntStream.range(0, 6).map(x -> ((x*40)+20)/2).mapToLong(i ->
		 * IntStream.range(0, i).map(a -> inputList[a]).sum() ).reduce(1, (a, b) -> a *
		 * b);
		 **/
		var res = strengths.stream().mapToLong(l -> l).sum();
		assertEquals(Integer.parseInt(solution), res);
	}

	private void checkIsOfInterest(int cycle, List<Integer> coi, int x, ArrayList<Long> strengths, StringBuilder crt) {
		var sprite = createSprite(x);
		var subSprite = sprite.substring(0, cycle%40);
		if(subSprite.endsWith("#")) {
			crt.append("#");
		}
		else {
			crt.append(".");
		}
		
		
		if (coi.contains(cycle)) {
			System.out.println(cycle + ":" + x + " -> " + (long) cycle * x);
			strengths.add((long) cycle * x);
		}
		if(cycle % 40 == 0) {
			crt.append("\n");
		}
	}

	private String createSprite(int x) {
		var before = IntStream.range(0, x-1).mapToObj(a -> ".").collect(Collectors.joining());
		var after = IntStream.range(x, 38).mapToObj(a -> ".").collect(Collectors.joining());
		
		return before+"###"+after;
	}

	int toInt(String s) {
		if ("noop".equals(s)) {
			return 0;
		}
		return Integer.parseInt(s.split(" ")[1]);
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = """
			##..##..##..##..##..##..##..##..##..##..
			###...###...###...###...###...###...###.
			####....####....####....####....####....
			#####.....#####.....#####.....#####.....
			######......######......######......####
			#######.......#######.......#######....."""), 
			@AocInputMapping(input = "input.txt", solution = """
			###...##..###..###..#..#..##..###....##.
			#..#.#..#.#..#.#..#.#.#..#..#.#..#....#.
			##.#.#....#..#.###..##...#..#.#..#....#.
			###..#....###..#..#.#.#..####.###.....#.
			##...#..#.#....#..#.#.#..#..#.#....#..#.
			#.....##..#....###..#..#.#..#.#.....##..
			""") })
	void part2(Stream<String> input, String solution) {
		var inputList = input.toList();
		// cycle of interest
		var coi = IntStream.range(0, 6).map(x -> ((x * 40) + 20)).boxed().toList();
		var strengths = new ArrayList<Long>();
		var crt = new StringBuilder();

		var X = 1;
		var cycle = 0;
		for (var instruction : inputList) {
			if ("noop".equals(instruction)) {
				cycle++;
				checkIsOfInterest(cycle, coi, X, strengths, crt);
			} else {
				System.out.println(instruction);
				cycle++;
				checkIsOfInterest(cycle, coi, X, strengths, crt);
				cycle++;
				checkIsOfInterest(cycle, coi, X, strengths, crt);
				X += Integer.parseInt(instruction.split(" ")[1]);
			}
		}

		assertEquals(solution, crt.toString());
	}

}