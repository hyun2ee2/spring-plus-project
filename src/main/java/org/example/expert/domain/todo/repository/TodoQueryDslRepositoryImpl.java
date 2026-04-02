package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TodoQueryDslRepositoryImpl implements TodoQueryDslRepository{
    private final JPAQueryFactory queryFactory;

    public TodoQueryDslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(QTodo.todo)
                        .leftJoin(QTodo.todo.user).fetchJoin()
                        .where(QTodo.todo.id.eq(todoId))
                        .fetchOne()
        );
    }
}
