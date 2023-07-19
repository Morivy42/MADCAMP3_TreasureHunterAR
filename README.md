# MADCAMP3_TreasureHunterAR
TreasureHunterAR

# A. 개발 팀원

김민재

모지훈

# B. 개발 환경

OS: Android(minSdk: 21, targetSdk: 34)

Language: Kotlin(for Jetpack Compose), Java

IDE: Android Studio

Server: Nodejs

Database: MySQL, Firebase

Target Device: Galaxy S7

# C. 앱 소개

## Tab 1. Location & Hints

<img width="194" alt="Tab1-2" src="https://github.com/Morivy42/MADCAMP3_TreasureHunterAR/assets/57134776/d02e63a3-24fd-4ef9-889f-08c20493f6e8">
<img width="194" alt="Tab1-2" src="https://github.com/Morivy42/MADCAMP3_TreasureHunterAR/assets/57134776/c559031d-bb97-4387-8004-fa189743410f">


Jetpack compose 내의 기능들을 활용해 유저들이 보물을 숨긴 위치와 힌트를 제공한다. 스와이프하여 전체 목록을 확인할 수 있다.

힌트의 사진을 탭하면 구글 맵 상의 해당 힌트의 장소로 이동한다.


## Tab 2. Map

<img width="194" alt="Tab1-2" src="https://github.com/Morivy42/MADCAMP3_TreasureHunterAR/assets/57134776/7eebf266-6124-41e0-8be9-8e993e5d6167">


 구글맵 api를 사용해 위치 권한을 받아오면 내 위치와 보물이 숨겨진 장소들의 마커를 확인할 수 있다. 마커를 탭하면 해당 마커의 힌트를 볼 수 있다.


## Tab 3. Collection

<img width="194" alt="Tab1-2" src="https://github.com/Morivy42/MADCAMP3_TreasureHunterAR/assets/57134776/250e6196-48f2-47d3-bc1a-223d71c9b813">


보물 획득 개수를 progress bar로 보여주며, 획득 가능한 전체 collection 목록을 확인할 수 있다. 

아직 얻지 못한 경우 어둡게 실루엣이 보이며, 이후 collection을 얻고 난 뒤엔 어둡던 실루엣이 모습을 보이게 된다.

## Tab 0. Treasure AR

<img width="194" alt="Tab1-2" src="https://github.com/Morivy42/MADCAMP3_TreasureHunterAR/assets/57134776/2aca4b4e-601f-4ee7-b0b0-d0b97733226f">
)
<img width="194" alt="Tab1-2" src="https://github.com/Morivy42/MADCAMP3_TreasureHunterAR/assets/57134776/088f9409-c0c4-4a32-a34c-0ef230dbaeae">


가운데 돋보기 버튼을 누르면 AR 화면으로 이동한다. 버튼은 좌우로 자유로운 이동이 가능하다.

AR 시작시 상단바에는 플레이어의 점수이 보이며 숨기기와 찾기 버튼을 통해 Treasure을 숨기거나 찾을 수 있다.

보물을 숨기고 싶은 경우 버튼을 누른 후 주위의 사물들을 인식해 object를 놓을 수 있는 평면이 나타나기를 기다린다. 푸른 점들이 나타난 후 격자가 생겨나면 평면이 나타난 것이다.

생성된 평면 위에 원하는 위치를 탭하면 보물 상자가 생성된다. 하단의 helper bar로 서버 연결 여부를 확인 가능하다.

보물을 찾고 싶은 경우 찾기 모드로 이동해 방 번호를 입력한다. 해당 방으로 이동한 뒤 보물을 숨긴 위치를 확인하면 보물을 찾을 수 있다.

보물을 탭하면 message box가 뜨고, 여기서 획득한 collection은 데이터베이스 상에 저장해 메인의 Collection 탭에서 확인이 가능하다.

