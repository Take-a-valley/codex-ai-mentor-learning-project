package com.example.devsupport.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.example.devsupport.common.dto.response.ErrorResponse;

public class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("UT-EXCEPTION-001: 例外応答の変換")
    void testGlobalExceptionHandler() {
        ResourceNotFoundException ex = new ResourceNotFoundException("ユーザーが見つかりません。");

        // handleResourceNotFound()の呼び出し前後の時間を取得して、タイムスタンプが正しい範囲内であることを検証する
        OffsetDateTime beforeCall = OffsetDateTime.now();
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleResourceNotFound(ex);
        OffsetDateTime afterCall = OffsetDateTime.now();

        // ResponseEntityがnullでないこと、HTTPステータスコードが404であること、ErrorResponseの各フィールドが正しいことを検証する
        assertNotNull(responseEntity, "ResponseEntityがnullでないこと");
        assertEquals(404, responseEntity.getStatusCode().value(), "HTTPステータスコードが404であること");
        ErrorResponse errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse, "ErrorResponseがnullでないこと");
        assertEquals("RESOURCE_NOT_FOUND", errorResponse.code(), "エラーコードが正しいこと");
        assertEquals("ユーザーが見つかりません。", errorResponse.message(), "エラーメッセージが正しいこと");
        assertNotNull(errorResponse.details(), "エラー詳細がnullでないこと");
        assertEquals(0, errorResponse.details().size(), "エラー詳細が空であること");
        assertNotNull(errorResponse.timestamp(), "タイムスタンプがnullでないこと");
        // timestampがbeforeCallより前、またはafterCallより後ではないことを検証する
        assertFalse(errorResponse.timestamp().isBefore(beforeCall) || errorResponse.timestamp().isAfter(afterCall),
                "タイムスタンプが呼び出し時間内であること");
    }
}
