# 리눅스 파일 시스템

### 파일 시스템

물리적으로 흩어져 있는 저장 장치의 블록 단위의 데이터들을 inode, 디렉터리 엔트리 등의 메타데이터로 관리하여 파일·디렉터리 계층을 구성하고,
<br> 이를 사용자가 `/` 로 시작하는 가상 경로를 통해 접근할 수 있도록 하는 논리적 체계.

### 장치 종류

| 구분                | 예             | 파일 시스템 생성 가능 여부 |
| ------------------- | -------------- | -------------------------- |
| **블록 디바이스**   | HDD, SSD, USB  | ✅                         |
| **캐릭터 디바이스** | 마우스, 키보드 | ❌                         |

### 명령어

#### 1. `lsblk` (list block devices)

현재 연결된 블록 디바이스 표시

```bash
lsblk

# 실행 결과
NAME   MAJ:MIN RM   SIZE RO TYPE MOUNTPOINT
sda      8:0    0   100G  0 disk
├─sda1   8:1    0   512M  0 part /boot
└─sda2   8:2    0  99.5G  0 part /
sdb      8:16   0    50G  0 disk
```

현재 연결된 모든 장치를 보고 싶은 경우,

```bash
ls /dev

# 실행 결과
null   zero   tty   sda   sda1   sda2   sdb   input   mouse0
```

sda, sda1, sdb → 저장 장치<br>
mouse0, tty, null → 마우스, 키보드 등과 같은 캐릭터 디바이스

#### 2. `fdisk` (fixed disk)

특정 블록 디바이스에 파티션 생성

```bash
fdisk /dev/sdb

Command (m for help): n
Partition type: p
Partition number: 1
First sector: (Enter)
Last sector: +20G

Command (m for help): w

lsblk

# 실헹 결과
NAME   MAJ:MIN RM   SIZE RO TYPE MOUNTPOINT
sda      8:0    0   100G  0 disk
├─sda1   8:1    0   512M  0 part /boot
└─sda2   8:2    0  99.5G  0 part /
sdb      8:16   0    50G  0 disk
└─sdb1   8:17   0    20G  0 part
```

디스크 전체에 바로 파일 시스템을 생성하고자 하는 경우,

```bash
mkfs.ext4 /dev/sdb
```

#### 3. `mkfs` (make file system)

파일 시스템 생성

```bash
mkfs.ext4 /dev/sdb1

Creating filesystem with 5242880 4k blocks
Filesystem UUID: 3f2c9d2b-...

lsblk -f
```

\* `-f` : 블록 디바이스 중에서 파일 시스템이 생성되어 있는 대상만 출력

```bash
# 실행 결과
NAME   FSTYPE LABEL UUID                                 MOUNTPOINT
sda1   ext4         a1b2c3d4-e001-4f5a-9abc-1234567890ab /boot
sda2   ext4         b2c3d4e5-e002-4f5a-9abc-1234567890ab /
sdb1   ext4         3f2c9d2b-1a2b-4c5d-8e9f-abcdef123456
```

#### 4. `mount`

```bash
mkdir /mnt/data             # 디렉터리 생성(make directory)

mount /dev/sdb1 /mnt/data   # 마운트

lsblk

# 실행 결과
NAME   MAJ:MIN RM   SIZE RO TYPE MOUNTPOINT
sda      8:0    0   100G  0 disk
├─sda1   8:1    0   512M  0 part /boot
└─sda2   8:2    0  99.5G  0 part /
sdb      8:16   0    50G  0 disk
└─sdb1   8:17   0    20G  0 part /mnt/data
```

\* 마운트:<br>
`/dev`는 현재 시스템이 인식한 장치들을 파일 형태로 나타낸 목록이며, 컴퓨터는 이 장치 파일을 통해 실제 하드웨어에 접근한다.<br>
마운트는 이러한 장치 위에 존재하는 파일 시스템의 내용을 사용자가 디렉터리 경로를 통해 확인할 수 있도록 연결하는 과정이다.
