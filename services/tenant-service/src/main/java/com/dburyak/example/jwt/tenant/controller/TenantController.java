package com.dburyak.example.jwt.tenant.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.tenant.Tenant;
import com.dburyak.example.jwt.tenant.service.TenantService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.internal.tenant.Paths.TENANT_EXISTS;
import static com.dburyak.example.jwt.api.tenant.PathParams.TENANT_NAME;
import static com.dburyak.example.jwt.api.tenant.PathParams.TENANT_UUID;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANT_BY_NAME;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANT_BY_UUID;

@RestController
@RequestMapping(TENANTS)
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    @JsonView(READ.class)
    public ResponseEntity<Tenant> create(@Validated(CREATE.class) @JsonView(CREATE.class) @RequestBody Tenant req) {
        var tenant = tenantService.create(req);
        return ResponseEntity.ok(tenant);
    }

    @DeleteMapping(TENANT_BY_UUID)
    public ResponseEntity<Void> delete(@PathVariable(name = TENANT_UUID) @NotNull UUID tenantUuid) {
        tenantService.delete(tenantUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(TENANT_BY_UUID)
    @JsonView(READ.class)
    public ResponseEntity<Tenant> getByUuid(@PathVariable(name = TENANT_UUID) @NotNull UUID tenantUuid) {
        var tenant = tenantService.get(tenantUuid);
        return tenant != null ? ResponseEntity.ok(tenant) : ResponseEntity.notFound().build();
    }

    @GetMapping(TENANT_BY_NAME)
    @JsonView(READ.class)
    public ResponseEntity<Tenant> getByName(@PathVariable(name = TENANT_NAME) @NotBlank String tenantName) {
        var tenant = tenantService.get(tenantName);
        return tenant != null ? ResponseEntity.ok(tenant) : ResponseEntity.notFound().build();
    }

    @GetMapping(TENANT_BY_UUID + TENANT_EXISTS)
    public ResponseEntity<Void> existsByUuid(@PathVariable(name = TENANT_UUID) @NotNull UUID tenantUuid) {
        tenantService.verifyExistsByUuid(tenantUuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping(TENANT_BY_NAME + TENANT_EXISTS)
    public ResponseEntity<Void> existsByName(@PathVariable(name = TENANT_NAME) @NotBlank String tenantName) {
        tenantService.verifyExistsByName(tenantName);
        return ResponseEntity.ok().build();
    }
}
