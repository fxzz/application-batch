# application-batch

## 📒 목표
application 서비스의 대용량 데이터 처리와 자동화를 담당합니다.

## Job 
- communityLikesUpdateJob
  
좋아요 테이블에서 데이터를 읽어 커뮤니티 게시판의 좋아요 컬럼에 업데이트 합니다. 중복 업데이트를 방지 하기 위해 좋아요 테이블에 상태를 같이 업데이트 합니다. 

## ⚙️ 개발 환경 및 기술 세부 스택
- Java 11
- Spring Boot 2.7.11
- MyBatis
- Spring Batch
- PostgreSQL
