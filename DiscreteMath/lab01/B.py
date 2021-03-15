n = int(input())

F = [0] * 5


def zhegalkin(cnt, res):
    triangle = [0] * len(res)
    triangle[0] = res
    for i in range(1, len(res)):
        size = len(res) - i;
        triangle[i] = [0] * size;
        for j in range(size):
            triangle[i][j] = triangle[i - 1][j] ^ triangle[i - 1][j + 1]

    for i in range(len(res)):
        if format(i, 'b').count('1') > 1 and triangle[i][0] == 1:
            return 1
    return 0


def bit(x, i):
    return x >> i & 1

F[2] = 1

for z in range(n):
    inp = input().split()
    cnt, res = int(inp[0]), list(map(int, inp[1]))
    h = 2 ** cnt
    for i in range(2 ** cnt):
        res[i] = int(res[i])
    if res[0] == 1: F[0] = 1
    if res[h - 1] == 0: F[1] = 1

    for i in range(2 ** cnt):
        for j in range(2 ** cnt):
            if (j | i) == j:
                F[2] = F[2] and res[j] >= res[i]

    for i in range(h):
        if res[i] == res[h - 1 - i]:
            F[3] = 1
    F[4] = F[4] or zhegalkin(cnt, res)

if F[2] == 1:
    F[2] = 0
else:
    F[2] = 1

if F[0] and F[1] and F[2] and F[3] and F[4]:
    print("YES")
else:
    print("NO")
