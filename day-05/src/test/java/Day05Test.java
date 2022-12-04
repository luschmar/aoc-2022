import org.junit.jupiter.params.ParameterizedTest;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {

	Pattern instructionPattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
	Pattern countCols = Pattern.compile("(\\d+)");

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "CMZ"),
			@AocInputMapping(input = "input.txt", solution = "FWNSHLDNZ"),
			// @AocInputMapping(input = "aoc_2022_day05_large_input.txt", solution = "GATHERING") // StringBuilder: 11.060s - Deque: 272.980s
	})
	void part1(Stream<String> input, String solution) {
		var inputList = input.toList();
		var stackStrList = new ArrayList<String>();
		var instructionList = new ArrayList<Instruction>();
		String stackNameList = null;
		// # find stackpos
		for (var s : inputList) {
			if (s.contains("[")) {
				stackStrList.add(s);
				continue;
			}
			if (s.contains("move")) {
				instructionList.add(convertToInstruction(s));
				continue;
			}
			if (s.length() > 0) {
				stackNameList = s;
			}
		}
		var colMatcher = countCols.matcher(stackNameList);
		var colCount = 0;
		while (colMatcher.find()) {
			colCount++;
		}

		var stackList = new ArrayList<Deque<String>>(colCount);
		for (int i = 0; i < colCount; i++) {
			stackList.add(new ArrayDeque<>());
		}
		for (var s : stackStrList) {
			for (int i = 0; i < colCount; i++) {
				var j = (i * 4) + 1;
				if (s.length() < j) {
					continue;
				}

				var c = s.charAt(j);
				if (c == ' ') {
					continue;
				}
				stackList.get(i).addLast(Character.toString(c));
			}
		}

		var stackString = new ArrayList<StringBuilder>();
		stackString.addAll(
				stackList.stream().map(d -> new StringBuilder(d.stream().collect(Collectors.joining()))).toList());
		for (var inst : instructionList) {
			inst.processString9000(stackString);
		}
		var result = stackString.stream().map(sb -> sb.substring(0, 1)).collect(Collectors.joining());

		/**
		 * for(var inst : instructionList) { inst.process9000(stackList); } var result
		 * =stackList.stream().map(Deque::getFirst).collect(Collectors.joining());
		 **/

		assertEquals(solution, result);
	}

	Instruction convertToInstruction(String s) {
		var matcher = instructionPattern.matcher(s);
		if (matcher.matches()) {
			return new Instruction(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3)));
		}
		throw new IllegalArgumentException("Illegal");
	}

	static class Instruction {
		private final int count;
		private final int source;
		private final int target;

		Instruction(int count, int source, int target) {
			this.count = count;
			this.source = source;
			this.target = target;
		}

		public void processString9000(List<StringBuilder> stackList) {
			var crate = new StringBuilder(stackList.get(source - 1).substring(0, count));
			crate = crate.reverse();
			stackList.set(source - 1, new StringBuilder(stackList.get(source - 1).substring(count)));
			stackList.get(target - 1).insert(0, crate);
		}

		public void processString9001(List<StringBuilder> stackList) {
			var crate = new StringBuilder(stackList.get(source - 1).substring(0, count));
			stackList.set(source - 1, new StringBuilder(stackList.get(source - 1).substring(count)));
			stackList.get(target - 1).insert(0, crate);
		}

		void process9000(List<Deque<String>> stacks) {
			for (int i = 0; i < count; i++) {
				var crate = stacks.get(source - 1).removeFirst();
				stacks.get(target - 1).addFirst(crate);
			}
		}

		void process9001(List<Deque<String>> stacks) {
			var crates = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				var crate = stacks.get(source - 1).removeFirst();
				crates.add(crate);
			}
			Collections.reverse(crates);
			for (var c : crates) {
				stacks.get(target - 1).addFirst(c);
			}
		}

	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "MCD"),
			@AocInputMapping(input = "input.txt", solution = "RNRGDNFQG"),
			// @AocInputMapping(input = "aoc_2022_day05_large_input.txt", solution = "DEVSCHUUR") // StringBuilder: 8.750s Deque: 288.256s
	})
	void part2(Stream<String> input, String solution) {
		var inputList = input.toList();
		var stackStrList = new ArrayList<String>();
		var instructionList = new ArrayList<Instruction>();
		String stackNameList = null;
		// # find stackpos
		for (var s : inputList) {
			if (s.contains("[")) {
				stackStrList.add(s);
				continue;
			}
			if (s.contains("move")) {
				instructionList.add(convertToInstruction(s));
				continue;
			}
			if (s.length() > 0) {
				stackNameList = s;
			}
		}
		var colMatcher = countCols.matcher(stackNameList);
		var colCount = 0;
		while (colMatcher.find()) {
			colCount++;
		}

		var stackList = new ArrayList<Deque<String>>(colCount);
		for (int i = 0; i < colCount; i++) {
			stackList.add(new ArrayDeque<>());
		}
		for (var s : stackStrList) {
			for (int i = 0; i < colCount; i++) {
				var j = (i * 4) + 1;
				if (s.length() < j) {
					continue;
				}
				var c = s.charAt(j);
				if (c == ' ') {
					continue;
				}
				stackList.get(i).addLast(Character.toString(c));
			}
		}

		var stackString = new ArrayList<StringBuilder>();
		stackString.addAll(
				stackList.stream().map(d -> new StringBuilder(d.stream().collect(Collectors.joining()))).toList());
		for (var inst : instructionList) {
			inst.processString9001(stackString);
		}
		/**
		 * for(var inst : instructionList) { inst.process9001(stackList); }
		 **/
		var result = stackString.stream().map(sb -> sb.substring(0, 1)).collect(Collectors.joining());

		// var result
		// =stackList.stream().map(Deque::getFirst).collect(Collectors.joining());
		assertEquals(solution, result);
	}

}