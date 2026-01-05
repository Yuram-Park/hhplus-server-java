package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.dto.CouponRequestDto;

import java.util.List;

public interface CouponIssuedRepository {

    void save(CouponRequestDto requestDto);

    boolean isAlreadyIssued(CouponRequestDto requestDto);

    List<CouponRequestDto> findWaitingList(int size);
}
