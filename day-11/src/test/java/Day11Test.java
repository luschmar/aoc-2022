import org.junit.jupiter.params.ParameterizedTest;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {
	@ParameterizedTest
	@AocFileSource(inputs = { @AocInputMapping(input = "test.txt", solution = "10605"),
			@AocInputMapping(input = "input.txt", solution = "108240"), })
	void part1(Stream<String> input, String solution) {
		var inputList = input.toList();
		var monkeys = IntStream.range(0, inputList.size() / 7)
				.mapToObj(i -> new Monkey(inputList.subList(i * 7, i * 7 + 7))).toList();

		var horde = new MonkeyHorde(monkeys);

		for (int i = 0; i < 20; i++) {
			// System.out.println("---> "+i);
			horde.doRound(true, 3);
		}

		var sorted = monkeys.stream().sorted(Comparator.comparingLong(s -> s.inspect)).toList();
		var result = sorted.get(sorted.size() - 1).inspect * sorted.get(sorted.size() - 2).inspect;

		assertEquals(Integer.parseInt(solution), result);
	}

	record Result(int item, int targetMonkey) {
	}

	class MonkeyHorde {
		private List<Day11Test.Monkey> monkeys;

		public MonkeyHorde(List<Monkey> monkeys) {
			this.monkeys = monkeys;
		}

		void doRound(boolean doThree, long mod) {
			for (var m : monkeys) {
				var result = m.evaluate(doThree, mod);

				for (var r : result) {
					// System.out.println(r.item+" -> "+r.targetMonkey);
					var tOpt = monkeys.stream().filter(s -> s.name == r.targetMonkey).findFirst();
					if (tOpt.isPresent()) {
						tOpt.get().catchItem(r.item);
					} else {
						// System.out.println("cannot find monkey "+r.targetMonkey);
					}
				}
			}
		}
	}

	Pattern NAME_PATTERN = Pattern.compile("Monkey (\\d+)\\:");
	Pattern ITEM_PATTERN = Pattern.compile("  Starting items: ([\\d\\, ]+)");
	Pattern OPERATION_PATTERN = Pattern.compile("  Operation: new = (.+)");
	Pattern TEST_PATTERN = Pattern.compile("  Test: divisible by (\\d+)");
	Pattern TRUE_CONDITION = Pattern.compile("    If true: throw to monkey (\\d+)");
	Pattern FALSE_CONDITION = Pattern.compile("    If false: throw to monkey (\\d+)");

	class Monkey {
		private int name;
		private List<Integer> items;
		private String operation;
		private int test;
		private int trueTarget;
		private int falseTarget;
		private long inspect;

		public Monkey(List<String> input) {
			var nameMatcher = NAME_PATTERN.matcher(input.get(0));
			nameMatcher.matches();
			this.name = Integer.parseInt(nameMatcher.group(1));
			var itemMatcher = ITEM_PATTERN.matcher(input.get(1));
			itemMatcher.matches();
			this.items = new ArrayList<>(Arrays.asList(itemMatcher.group(1).split(",")).stream()
					.map(s -> Integer.parseInt(s.trim())).toList());
			var operationMatcher = OPERATION_PATTERN.matcher(input.get(2));
			operationMatcher.matches();
			this.operation = operationMatcher.group(1);
			var testMatcher = TEST_PATTERN.matcher(input.get(3));
			testMatcher.matches();
			this.test = Integer.parseInt(testMatcher.group(1));
			var trueTargetMatcher = TRUE_CONDITION.matcher(input.get(4));
			trueTargetMatcher.matches();
			this.trueTarget = Integer.parseInt(trueTargetMatcher.group(1));
			var falseTargetMatcher = FALSE_CONDITION.matcher(input.get(5));
			falseTargetMatcher.matches();
			this.falseTarget = Integer.parseInt(falseTargetMatcher.group(1));
		}

		void catchItem(int item) {
			items.add(item);
		}

		List<Result> evaluate(boolean withThree, long mod) {
			var results = items.stream().map(i -> evaluateItem(i, withThree, mod)).toList();
			items.clear();
			return results;
		}

		Result evaluateItem(int item, boolean withThree, long mod) {
			inspect++;

			var expression = new ExpressionBuilder(operation.replaceAll("old", Integer.toString(item))).build();

			int result = 0;
			if (withThree) {
				var resultLong = Math.floorDiv((long) expression.evaluate(), (long) mod);
				result = (int) resultLong;
			} else {
				var resultLong = Math.floorMod((long) expression.evaluate(), (long) mod);
				result = (int) resultLong;
			}

			var targetMonkey = 0;
			if (result % test == 0) {
				targetMonkey = trueTarget;
			} else {
				targetMonkey = falseTarget;
			}

			return new Result(result, targetMonkey);

		}

	}

	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "2713310158"),
			@AocInputMapping(input = "input.txt", solution = "25712998901")
			})
	void part2(Stream<String> input, String solution) {
		var inputList = input.toList();
		List<Monkey> monkeys = IntStream.range(0, inputList.size() / 7)
				.mapToObj(i -> new Monkey(inputList.subList(i * 7, i * 7 + 7))).toList();
		var horde = new MonkeyHorde(monkeys);

		for (int i = 0; i < 10000; i++) {
			horde.doRound(false, monkeys.stream().map(s -> s.test).reduce(1, (a, b) -> a * b));
		}

		var sorted = monkeys.stream().sorted(Comparator.comparingLong(s -> s.inspect)).toList();
		var result = (long) sorted.get(sorted.size() - 1).inspect * (long) sorted.get(sorted.size() - 2).inspect;

		System.out.println(
				(long) sorted.get(sorted.size() - 1).inspect + " * " + (long) sorted.get(sorted.size() - 2).inspect);
		assertEquals(Long.parseLong(solution), result);
	}

}