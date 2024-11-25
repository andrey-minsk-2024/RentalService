package com.videorental;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

import com.videorental.initializer.Initializer;
import com.videorental.model.Film;
import com.videorental.service.RentalService;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class VideorentalApplicationTests {

    @Autowired
    private RentalService rentalService;

//    @MockBean
//    private Initializer initializer;
//
//    private static Film cart;
//
//    @BeforeEach
//    void setup() {
//        doReturn(new UserWithdraw().setWithdrawalRate(5)).when(userWithdrawRepository)
//                .getWithdrawalByUserId(eq(123));
//        doReturn(new UserWithdraw().setWithdrawalRate(10)).when(userWithdrawRepository)
//                .getWithdrawalByUserId(eq(321));
//
//        final List<Goods> goods = List.of(new Goods()
//                .setId(12)
//                .setInitialPrice(BigDecimal.valueOf(101.00)));
//
//        cart = new Cart()
//                .setGoods(goods);
//    }
//
//
//    @Test
//    void testUserCartSuccess() {
//
//        final Cart cartWithWithdrawalUser1 = serviceA.getCartWithWithdrawal(cart, 123);
//        assertEquals(BigDecimal.valueOf(95.95),
//                cartWithWithdrawalUser1.getGoods().get(0).getFinalPrice());
//
//        final Cart cartWithWithdrawalUser2 = serviceA.getCartWithWithdrawal(cart, 321);
//        assertEquals(BigDecimal.valueOf(90.90).setScale(2),
//                cartWithWithdrawalUser2.getGoods().get(0).getFinalPrice());
//    }
//
//    @Test
//    void testUserCartSuccess3() {
//
//        final Cart cartWithWithdrawalUser1 = serviceA.getCartWithWithdrawal(cart, 123);
//        assertEquals(BigDecimal.valueOf(95.95),
//                cartWithWithdrawalUser1.getGoods().get(0).getFinalPrice());
//
//        final Cart cartWithWithdrawalUser2 = serviceA.getCartWithWithdrawal(cart, 321);
//        assertEquals(BigDecimal.valueOf(90.90),
//                cartWithWithdrawalUser2.getGoods().get(0).getFinalPrice());
//    }


}
