# JpaShopExample
- JPA에 대해 학습한 후 인프런 강의를 통해 개발된 간단한 웹 프로젝트입니다.
- 최종 커밋 : 2023-12-22
- 변경 사항 : REST API 추가 예정


/**
* xToOne(ManyToOne, OneToOne에서의 성능 최적화 방법)
* DTO에서 직접 조회
* Entity를 DTO로 변환하거나 DTO상에서 바로 조회하는 것은 각각의 trade-off 존재
* 상황에 따라 더 나은 방법을 선택하는 것이 중요
*
* (1) 우선 Entity를 dto로 변환하는 방법 선택(유지보수성 증가) : v2
* (2) 필요하면 Fetch join으로 성능 최적화 : v3
* (3) 그래도 문제가 해결되지 않을 시 DTO로 직접 조회하는 방법 사용 : v4
* (4) 최후의 방법은 JPA가 제공하는 Native SQL이나 Spring JDBC Template을 사용해서 SQL을 직접 사용한다.
*/
