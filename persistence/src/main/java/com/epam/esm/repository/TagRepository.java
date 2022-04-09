package com.epam.esm.repository;

import com.epam.esm.repository.model.Tag;

/*
* A specification how to interact with datasource which contains tags
* @author Flexus
* */
public interface TagRepository extends GenericRepository<Tag>,Identifiable<Tag>,Nameable<Tag>{
}
