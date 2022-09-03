package com.heylocal.traveler.controller;

import com.heylocal.traveler.controller.api.ArticlesApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Articles")
@RestController
public class ArticleController implements ArticlesApi {
}
