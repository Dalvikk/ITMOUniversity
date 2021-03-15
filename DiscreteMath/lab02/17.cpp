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
#define int long long


const int MAXN = 20;
long long dp[2 * MAXN + 1][MAXN + 1];
long long n, k;

void in() {
     cin >> n >> k;
     k++;
}

long long get(int i, int j) {
    return 0 <= j && j <= MAXN ? dp[i][j] : 0;
}

void preCalc() {
    dp[0][0] = 1;
    for (int i = 1; i <= 2 * MAXN; i++) {
        for (int j = 0; j <= MAXN; j++) {
            dp[i][j] = get(i - 1, j - 1) + get(i - 1, j + 1);
        }
    }
}


string correct_bracket_sequence_by_number() {
    string res;
    int balance = 0;
    for (int i = 0; i < 2 * n; i++) {
        if (k <= get(2 * n - (i + 1) , balance + 1)) {
            res.push_back('(');
            balance++;
        } else {
            k -= get(2 * n - (i + 1) , balance + 1);
            res.push_back(')');
            balance--;
        }
    }
    return res;
}


signed main() {
    freopen("num2brackets.in", "r", stdin);
    freopen("num2brackets.out", "w", stdout);
    in();
    preCalc();
    cout << correct_bracket_sequence_by_number() << "\n";
    return 0;
}
