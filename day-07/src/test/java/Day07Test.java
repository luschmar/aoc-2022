import org.junit.jupiter.params.ParameterizedTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {
    @ParameterizedTest
    @AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "95437"),
			@AocInputMapping(input = "input.txt", solution = "1886043"),
	})
    void part1(Stream<String> input, String solution) {
        var inputList = input.toList();

        var fileSystem = readInFileSystem(inputList);

        var result = fileSystem.keySet().stream().mapToInt(e -> {
            int size = sumDirAndSubDir(e, fileSystem);
            return size < 100000 ? size : 0;
        }).sum();

        assertEquals(Integer.parseInt(solution), result);
    }

	private Map<String, List<ElfFile>> readInFileSystem(List<String> inputList) {
		Map<String, List<ElfFile>> fileSystem = new HashMap<>();

		var cdRootPattern = Pattern.compile("\\$ cd \\/$");
		var cdDownPattern = Pattern.compile("\\$ cd (\\w+)$");
		var cdUpPattern = Pattern.compile("\\$ cd \\.\\.$");
		var filePattern = Pattern.compile("(\\d+) ([\\w\\.]+)$");
		var currentDir = "/";
		for (var consoleLine : inputList) {
			if (cdRootPattern.matcher(consoleLine).matches()) {
				currentDir = "/";
				if (!fileSystem.containsKey(currentDir)) {
					fileSystem.put(currentDir, new ArrayList<>());
				}
			} else if (cdUpPattern.matcher(consoleLine).matches()) {
				currentDir = currentDir.substring(0, currentDir.lastIndexOf("/"));
			} else {
				var cdDownMatcher = cdDownPattern.matcher(consoleLine);
				if (cdDownMatcher.matches()) {
					if (currentDir.equals("/")) {
						currentDir = currentDir + cdDownMatcher.group(1);
					} else {
						currentDir = currentDir + "/" + cdDownMatcher.group(1);
					}
					if (!fileSystem.containsKey(currentDir)) {
						fileSystem.put(currentDir, new ArrayList<>());
					}
				}
				var fileMatcher = filePattern.matcher(consoleLine);
				if (fileMatcher.matches()) {
					fileSystem.get(currentDir).add(new ElfFile(fileMatcher.group(2), Integer.parseInt(fileMatcher.group(1))));
				}
			}
		}

		return fileSystem;
	}

	private int sumDirAndSubDir(String path, Map<String, List<ElfFile>> fileSystem) {
        return fileSystem.entrySet().stream().filter(entry -> entry.getKey().startsWith(path)).flatMap(e -> e.getValue().stream()).mapToInt(ElfFile::size).sum();
    }

    record ElfFile(String name, int size) {

    }

    @ParameterizedTest
    @AocFileSource(inputs = {
			@AocInputMapping(input = "test.txt", solution = "24933642"),
			@AocInputMapping(input = "input.txt", solution = "3842121"),
	})
    void part2(Stream<String> input, String solution) {
        var inputList = input.toList();

        var fileSystem = readInFileSystem(inputList);

        int totalOcc = sumDirAndSubDir("/", fileSystem);
        int needToFreeAtLeast = 30000000 - (70000000 - totalOcc);

        var result = fileSystem.keySet().stream().mapToInt(e -> {
			var size = sumDirAndSubDir(e, fileSystem);
			return size > needToFreeAtLeast ? size : 0;
		}).filter(i -> i > 0).min().getAsInt();

        assertEquals(Integer.parseInt(solution), result);
    }
}