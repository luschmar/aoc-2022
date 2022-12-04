import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input="test.txt", solution="157"),
			@AocInputMapping(input="input.txt", solution="7568")
	}
	)
	void part1(Stream<String> input, String solution) {
		var inputList = input.map(this::packRucksack)
				.map(this::searchCommon)
				.mapToInt(this::getValue).sum();
		
		
		assertEquals(Integer.parseInt(solution), inputList);

	}
	
	int getValue(char c) {
		if(Character.isLowerCase(c)) {
			System.out.println(c+" -> "+ (Character.getNumericValue(c)-9));

			return Character.getNumericValue(c)-9;
			}
		System.out.println(c+" -> "+ (Character.getNumericValue(c)+17));

		return Character.getNumericValue(c)+17;
	}
	
	char searchCommon(Rucksack r) {
		return r.compartmentOne.chars()
				.mapToObj(Character::toString)
				.filter(s -> r.compartmentTwo.contains(s))
				.findFirst().get().charAt(0);
	}

	Rucksack packRucksack(String s) {
		var compOne = s.substring(0, s.length() / 2);
		var compTwo = s.substring(s.length() / 2);
		return new Rucksack(compOne, compTwo);
	}

	record Rucksack(String compartmentOne, String compartmentTwo) {

	}
	
	@ParameterizedTest
	@AocFileSource(inputs = {
			@AocInputMapping(input="test.txt", solution="70"),
			@AocInputMapping(input="input.txt", solution="2780")
		}
	)
	void part2(Stream<String> input, String solution) {
		var inputList = input.toList();
		var badges = new ArrayList<Badge>();
		
		for(int i = 0; i < inputList.size(); i+=3){
			var firstElf = inputList.get(i);
			var secoundElf = inputList.get(i+1);
			var thirdElf = inputList.get(i+2);
			badges.add(new Badge(firstElf, secoundElf, thirdElf));
			
		}
		
		var sum = badges.stream().map(this::searchCommon)
				.mapToInt(this::getValue).sum();
		
		
		assertEquals(Integer.parseInt(solution), sum);
	}
	
	char searchCommon(Badge r) {
		return r.first.chars()
				.mapToObj(Character::toString)
				.filter(s -> r.secound.contains(s) && r.third.contains(s))
				.findFirst().get().charAt(0);
	}
	record Badge(String first, String secound, String third) {
	}
}