s = input()
code = {}
min_code = 0
for i in range(ord('a'), ord('z') + 1, 1):
    code[chr(i)] = min_code
    min_code = min_code + 1
t = ""
for char in s:
    if code.get(t + char) is None:
        print(code[t], end=" ")
        code[t + char] = min_code
        min_code = min_code + 1
        t = char
    else:
        t = t + char
print(code[t])
