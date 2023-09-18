package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.")
    @NotBlank(message = "회원 이름은 공백일 수 없습니다.")
    private String name;

    @NotEmpty(message = "도시 이름은 필수입니다.")
    @NotBlank(message = "도시 입력은 공백일 수 없습니다.")
    private String city;

    @NotEmpty(message = "거리 입력은 필수입니다.")
    @NotBlank(message = "거리 입력은 공백일 수 없습니다.")
    private String street;

    @NotEmpty(message = "우편번호는 필수입니다.")
    @NotBlank(message = "우편번호는 공백일 수 없습니다.")
    private String zipcode;
}
