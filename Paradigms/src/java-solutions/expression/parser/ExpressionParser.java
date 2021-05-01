package expression.parser;

import expression.GenericExpression;
import expression.exceptions.MessageCreator;
import expression.exceptions.MissingLeftBracketException;
import expression.exceptions.MissingRightBracketException;
import expression.exceptions.ParseException;
import expression.operations.*;


public class ExpressionParser extends BaseParser {
    private String expression;

    public void init(final String expression) {
        super.init(new StringSource(expression));
        this.expression = expression;
    }

    public GenericExpression parse(final String expression) throws ParseException {
        init(expression);
        final GenericExpression result = parseExpression();
        skipWhitespaces();
        if (!eof()) {
            throw new ParseException(MessageCreator.createHighlightMessage(
                    "Unexpected symbol: " + getNext(), expression, getReadCnt() + 1));
        } else {
            return result;
        }
    }

    private GenericExpression parseExpression() {
        skipWhitespaces();
        return parseBinaryTerm(0);
    }

    private GenericExpression nextBinaryTerm(final int priority) {
        if (priority == Operation.getPriority(Operation.CONST)) {
            return parseUnary();
        } else {
            return parseBinaryTerm(priority);
        }
    }

    private GenericExpression parseBinaryTerm(final int priority) {
        GenericExpression result = nextBinaryTerm(priority + 1);
        skipWhitespaces();
        while (true) {
            final Operation operation = getNextOperation(priority);
            if (operation == null) {
                return result;
            }
            final GenericExpression rightTerm = nextBinaryTerm(priority + 1);
            result = binaryOperation(operation, result, rightTerm);
            skipWhitespaces();
        }
    }

    private Operation getNextOperation(final int priority) {
        for (final Operation operation : Operation.OPERATIONS_BY_PRIORITIES.get(priority)) {
            final String op = Operation.STRING_BY_OPERATOR.get(operation);
            if (op != null && test(op)) {
                return operation;
            }
        }
        if (priority == Operation.getPriority(Operation.CONST)) {
            if (isNextCharBetween('x', 'z')) {
                return Operation.VAR;
            }
            if (isNextCharBetween('0', '9')) {
                return Operation.CONST;
            }
        }
        return null;
    }

    private GenericExpression binaryOperation(final Operation operation, final GenericExpression left, final GenericExpression right) {
        switch (operation) {
            case ADD:
                return new Add(left, right);
            case SUB:
                return new Subtract(left, right);
            case MUL:
                return new Multiply(left, right);
            case DIV:
                return new Divide(left, right);
            case MOD:
                return new Mod(left, right);
            default:
                throw new AssertionError("Unknown operation:" + operation);
        }
    }


    private GenericExpression parseUnary() {
        skipWhitespaces();
        final Operation operation = getNextOperation(Operation.getPriority(Operation.CONST));
        if (operation != null) {
            switch (operation) {
                case CONST:
                    return parseConst(true);
                case VAR:
                    return parseVariable();
                case LB:
                    final GenericExpression result = parseExpression();
                    skipWhitespaces();
                    if (!test(')')) {
                        throw new MissingRightBracketException("Right bracket missing");
                    }
                    return result;
                case RB:
                    throw new MissingLeftBracketException("RB");
                case NEGATE:
                    if (isNextCharBetween('0', '9')) {
                        return parseConst(false);
                    }
                    return new Negate(parseUnary());
                case SQUARE:
                    return new Square(parseUnary());
                case ABS:
                    return new Abs(parseUnary());
            }
        }
        throw new ParseException(MessageCreator.createHighlightMessage
                ("Unexpected symbol: " + getNext(), expression, getReadCnt() + 1));
    }


    private Variable parseVariable() {
        final StringBuilder sb = new StringBuilder();
        while (Character.isLetter(getNext())) {
            sb.append(getNext());
            pop();
        }
        return new Variable(sb.toString());
    }

    private Const parseConst(final boolean isPositive) {
        final StringBuilder sb = new StringBuilder();
        if (!isPositive) {
            sb.append('-');
        }
        while (isNextCharBetween('0', '9')) {
            sb.append(getNext());
            pop();
        }
        return new Const(sb.toString());
    }
}
