//
// Created by Vladislav Kovalchuk on 06.12.2020.
//

#include <iostream>
#include <vector>
#include <algorithm>

#define INF (int)1e9
#define endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

bool good(char a, char b) {
    return (a == '(' && b == ')') || (a == '[' && b == ']') || (a == '{' && b == '}');
}

vector<vector<int> > dp;
vector<vector<int> > ep;
vector<vector<string> > mn;
string s;
int n;


int main() {
    cin >> s;
    /*
     * dp[l][r] --- наименьшее количество символов которое необходимо удалить
     * из подстроки [l,r] чтобы оставшиеся символы образовывали ПСП
     */
    n = s.size();
    dp.resize(n + 1, vector<int>(n + 1, 0));
    ep.resize(n + 1, vector<int>(n + 1, -1));
    mn.resize(n + 1, vector<string>(n + 1));
    for (int j = 0; j < n; j++) {
        for (int i = j; i >= 0; i--) {
            if (i == j) {
                dp[i][j] = 1;
                mn[i][j] = "";
            } else {
                dp[i][j] = INF;
                if (good(s[i], s[j]) && dp[i + 1][j - 1] < dp[i][j]) {
                    dp[i][j] = dp[i + 1][j - 1];
                    mn[i][j] = s[i] + mn[i + 1][j - 1] + s[j];
                }
                for (int k = i; k < j; k++) {
                    if (dp[i][k] + dp[k + 1][j] < dp[i][j]) {
                        dp[i][j] = dp[i][k] + dp[k + 1][j];
                        mn[i][j] = mn[i][k] + mn[k + 1][j];
                        ep[i][j] = k;
                    }
                }
            }
        }
    }
    cout << mn[0][n - 1] << "\n";
    return 0;
}
