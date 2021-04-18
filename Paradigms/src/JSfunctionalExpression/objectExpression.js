"use strict"

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
}

function NullAryFactory(constructor, calc, diff) {
    constructor.prototype = Object.create(NullAryOperation)
    initCommonParams(constructor,
        "evaluate", calc,
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
    diff(x) {
        return this.diffImpl(x, ...this.terms, ...this.terms.map(it => it.diff(x)))
    },
}

function createOperation(calc, token, diffImpl) {
    const constructor = function (...terms) {
        this.terms = terms
    }
    constructor.prototype = Object.create(Operation)
    initCommonParams(constructor,
        "calculate", calc,
        "token", token,
        "diffImpl", diffImpl,
    )
    return constructor
}

let Hypot = createOperation((a, b) => a * a + b * b, "hypot", (v, x, y) => (
    new Add(
        new Multiply(x, x).diff(v),
        new Multiply(y, y).diff(v))));

let HMean = createOperation((a, b) => 2 / (1 / a + 1 / b), "hmean", (v, x, y) => {
    let a = new Multiply(new Const(2), new Multiply(x, y))
    let b = new Add(x, y)
    return new Divide(
        new Subtract(new Multiply(a.diff(v), b), new Multiply(a, b.diff(v))),
        new Multiply(b, b)
    )
})

let Negate = createOperation(x => -x, "negate", (v, x, dx) => (new Negate(dx)))

let Add = createOperation((a, b) => (a + b), "+",
    (v, x, y, dx, dy) => new Add(dx, dy))

let Subtract = createOperation((a, b) => (a - b), "-",
    (v, x, y, dx, dy) => new Subtract(dx, dy))

let Multiply = createOperation((a, b) => (a * b), "*",
    (v, x, y, dx, dy) => new Add(new Multiply(dx, y), new Multiply(x, dy)))

let Divide = createOperation((a, b) => (a / b), "/",
    (v, x, y, dx, dy) => new Divide(
        new Subtract(new Multiply(dx, y), new Multiply(x, dy)),
        new Multiply(y, y)))


const OPERATION_BY_STRING = {
    "+": [Add, 2],
    "-": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "negate": [Negate, 1],
    "hypot": [Hypot, 2],
    "hmean": [HMean, 2],
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
