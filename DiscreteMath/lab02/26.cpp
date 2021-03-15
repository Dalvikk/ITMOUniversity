//
// Created by Vladislav Kovalchuk on 04.12.2020.
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

#define fastIO ios_base::sync_with_stdio(0); cin.tie(0);
#define INF (int)1e9
#define INFL (long long)1e18
#define ll long long
#define endl '\n'
using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

int n, k;
vector<vector<int> > sets;

vector<int> parseToVector(string s) {
    vector<int> ans;
    while (s.find(" ") != string::npos) {
        int pos = s.find(" ");
        string num = s.substr(0, pos);
        if (num.size() != 0) {
            ans.push_back(stoi(num));
        }
        s.erase(s.begin(), s.begin() + pos + 1);
    }
    ans.push_back(stoi(s));
    return ans;
}

vector<vector<int>> nextSetPartition(vector<vector<int>> a) {
    set<int> used;
    bool fl = false;
    auto iterator = used.begin();

    for (int i = k - 1; i >= 0; i--) {
        auto anEnd = used.end();
        if (used.size() && *(--anEnd) > a[i].back()) {
            iterator = anEnd;
            for (auto it = used.begin(); it != used.end(); it++) {
                if (*it > a[i].back()) {
                    iterator = it;
                    break;
                }
            }
            a[i].push_back(*iterator);
            used.erase(iterator);
            break;
        }

        for (int j = a[i].size() - 1; j >= 0; j--) {
            auto anEnd = used.end();
            if (used.size() && j && *(--anEnd) > a[i][j]) {
                iterator = anEnd;
                for (auto it = used.begin(); it != used.end(); it++) {
                    if (*it > a[i][j]) {
                        iterator = it;
                        break;
                    }
                }
                int tmp = a[i][j];
                a[i][j] = *iterator;
                used.erase(iterator);
                used.insert(tmp);
                fl = true;
                break;
            }

            used.insert(a[i][j]);
            a[i].erase((--a[i].end()));
            if (a[i].size() == 0) {
                a.erase(a.begin() + i);
            }
        }
        if (fl) {
            break;
        }
    }

    if (used.empty()) {
        return a;
    }
    for (const auto &item : used) {
        a.push_back(vector<int> {item});
    }
    return a;
}


void solve() {
    while (true) {
        cin >> n >> k;
        if (n == 0 & k == 0) {
            return;
        }
        sets.resize(k);
        string s;
        getline(cin, s);
        for (int i = 0; i < k; ++i) {
            sets[i].clear();
            getline(cin, s);
            sets[i] = parseToVector(s);
        }
        vector<vector<int> > ans = nextSetPartition(sets);
        cout << n << " " << ans.size() << endl;
        for (const auto &item : ans) {
            for (const auto &item1 : item) {
                cout << item1 << " ";
            }
            cout << endl;
        }
        cout << endl;
    }
}

int main() {
    freopen("nextsetpartition.in", "r", stdin);
    freopen("nextsetpartition.out", "w", stdout);
    solve();
    return 0;
}
