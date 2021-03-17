from queue import PriorityQueue

MAX_N = 1000
cnt = [0] * MAX_N
ans = 0
n = int(input())
p = [int(i) for i in input().split()]
queue = PriorityQueue()

for i in range(n):
    queue.put((p[i], i))
    cnt[i] = p[i]

for i in range(n - 1):
    first, id1 = queue.get()
    second, id2 = queue.get()
    first += second
    queue.put((first, id1))
    cnt[id1] += cnt[id2]
    ans += cnt[id1]
print(ans)
