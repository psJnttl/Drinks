package base.repository;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.Repository;

import base.domain.Drink;

public interface DrinkQueryDslRepository extends Repository<Drink, Long>, QueryDslPredicateExecutor<Drink> {

}
