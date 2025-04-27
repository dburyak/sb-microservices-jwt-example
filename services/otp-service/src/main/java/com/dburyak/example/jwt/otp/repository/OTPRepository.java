package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OTPRepository extends MongoRepository<OTP, String>, OTPRepositoryCustom {
}
