# 한국 영화 검색 앱 🎬

Android Retrofit을 활용한 KOBIS(한국영화진흥위원회) 오픈API + TMDB API 연동 영화 검색 앱

## 🌟 주요 기능

- **일일 박스오피스 조회** - 어제 기준 박스오피스 순위
- **주간 박스오피스 조회** - 지난주 기준 박스오피스 순위  
- **영화 검색** - 영화 제목으로 검색
- **영화 상세정보** - 영화 코드로 상세 정보 조회
- **포스터 이미지** - TMDB API를 통한 영화 포스터 표시

## 🛠️ 기술 스택

- **언어**: Kotlin
- **UI**: Jetpack Compose
- **아키텍처**: MVVM + Repository Pattern
- **의존성 주입**: Hilt
- **네트워킹**: Retrofit2, OkHttp3, Moshi
- **비동기 처리**: Coroutines, Flow
- **이미지 로딩**: Coil

## 🚀 빠른 시작

### 1. 프로젝트 클론
```bash
git clone https://github.com/your-username/movie-search.git
cd movie-search
```

### 2. API 키 설정
프로젝트 루트의 `local.properties` 파일

## 📱 스크린샷

[앱 스크린샷 추가 예정]

## 🏗️ 프로젝트 구조

```
app/src/main/java/com/jg/moviesearch/
├── core/
│   ├── data/
│   │   ├── di/          # Hilt 모듈
│   │   ├── network/     # API 인터페이스
│   │   └── repository/  # Repository 구현
│   ├── domain/
│   │   └── repository/  # Repository 인터페이스
│   └── model/           # 데이터 모델
├── ui/
│   ├── screen/          # Compose 화면
│   ├── theme/           # 테마 설정
│   └── viewmodel/       # ViewModel
├── MainActivity.kt
└── MovieSearchApplication.kt
```

## 🔧 주요 컴포넌트

### API 연동
- **KobisApi**: 한국영화진흥위원회 오픈API 인터페이스
- **TmdbApi**: The Movie Database API 인터페이스 (포스터 이미지)
- **MovieRepository**: 데이터 소스 추상화

### UI 구성
- **SearchMovieScreen**: 메인 검색 화면
- **MovieCard**: 영화 정보 카드 컴포넌트
- **MovieViewModel**: UI 상태 관리

### 데이터 모델
- **Movie**: KOBIS API 영화 정보
- **MovieWithPoster**: 포스터 URL이 포함된 영화 정보
- **MovieDetail**: 상세 영화 정보

## 🔐 보안

### API 키 관리
- ✅ **local.properties 사용** - Git에 포함되지 않음
- ✅ **BuildConfig 활용** - 컴파일 시에만 생성
- ✅ **소스코드 노출 방지** - 하드코딩 없음

### 파일 보안
```gitignore
# API 키 보안
local.properties
ApiKeys.kt
```

## 📊 API 정보

### KOBIS API
- **제공자**: 한국영화진흥위원회
- **기능**: 박스오피스, 영화 상세정보
- **제한**: 일일 호출 제한 있음
- **무료**: ✅

### TMDB API  
- **제공자**: The Movie Database
- **기능**: 영화 포스터, 배경 이미지
- **제한**: 시간당 40회 요청
- **무료**: ✅

## 🔄 데이터 플로우

```
UI (Compose) 
    ↕️
ViewModel 
    ↕️
Repository 
    ↕️
API Service (Retrofit)
    ↕️
External APIs (KOBIS + TMDB)
```

## 🧪 테스트

```bash
# 단위 테스트 실행
./gradlew test

# UI 테스트 실행  
./gradlew connectedAndroidTest
```

## 📝 사용 예시

### 일일 박스오피스 조회
```kotlin
// 어제 날짜 박스오피스
viewModel.getDailyBoxOffice()

// 특정 날짜 박스오피스
viewModel.getDailyBoxOffice("20240101")
```

### 영화 검색
```kotlin
viewModel.searchMovies("기생충")
```

### 상세 정보 조회
```kotlin
viewModel.getMovieDetail("20124079")
```

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 있습니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참고하세요.

## 📞 문의

문제가 발생하거나 질문이 있으시면 [Issues](https://github.com/your-username/movie-search/issues)에서 알려주세요.

## 🙏 감사의 말

- [한국영화진흥위원회](https://www.kobis.or.kr/) - KOBIS 오픈API 제공
- [The Movie Database](https://www.themoviedb.org/) - 영화 포스터 API 제공
- [Google](https://developer.android.com/) - Android 개발 도구 제공

---

**⭐ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!**
