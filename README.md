# nCloud_ImageSearch



![동작화면](./nCloud_ImageSearch.gif)


### 프로젝트 주제 설명

- ML Kit을 사용한 로컬 이미지 라벨링 및 검색 기능 구현
사진에 나온 물체를 인식해서 검색을 가능하게하면
사용자들에게 좋은 고객경험을 만들어 줄 수 있습니다.

    ML Kit의 Image Labeling, Landmark Detection기능을 사용해서 사진의 검색 기능을 강화해봅시다.

### 개발 요구사항(결과물 구현 시 필수 )

- Android 개발 경험
- 기본적인 UI 구현
- DB 모델링
- ML Kit 사용

### 개발 요구사항(결과물 구현 시 선택)

- MVVM
- AAC (ViewModel, WorkManager, Room 등등)

### Job Role
- Android 개발

### Technology(플랫폼)
- Android
- Git
- SQLite

### 개발언어
- Kotlin

### 기타 사항
- ML Kit 외에 다른 SDK도 자유롭게 사용 가능합니다.

### 역할
- ML Kit 전반적인 구현

### 주요 이슈 및 해결방안
증상 : 이미지를 라벨링 하는데 중간 부분부터 진행상황이 업데이트 되지 않는다.
~~~
해결 : 로그를 보니 Worker가 이미 일이 끝났는데 진행상황을 업데이트(setProgressAsync) 하려고 한다고 찍혔다
라벨링 과정의 addListener의 작업이 비동기로 이뤄져 라벨링 후 db 삽입을 해야하는데, 순서를 무조건 지켜야 하는데
어긋나는 모양이었다. Task.await를 이용해 라벨링 작업이 끝난 이후에 무조건 db 삽입하게 동기식으로 바꿔줬다.
~~~

증상 : 핸드폰을 킬 때마다 라벨링이 되지 않은 이미지만 라벨링 해야하는데, 구현 방법이 비효율적이다
~~~
기존에는 모든 사진을 체크해서 db에 uri이 있는지 체크했다.
해결 : SharedPreferences를 이용해서 라벨링을 할 때마다 가장 최근의 date를 담아둬서 앱 재시작시 그 date 이후의 것만
query의 대상이 되게 한다.
~~~

증상 : 실시간으로 사진이 라벨링 되고 있는데 앱을 종료하면 포그라운드 서비스에 의해 라벨링은 계속 되는데 
다시 들어가면 기존 라벨링 하는 것에 새로 다시 라벨링을 시작한다.
~~~
해결 : worker를 생성할 때 Tag를 붙여줬다. 나중에 그 Tag가 있는 Worker가 Running 중인지 체크해서 작업 실행 여부를 판단했다.
기존에 UUID를 이용해서 찾았는데 UUID를 이용해서 찾으면 현재 실행한 앱의 Worker의 id여서 방법의 의미가 없었다.
~~~

증상 : 실시간으로 사진이 라벨링 되고 있는데 앱을 종료하면 포그라운드 서비스에 의해 라벨링은 계속 되는데 
다시 들어가면 기존 라벨링 하는 것에 새로 다시 라벨링을 시작한다.
~~~
해결 : worker를 생성할 때 Tag를 붙여줬다. 나중에 그 Tag가 있는 Worker가 Running 중인지 체크해서 작업 실행 여부를 판단했다.
기존에 UUID를 이용해서 찾았는데 UUID를 이용해서 찾으면 현재 실행한 앱의 Worker의 id여서 방법의 의미가 없었다.
~~~

증상 : 라벨링 대상이 되는 사진이 한 장도 없을 때 notification이 계속 살아있다.
~~~
해결 : setForegroundAsunc는 이름대로 비동기로 동작한다.
주석을 보면 ListenableWorker가 종료되기 전에 setForegroundAsync가 완료되어야 된다는 건데, MLLabelWorker(해야 할 일의 구현)에서 새로운 사진이 없으면 setForegroundAsunc 호출 후 바로 종료될거라 'Worker 종료' -> 'setForegroundAsync 종료' 순서로 실행되면서 notification이 사라지지 않는 것으로 추측한다. 아마 계속 Foreground Service로 돌고 있는 듯 하다.
새로운 사진이 있을 때만 setForegroundAsync를 호출하여 해결하였다.
~~~
