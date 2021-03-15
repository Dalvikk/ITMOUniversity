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
const int brackets_types = 2;
char BRACES[2 * brackets_types] = {'(', ')', '[', ']'};
const int BALANCE_CHANGE[2 * brackets_types] = {1, -1, 1, -1};

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

long long to_pow(long long a, int pw) {
    long long res = 1;
    for (int i = 1; i <= pw; i++) {
        res *= a;
    }
    return res;
}

long long number_bracket_sequence() {
    long long ans = 0;
    int balance = 0;
    stack <char> st;
    for (int i = 0; i < 2 * n; ++i) {
        int length_left = 2 * n - (i + 1);
        int current_idx = -1;
        for (int j = 0; j < sizeof(BRACES); j++) {
            if (BRACES[j] == s[i]) {
                current_idx = j;
                break;
            }
        }
        for (int j = 0; j < current_idx; j++) {
            // "[ )" is bad bracket sequence <-> ans += 0
            if (BRACES[j] == ')' && (st.empty() || st.top() != '(')) {
                continue;
            }
            ans += get(length_left, balance + BALANCE_CHANGE[j]) *
                   to_pow(brackets_types, ((length_left - (balance + BALANCE_CHANGE[j])) / 2));
        }
        if (current_idx & 1) { // j odd -> ')' or ']'
            st.pop();
        } else { // j even -> '(' or '['
            st.push(BRACES[current_idx]);
        }
        balance += BALANCE_CHANGE[current_idx];
    }
    return ans;
}


int main() {
    freopen("brackets2num2.in", "r", stdin);
    freopen("brackets2num2.out", "w", stdout);
    in();
    preCalc();
    cout << number_bracket_sequence() << "\n";
    return 0;
}
