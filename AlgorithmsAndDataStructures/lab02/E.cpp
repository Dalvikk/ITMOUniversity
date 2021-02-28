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

inline bool isNum(string &s) {
    try {
        int a = stoi(s);
        return true;
    } catch (exception e) {
        return false;
    }
}

inline int extract(stack<int> &st) {
    int ans = st.top();
    st.pop();
    return ans;
}

int main() {
    fastIO;
    stack<int> st;
    string s;
    int n = -1;
    while (cin >> s) {
        if (isNum(s)) {
            st.push(stoi(s));
        } else {
            int b = extract(st);
            int a = extract(st);
            if (s == "+") {
                st.push(a + b);
            } else if (s == "-") {
                st.push(a - b);
            } else if (s == "*") {
                st.push(a * b);
            }
        }
    }
    cout << st.top() << endl;
    return 0;
}    
