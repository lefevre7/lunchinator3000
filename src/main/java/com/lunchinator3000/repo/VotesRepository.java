package com.lunchinator3000.repo;

import com.lunchinator3000.dto.vote.Votes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
public interface VotesRepository extends CrudRepository<Votes, Integer> {
}