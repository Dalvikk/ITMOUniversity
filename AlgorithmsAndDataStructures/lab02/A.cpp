//
// Created by Vladislav Kovalchuk on 07.11.2020.
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
#define  forn(i, a, b) for (int i = (a); i < (b); i++)
#define  ford(i, a, b) for (int i = (b)-1; i >= (a); i--)
#define   all(x) (x).begin(),(x).end()
#define   fi first
#define   se second
#define   szof(x) ((int)(x).size())
#define   rsz resize
#define   lb lower_bound
#define   ub upper_bound
#define   INF (int)1e9
#define   INFL (long long)1e18
#define   re return
#define   pb push_back
#define   mp make_pair
#define   ll long long
#define   ld long double
#define   PII pair<int,int>
#define   VI vector<int>
#define   VVI vector<vector <int> >
#define   VVLL vector<vector <long long> >
#define   VLL vector <long long>
#define   mt make_tuple
#define   endl '\n'
#define  debug(x) cout << #x << " is " << x << endl;
#define   debug2(x) cout << #x << " is "; for (auto elem : x) {cout << elem << " ";} cout << endl;

using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

int n;
stack<pair<int, int> > s;

int get_min() {
    return s.empty() ? 2*1e9 : s.top().second;
}

int main() {
    fastIO;
    n = nxt();
    for (int i = 0; i < n; ++i) {
        int t = nxt();
        if (t == 1) {
            int x;
            cin >> x;
            s.push(mp(x, min(get_min(), x)));
        } else if (t == 2) {
            s.pop();
        } else {
            cout << get_min() << endl;
        }
    }
    return 0;
}
