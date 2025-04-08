package com.dburyak.example.jwt.tenant.controller;

import com.dburyak.example.jwt.api.common.ApiView.CREATE;
import com.dburyak.example.jwt.api.tenant.Tenant;
import com.dburyak.example.jwt.tenant.service.TenantService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS_ROOT;
import static com.dburyak.example.jwt.api.tenant.Paths.TENANT_EXISTS;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_ID;

@RestController
@RequestMapping(TENANTS_ROOT)
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<Tenant> create(@Validated(CREATE.class) @RequestBody Tenant req) {
        var tenant = tenantService.create(req);
        return ResponseEntity.ok(tenant);
    }

    @GetMapping(TENANT_EXISTS)
    public ResponseEntity<Void> exists(@NotBlank @RequestAttribute(TENANT_ID) String tenantId) {
        var exists = tenantService.exists(tenantId);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
