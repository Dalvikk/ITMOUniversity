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

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

const int MAXN = 31;
int C[MAXN][MAXN];

void preCalc() {
    C[0][0] = 1;
    for (int i = 1; i < MAXN; i++) {
        C[i][0] = C[i][i] = 1;
        for (int j = 1; j < i; j++) {
            C[i][j] = C[i-1][j-1] + C[i-1][j];
        }
    }
}

int main() {
    freopen("num2choose.in", "r", stdin);
    freopen("num2choose.out", "w", stdout);
    preCalc();
    int n = nxt(), k = nxt(), m = nxt();
    for (int i = 1; i <= n; i++) {
        int cnt = C[n - i][k - 1];
        if (m >= cnt) {
            m -= cnt;
        } else if (k > 0) {
            cout << i << " ";
            k--;
        } else {
            break;
        }
    }
    return 0;
}
