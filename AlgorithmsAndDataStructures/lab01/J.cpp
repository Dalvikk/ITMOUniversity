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
#define forn(i, a, b) for (int i = (a); i < (b); i++)
#define ford(i, a, b) for (int i = (b)-1; i >= (a); i--)
#define  all(x) (x).begin(),(x).end()
#define  fi first
#define  se second
#define  szof(x) ((int)(x).size())
#define  rsz resize
#define  lb lower_bound
#define  ub upper_bound
#define  INF (int)1e9
#define  INFL (long long)1e18
#define  re return
#define  pb push_back
#define  mp make_pair
#define  ll long long
#define  ld long double
#define  PII pair<int,int>
#define  VI vector<int>
#define  VVI vector<vector <int> >
#define  VVLL vector<vector <long long> >
#define  VLL vector <long long>
#define  mt make_tuple
#define  endl '\n'
#define debug(x) cout << #x << " is " << x << endl;
#define  debug2(x) cout << #x << " is "; for (auto elem : x) {cout << elem << " ";} cout << endl;

using namespace std;

inline int nxt() {
    int a;
    cin >> a;
    return a;
}

ld a;
ld vp, vf;


inline ld f2(ld x1, ld x2) {
    return (x2 - x1) / vp + sqrt((1 - x2) * (1 - x2) + a * a) / vf;
}

inline ld f(ld x) {
    ld l = x;
    ld r = 1;
    while (r - l > 1e-8) {
        ld m1 = l + (r - l) / 3.00;
        ld m2 = l + 2 * (r - l) / 3.00;
        if (f2(x, m1) <= f2(x, m2)) {
            r = m2;
        } else {
            l = m1;
        }
    }
    return f2(x, l);
}

/*
ld dist(ld x1, ld x2) {
    return sqrt(x1 * x1 + (1 - a) * (1 - a)) / vp + (x2 - x1) / vp + sqrt((1 - x2) * (1 - x2) + a * a) / vf;
}
*/

int main() {
    vp = nxt(), vf = nxt();
    cin >> a;
 //   fastIO;
    ld l = 0;
    ld r = 1;
    while (r - l > 1e-8) {
        ld m1 = l + (r - l) / 3.00;
        ld m2 = l + 2 * (r - l) / 3.00;
        ld d1 = sqrt(m1 * m1 + (1 - a) * (1 - a)) / vp;
        ld d2 = sqrt(m2 * m2 + (1 - a) * (1 - a)) / vp;
        if (d1 + f(m1) <= d2 + f(m2)) {
            r = m2;
        } else {
            l = m1;
        }
    }
    cout.setf(ios::fixed);
    cout.precision(20);
    cout << l << endl;
    return 0;
}
