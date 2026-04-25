package com.bastug.novashop.product.mapper;

import com.bastug.novashop.product.dto.ProductResponse;
import com.bastug.novashop.product.dto.ProductSaveRequest;
import com.bastug.novashop.product.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target="id",ignore=true)
    @Mapping(target="createdAt",ignore=true)
    @Mapping(target="updatedAt",ignore=true)
    Product toEntity(ProductSaveRequest productSaveRequest);

    ProductResponse toProductResponse(Product product);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy=NullValuePropertyMappingStrategy.IGNORE)
    Product updateProductFromRequest(ProductSaveRequest productUpdateRequest, @MappingTarget Product product);
}
