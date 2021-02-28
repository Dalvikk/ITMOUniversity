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

const int MAXN = 1e5;
int queuePos[MAXN + 1];
int manIdx[MAXN + 1];

int main() {
    fastIO;
    int n = nxt();
    int leaveFront = 0;
    int allCnt = 0;
    for (int i = 0; i < n; ++i) {
        int cmd = nxt();
        switch (cmd) {
            case 1: {
                int id = nxt();
                manIdx[allCnt] = id;
                queuePos[id] = allCnt;
                allCnt++;
                break;
            }
            case 2:
                leaveFront++;
                break;
            case 3:
                allCnt--;
                break;
            case 4: {
                int q = nxt();
                cout << queuePos[q] - leaveFront << endl;
                break;
            }
            case 5:
                cout << manIdx[leaveFront] << endl;
        }
    }
    return 0;
}
