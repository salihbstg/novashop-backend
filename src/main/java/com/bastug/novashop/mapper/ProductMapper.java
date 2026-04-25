package com.bastug.novashop.mapper;

import com.bastug.novashop.dto.ProductResponse;
import com.bastug.novashop.dto.ProductSaveRequest;
import com.bastug.novashop.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target="id",ignore=true)
    @Mapping(target="createdAt",ignore=true)
    @Mapping(target="updatedAt",ignore=true)
    Product toEntity(ProductSaveRequest productSaveRequest);

    ProductResponse toProductResponse(Product product);
}
