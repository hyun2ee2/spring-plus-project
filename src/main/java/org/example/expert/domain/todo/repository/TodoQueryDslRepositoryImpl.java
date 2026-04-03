package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TodoQueryDslRepositoryImpl implements TodoQueryDslRepository {
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

    @Override
    public Page<TodoSearchResponse> searchTodos(
            String title,
            String nickName,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {
        List<TodoSearchResponse> results = queryFactory
                .select(new QTodoSearchResponse(
                        QTodo.todo.title,
                        QManager.manager.count(),
                        QComment.comment.count()
                ))
                .from(QTodo.todo)
                .leftJoin(QTodo.todo.managers, QManager.manager)
                .leftJoin(QTodo.todo.comments, QComment.comment)
                .where(buildSearchCondition(title, nickName, startDate, endDate))
                .groupBy(QTodo.todo.id)
                .orderBy(QTodo.todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    private BooleanBuilder buildSearchCondition(String title, String nickName, LocalDateTime startDate, LocalDateTime endDate) {
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null) builder.and(QTodo.todo.title.contains(title));
        if (nickName != null) builder.and(QTodo.todo.user.nickName.contains(nickName));
        if (startDate != null && endDate != null) builder.and(QTodo.todo.createdAt.between(startDate, endDate));

        return builder;
    }
}
