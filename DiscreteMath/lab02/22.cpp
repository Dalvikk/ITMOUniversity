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


const int MAXN = 100;
ll dp[MAXN + 1][MAXN + 1];

void preCalc();

long long int dp_get(int i, int j);

long long termsDecompositionNumber(string s);

vector<int> to_vector(string string, char delimiter);

int main() {
    freopen("part2num.in", "r", stdin);
    freopen("part2num.out", "w", stdout);
    string s; cin >> s;
    preCalc();
    cout << termsDecompositionNumber(s) << "\n";
    return 0;
}

vector<int> to_vector(string string, char delimiter) {
    vector<int> ans;
    for (int i = 0; i < string.size(); i++) {
        int j = i;
        while (j < string.size() && string[j] != delimiter) {
            j++;
        }
        if (j - i > 0) {
            ans.push_back(stoi(string.substr(i, j - i)));
        }
        i = j;
    }
    return ans;
}

long long termsDecompositionNumber(string s) {
    vector<int> a = to_vector(s, '+');
    string result = "";
    int current_sum = 0;
    int max_term = 1;
    long long ans = 0;


    for (const auto &item : a) {
        current_sum += item;
    }

    for (const auto &item : a) {
        for (int i = max_term; i < item; ++i) {
            ans += dp_get(current_sum - i, i);
        }
        max_term = item;
        current_sum -= item;

    }
    return ans;
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
