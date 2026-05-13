package com.turkcell.spring_cqrs.application.features.category.query.getall;

import org.springframework.data.domain.Page;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Query;

public record GetAllCategoriesQuery(int pageNumber, int pageSize) implements Query<Page<GetAllCategoriesResponse>> {}

// JWT loginden alınır
// JWTsiz bir şekilde category Get isteği hata vermeli (RuntimeExeption) - AuthorizationBehavior'da kontrol edilecek
// JWT "Authorization" Baerer {jwt} eklenirse sonuç gelmeli...

// 1- JWT yapısına ve UserContext'e rolleri de ekleyelim (JwtAuthFilter) - Her request dilerse rol gerektirebilir. -> Role listesi doldurulursa roller de kontrol edilmeli.
// 2- Auth hataları kendine has exceptionlar fırlatmalı. (AuthenticatedException, UnauthorizedException, AuthenticatedException) - 401, 403 ayrımı yapılmalı. (AuthorizationBehavior)
// 3- Custom exceptionlar custom handle edilip 401-403 olarak döndürülmeli. (GlobalExceptionHandler)