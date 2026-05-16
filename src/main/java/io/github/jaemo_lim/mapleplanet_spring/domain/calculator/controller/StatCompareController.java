package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.controller;

import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCalcRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCalcResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.StatCompareRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.StatCompareResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service.DamageCalcService;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service.StatCompareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/calculator")
@RequiredArgsConstructor
public class StatCompareController {

    private final StatCompareService statCompareService;
    private final DamageCalcService damageCalcService;

    @PostMapping("/stat-compare")
    public ResponseEntity<StatCompareResponse> compare(@RequestBody @Valid StatCompareRequest request) {
        return ResponseEntity.ok(statCompareService.compare(request));
    }

    @PostMapping("/damage")
    public ResponseEntity<DamageCalcResponse> damage(@RequestBody @Valid DamageCalcRequest request) {
        return ResponseEntity.ok(damageCalcService.calculate(request));
    }
}
