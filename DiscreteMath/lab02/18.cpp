//
// Created by Vladislav Kovalchuk on 23.11.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <stack>
#include <queue>
#include <deque>
#include <cmath>

#define endl '\n'
using namespace std;


const int MAXN = 20;
long long dp[2 * MAXN + 1][MAXN + 1];
string s;
int n;

void in() {
    cin >> s;
    n = s.size() / 2;
}

long long get(int i, int j) {
    return (0 <= j && j <= n) ? dp[i][j] : 0;
}

void preCalc() {
    dp[0][0] = 1;
    for (int i = 1; i <= 2 * n; i++) {
        for (int j = 0; j <= n; j++) {
            dp[i][j] = get(i - 1, j - 1) + get(i - 1, j + 1);
        }
    }
}


long long number_bracket_sequence() {
    long long ans = 0;
    int balance = 0;
    for (int i = 0; i < 2 * n; ++i) {
        if (s[i] == '(') {
            balance++;
        } else {
            ans += get(2 * n - i - 1, balance + 1);
            balance--;
        }
    }
    return ans;
}


int main() {
    freopen("brackets2num.in", "r", stdin);
    freopen("brackets2num.out", "w", stdout);
    in();
    preCalc();
    cout << number_bracket_sequence() << "\n";
    return 0;
}
