import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "1"),
			@AocInputMapping(input = "input.txt", solution = "1") 
			})
	void part1(Stream<String> input, String solution) {
		var inputList = input.toList();

		assertEquals(Integer.parseInt(solution), inputList.size());
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "1"),
			@AocInputMapping(input = "input.txt", solution = "1") 
			})
	void part2(Stream<String> input, String solution) {
		var inputList = input.toList();

		assertEquals(Integer.parseInt(solution), inputList.size());
	}

}