import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input="test.txt", solution="2"),		
			@AocInputMapping(input="input.txt", solution="657")
	}
	)
	void part1(Stream<String> input, String solution) {
		var count = input.map(this::createPairs).filter(this::rangeContains).toList();
		assertEquals(Integer.parseInt(solution), count.size());
	}
	
	ElfPair createPairs(String s) {
		var firstS = Integer.parseInt(s.split(",")[0].split("-")[0]);
		var firstE = Integer.parseInt(s.split(",")[0].split("-")[1]);
		var firstR = IntStream.rangeClosed(firstS, firstE).boxed().toList();
		
		var secondS = Integer.parseInt(s.split(",")[1].split("-")[0]);
		var secondE = Integer.parseInt(s.split(",")[1].split("-")[1]);
		var secondR = IntStream.rangeClosed(secondS, secondE).boxed().toList();

		
		return new ElfPair(firstR, secondR);
	}
	
	boolean rangeContains(ElfPair ep) {
		return ep.first.containsAll(ep.second) || ep.second.containsAll(ep.first);
	}
	
	
	record ElfPair(List<Integer> first, List<Integer>  second) {}
	

	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input="test.txt", solution="4"),
			@AocInputMapping(input="input.txt", solution="938")
	}
	)
	void part2(Stream<String> input, String solution) {
		var count = input.map(this::createPairs).filter(this::containsAny).toList();
		assertEquals(Integer.parseInt(solution), count.size());		
	}
	
	boolean containsAny(ElfPair ep) {
		return ep.first.stream().anyMatch(e -> ep.second.contains(e)) || 
				ep.second.stream().anyMatch(e -> ep.first.contains(e));
	}
}