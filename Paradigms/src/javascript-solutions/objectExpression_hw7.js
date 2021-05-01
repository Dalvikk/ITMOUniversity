"use strict"

// ========================Factories=====================

function initCommonParams(constructor, ...params) {
    for (let i = 1; i < params.length; i += 2) {
        constructor.prototype[params[i - 1]] = params[i]
    }
}

const NullAryOperation = {
    toString() {
        return this.x.toString()
    },
    prefix() {
        return this.toString()
    },
    postfix() {
        return this.toString()
    },
}

function NullAryFactory(constructor, evaluate, diff) {
    constructor.prototype = Object.create(NullAryOperation)
    initCommonParams(constructor,
        "evaluate", evaluate,
        "diff", diff,
    )
    return constructor
}

let Const = NullAryFactory(
    function (x) {
        this.x = x
    },
    function () {
        return this.x
    },
    () => new Const(0)
)

let Variable = NullAryFactory(
    function (x) {
        this.x = x
        this.index = INDEX_BY_VARIABLE_NAME[x]
    },
    function (...args) {
        return args[this.index]
    },
    function (v) {
        return this.x === v ? new Const(1) : new Const(0)
    }
)

const INDEX_BY_VARIABLE_NAME = {
    "x": 0,
    "y": 1,
    "z": 2,
}

const Operation = {
    evaluate(...args) {
        return this.calculate(...this.terms.map(it => it.evaluate(...args)))
    },
    toString() {
        return `${this.terms.map(it => it.toString()).join(" ")} ${this.token}`
    },
    prefix() {
        return `(${this.token} ${this.terms.map(it => it.prefix()).join(" ")})`
    },
    postfix() {
        return `(${this.terms.map(it => it.postfix()).join(" ")} ${this.token})`
    },
    diff(x) {
        return this.diffImpl(this.terms, this.terms.map(it => it.diff(x)))
    },
}

const OPERATION_BY_STRING = {}

function createOperation(calculate, token, diffImpl) {
    const constructor = function (...terms) {
        this.terms = terms
    }
    OPERATION_BY_STRING[token] = [constructor, calculate.length > 0 ? calculate.length : Infinity]
    constructor.prototype = Object.create(Operation)
    initCommonParams(constructor,
        "calculate", calculate,
        "token", token,
        "diffImpl", diffImpl,
    )
    return constructor
}

// ========================Operations=====================

let Hypot = createOperation((a, b) => a * a + b * b, "hypot",
    (xs, dxs) => new Add(
        multiMul(new Const(2), xs[0], dxs[0]),
        multiMul(new Const(2), xs[1], dxs[1])));

let HMean = createOperation((a, b) => 2 / (1 / a + 1 / b), "hmean", (xs, dxs) => {
    let a = multiMul(new Const(2), xs[0], xs[1])
    let b = new Add(xs[0], xs[1])
    return new Divide(
        new Subtract(
            new Multiply(b, new Add(multiMul(new Const(2), dxs[0], xs[1]), multiMul(new Const(2), dxs[1], xs[0]))),
            new Multiply(a, new Add(dxs[0], dxs[1]))),
        new Multiply(b, b)
    )
})

let Negate = createOperation(x => -x, "negate", (xs, dxs) => new Negate(dxs[0]))

let Add = createOperation((a, b) => (a + b), "+",
    (xs, dxs) => new Add(dxs[0], dxs[1]))

let Subtract = createOperation((a, b) => (a - b), "-",
    (xs, dxs) => new Subtract(dxs[0], dxs[1]))

let Multiply = createOperation((a, b) => (a * b), "*",
    (xs, dxs) => new Add(new Multiply(dxs[0], xs[1]), new Multiply(xs[0], dxs[1])))

let multiMul = (...args) => args.reduce((a, b) => new Multiply(a, b))


let Divide = createOperation((a, b) => (a / b), "/",
    (xs, dxs) => new Divide(
        new Subtract(new Multiply(dxs[0], xs[1]), new Multiply(xs[0], dxs[1])),
        new Multiply(xs[1], xs[1])))

