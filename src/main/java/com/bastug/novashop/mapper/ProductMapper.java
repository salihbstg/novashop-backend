package com.bastug.novashop.mapper;

import com.bastug.novashop.dto.ProductResponse;
import com.bastug.novashop.dto.ProductSaveRequest;
import com.bastug.novashop.dto.ProductUpdateRequest;
import com.bastug.novashop.product.Product;
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
    Product updateProductFromRequest(ProductUpdateRequest productUpdateRequest, @MappingTarget Product product);
}
