# 리눅스 권한 관리

리눅스는 모든 파일과 디렉터리를 권한(permission) 으로 보호한다.<br>
이 권한은 누가(owner / group / others) 무엇을(read / write / execute) 할 수 있는지를 정의한다.

### 권한 구조

모든 파일은 다음 세 주체에 대해 권한이 부여되어 있다.

| 구분       | 의미              |
| ---------- | ----------------- |
| user (u)   | 파일 소유자       |
| group (g)  | 파일이 속한 그룹  |
| others (o) | 그 외 모든 사용자 |

### 권한 종류

| 권한        | 파일에서의 의미 | 디렉터리에서의 의미    |
| ----------- | --------------- | ---------------------- |
| r (read)    | 파일 내용 읽기  | 디렉터리 목록(ls) 보기 |
| w (write)   | 파일 내용 수정  | 파일 생성/삭제         |
| x (execute) | 실행 가능       | 디렉터리 진입(cd)      |

각 권한은 비트 값으로 표현되며,

| 권한 | 값  |
| ---- | --- |
| r    | 4   |
| w    | 2   |
| x    | 1   |

합으로 계산된다.

| 조합 | 값  |
| ---- | --- |
| rwx  | 7   |
| rw-  | 6   |
| r-x  | 5   |
| r--  | 4   |
| ---  | 0   |

### 권한 확인

```bash
ls -l test.txt

# 실행 결과
-rw-r--r-- 1 user group  1234 Jan 1 12:00 test.txt
```

```bash
(-)(rw-)(r--)(r--)
 │   │    │    │
 │   │    │    └── others 권한
 │   │    └─── group 권한
 │   └──── user 권한
 └───── 파일 타입
```

\* 그룹 권한이란, 파일 접근 시 현재 사용자가 파일의 소유자가 아닌 경우, 해당 파일에 기록된 소유 그룹에 속해 있는지를 기준으로 적용되는 접근 권한이다.<br>
소유 그룹은 파일이 생성될 때 생성자가 속한 기본 그룹으로 설정되며, 이후 소유자의 그룹이 변경되더라도 이미 생성된 파일의 소유 그룹은 변경되지 않는다.

#### 파일 타입

| 문자 | 의미        |
| ---- | ----------- |
| `-`  | 일반 파일   |
| `d`  | 디렉터리    |
| `l`  | 심볼릭 링크 |

### 명령어

#### 1. `umask` (user file-creation mode mask)

현재 쉘 프로세스에서 새 파일/디렉터리를 만들 때 기본 권한에서 제거할 권한을 설정

#### 기본 권한

| 대상     | 기본 권한 |
| -------- | --------- |
| 파일     | 666       |
| 디렉터리 | 777       |

```bash
umask

# 실행 결과, 맨 앞의 0은 특수 권한(setuid/setgid/sticky bit)에 대한 자리
0022
```

→ 즉, 현재 쉘 프로세스에서 파일 생성 시 666 - 022 = 644 (rw-r--r--)로 권한이 부여되고, 디렉터리 생성 시 777 - 022 = 755 (rwxr-xr-x)로 권한이 부여된다.

```bash
touch a.txt   # 새 파일 생성
mkdir dirA    # 새 디렉터리 생성
ls -l         # 현재 디렉터리 내 파일/디렉터리의 상세 정보 출력

# 실행 결과
-rw-r--r--  1 user user    0 Jan 29 18:42 a.txt
drwxr-xr-x  2 user user 4096 Jan 29 18:42 dirA

# 권한 | 하드 링크 수(inode를 가리키는 이름의 개수) | 소유자 | 소유 그룹 | 크기(byte) | 최종 수정 시각 | 이름
```

```bash
umask 027 # 현재 umask 변경
```

→ 이후 생성되는 파일은 640 (rw-r-----), 디렉터리는 750 (rwxr-x---) 권한이 부여됨.

#### 2. `chmod` (change mode)

권한 변경

| 기호 | 의미      |
| ---- | --------- |
| u    | user      |
| g    | group     |
| o    | others    |
| a    | all       |
| +    | 권한 추가 |
| -    | 권한 제거 |
| =    | 권한 지정 |

```bash
# 숫자 방식
chmod 644 test.txt
chmod 755 script.sh

# 심볼릭 방식
chmod u+x script.sh
chmod g-w file.txt
chmod o=r file.txt
chmod a+rx dir/
```

상황에 따라 특수 권한을 부여해야 하는 경우가 존재한다.<br>
일반 사용자가 특정 파일을 실행할 때, 해당 파일이 root 권한으로 실행되어야만 커널이 허용하는 접근 범위가 확장되어 정상적인 동작이 가능한 경우가 이에 해당한다.

1\) setuid (Set User ID)

실행 시 프로세스의 UID를 파일 소유자의 UID로 설정

```bash
touch test.txt
chmod 755 test.txt
ls -l test.txt

# 실행 결과
-rwxr-xr-x 1 user user 0 Feb  1 12:00 test.txt

chmod u+s test.txt # setuid 설정
ls -l test.txt

# 실행 결과
-rwsr-xr-x 1 user user 0 Feb  1 12:01 test.txt
```

2\) setgid (Set Group ID)

실행 시 프로세스의 GID를 파일 소유자의 GID로 설정

```bash
touch test.txt
chmod 755 test.txt
ls -l test.txt

# 실행 결과
-rwxr-xr-x 1 user dev 0 Feb  1 12:00 test.txt

chmod g+s test.txt
ls -l test.txt

# 실행 결과
-rwxr-sr-x 1 user dev 0 Feb  1 12:02 test.txt
```

\* setuid와 setgid는 해당 위치의 실행 비트가 `x`로 설정되어 있을 때에만 의미 있게 동작하며,<br>
실행 비트 `x`가 없는 파일 또는 디렉터리에 특수 권한 비트만 설정한 경우에는 대문자 `S`로 표시되어 특수 권한 설정이 적용되지 않는다.

3\) sticky bit

sticky bit가 설정된 디렉터리 내에서는 생성자 외 사용자도 `rwx` 권한을 모두 가질 수 있으나,<br>
삭제(`rm`) 및 이름 변경/이동(`mv`)은 해당 파일/디렉터리 소유자와 sticky bit 디렉터리 소유자만 수행할 수 있다.

```bash
ls -l shared_dir

# 실행 결과
drwxrwxr-x  2 user dev  Feb  1 12:00 shared_dir

chmod +t shared_dir
ls -ld shared_dir

# 실행 결과
drwxrwxrwt  2 user dev  Feb  1 12:02 shared_dir
```

\* sticky bit는 other 위치의 실행 비트 `x`로 설정되어 있을 때에만 `t`로 표시되며,<br>
실행 비트 없이 설정된 경우에는 `T`로 표시되어 특수 권한 설정이 적용되지 않는다.

#### 3. `chown` (change owner)

소유자 변경

```bash
ls -l report.txt

# 실행 결과
-rw-r-----  1 alice dev  1234 Jan 29 18:42 report.txt

sudo chown bob report.txt

ls -l report.txt

# 실행 결과
-rw-r-----  1 bob dev  1234 Jan 29 18:43 report.txt
```

#### 4. `chgrp` (change grpoup)

소유 그룹 변경

```bash
ls -l report.txt

# 실행 결과
-rw-r-----  1 bob dev  1234 Jan 29 18:42 report.txt

sudo chgrp ops report.txt

ls -l report.txt

# 실행 결과
-rw-r-----  1 bob ops  1234 Jan 29 18:43 report.txt
```
