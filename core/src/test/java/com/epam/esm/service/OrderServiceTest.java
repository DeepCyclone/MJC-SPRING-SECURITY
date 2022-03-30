package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.repository.template.GiftCertificateRepository;
import com.epam.esm.repository.template.OrderRepository;
import com.epam.esm.repository.template.UserRepository;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

   @Mock
   private static OrderRepository orderRepository;
   @Mock
   private static GiftCertificateRepository giftCertificateRepository;
   @Mock
   private static UserRepository userRepository;

   private static OrderServiceImpl service = new OrderServiceImpl(orderRepository,giftCertificateRepository,userRepository);

   private static final Optional<Order> ORDER = Optional.of(Order.builder().id(1).build());
   private static final Optional<Order> NEW_ORDER = Optional.of(Order.builder().build());
   private static final Optional<Order> NON_EXISTING_ORDER = Optional.empty();
   private static final Optional<User> user = Optional.of(User.builder().id(1L).orders(new ArrayList<Order>(Arrays.asList(ORDER.get()))).build());
   private static final Optional<GiftCertificate> cert = Optional.of(GiftCertificate.builder().id(1L).price(new BigDecimal("9999")).build());

   private static final List<Order> ORDERS = Arrays.asList(new Order());


   @BeforeEach
   void init(){
       service = new OrderServiceImpl(orderRepository,giftCertificateRepository,userRepository);
   }

   @Test
   void getByIDExistingEntry(){
       Mockito.when(orderRepository.findByID(1L)).thenReturn(ORDER);
       Assertions.assertEquals(service.getById(1L),ORDER.get());

   }

   @Test
   void getByIDNonExistingEntry(){
       Mockito.when(orderRepository.findByID(9999L)).thenReturn(NON_EXISTING_ORDER);
       Assertions.assertThrows(ServiceException.class, ()->service.getById(9999L));

   }

   @Test
   void getAll(){
       Mockito.when(orderRepository.readAll(0,0)).thenReturn(ORDERS);
       Assertions.assertEquals(ORDERS,service.getAll(0,0));
   }

   @Test
   void deleteNonExistingEntry(){
       Mockito.when(orderRepository.deleteByID(25L)).thenReturn(false);
       Assertions.assertThrows(ServiceException.class,()->service.delete(25L));
   }

   @Test
   void deleteEntry(){
       Mockito.when(orderRepository.deleteByID(1L)).thenReturn(true);
       Assertions.assertDoesNotThrow(()->service.delete(1L));
   }

   @Test
   void addEntity(){
       Mockito.when(userRepository.findByID(1L)).thenReturn(user);
       Mockito.when(giftCertificateRepository.findByID(1L)).thenReturn(cert);
       Mockito.when(orderRepository.makeOrder(new BigDecimal("9999"))).thenReturn(ORDER);
       Order entity = service.makeOrder(Arrays.asList(1L),1L);
       Assertions.assertEquals(ORDER.get(),entity);
   }

   @Test
   void updateEntity(){
       Mockito.when(orderRepository.update(ORDER.get(),1L)).thenReturn(true);
       Mockito.when(orderRepository.findByID(Mockito.eq(1L))).thenReturn(ORDER);
       Assertions.assertEquals(ORDER.get(),service.update(ORDER.get(),1L));
   }

   @Test
   void updateNonExistingEntity(){
       Assertions.assertThrows(ServiceException.class,() -> service.update(ORDER.get(),1L));
   }
}

