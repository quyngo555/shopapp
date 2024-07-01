package com.project.shopapp.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopapp.dto.response.ProductListResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductRedisService implements IProductRedisService {
    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper redisObjectMapper;

    @NonFinal
    @Value("${spring.data.redis.use-redis-cache}")
    boolean useRedisCache;

    String getKeyFrom(String keyword,
                      Long categoryId,
                      PageRequest pageRequest) {
        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = sort.getOrderFor("id")
                .getDirection() == Sort.Direction.ASC ? "asc": "desc";
        String key = String.format("all_products:%s:%d:%d:%d:%s",
                keyword, categoryId, pageNumber, pageSize, sortDirection);
        return key;
    }

    @Override
    public ProductListResponse getAllProducts(
            String keyword,
            Long categoryId,
            PageRequest pageRequest
    ) throws JsonProcessingException {

        if(useRedisCache == false) {
            return null;
        }
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        ProductListResponse productResponses =
                json != null ?
                        redisObjectMapper.readValue(json, new TypeReference<ProductListResponse>() {})
                        : null;
        return productResponses;
    }

    @Override
    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
//        redisTemplate.getConnectionFactory().
    }

    @Override
    public void saveAllProducts(
            ProductListResponse productListResponse,
            String keyword,
            Long categoryId,
            PageRequest pageRequest
    ) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categoryId, pageRequest);
        String json = redisObjectMapper.writeValueAsString(productListResponse);
        redisTemplate.opsForValue().set(key, json);
    }
}
