# 백트래킹

### 개요

백트래킹은 특정 조건을 만족하지 않거나 목표 조건에 도달하여 더 이상 탐색을 진행할 필요가 없을 때, 이전 상태로 되돌아가 다른 경로를 탐색하는 DFS 기반 탐색 기법이다.

### 구현 예시

```C++
// 기본 템플릿

void dfs(int depth){
  if(depth == N or "특정 조건"){
    
    return;
  }
  
  for(int i = 0; i < N; i++) {
    if(is_visited[i])
      continue;
    
    // 선택
    visited[i] = true;
    
    // 현재 상태로 그 다음 진행
    dfs(depth+1);
    
    // 선택 취소
    visited[i] = false;
  }
}

int main() {
  dfs(0);
}
```