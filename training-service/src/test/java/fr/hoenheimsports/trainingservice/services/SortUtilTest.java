package fr.hoenheimsports.trainingservice.services;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SortUtilTest {

    @Test
    void createSort_ValidSortParams_ReturnsSort() {
        List<String> sortParams = List.of("name,asc", "address.city,desc");
        Sort sort = new SortUtil().createSort(sortParams);

        assertThat(sort).isNotNull();
        assertThat(Objects.requireNonNull(sort.getOrderFor("name")).getDirection()).isEqualTo(Sort.Direction.ASC);
        assertThat(Objects.requireNonNull(sort.getOrderFor("address.city")).getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void createSort_EmptySortParams_ReturnsUnsorted() {
        List<String> sortParams = List.of();
        Sort sort = new SortUtil().createSort(sortParams);

        assertThat(sort).isNotNull();
        assertThat(sort.isUnsorted()).isTrue();
    }

    @Test
    void createSort_NullSortParams_ReturnsUnsorted() {
        Sort sort = new SortUtil().createSort(null);

        assertThat(sort).isNotNull();
        assertThat(sort.isUnsorted()).isTrue();
    }

    @Test
    void createSort_InvalidSortParams_ThrowsException() {
        List<String> sortParams = List.of("invalid,number,elements");

        assertThatThrownBy(() -> new SortUtil().createSort(sortParams))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSortParams_ValidSort_ReturnsSortParams() {
        Sort sort = Sort.by(Sort.Order.asc("name"), Sort.Order.desc("address.city"));
        List<String> sortParams = new SortUtil().createSortParams(sort);

        assertThat(sortParams).containsExactly("name,asc", "address.city,desc");
    }

    @Test
    void createSortParams_Unsorted_ReturnsEmptyList() {
        Sort sort = Sort.unsorted();
        List<String> sortParams = new SortUtil().createSortParams(sort);

        assertThat(sortParams).isEmpty();
    }
}