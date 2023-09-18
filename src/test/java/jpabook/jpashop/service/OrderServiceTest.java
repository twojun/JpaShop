package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // Given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문 시 상태는 ORDER");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        Assertions.assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고 감소");

    }

    @Test
    public void 주문취소() throws Exception {
        // Given (테스트를 위해 주어지는 값들)
        Member member = createMember();
        Book item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // When (실제 테스트하고 싶은 대상)
        orderService.cancelOrder(orderId);

        // Then (기대한 결과값)
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소 시 상태는 CANCLE이다.");
        Assertions.assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        int orderCount = 2000;

        // When
//        orderService.order(member.getId(), item.getId(), orderCount);

        // Then
        Assertions.assertThrows(NotEnoughStockException.class, () ->
                orderService.order(member.getId(), item.getId(), orderCount));
    }

    private Book createBook(String name, int price, int quantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

}