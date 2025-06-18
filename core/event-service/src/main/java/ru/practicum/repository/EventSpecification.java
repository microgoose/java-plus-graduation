package ru.practicum.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.event_service.dto.SearchEventsDto;
import ru.practicum.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<Event> withFilters(SearchEventsDto search) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search.getText() != null && !search.getText().isBlank()) {
                Predicate annotationMatch = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("annotation")),
                    "%" + search.getText().toLowerCase() + "%"
                );
                Predicate descriptionMatch = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%" + search.getText().toLowerCase() + "%"
                );

                predicates.add(criteriaBuilder.or(annotationMatch, descriptionMatch));
            }

            if (search.getCategories() != null && !search.getCategories().isEmpty()) {
                predicates.add(root.get("category").get("id").in(search.getCategories()));
            }

            if (search.getUsers() != null && !search.getUsers().isEmpty()) {
                predicates.add(root.get("initiator").in(search.getUsers()));
            }

            if (search.getStates() != null && !search.getStates().isEmpty()) {
                predicates.add(root.get("state").in(search.getStates()));
            }

            if (search.getPaid() != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), search.getPaid()));
            }

            if (search.getRangeStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), search.getRangeStart()));
            }

            if (search.getRangeEnd() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("eventDate"), search.getRangeEnd()));
            }

            if (search.getOnlyAvailable() != null && search.getOnlyAvailable()) {
                predicates.add(criteriaBuilder.equal(root.get("participantLimit"), 0));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
