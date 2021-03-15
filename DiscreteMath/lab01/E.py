n = int(input())
result = [0] * (2 ** n)
for i in range(len(result)): result[i] = int(input().split()[1])
for i in range(0, 2 ** n):
    if i != 0:
        for j in range(2 ** n - i):
            result[j] ^= result[j + 1]
    binFormat = format(i, 'b')
    resStr = "0" * (n - len(binFormat)) + binFormat
    print(resStr, result[0])
