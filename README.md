## 프로젝트
### 아키텍처 - 레이어드 + 인터페이스 아키텍처
![img_5.png](img_5.png)
- 선택한 이유
    - 헥사고날 아키텍쳐 또는 클린 아키텍쳐를 적용하기 전, 레이어드 아키텍쳐+인터페이스 아키텍처 적용으로 인터페이스 사용에 적응하고 OCP 설계 원칙을 적용시켜 하위구조가 변경되어도 상위 계층에 영향을 미치지 않는 연습을 하기 위해
### 요구사항 분석
- **기능적** 요구사항
    - 잔액을 충전한다.
        - 잔액 충전은 1,000원 단위로만 가능하다.
        - 0원 또는 음수 금액을 충전할 수 없다.
        - 100,000원 까지만 충전이 가능하다.
    - 잔액을 조회한다.
        - id로 잔액을 조회 시 현재 남은 잔액을 보여준다.
        - id로 히스토리 조회 시 현재까지 사용한 기록을 보여준다.
    - 상품을 조회한다.
        - 상품 목록은 1페이지 당 10개씩 제공한다.
        - 상품 목록에서 상품이름/상품이미지를 제공한다.
        - 상품 클릭 시 상세페이지로 이동한다.
        - 상품 상세페이지에서 상품이름/상품이미지/상품설명/상품 주문 가능 수량을 제공한다.
    - 상품을 주문한다.
        - 상세페이지에서 수량을 정하고 주문 버튼을 누르면, 주문페이지로 이동한다.
        - 주문페이지에서는 선택한 상품이름/상품수량 정보를 제공한다.
    - 상품을 결제한다.
        - 사용자 id와 배송받는사람/배송주소 및 선택한 상품id/상품수량 정보를 받으면 주문가격을 계산하고 쿠폰 사용 여부를 확인한다.
            - id가 소지한 쿠폰 목록을 확인한다.
            - 쿠폰을 선택하여 주문 가격에 적용한다.
        - 최종 주문가격이 나오면 잔액을 비교하여 주문 가능한지 여부를 확인한다.
            - 주문이 가능할 경우 id의 주문내역에 추가되고, 해당 상품 수량이 감소하며, 잔액이 차감되고, 주문 정보를 외부 데이터 플랫폼에 전송한다.
            - 주문이 불가능할 경우 주문이 실패하며, “잔액이 부족합니다” 안내가 뜬다.
    - 선착순 쿠폰을 발급한다.
        - 1등은 30%/ 2등은 20%/ 3등은 10% 할인 쿠폰을 제공한다.
        - 언제 제공?
    - 인기 판매 상품을 조회한다.
        - 최근 3일간 가장 많이 팔린 상위 5개 상품 목록을 제공한다.
- **비기능적** 요구사항

### 시퀀스 다이어그램
- 포인트 충전 및 조회
```mermaid
sequenceDiagram
    PointController->>+PointService: 포인트 충전 요청
    alt (amount > 0)   
        PointService->>+PointRepository: 포인트 충전
        PointRepository-->PointRepository: 잔액 추가 및 히스토리 적재
        PointRepository-->>-PointService: 충전 성공 응답
        PointService-->>PointController: 충전 성공 응답
    else (amount <= 0)
        PointService-->>PointController: 포인트 충전 실패
    end
    PointController->>+PointService: 포인트 조회 요청
    PointService->>+PointRepository: 포인트 조회
    PointRepository-->>-PointService: 현재 잔액 응답
    PointService-->>+PointController: 현재 잔액 응답
```
![img.png](img.png)


- 쿠폰 발급 및 조회, 사용
```mermaid
sequenceDiagram
        CouponController->>CouponService: 쿠폰 발급 요청
        CouponService->>CouponService: 쿠폰 발급 심사
        alt 쿠폰 발급 가능
            CouponService->>CouponRepository: 발급 쿠폰 저장 요청
            CouponRepository-->>CouponRepository: 보유 쿠폰 리스트 적재
            CouponRepository-->>CouponController: 쿠폰 발급 성공 응답
        else 쿠폰 발급 불가능
            CouponService-->>CouponController: 쿠폰 발급 불가능 응답
        end
        CouponController->>CouponService: 보유 쿠폰 조회 요청
        CouponService->>CouponRepository: 보유 쿠폰 조회
        CouponRepository-->>CouponController: 보유 쿠폰 리스트 응답
```
![img_1.png](img_1.png)


