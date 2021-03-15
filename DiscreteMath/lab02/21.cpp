//
// Created by Vladislav Kovalchuk on 26.11.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <stack>
#include <queue>
#include <cassert>
#include <deque>
#include <cmath>
#include <iomanip>

#define fastIO ios_base::sync_with_stdio(0); cin.tie(0);
#define INF (int)1e9
#define INFL (long long)1e18
#define ll long long
#define endl '\n'
using namespace std;

int n;
ll r;
const int MAXN = 100;
ll dp[MAXN + 1][MAXN + 1];

void preCalc();

long long int dp_get(int i, int j);

string terms_decomposition_by_number();

int main() {
    freopen("num2part.in", "r", stdin);
    freopen("num2part.out", "w", stdout);
    cin >> n >> r;
    preCalc();
    cout << terms_decomposition_by_number() << "\n";
    return 0;
}

string terms_decomposition_by_number() {
    string result = "";
    int current_sum = n;
    int max_term = 1;
    ll k = r;
    while (current_sum != 0) {
        for (int i = max_term; i <= n; ++i) {
            if (k < dp_get(current_sum - i, i)) {
                max_term = i;
                current_sum -= i;
                result.push_back('+');
                result += to_string(i);
                break;
            } else {
                k -= dp_get(current_sum - i, i);
            }
        }
    }
    return result.substr(1, result.size() - 1);
}

void preCalc() {
    for (int i = 1; i <= MAXN; i++) {
        for (int j = i; j >= 0; j--) {
            dp[i][j] = dp_get(i, j + 1) + dp_get(i - j, j);
        }
    }
}

long long int dp_get(int i, int j) {
    if (i == 0) {
        return 1;
    } else if (j > i) {
        return 0;
    } else {
        return dp[i][j];
    }
}
