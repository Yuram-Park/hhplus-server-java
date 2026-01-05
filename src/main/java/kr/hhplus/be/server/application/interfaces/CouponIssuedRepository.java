package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.dto.CouponRequestDto;

public interface CouponIssuedRepository {

    void save(CouponRequestDto requestDto);

    boolean isAlreadyIssued(CouponRequestDto requestDto);
}
