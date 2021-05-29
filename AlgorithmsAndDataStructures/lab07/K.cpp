#include <iostream>
#include <string>
#include <vector>

using namespace std;

const int32_t MAXN = 500000;
vector<vector<int32_t>> graph(MAXN);
vector<pair<int32_t, int32_t>> queries;
int32_t k;

vector<pair<int32_t, int32_t>> euler_tour;
vector<int> tin(MAXN);
vector<int> tout(MAXN);
int32_t counter = 0;

vector<vector<pair<int32_t, int32_t>>> sparse_table;
vector<int32_t> lg;

pair<int32_t, int32_t> min(pair<int32_t, int32_t> a, pair<int32_t, int32_t> b) {
  if (a.first <= b.first) {
    return a;
  }
  return b;
}

void calc_log(int32_t n) {
  lg.resize(n + 1, 0);
  for (int i = 2; i <= n; ++i) {
    lg[i] = lg[i / 2] + 1;
  }
}

void build_sp() {
  int32_t n = euler_tour.size();
  calc_log(n);
  sparse_table.resize(n, vector<pair<int32_t, int32_t>> (lg[n] + 1));
  for (int32_t i = 0; i < n; ++i) {
    sparse_table[i][0] = euler_tour[i];
  }
  for (int32_t k = 1; k <= lg[n]; ++k) {
    for (int32_t i = 0; i < n - (1 << (k - 1)); ++i) {
      sparse_table[i][k] = min(sparse_table[i][k - 1], sparse_table[i + (1 << (k - 1))][k - 1]);
    }
  }
}

pair<int32_t, int32_t> sp_get(int32_t l, int32_t r) {
  int32_t log = lg[r - l + 1];
  return min(sparse_table[l][log], sparse_table[r - (1 << log) + 1][log]);
}

void dfs(int32_t v = 0, int32_t depth = 0) {
  tin[v] = counter++;
  euler_tour.push_back(make_pair(depth, v));
  for (auto u: graph[v]) {
    dfs(u, depth + 1);
    euler_tour.push_back(make_pair(depth, v));
  }
  tout[v] = counter++;
}

bool is_ancestor(int32_t u, int32_t v) {
  return tin[u] <= tin[v] && tout[u] >= tout[v];
}

int32_t LCA(int32_t u, int32_t v) {
  if (is_ancestor(u, v)) return u;
  if (is_ancestor(v, u)) return v;
  if (tin[u] >= tin[v]) {
    swap(u, v);
  }
  return sp_get(tout[u], tin[v]).second;
}

void in() {
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  cout.tie(0);
  cin >> k;
  for (int i = 0; i < k; i++) {
    string s;
    cin >> s;
    int32_t a, b;
    cin >> a >> b;
    if (s == "ADD") {
      graph[a - 1].push_back(b - 1);
    } else {
      queries.push_back(make_pair(a - 1, b - 1));
    }
  }
}

int main() {
  in();
  dfs();
  build_sp();
  for (auto [u, v] : queries) {
    cout << LCA(u, v) + 1 << "\n";
  }
  return 0;
}
