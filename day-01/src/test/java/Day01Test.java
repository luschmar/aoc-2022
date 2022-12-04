import org.junit.jupiter.params.ParameterizedTest;
import org.junit.platform.commons.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "24000"),
			@AocInputMapping(input = "input.txt", solution = "69177") 
			})
	void part1(Stream<String> input, String solution) {
		var sums = new ArrayList<Integer>();
		var a = input.toList();
		int aSum = 0;
		for (int i = 0; i < a.size(); i++) {
			if (StringUtils.isBlank(a.get(i))) {
				sums.add(aSum);
				aSum = 0;
			} else {
				aSum += Integer.parseInt(a.get(i));
			}
		}
		var max = sums.stream().max(Comparator.naturalOrder()).get();

		assertEquals(Integer.parseInt(solution), max);
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "45000"),
			@AocInputMapping(input = "input.txt", solution = "207456") 
			})
	void part2(Stream<String> input, String solution) {
		var sums = new ArrayList<Integer>();
		var a = input.toList();
		int aSum = 0;
		for (int i = 0; i < a.size(); i++) {
			if (StringUtils.isBlank(a.get(i))) {
				sums.add(aSum);
				aSum = 0;
			} else {
				aSum += Integer.parseInt(a.get(i));
			}
		}
		sums.add(aSum);
		var max1 = sums.stream().max(Comparator.naturalOrder()).get();
		sums.remove(max1);
		var max2 = sums.stream().max(Comparator.naturalOrder()).get();
		sums.remove(max2);
		var max3 = sums.stream().max(Comparator.naturalOrder()).get();

		var result = max1 + max2 + max3;

		assertEquals(Integer.parseInt(solution), result);
	}
}