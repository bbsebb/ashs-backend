package fr.hoenheimsports.trainingservice.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class SortUtil {

    public Sort createSort(List<String> sortParams) {
        Sort sort = Sort.unsorted();

        if (sortParams != null && !sortParams.isEmpty()) {
            List<Sort.Order> orders = sortParams.stream()
                    .map(this::createOrder)
                    .toList();
            sort = Sort.by(orders);
        }

        return sort;
    }


    private Sort.Order createOrder(String sortParam) {
        List<String> sortParamList = Pattern.compile(",").splitAsStream(sortParam).toList();
        return switch (sortParamList.size()) {
            case 1 -> new Sort.Order(Sort.Direction.ASC, sortParamList.get(0));
            case 2 -> new Sort.Order(Sort.Direction.fromString(sortParamList.get(1)), sortParamList.get(0));
            default -> throw new IllegalArgumentException("Invalid sort parameter: " + sortParam);
        };
    }

    public List<String> createSortParams(Sort sort) {
        List<String> sortParams = new ArrayList<>();

        sort.forEach(order -> {
            String sortParam = order.getProperty() + "," + order.getDirection().name().toLowerCase();
            sortParams.add(sortParam);
        });

        return sortParams;
    }
}