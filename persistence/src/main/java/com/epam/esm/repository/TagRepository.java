package com.epam.esm.repository;

import com.epam.esm.repository.model.Tag;

import org.springframework.data.repository.PagingAndSortingRepository;

/*
* A specification how to interact with datasource which contains tags
* @author Flexus
* */
public interface TagRepository extends PagingAndSortingRepository<Tag,Long>,Nameable<Tag>,TagCustomRepository{
}
