package expression.parser;

import expression.exceptions.ParseException;

import java.util.ArrayDeque;
import java.util.Iterator;

public abstract class BaseParser {
    public static final char END = '\0';
    private final ArrayDeque<Character> buffer = new ArrayDeque<>();
    private CharSource source;
    private int readCnt = 0;

    public void init(final CharSource source) {
        this.source = source;
        readCnt = 0;
        buffer.clear();
    }

    protected char getNext() {
        if (buffer.isEmpty()) {
            readNextFromSource();
        }
        return buffer.peekFirst();
    }

    private void readNextFromSource() {
        buffer.addLast(source.hasNext() ? source.next() : END);
    }

    protected void pop() {
        buffer.pollFirst();
    }

    protected void skipWhitespaces() {
        while (Character.isWhitespace(getNext())) {
            pop();
        }
    }

    protected boolean test(final char e) {
        if (getNext() == e) {
            pop();
            return true;
        }
        return false;
    }

    protected boolean test(final String expected) {
        while (buffer.size() <= expected.length()) {
            readNextFromSource();
        }
        final Iterator<Character> it = buffer.iterator();
        for (int i = 0; i < expected.length(); i++) {
            if (it.next() != expected.charAt(i)) {
                return false;
            }
        }
        final boolean lastChar = Character.isLetter(expected.charAt(expected.length() - 1));
        final boolean nextChar = Character.isLetter(it.next());
        if (!(lastChar && nextChar)) {
            for (int i = 0; i < expected.length(); i++) {
                pop();
            }
            return true;
        }
        return false;
    }

    protected void expect(final char c) {
        if (getNext() != c) {
            throw new ParseException("Expected '" + c + "', found '" + getNext() + "' at pos " + getReadCnt() + 1);
        }
        pop();
    }

    protected boolean eof() {
        return test(END);
    }

    protected boolean isNextCharBetween(final char from, final char to) {
        return from <= getNext() && getNext() <= to;
    }

    protected int getReadCnt() {
        return readCnt;
    }
}
