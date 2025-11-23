package stats;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticsCalculatorTest {

    @Test
    void handlesEmptyFolder() throws Exception {
        Path tmp = Path.of("src/test/resources/empty"); // створи порожню папку
        Map<String, Long> stats = StatisticsCalculator.calculate(
                tmp,
                AttributeExtractor.forAttribute("director"),
                2
        );
        assertTrue(stats.isEmpty());
    }
}
