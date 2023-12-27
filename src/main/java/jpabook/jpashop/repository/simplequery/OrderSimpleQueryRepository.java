package jpabook.jpashop.repository.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    /**
     * new를 통해 jpql의 결과를 dto로 즉시 변환
     * 재사용성은 떨어진다. api 스펙에 맞춘 코드가 직접적으로 repository 내부에 존재하기 때문
     * 통계용 api, 복잡한 join을 통해 별도로 dto로 뽑아야 하는 경우가 많음 -> 이럴 때 별도의 Repository를 만들어서 처리
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();

    }
}


