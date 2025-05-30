package com.example.SpringGateway.global.base;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public class UUIDv7Generator {
    public static UUID generate() {
        // UUIDv7 직접 생성은 아직 JDK 공식 지원 없음 → 외부 라이브러리 사용 필요
        return UuidCreator.getTimeOrderedEpoch(); // UUIDv7
    }
}