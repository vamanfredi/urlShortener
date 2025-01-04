package com.vamanfredi.URLShortenervnm.controller;

import com.vamanfredi.URLShortenervnm.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ShortenerController {
    private final RedisService redisService;

    public ShortenerController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/{shortUrl}")
    public ModelAndView redirect(@PathVariable String shortUrl) {
        if (shortUrl.equals("index.html")) {
            return new ModelAndView("index");
        }
        return new ModelAndView("redirect:"+redisService.getValue(shortUrl)) ;
    }


    @PostMapping("/api")
    public ResponseEntity<Map<String,String>> shorten(@PathParam("longUrl") String longUrl, HttpServletRequest request) {
        Map<String,String> map = new HashMap<>();

        String shortUrlPath= UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        redisService.setValue(shortUrlPath, longUrl);
        String scheme = request.getScheme(); // http o https
        String serverName = request.getServerName(); // dominio o IP
        int serverPort = request.getServerPort(); // puerto
        String contextPath = request.getContextPath(); // contexto de la aplicación

        // Construir la URL completa
        String domain = scheme + "://" + serverName + (serverPort != 80 && serverPort != 443 ? ":" + serverPort : "") + contextPath;

        map.put("longUrl", longUrl);
        map.put("shortUrl", domain + '/' + shortUrlPath);

        return ResponseEntity.ok(map);
    }

}
