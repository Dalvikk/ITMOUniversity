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
    freopen("choose2num.in", "r", stdin);
    freopen("choose2num.out", "w", stdout);
    preCalc();
    int n = nxt(), k = nxt();
    vector<int> a;
    a.push_back(0);
    for (int i = 0; i < k; ++i) {
        a.push_back(nxt());
    }
    int cnt = 0;
    for (int i = 0; i + 1 < a.size(); ++i) {
        for (int j = a[i] + 1; j < a[i+1]; j++) {
            cnt += C[n - j][a.size() - i - 2];
        }
    }
    cout << cnt << "\n";
    return 0;
}
