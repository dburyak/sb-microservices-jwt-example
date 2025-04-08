package com.dburyak.example.jwt.tenant.service;

import com.dburyak.example.jwt.api.tenant.Tenant;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    public Tenant create(Tenant req) {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean exists(String tenantId) {
        throw new UnsupportedOperationException("not implemented");
    }
}
