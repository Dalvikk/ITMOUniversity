package jstest.functional;

import jstest.expression.Builder;
import jstest.expression.Language;
import jstest.expression.Selector;

import java.util.List;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalTest {
    private static final Selector SELECTOR = new Selector(List.of("easy", "hard"), (operations, mode) -> {
        final Builder builder = new Builder(false, operations);
        final Language language = builder.language(
                ExpressionTest.ARITHMETIC,
                builder.dialect(ExpressionTest.POLISH, alias -> alias.chars().noneMatch(Character::isLetter))
        );
        return ExpressionTest.tester(language, mode >= 1);
    })
            .add("Variables")
            .add("OneTwo",      ONE,  TWO)
            .add("OneMinMax",   ONE,  TWO, min(5), max(3))
            .add("OneFP",       ONE,  TWO, FLOOR, CEIL, MADD);


    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
