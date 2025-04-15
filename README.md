# 쇼핑노트

마트 영수증을 스캔하면 자동으로 쇼핑 데이터를 저장해주는 서비스

## 📌 기획 배경
> 저번에 샀던 게 뭐였더라? 얼마에 샀었지?  
> 이번에 사야 될 목록을 저번에 산 것들 중에 골라서 만들고 싶어.  
> 영수증을 사진 찍는 것만으로 자동으로 항목을 저장하면 좋을 텐데...

이러한 일상적인 고민에서 출발해,  
**장보기 목록을 쉽게 만들고, 이전 구매 이력을 확인할 수 있는 개인 장보기 도우미 서비스**를 만들었습니다.

## 🚀 주요 기능
- OCR을 통한 영수증 인식
- 항목 자동 분류 및 저장

## 🛠 기술 스택
- **Backend**: Spring Boot, Kotlin
- **Database**: MySQL, Redis
- **OCR**: AWS Textract (또는 Tesseract), NCloud OCR
- **Infra**: AWS ECS Fargate, S3, CloudFront

## 📸 스크린샷

## 🛣 개발 로드맵
- [ ] 영수증 조회 및 수동 입력
- [ ] 영수증 스캔 및 항목 저장
- [ ] 카테고리 자동 분류
- [ ] 모바일 앱 연동

## 🔧 설치 방법
```bash
git clone https://github.com/f-lab-edu/shopping-note.git
```

## 📮 API 명세
- [Swagger 문서 보기](http://localhost:8080/swagger-ui)
