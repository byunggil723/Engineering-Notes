# 위상 정렬

### 시간 복잡도

$$
\text{주어진 정점 수가 V, 간선 수가 E라 할 때,}
$$

$$
O(V+E)
$$

### 개요

사이클이 없는 방향 그래프(DAG)에서 간선의 방향을 유지하면서 모든 정점을 일렬로 나열하는 정렬 방법.

예를 들어, 1 다음에 2가 와야 하고 1 다음에 3이 와야 한다면, 1 → 2 → 3 혹은 1 → 3 → 2 와 같이 정렬할 수 있다.

### 구현 예시

일반적으로 Kahn 알고리즘(진입 차수 기반 BFS)을 통해 구현한다.<br>
진입 차수란 방향 그래프에서 특정 정점을 향해 들어오는 간선의 개수를 말한다.

우선, 모든 정점의 진입 차수를 계산한 뒤, 진입 차수가 0인 모든 정점을 큐에 삽입한다.<br>
이후 큐에서 하나의 정점을 꺼내어 정렬 결과에 추가하고, 해당 정점과 연결된 모든 정점들을 순회하면서 각각의 진입 차수를 감소시킨다.<br>
이때, 감소된 진입 차수가 0이 되면 해당 정점을 다시 큐에 삽입한다.
이 과정을 큐가 빌 때까지 반복한다.

```C++
#include <iostream>
#include <vector>
#include <queue>

using namespace std;

int N = 0, M = 0;
vector<vector<int>> graph;
vector<int> in_degree;
queue<int> q;

void topological_sort()
{
  for (int i = 1; i <= N; i++)
  {
    if (in_degree[i] == 0)
      q.push(i);
  }

  while (!q.empty())
  {
    int current_node = q.front();

    cout << current_node << ' ';

    q.pop();

    for (int i = 0; i < graph[current_node].size(); i++)
    {
      int next_node = graph[current_node][i];

      in_degree[next_node]--;

      if (in_degree[next_node] == 0)
        q.push(next_node);
    }
  }
}

int main()
{
  cin.tie(NULL);
  ios::sync_with_stdio(false);

  cin >> N >> M;

  graph.assign(N + 1, vector<int>());
  in_degree.assign(N + 1, 0);

  int u = 0, v = 0;
  for (int i = 0; i < M; i++)
  {
    cin >> u >> v;

    graph[u].push_back(v);
    in_degree[v]++;
  }

  topological_sort();
}
```