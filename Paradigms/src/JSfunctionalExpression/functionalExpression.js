"use strict"

const abOp = f => (...terms) => (...args) => f(...terms.map(it => it(...args)))

const add = abOp((a, b) => (a + b))
const subtract = abOp((a, b) => (a - b))
const multiply = abOp((a, b) => (a * b))
const divide = abOp((a, b) => (a / b))

const negate = abOp(x => -x)
const madd = abOp((a, b, c) => (a * b + c))
const floor = abOp(a => Math.floor(a))
const ceil = abOp(a => Math.ceil(a))
const cnst = value => _ => value
const one = cnst(1)
const two = cnst(2)

const variable = variable => {
    let idx = INDEX_BY_VARIABLE_NAME[variable]
    return (...vars) => vars[idx]
}

const OPERATION_BY_STRING = {
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1],
    "*+": [madd, 3],
    "madd": [madd, 3],
    "_": [floor, 1],
    "floor": [floor, 1],
    "^": [ceil, 1],
    "ceil": [ceil, 1]
}

const INDEX_BY_VARIABLE_NAME = {
    "x": 0,
    "y": 1,
    "z": 2
}

const CONSTANTS = {
    "one": one,
    "two": two
}


const parse = s => {
    const stack = []
    for (const token of s.trim().split(/\s+/)) {
        if (token in OPERATION_BY_STRING) {
            let [operation, termsCount] = OPERATION_BY_STRING[token]
            let args = stack.splice(-termsCount)
            stack.push(operation(...args))
        } else if (token in INDEX_BY_VARIABLE_NAME) {
            stack.push(variable(token))
        } else if (token in CONSTANTS) {
            stack.push(CONSTANTS[token])
        } else {
            stack.push(cnst(+token))
        }
    }
    return stack.pop()
}

const myExpr = parse("x x * 2 x * - 1 +")
for (let i = 0; i <= 10; i++) {
    console.log(myExpr(i))
}
