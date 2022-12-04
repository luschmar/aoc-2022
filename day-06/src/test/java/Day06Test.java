import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "input.txt", solution = "1892"),
            @AocInputMapping(input = "test.txt", solution = "7"),
            @AocInputMapping(input = "test2.txt", solution = "5"),
            @AocInputMapping(input = "test3.txt", solution = "6"),
            @AocInputMapping(input = "test4.txt", solution = "10"),
            @AocInputMapping(input = "test5.txt", solution = "11"),
            // @AocInputMapping(input = "aoc_2022_day06_extra_input.txt", solution = "299726"), // 0.85s
    })
    void part1(Stream<String> input, String solution) {
        var str = input.toList().get(0);

        var result = IntStream.range(0, str.length() - 4).parallel()
				.mapToObj(i -> new Slice(str.substring(i, i + 4), i + 4))
				.filter(Slice::hasNoDuplicates)
				.mapToInt(s -> s.index).min()
                .orElseThrow(() -> new IllegalArgumentException("this handheld device is broken"));

        assertEquals(Integer.parseInt(solution), result);
    }

   

    static class Slice {
        private final String slice;
        private final int index;

        Slice(String slice, int index) {
            this.slice = slice;
            this.index = index;
        }

        boolean hasNoDuplicates() {
        	// optimized with varienaja
        	return slice.chars().distinct().count() == slice.length();
			/*
			 * Unoptimized code:
			 * return slice.chars().mapToObj(Character::toString)
			 * .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
			 * .entrySet().stream() .noneMatch(b -> b.getValue() > 1);
			 */
        }
    }

    @ParameterizedTest
    @AocFileSource(inputs = {
            @AocInputMapping(input = "input.txt", solution = "2313"),
            @AocInputMapping(input = "test.txt", solution = "19"),
            @AocInputMapping(input = "test2.txt", solution = "23"),
            @AocInputMapping(input = "test3.txt", solution = "23"),
            @AocInputMapping(input = "test4.txt", solution = "29"),
            @AocInputMapping(input = "test5.txt", solution = "26"),
            // @AocInputMapping(input = "aoc_2022_day06_extra_input.txt", solution = "1611788"), // 1.75s
    })
    void part2(Stream<String> input, String solution) {
        var str = input.toList().get(0);
        var result = IntStream.range(0, str.length() - 14).parallel()
				.mapToObj(i -> new Slice(str.substring(i, i + 14), i + 14))
				.filter(Slice::hasNoDuplicates)
				.mapToInt(s -> s.index).min()
                .orElseThrow(() -> new IllegalArgumentException("this handheld device is broken"));

        

        assertEquals(Integer.parseInt(solution), result);
    }
    
    /**
     * @see <a href="https://gathering.tweakers.net/forum/list_message/73708198#73708198">tweakers.net</a>
     */
    @Disabled
    @ParameterizedTest
    @AocFileSource(inputs = {
             @AocInputMapping(input = "aoc_2022_day06_extra_input.txt", solution = "9876543"), // 5.471s
    })
    void vraag_1(Stream<String> input, String solution) {
        var str = input.toList().get(0);
        var result = IntStream.range(0, str.length() - 94).parallel()
				.mapToObj(i -> new Slice(str.substring(i, i + 94), i + 94))
				.filter(Slice::hasNoDuplicates)
				.mapToInt(s -> s.index).min()
                .orElseThrow(() -> new IllegalArgumentException("this handheld device is broken"));

        

        assertEquals(Integer.parseInt(solution), result);
    }
    
    /**
     * @see <a href="https://gathering.tweakers.net/forum/list_message/73708198#73708198">tweakers.net</a>
     */
    @Disabled
    @ParameterizedTest
    @AocFileSource(inputs = {
             @AocInputMapping(input = "aoc_2022_day06_extra_input.txt", solution = "506020000"), // 281.787s
    })
    void vraag_2(Stream<String> input, String solution) {
        var str = input.toList().get(0);
        var result = IntStream.range(0, 94).parallel().mapToLong(i -> solve(str, i+1)).sum();


        assertEquals(Integer.parseInt(solution), result);
    }
    
    
    long solve(String str, int n) {
        return IntStream.range(0, str.length() - n).parallel()
				.mapToObj(i -> new Slice(str.substring(i, i + n), i +n))
				.filter(Slice::hasNoDuplicates)
				.mapToInt(s -> s.index).min()
                .orElseThrow(() -> new IllegalArgumentException("this handheld device is broken"));
    }
}
