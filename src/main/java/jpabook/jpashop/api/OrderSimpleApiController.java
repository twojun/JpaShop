package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne에서의 성능 최적화 방법)
 * Order 조회
 * Order -> Member
 * Order -> Delivery
 * Order에서 Member, Delivery에 연관관계를 설정
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();  // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
            return all;
    }

    /**
     * Entity를 DTO로 변환
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Entity를 DTO로 변환, fetch join 최적화
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * DTO에서 직접 조회
     * Entity를 DTO로 변환하거나 DTO상에서 바로 조회하는 것은 각각의 trade-off 존재
     * 상황에 따라 더 나은 방법을 선택하는 것이 중요
     *
     * (1) 우선 entity를 dto로 변환하는 방법 선택(유지보수성 증가) : v2
     * (2) 필요하면 fetch join으로 성능 최적화 : v3
     * (3) 그래도 문제가 해결되지 않을 시 DTO로 직접 조회하는 방법 사용 : v4
     * (4) 최후의 방법은 jpa가 제공하는 native sql이나 spring jdbc template를 사용해서 sql를 직접 사용한다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }


}
