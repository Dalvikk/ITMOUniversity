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

    def __str__(self) -> str:
        return str(self.p) + " / " + str(self.q)


if __name__ == '__main__':
    n = int(input())
    S = input()
    c = [0] * n
    for char in S:
        c[ord(char) - ord('a')] += 1
    print(n)
    for i in range(n):
        print(c[i], end=" ")
    print()
    left_border, right_border, c_sum, cur_sum = [0] * 26, [0] * 26, 0, 0
    for elem in c:
        c_sum += elem
    # Расположим на отрезке [0,1] отрезки длинной c[0], c[1]...
    # Для i-ого left(right)_border[i] это на какой относительной части
    # этого отрезка находятся границы отрезка
    for i in range(n):
        left_border[i] = Frac(cur_sum, c_sum)
        cur_sum += c[i]
        right_border[i] = Frac(cur_sum, c_sum)
    left, right = Frac(0, 1), Frac(1, 1)
    for char in S:
        # new_left, new_right это границы зазумленного отрезка
        new_left = right.sub(left).mul(left_border[ord(char) - ord('a')]).add(left)
        new_right = right.sub(left).mul(right_border[ord(char) - ord('a')]).add(left)
        left, right = new_left, new_right
    q = 1
    num = 2
    # Перебираем q
    while True:
        # left < p/2^q < right
        # left * 2^q < p < right * 2^q
        top = right.mul(Frac(num, 1))
        bottom = left.mul(Frac(num, 1))
        tmp = bottom.p // bottom.q
        # tmp это наше целочисленное p
        if tmp * bottom.q != bottom.p:
            tmp += 1
        if Frac(tmp, 1).equals(top) == -1:
            print(bin(tmp)[2:].zfill(q))
            break
        q += 1
        num *= 2
