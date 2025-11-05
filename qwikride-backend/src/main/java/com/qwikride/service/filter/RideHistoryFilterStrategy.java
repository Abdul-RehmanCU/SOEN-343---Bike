package com.qwikride.service.filter;

import com.qwikride.model.RideHistory;
import org.springframework.data.jpa.domain.Specification;

/**
 * Strategy interface for filtering ride history queries.
 * Implements Strategy Pattern for flexible filtering options.
 */
public interface RideHistoryFilterStrategy {
    /**
     * Creates a JPA Specification for filtering ride history.
     * @param filter The filter criteria
     * @return JPA Specification for the filter
     */
    Specification<RideHistory> buildSpecification(RideHistoryFilterCriteria filter);
}

