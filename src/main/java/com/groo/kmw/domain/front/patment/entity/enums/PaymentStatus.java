package com.groo.kmw.domain.front.patment.entity.enums;

public enum PaymentStatus {

    READY("결제준비"), //결제 요청 전 초기 상태

    IN_PROGRESS("결제중"), // 토스페이먼츠 결제창 호출 후 결제 진행 중 상태

    DONE("결제완료"), // 결제 완료 상태

    CANCELED("결제취소"), //결제 취소된 상태

    FAILED("결제실패"); //결제 실패 잔액 부족 한도 초과 등

    private final String description; // final로 상수 설정


    /**
     * enum 생성자
     * - enum 생성자는 항상 private (명시적으로 작성하지 않아도 됨)
     * - 각 enum 상수 생성 시에만 호출됨
     * @param description 상태 설명
     * 이해가 가지않음 따로 공부해야함
     */

    PaymentStatus(String description){
        this.description = description;
    }

    /**
     * 상태 설명 조회
     * - setter는 제공하지 않음 (enum은 불변이어야 함)
     * @return 상태에 대한 설명
     * 이부분도 따로 공부해야함
     */
    public String getDescription(){
        return this.description;
    }
}
