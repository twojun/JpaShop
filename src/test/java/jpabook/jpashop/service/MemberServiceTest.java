package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    public void assignMember() throws Exception {
        // Given
        Member member = new Member();
        member.setName("wonjun");

        // When
        Long savedId = memberService.join(member);

        // Then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void duplicateMemberException() throws Exception {
        // Given
        Member member1 = new Member();
        member1.setName("wonjun");

        Member member2 = new Member();
        member2.setName("wonjun");

        // When
        memberService.join(member1);

        // Then
        Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }

}