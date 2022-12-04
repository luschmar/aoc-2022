import org.junit.jupiter.params.ParameterizedTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "15"),
			@AocInputMapping(input = "input.txt", solution = "12586")
	})
	void part1(Stream<String> input, String solution) {
		var games = input.toList();
		var win = Map.of("A", "C", "B", "A", "C", "B");
		var results = new ArrayList<Integer>();

		for (var g : games) {
			var line = g;
			// Substitute
			line = line.replace("X", "A");
			line = line.replace("Y", "B");
			line = line.replace("Z", "C");

			var elf = line.charAt(0);
			var me = line.charAt(2);

			// Draw
			if (me == elf) {
				results.add(3 + handValue(me));
				continue;
			}

			// Win
			if(win.get(Character.toString(me)).charAt(0) == elf) {
				results.add(6 + handValue(me));
				continue;
			}		
			results.add(handValue(me));
		}

		var result = results.stream().mapToInt(a -> a.intValue()).sum();
		assertEquals(Integer.parseInt(solution), result);
	}

	private int handValue(char hand) {
		if (hand == 'A') {
			return 1;
		} else if (hand == 'B') {
			return 2;
		}
			return 3;
	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "12"),
			@AocInputMapping(input = "input.txt", solution = "13193")
			})
	void part2(Stream<String> input, String solution) {
		var games = input.toList();		
		var win = Map.of("A", "C", "B", "A", "C", "B");
		var loose = Map.of("A", "B", "B", "C", "C", "A");

		var results = new ArrayList<Integer>();

		for (var g : games) {
			var line = g;
			var elf = line.charAt(0);

			// Draw
			if (line.contains("Y")) {
				results.add(3 + handValue(elf));
				continue;
			}

			// Win
			if(line.contains("Z")) {
				var me = loose.get(Character.toString(elf));
				results.add(6 + handValue(me.charAt(0)));
				continue;
			}		
			var me = win.get(Character.toString(elf));
			results.add(handValue(me.charAt(0)));
		}

		var result = results.stream().mapToInt(a -> a.intValue()).sum();
		assertEquals(Integer.parseInt(solution), result);
	}
}