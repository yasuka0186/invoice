package com.dev.k.invoice.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web設定クラス
 *
 * 【役割】
 * - CORS（Cross-Origin Resource Sharing）の設定を行う
 *
 * 【背景】
 * - フロントエンド（Vue: http://localhost:5173）と
 *   バックエンド（Spring Boot: http://localhost:8080）は
 *   異なるオリジンのため、そのままではAPI通信がブロックされる
 *
 * 【対応】
 * - /api/** のエンドポイントに対してCORSを許可する
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
     * CORS設定
     *
     * 【設定内容】
     * - 対象パス：/api/**（APIのみ許可）
     * - 許可オリジン：http://localhost:5173（Vue開発サーバ）
     * - 許可メソッド：すべて（GET, POST, PUT, DELETE など）
     * - 許可ヘッダー：すべて
     *
     * 【ポイント】
     * - フロントとバックのローカル開発時に必須
     * - 本番環境では allowedOrigins を制限するのが望ましい
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}