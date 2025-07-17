# 🎬 Movie Search App

Android 영화 검색 애플리케이션으로, KOBIS(영화진흥위원회) API와 TMDB API를 활용하여 영화 정보를 검색하고 관리할 수 있습니다.

## ✨ 주요 기능

### 🔍 영화 검색
- **실시간 검색**: 입력과 동시에 영화를 검색
- **무한 스크롤**: 끊김 없는 스크롤로 더 많은 영화 탐색
- **다양한 UI 패턴**: 포스터형 카드와 텍스트형 카드가 3개씩 번갈아 표시

### 📱 상세 화면
- **스와이프 네비게이션**: ViewPager2를 이용한 좌우 스와이프로 영화 간 이동
- **상세 정보**: 감독, 출연진, 장르, 개봉일 등 풍부한 영화 정보
- **포스터 이미지**: TMDB API를 통한 고품질 영화 포스터

### ⭐ 즐겨찾기
- **즐겨찾기 추가/제거**: 원터치로 간편한 즐겨찾기 관리
- **로컬 저장**: Room Database를 이용한 오프라인 즐겨찾기 관리
- **실시간 동기화**: 즐겨찾기 상태 실시간 업데이트

## 🛠 기술 스택

### **언어 & 프레임워크**
- **Kotlin** - 주 개발 언어
- **Android Jetpack Compose** - UI 개발
- **Fragment** - 일부 화면 구현

### **아키텍처 & 디자인 패턴**
- **Clean Architecture** - 계층화된 구조
- **MVVM Pattern** - ViewModel을 통한 데이터 바인딩
- **Repository Pattern** - 데이터 소스 추상화
- **UseCase Pattern** - 비즈니스 로직 분리

### **의존성 주입**
- **Hilt** - Dagger 기반 의존성 주입

### **네트워킹**
- **Retrofit2** - REST API 통신
- **OkHttp3** - HTTP 클라이언트
- **Moshi** - JSON 파싱

### **로컬 데이터베이스**
- **Room** - 로컬 데이터베이스
- **SQLite** - 내부 데이터베이스 엔진

### **비동기 처리**
- **Coroutines** - 비동기 작업 처리
- **Flow** - 반응형 데이터 스트림

### **이미지 로딩**
- **Coil** - 이미지 로딩 및 캐싱

### **기타**
- **Navigation Component** - 화면 간 네비게이션
- **ViewPager2** - 스와이프 페이징
- **Material 3** - Google Material Design

## 🏗 아키텍처

```
📱 Presentation Layer (UI)
├── 🎨 Compose Screens
├── 📄 Fragments  
├── 🔄 ViewModels
└── 🎯 Navigation

💼 Domain Layer (Business Logic)
├── 🎪 Use Cases
├── 📋 Domain Models
└── 🔌 Repository Interfaces

💾 Data Layer
├── 🌐 Network (APIs)
│   ├── KOBIS API
│   └── TMDB API
├── 💿 Local Database (Room)
├── 📦 Repository Implementations
└── 🔄 Data Sources
```

## 🎯 주요 구현 사항

### 💡 스마트 UI 패턴
```kotlin
val isPosterType = (index % 6) < 3  // 3개 포스터 + 3개 텍스트 패턴
```

### 🔄 무한 스크롤
```kotlin
// 마지막 3개 아이템 전에 미리 로드
if (lastVisibleItemIndex >= uiState.movies.size - 3) {
    viewModel.loadMoreMovies()
}
```

### 📱 스와이프 네비게이션
- ViewPager2 기반 좌우 스와이프
- 각 페이지별 독립적인 ViewModel 관리

### 💾 즐겨찾기 시스템
- Room Database + Clean Architecture
- Repository 패턴으로 데이터 추상화
- UseCase 분리로 비즈니스 로직 관리

## 📁 프로젝트 구조

```
app/
├── src/main/java/com/jg/moviesearch/
│   ├── 🎨 ui/
│   │   ├── screen/          # Compose 화면들
│   │   ├── fragment/        # Fragment 화면들  
│   │   ├── viewmodel/       # ViewModel들
│   │   └── theme/          # UI 테마
│   ├── 💼 core/
│   │   ├── domain/         # 도메인 계층
│   │   │   ├── usecase/    # Use Cases
│   │   │   └── repository/ # Repository 인터페이스
│   │   ├── data/           # 데이터 계층
│   │   │   ├── network/    # API 관련
│   │   │   ├── local/      # 로컬 DB 관련
│   │   │   └── repository/ # Repository 구현
│   │   ├── database/       # Room 데이터베이스
│   │   └── model/          # 데이터 모델들
│   └── di/                 # Hilt 모듈들
└── src/main/res/           # 리소스 파일들
```

## 🎨 주요 화면

### 🔍 검색 화면
- 실시간 영화 검색
- 포스터형 + 텍스트형 카드 패턴
- 무한 스크롤 지원

### 📄 상세 화면  
- 좌우 스와이프로 영화 간 이동
- 상세 영화 정보 표시
- 즐겨찾기 토글 기능
