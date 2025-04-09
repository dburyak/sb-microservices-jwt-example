package com.dburyak.example.jwt.tenant.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.tenant.PathParams;
import com.dburyak.example.jwt.api.tenant.Tenant;
import com.dburyak.example.jwt.tenant.service.TenantService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
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

import static com.dburyak.example.jwt.api.tenant.Paths.TENANT;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS_ROOT;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANT_EXISTS;

@RestController
@RequestMapping(TENANTS_ROOT)
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    @JsonView(READ.class)
    public ResponseEntity<Tenant> create(@Validated(CREATE.class) @RequestBody Tenant req) {
        var tenant = tenantService.create(req);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping(TENANT)
    @JsonView(READ.class)
    public ResponseEntity<Tenant> get(@NotBlank @PathVariable(name = PathParams.TENANT_ID) UUID tenantId) {
        var tenant = tenantService.get(tenantId);
        return tenant != null ? ResponseEntity.ok(tenant) : ResponseEntity.notFound().build();
    }

    @DeleteMapping(TENANT)
    public ResponseEntity<Void> delete(@NotBlank @PathVariable(name = PathParams.TENANT_ID) UUID tenantId) {
        tenantService.delete(tenantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(TENANT + TENANT_EXISTS)
    public ResponseEntity<Void> exists(@NotBlank @PathVariable(name = PathParams.TENANT_ID) UUID tenantId) {
        tenantService.verifyExists(tenantId);
        return ResponseEntity.ok().build();
    }
}
