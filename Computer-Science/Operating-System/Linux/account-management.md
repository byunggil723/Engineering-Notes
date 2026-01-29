# 리눅스 계정 관리

리눅스는 기본적으로 **다중 사용자(Multi-user) 운영체제**이며, 시스템 내부에는 다음과 같은 계정들이 존재한다.

### 계정 유형

#### (1) root 계정

- UID: 0
- 시스템의 모든 권한을 가진 관리자 계정
- 파일, 프로세스, 커널 자원에 대한 제한이 없음

#### (2) 시스템 계정 (서비스 계정)

- UID: 보통 1 ~ 999
- 사람이 로그인하기 위한 목적 X
- 프로세스가 실행될 때 권한 분리를 위해 사용
  - 예: www-data, nobody, mysql 등

#### (3) 일반 사용자 계정

- UID: 보통 1000 이상
- 실제 사람이 로그인해서 사용하는 계정
- 권한은 제한적이며, **필요 시 sudo를 통해 root 권한 획득**

### 명령어

#### 1. `adduser`

계정 생성

```bash
sudo adduser alice

# 실행 결과
Adding user `alice' ...
Adding new group `alice' (1001) ...
Adding new user `alice' (1001) with group `alice' ...

# 리눅스에서는 사용자를 여러 그룹에 소속시킬 수 있으며, 새로운 계정을 생성하면 기본적으로 사용자 이름과 동일한 그룹이 함께 생성된다.

Creating home directory `/home/alice' ...
Copying files from `/etc/skel' ...
New password:
Retype new password:
passwd: password updated successfully
Changing the user information for alice
Enter the new value, or press ENTER for the default
        Full Name []:
        Room Number []:
        Work Phone []:
        Home Phone []:
        Other []:
Is the information correct? [Y/n] Y
```

```bash
# 계정 목록 확인

cat /etc/passwd

# 실행 결과
...
alice:x:1001:1001:Alice:/home/alice:/bin/bash
...
```

| 위치        | 의미                            |
| ----------- | ------------------------------- |
| alice       | 사용자 이름                     |
| x           | 비밀번호는 /etc/shadow에 저장됨 |
| 1001        | UID (User ID)                   |
| 1001        | GID (Primary Group ID)          |
| Alice       | 사용자 설명 (GECOS 필드)        |
| /home/alice | 홈 디렉터리                     |
| /bin/bash   | 로그인 시 실행되는 쉘           |

```bash
# 비밀번호 관련 목록 확인

cat /etc/shadow

# 실행 결과
...
alice:$y$j9T$k8...hashed...value:19800:0:99999:7:::
...
```

| 위치       | 의미                                           |
| ---------- | ---------------------------------------------- |
| alice      | 사용자 이름                                    |
| $y$j9T$... | 암호화된 비밀번호 해시                         |
| 19800      | 마지막 비밀번호 변경일 (1970-01-01 기준 일 수) |
| 0          | 최소 변경 가능 일                              |
| 99999      | 최대 사용 가능 일                              |
| 7          | 만료 경고 일수                                 |
| (빈칸)     | 비활성 계정 전환까지의 기간                    |
| (빈칸)     | 계정 만료일                                    |

→ `/etc/shadow`는 루트 계정만 읽을 수 있다.

#### 2. `userdel`

계정 삭제

```bash
sudo userdel -r alice
```

\* `-r` : home directory + mail spool 삭제

#### 3. groupdel

그룹 삭제

```bash
groupdel alice
```