let Sign = createOperation(x => x > 0 ? 1 : (x < 0 ? -1 : 0), "sign", (xs, dxs) => new Const(0))

let Abs = createOperation(x => x > 0 ? x : -x, "abs",
    (xs, dxs) => new Multiply(new Sign(xs[0]), dxs[0]))

let Log = createOperation(x => Math.log(x), "log", (xs, dxs) => new Divide(new Const(1), xs[0]))

let Pow = createOperation((x, y) => Math.pow(x, y), "pow",
    (xs, dxs) =>
        new Multiply(
            new Pow(xs[0], new Subtract(xs[1], new Const(1))),
            new Add(new Multiply(xs[1], dxs[0]), multiMul(xs[0], dxs[1], new Log(xs[0])))))

let ArithMean = createOperation((...args) => args.reduce((a, b) => a + b) / args.length, "arith-mean",
    (xs, dxs) => new Divide(
        dxs.reduce((a, b) => new Add(a, b)),
        new Const(xs.length)))

let GeomMean = createOperation(
    (...args) => Math.pow(Math.abs(args.reduce((a, b) => a * b)), 1 / args.length), "geom-mean",
    (xs, dxs) => {
        let n = xs.length, muld = new Const(0), mul = multiMul(...xs)
        // (x_1 * x_2 * ... x_n)' = (x_1' * x_2 ... x_n) + (x_1 * x_2' ... x_n) +
        // + ... (x_1 * x_2 ... x_n')
        for (let i = 0; i < n; i++) {
            let backup = xs[i]
            xs[i] = dxs[i]
            muld = new Add(muld, multiMul(...xs))
            xs[i] = backup
        }
        return multiMul(new Sign(mul), new Const(1 / n), muld, new Pow(new Abs(mul), new Const(1 / n - 1)))
    })

let HarmMean = createOperation(
    (...args) => args.length / args.map(a => 1 / a).reduce((a, b) => a + b), "harm-mean",
    (xs, dxs) => {
        let num = new Const(0), denom = new Const(0)
        for (let i = 0; i < xs.length; i++) {
            num = new Add(num, new Divide(dxs[i], new Multiply(xs[i], xs[i])))
            denom = new Add(denom, new Divide(new Const(1), xs[i]))
        }
        // n * (x1'/x1^2 + x2'/x2^2 + ...) / (1/x1 + 1/x2 + ...)^ 2
        return new Multiply(new Const(xs.length), new Divide(num, new Multiply(denom, denom)))
    })

// ========================Errors=====================

class ParseError {
    constructor(_message, _expression, _pos, _name = 'ParseError') {
        this.name = _name
        this.message = this.createHighlightMessage(_message, _expression, _pos)
    }

    createHighlightMessage(msg, expr, pos) {
        let bottom = '='.repeat(pos) + '^' + '='.repeat(expr.length - pos)
        return "${msg}\n" +
            "${'='.repeat(expr.length + 1)}\n" +
            "${expr}\n" +
            "${bottom}"
    }
}

class UnaryExpectedError extends ParseError {
    constructor(_expr, _pos) {
        super("Const, (x|y|z) variable or expression in brackets expected", _expr, _pos, "UnaryExpectedError");
    }
}

class MissingLPError extends ParseError {
    constructor(_expr, _pos) {
        super("Missing left parenthesis", _expr, _pos, "MissingLPError");
    }
}

class MissingRPError extends ParseError {
    constructor(_expr, _pos) {
        super("Missing right parenthesis", _expr, _pos, "MissingRPError");
    }
}

class IllegalOperationError extends ParseError {
    constructor(_expr, _pos) {
        super("Wrong operation.", _expr, _pos, "IllegalOperationError");
    }
}

class ExtraTokensError extends ParseError {
    constructor(_expr, _pos) {
        super("EOF expected.", _expr, _pos, "ExtraTokensError");
    }
}

