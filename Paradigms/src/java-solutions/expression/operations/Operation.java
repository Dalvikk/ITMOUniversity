package expression.operations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Operation {
    ADD, SUB,
    MUL, DIV,
    MOD,
    SQUARE, ABS, NEGATE, CONST, VAR, LB, RB;

    // List.get(i) --- operations with priority i
    public static final List<List<Operation>> OPERATIONS_BY_PRIORITIES = List.of(
            List.of(ADD, SUB),
            List.of(MUL, DIV),
            List.of(MOD),
            List.of(NEGATE, SQUARE, ABS, CONST, VAR, LB, RB)
    );
    public static final Map<Operation, String> STRING_BY_OPERATOR = Map.of(
            ADD, "+", SUB, "-",
            MUL, "*", DIV, "/",
            MOD, "mod",
            NEGATE, "-",
            SQUARE, "square",
            ABS, "abs",
            LB, "(",
            RB, ")"
    );

    private static Map<Operation, Integer> PRIORITIES;

    private static void init() {
        PRIORITIES = new HashMap<>();
        for (int priority = 0; priority < OPERATIONS_BY_PRIORITIES.size(); priority++) {
            for (final Operation operation : OPERATIONS_BY_PRIORITIES.get(priority)) {
                PRIORITIES.put(operation, priority);
            }
        }
    }

    public static int getPriority(final Operation operation) {
        if (PRIORITIES == null) {
            init();
        }
        return PRIORITIES.get(operation);
    }
}
