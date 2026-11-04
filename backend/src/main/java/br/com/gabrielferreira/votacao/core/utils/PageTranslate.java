package br.com.gabrielferreira.votacao.core.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageTranslate {

    private PageTranslate() {}

    public static Pageable toPageable(Pageable pageable, Map<String, String> fieldsMapping) {
        List<Sort.Order> sorts = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            Sort.Direction direction = order.getDirection();

            if (fieldsMapping.containsKey(property)) {
                String mappedProperty = fieldsMapping.get(property);
                Sort.Order newSort = new Sort.Order(direction, mappedProperty);
                sorts.add(newSort);
            }
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sorts));
    }

    public static Map<String, String> getAssociatePageableFieldsMapping() {
        HashMap<String, String> fieldsMapping = new HashMap<>();
        // DTO - Entity
        fieldsMapping.put("id", "idExternalUuid");
        fieldsMapping.put("name", "name");
        fieldsMapping.put("email", "email");
        fieldsMapping.put("documentNumber", "documentNumber");
        return fieldsMapping;
    }
}
