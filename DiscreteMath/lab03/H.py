import math


def lcm(a, b):
    return a // math.gcd(a, b) * b


class Frac:
    p, q = 0, 0

    def __init__(self, a, b):
        gcd = math.gcd(a, b)
        self.p, self.q = a // gcd, b // gcd

    def add(self, b):
        denominator = lcm(b.q, self.q)
        numerator = denominator * self.p // self.q + denominator // b.q * b.p
        return Frac(numerator, denominator)

    def sub(self, b):
        b.p = - b.p
        ans = self.add(b)
        b.p = - b.p
        return ans

    def mul(self, b):
        return Frac(b.p * self.p, b.q * self.q)

    def equals(self, b):
        denominator = lcm(b.q, self.q)
        first = denominator // self.q * self.p
        second = denominator // b.q * b.p
        if first < second:
            return -1
        elif first == second:
            return 0
        else:
            return 1

    def between(self, a, b):
        return self.equals(a) >= 0 and self.equals(b) == -1

    def __str__(self) -> str:
        return str(self.p) + " / " + str(self.q)


if __name__ == '__main__':
    n = int(input())
    c = []
    s_size = 0
    for i in input().split():
        c.append(int(i))
        s_size += int(i)
    num = input()
    res = Frac(int(num, 2), 2 ** (len(num)))
    left_border, right_border, c_sum, cur_sum = [0] * n, [0] * n, 0, 0
    for elem in c:
        c_sum += elem
    for i in range(n):
        left_border[i] = Frac(cur_sum, c_sum)
        cur_sum += c[i]
        right_border[i] = Frac(cur_sum, c_sum)
    left, right = Frac(0, 1), Frac(1, 1)
    for i in range(s_size):
        for j in range(n):
            new_left = right.sub(left).mul(left_border[j]).add(left)
            new_right = right.sub(left).mul(right_border[j]).add(left)
            if res.between(new_left, new_right):
                left, right = new_left, new_right
                print(chr(ord('a') + j), end="")
                break
