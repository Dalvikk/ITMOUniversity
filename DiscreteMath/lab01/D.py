n = int(input())
v = n
output = []


def printVertex(cmd, a, b=0):
    if cmd == 1:
        output.append((1, a))
    else:
        output.append((cmd, a, b))
    global v
    v = v + 1


constZero = True
lastOr = -1


def fckOtherCase():
    lastAnd = -1
    for i in range(2 ** n):
        tmp = format(i, 'b')
        str = "0" * (n - len(tmp)) + tmp
        lastOr = -1
        for j in range(n):
            if str[j] == '1':
                printVertex(1, j + 1)
                if lastOr != -1:
                    printVertex(3, lastOr, v)
                lastOr = v
            else:
                if lastOr != -1:
                    printVertex(3, lastOr, j + 1)
                    lastOr = v
                else:
                    lastOr = j + 1

        if lastAnd != -1:
            printVertex(2, lastOr, lastAnd)
            lastAnd = v
        else:
            lastAnd = lastOr


for i in range(2 ** n):
    inp = input().split()
    str = inp[0]
    res = int(inp[1])
    if res == 1:
        constZero = False
        lastAnd = -1
        for j in range(n):
            if str[j] == '0':
                printVertex(1, j + 1)
                if lastAnd != -1:
                    printVertex(2, lastAnd, v)
                lastAnd = v
            else:
                if lastAnd != -1:
                    printVertex(2, lastAnd, j + 1)
                    lastAnd = v
                else:
                    lastAnd = j + 1

        if (lastOr != -1):
            printVertex(3, lastOr, lastAnd)
            lastOr = v
        else:
            lastOr = lastAnd;

if constZero:
    fckOtherCase()
print(v);
for i in output:
    print(*i)