- 상품 주문 및 결제
```mermaid
sequenceDiagram
  OrderController ->> OrderService: 상품 주문 요청
  OrderService ->> ProductService: 상품 재고 조회 요청
  ProductService ->> ProductService: 상품 재고 조회 요청
  alt 요청수량 <= 상품재고 : 주문 가능
    ProductService -->> OrderController: 상품 주문 가능 응답
    opt 
      OrderController ->> CouponService: 보유 쿠폰 목록 요청
      CouponService -->> OrderController: 보유 쿠폰 목록 응답
      OrderController -->> OrderController: 주문금액에 쿠폰 적용
    end
    OrderController ->> OrderService: 주문 결제 요청
    OrderService ->> PointService: 잔액 확인 요청
    PointService -->> PointService: 잔액 확인
    PointService -->> OrderService: 잔액 응답
    alt 주문금액 <= 잔액 : 주문 가능
      OrderService ->> ProductService: 상품 재고 차감 요청
      ProductService -->> ProductService: 상품 재고 차감
      ProductService -->> OrderService: 차감 완료 응답
      opt 
        OrderService ->> CouponService: 쿠폰 적용 요청
        CouponService -->> CouponService: 사용 쿠폰 삭제, 히스토리 적재
      end
      OrderService ->> PointService: 포인트 사용 요청
      PointService -->> PointService: 잔액 차감 및 히스토리 적재
      PointService -->> OrderService: 재고 차감 완료 응답
      OrderService ->> OrderRepository: 주문 내역 생성
      OrderRepository -->> OrderController: 주문 번호 반환
    else 주문금액 > 잔액: 주문 불가능
      OrderService -->> OrderController: 잔액 부족 에러
    end
  else 요청수량 > 상품재고 : 주문 불가능
    ProductService -->> OrderController: 재고 부족 응답
  end
```
![img_2.png](img_2.png)

### ERD

![img_3.png](img_3.png)

```sql
// 회원 쿠폰(User Coupon)
CREATE TABLE user_coupon (
coupon_id INT AUTO_INCREMENT PRIMARY KEY,
user_id VARCHAR(10) NOT NULL,
coupon_type CHAR(1) NOT NULL,
is_used BOOLEAN NOT NULL DEFAULT FALSE,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL,
key idx_user_id(user_id) 
);

// 쿠폰(Coupon)
CREATE TABLE coupon (
coupon_type CHAR(1) PRIMARY KEY,
coupon_discount_percent TINYINT NOT NULL,
coupon_inventory INT NOT NULL
);

// 주문(User Order)
CREATE TABLE user_order (
order_id INT AUTO_INCREMENT PRIMARY KEY,
user_id VARCHAR(10) NOT NULL,
order_quantity INT NOT NULL,
original_payment_amount INT NOT NULL,
coupon_id INT NULL,
discount_payment_amount INT NOT NULL,
final_payment_amount INT NOT NULL,
created_at TIMESTAMP NOT NULL, // 주문 날짜로 검색한다면? date로 저장하는게 나은지..
updated_at TIMESTAMP NOT NULL,
key idx_user_id(user_id)
);

// 회원(User)
CREATE TABLE user (
user_id VARCHAR(10) PRIMARY KEY,
user_password VARCHAR(20) NOT NULL,
user_phone_number VARCHAR(13) NULL,
user_address VARCHAR NULL,
user_point INT NOT NULL DEFAULT 0,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL
);

// 포인트 히스토리(Point History)
CREATE TABLE point_history (
point_history_id INT AUTO_INCREMENT PRIMARY KEY,
user_id VARCHAR(10) NOT NULL,
point_history_type VARCHAR(6) NOT NULL, // 'CHARGE', 'USE'
order_id INT NULL,
amount INT NOT NULL,
created_at TIMESTAMP NOT NULL,
key idx_user_id(user_id)
);

// 주문 항목(Order Item)
CREATE TABLE order_item (
order_item_id INT AUTO_INCREMENT PRIMARY KEY,
order_id INT NOT NULL,
product_id VARCHAR(20) NOT NULL,
order_item_quantity INT NOT NULL,
order_item_payment INT NOT NULL,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL,
key idx_order_id(order_id)
);

// 상품(Product)
CREATE TABLE product (
product_id VARCHAR(20) PRIMARY KEY,
product_name VARCHAR(20) NOT NULL,
product_description TEXT NOT NULL,
product_inventory INT NOT NULL,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL
);
```

### Swgger-UI
![img_4.png](img_4.png)


## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```