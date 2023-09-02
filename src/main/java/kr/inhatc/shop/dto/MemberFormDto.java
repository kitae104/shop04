package kr.inhatc.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberFormDto {
    private String name;
    private String email;
    private String password;
    private String address;
}
