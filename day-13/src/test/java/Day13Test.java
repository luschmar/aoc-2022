import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test {
	@ParameterizedTest
	@AocFileSource(inputs = { 
			@AocInputMapping(input = "test.txt", solution = "13"),
			@AocInputMapping(input = "input.txt", solution = "13")
			})
	void part1(Stream<String> input, String solution) {
		var inputList = input.toList();
		var count = inputList.stream().count();

		var resultArr = IntStream.rangeClosed(0, (int)(count / 3)).mapToObj(i -> toElfPair(i, inputList)).filter(ElfPair::valid).mapToInt(ElfPair::getIndex).toArray();
		var result = IntStream.rangeClosed(0, (int)(count / 3)).mapToObj(i -> toElfPair(i, inputList)).filter(ElfPair::valid).mapToInt(ElfPair::getIndex).sum();

		System.out.println(Arrays.stream(resultArr).toArray());

		assertEquals(Integer.parseInt(solution), result);
	}

	class ElfPair {
		private final int index;
		private final List<NestedNumber> pair1;
		private final List<NestedNumber> pair2;
		private final String pair1Str;
		private final String pair2Str;

		ElfPair(int index, String pair1, String pair2) {
			this.index = index +1;
			this.pair1Str = pair1;
			this.pair2Str = pair2;
			this.pair1 = transformToList(pair1);
			this.pair2 = transformToList(pair2);
		}

		public boolean valid() {
			var min = Integer.min(pair1.size(), pair2.size());

			NestedNumber p1 = null;
			NestedNumber p2 = null;
			for(int i = 0; i < min; i++) {
				p1 = pair1.get(i);
				p2 = pair2.get(i);

				System.out.println("Compare "+p1.value +" vs "+p2.value );

				// both run out of items
				if(p1.value == null && p2.value == null) {
					return p1.lvlUp < p2.lvlUp;
				}

				// left run out of items
				if(p1.value == null) {
					return true;
				}
				// right run out of items
				if(p2.value == null) {
					return false;
				}

				if(p1.value < p2.value) {
					return true;
				}

				if(p1.value > p2.value) {
					return false;
				}

				if(p1.lvl == 0) {
					return true;
				}

				if(p2.lvl == 0) {
					return false;
				}
			}

			return false;
		}

		List<NestedNumber> transformToList(String input) {
			var currentLevel = 0;
			var resList = new ArrayList<NestedNumber>();
			for(var s : input.split(",")) {
				var lvlUp = (int)s.chars().filter(c -> c == '[').count();
				var lvlDown = (int)s.chars().filter(c -> c == ']').count();
				var valueStr = s.replaceAll("\\D", "");
				currentLevel = (currentLevel+lvlUp-lvlDown);

				resList.add(new NestedNumber("".equals(valueStr) ? null : Integer.parseInt(valueStr), currentLevel, lvlUp, lvlDown));
			}
			return resList;
		}

		public int getIndex() {
			return index;
		}
	}

	record NestedNumber (Integer value, int lvl, int lvlUp, int lvlDown) {

	}

	ElfPair toElfPair(int i, List<String> input) {
		return new ElfPair(i, input.get(i*3), input.get(i*3+1));
	}
	@ParameterizedTest
	@CsvSource(delimiterString = ";", textBlock = """
    [];[3]
    [[4,4],4,4];[[4,4],4,4,4]
    [[1],[2,3,4]];[[1],4]
    [1,1,3,1,1];[1,1,5,1,1]
    [[6],[]];[[7],[],[[2]],[],[[[5,0,0,2],1],[]]]
    [[1,2],3,4];[[1,2],3,4,5]
    [[[]],[[7,[5,9,7]],[[6,4,9,9],1,[6,1,7,10,6],[0,6,5,1,4],6],[[0,9,8,5,4]],[[6,8],[],[1,9,2,5],[6,2,7]]],[[],[5],5]];[[[2,6,8,[1,6]],[[4,5,4],[6,10,3,8,7],[7,5,1,5,9]],5],[8,[[8,1,7]],[0]]]
    """)
	void testValid(String p1, String p2){
		var elf = new ElfPair(0, p1, p2);
		assertTrue(elf.valid());
	}

	@ParameterizedTest
	@CsvSource(delimiterString = ";", textBlock = """
    [9];[[8,7,6]]
    [7,7,7,7];[7,7,7]
    [1,2,3,4];[1,2,3]
    [[[]]];[[]]
    [1,[2,[3,[4,[5,6,7]]]],8,9];[1,[2,[3,[4,[5,6,0]]]],8,9]
    [[[[7,1,6],[1]],5,[9,6,10]]];[]
    """)
	void testInValid(String p1, String p2){
		var elf = new ElfPair(0, p1, p2);
		assertFalse(elf.valid());
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

	@Test
	void test() {
		var string = "[1,[2,[3,[4,[5,6,7]]]],8,9]";
		var stack = new ArrayDeque<>();
		var objects = new ArrayList<>();
		stack.add(objects);
		var strRegister = "";
		for(int i = 0; i < string.length(); i++){
			if(string.charAt(i) == '[') {
				var a = new ArrayList<>();
				((List)stack.peek()).add(a);
				stack.push(a);
			}
			else if(string.charAt(i) == ']') {
				if(!"".equals(strRegister)){
					((List)stack.peek()).add(Integer.parseInt(strRegister));
				}
				stack.pop();
				strRegister = "";
			}
			else if(string.charAt(i) == ',') {
				if(!"".equals(strRegister)){
					((List)stack.peek()).add(Integer.parseInt(strRegister));
				}
				strRegister = "";
			}
			else {
				strRegister = string.charAt(i) + strRegister;
			}
		}

		System.out.println(objects);
	}
}