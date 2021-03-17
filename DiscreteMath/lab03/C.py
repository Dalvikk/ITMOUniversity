s = [i for i in input()]
t = [['$' for i in s] for i in s]

tmp = [0] * len(s)
for i in range(len(s)):
    tmp[i] = (s[i], i)
list.sort(tmp)
for i in range(len(s)):
    s[tmp[i][1]] = (tmp[i][0], i)
v = 0
ans = ['' for i in s]
for i in range(len(s)):
    ans[-i - 1] = s[v][0]
    v = s[v][1]
for i in ans:
    print(i, end="")
print()
