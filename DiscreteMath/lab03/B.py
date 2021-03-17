s = input()
t = ["" for i in s]
for i in range(len(s)):
    t[i] = s[i:] + s[:i]
list.sort(t)
for i in t:
    print(i[-1], end="")
print()
