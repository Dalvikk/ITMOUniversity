t = [i for i in input()]

alphabet = []
for i in t:
    alphabet.append(i)
alphabet = list(set(alphabet))
list.sort(alphabet)

for i in t:
    idx = 1
    for j in alphabet:
        if j == i:
            print(idx, end=" ")
            break
        idx = idx + 1
    for j in range(idx - 1, 0, -1):
        alphabet[j] = alphabet[j - 1]
    alphabet[0] = i