class IllegalArgumentsCountError extends ParseError {
    constructor(_expr, _pos, _expected, _actual) {
        super("Wrong arguments count. Expected: " + _expected + ", actual: " + _actual, _expr, _pos, "IllegalArgumentsCountError");
    }
}

// ========================Parser=====================


const ExpressionParser = {
    getPointer() {
        return this._pointer
    },
    getSource() {
        return this._str
    },
    getLexems() {
        return this._lexems
    },
    addPointer(add = 1) {
        return this._pointer += add
    },
    next() {
        return this.getLexems()[this.getPointer()]
    },
    getNext() {
        const res = this.next()
        this.addPointer()
        return res
    },
    eof() {
        return this.getPointer() >= this.getLexems().length
    },

    parseUnary() {
        if (this.eof()) throw new UnaryExpectedError(this._str, this._str.length)
        let lexem = this.getNext()
        let token = lexem[0]
        let expr
        if (token === '(') {
            expr = this.parse()
            this.getNext() // ')'
        } else if (token in INDEX_BY_VARIABLE_NAME) {
            expr = new Variable(token)
        } else if (!isNaN(+token)) {
            expr = new Const(+token)
        } else {
            throw new UnaryExpectedError(this._str, lexem.index)
        }
        return expr
    },

    parseArguments() {
        let args = []
        while (!this.eof() && this.next()[0] !== ')' && !(this.next()[0] in OPERATION_BY_STRING)) {
            args.push(this.parseUnary())
        }
        return args
    },

    parseOperation() {
        if (this.eof() || !(this.next()[0] in OPERATION_BY_STRING)) {
            throw new IllegalOperationError(this._str, this.eof() ? this._str.length : this.next().index)
        }
        return OPERATION_BY_STRING[this.getNext()[0]]
    },

    parse() {
        let operationConstructor, operationArgsCount, args
        let index
        if (this.mode === "prefix") {
            index = this.next().index;
            [operationConstructor, operationArgsCount] = this.parseOperation()
            args = this.parseArguments()
        } else if (this.mode === "postfix") {
            args = this.parseArguments();
            index = this.next().index;
            [operationConstructor, operationArgsCount] = this.parseOperation()
        }
        if (operationArgsCount !== Infinity && operationArgsCount !== args.length) {
            throw new IllegalArgumentsCountError(this._str, index, operationArgsCount, args.length)
        }
        return new operationConstructor(...args)
    },

    checkBrackets() {
        const stack = []
        for (const lexem of this.getLexems()) {
            if (lexem[0] === '(') {
                stack.push(lexem)
            } else if (lexem[0] === ')') {
                if (stack.length === 0) {
                    throw new MissingLPError(this._str, lexem.index)
                }
                stack.pop()
            }
        }
        if (stack.length > 0) {
            throw new MissingRPError(this._str, stack[stack.length - 1].index)
        }
    },

    parseExpression(mode) {
        this.checkBrackets()
        this.mode = mode
        let expr = this.parseUnary()
        if (!this.eof()) {
            throw new ExtraTokensError(this._str, this.next().index)
        }
        return expr
    },
}


function ExpressionParserFactory() {
    const constructor = function (str) {
        this._str = str
        this._lexems = Array.from(str.matchAll(/\(|\)|[^\s()]+/g))
        this._pointer = 0
    }
    constructor.prototype = Object.create(ExpressionParser)
    return constructor
}

const parsePrefix = s => {
    const parser = new (ExpressionParserFactory())(s)
    return parser.parseExpression("prefix")
}

const parsePostfix = s => {
    const parser = new (ExpressionParserFactory())(s)
    return parser.parseExpression("postfix")
}

const parse = s => {
    const stack = []
    for (const token of s.trim().split(/\s+/)) {
        if (token in OPERATION_BY_STRING) {
            let [operation, termsCount] = OPERATION_BY_STRING[token]
            stack.push(new operation(...stack.splice(-termsCount)))
        } else if (token in INDEX_BY_VARIABLE_NAME) {
            stack.push(new Variable(token))
        } else {
            stack.push(new Const(+token))
        }
    }
    return stack.pop()
}
