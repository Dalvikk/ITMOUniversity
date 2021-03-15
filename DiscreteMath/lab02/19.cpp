//
// Created by Vladislav Kovalchuk on 23.11.2020.
//

#include <iostream>
#include <algorithm>
#include <cmath>
#include <stack>

#define endl '\n'
using namespace std;

const int MAXN = 20;
const int brackets_types = 2;
char BRACES[2 * brackets_types] = {'(', ')', '[', ']'};
const int BALANCE_CHANGE[2 * brackets_types] = {1, -1, 1, -1};

long long dp[2 * MAXN + 1][MAXN + 1];
long long n, k;

void in();

long long get(int i, int j);

void preCalc();

long long to_pow(long long a, int pw);

string correct_bracket_sequence_by_number() {
    string res;
    int balance = 0;
    stack<char> st;
    for (int i = 0; i < 2 * n; i++) {
        int length_left = 2 * n - (i + 1);
        for (int j = 0; j < sizeof(BRACES); j++) {
            if (BRACES[j] == ')' && (st.empty() || st.top() != '(')) {
                continue;
            }
            long long cur = get(length_left, balance + BALANCE_CHANGE[j]) *
                            to_pow(brackets_types, ( (length_left - (balance + BALANCE_CHANGE[j])) / 2) );
            if (k <= cur) {
                res.push_back(BRACES[j]);
                if (j & 1) { // j odd -> ')' or ']'
                    st.pop();
                } else { // j even -> '(' or '['
                    st.push(BRACES[j]);
                }
                balance += BALANCE_CHANGE[j];
                break;
            }
            k -= cur;
        }
    }
    return res;
}


int main() {
    freopen("num2brackets2.in", "r", stdin);
    freopen("num2brackets2.out", "w", stdout);
    in();
    preCalc();
    cout << correct_bracket_sequence_by_number() << "\n";
    return 0;
}

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

long long to_pow(long long a, int pw) {
    long long res = 1;
    for (int i = 1; i <= pw; i++) {
        res *= a;
    }
    return res;
}