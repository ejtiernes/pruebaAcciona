package com.prueba.acciona.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.acciona.model.TweetEntity;

@Repository
public interface TwitterRepository extends JpaRepository<TweetEntity, Long> {

}
