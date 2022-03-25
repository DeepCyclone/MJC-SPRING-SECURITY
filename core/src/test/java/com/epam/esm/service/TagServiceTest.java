package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.template.TagRepository;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

//    @Mock
//    private static TagRepository tagRepository;

//    private static TagServiceImpl service;

//    private static final List<Tag> tags = Arrays.asList();
//    private static final Optional<Tag> tag = Optional.of(Tag.builder().build());
//    private static final Optional<Tag> tagID = Optional.of(Tag.builder().id(1L).build());

//    @BeforeEach
//    void init(){
//        service = new TagServiceImpl(tagRepository);
//    }

//    @Test
//    void getAll(){
//        Mockito.when(tagRepository.readAll()).thenReturn(tags);
//        Assertions.assertEquals(tags,tagRepository.readAll());
//    }

//    @Test
//    void getByIDExisting(){
//        Mockito.when(tagRepository.getByID(Mockito.anyLong())).thenReturn(tag);
//        Assertions.assertEquals(tag.get(),service.getByID(1L));
//    }

//    @Test
//    void addEntity(){
//        Mockito.when(tagRepository.create(tag.get())).thenReturn(tagID.get());
//        Assertions.assertEquals(tagID.get(),service.addEntity(tag.get()));
//    }

//    @Test
//    void deleteExistingEntity(){
//        Mockito.when(tagRepository.deleteByID(1L)).thenReturn(true);
//        Assertions.assertDoesNotThrow(()->service.deleteByID(1L));
//    }

//    @Test
//    void deleteNonExistingEntity(){
//        Mockito.when(tagRepository.deleteByID(1L)).thenReturn(false);
//        Assertions.assertThrows(ServiceException.class,()->service.deleteByID(1L));
//    }


}
