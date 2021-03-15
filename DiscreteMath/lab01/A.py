n = int(input())

A = [[int(j) for j in input().split()] for i in range(n)]
B = [[int(j) for j in input().split()] for i in range(n)]


def analyze(a):
    ans = [1] * 5
    for i in range(n):
        if a[i][i] == 0:
            ans[0] = 0
        else:
            ans[1] = 0

    for i in range(n):
        for j in range(n):
            if j != i and a[i][j] == 1:
                if a[i][j] == a[j][i]:
                    ans[3] = 0
                else:
                    ans[2] = 0

    for i in range(n):
        for j in range(n):
            for k in range(n):
                if a[i][j] == 1 and a[j][k] == 1 and a[i][k] == 0:
                    ans[4] = 0

    for i in range(5):
        print(ans[i], end=" ")
    print();

analyze(A);
analyze(B);

c = [0] * n
for i in range(n):
    c[i] = [0] * n

for i in range(n):
    for j in range(n):
        for k in range(n):
            if A[i][j] == 1 and B[j][k] == 1:
                c[i][k] = 1

for i in range(n):
    for j in range(n):
        print(c[i][j], end = " ")
    print()