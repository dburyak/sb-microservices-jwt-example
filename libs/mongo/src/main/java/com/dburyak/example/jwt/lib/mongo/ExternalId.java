package com.dburyak.example.jwt.lib.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalId {
    // field names for queries
    public static final String FIELD_EMAIL = "email";

    private String email;

    // Potentially here we can have other external identifiers like phone numbers,
    // social media IDs, apple ID, google ID, etc. Security-wise it's also a good
    // idea to store direct PII identifiers (email, phone number) hashed. For
    // example, we better store not "email" as is, but rather a hashed value in
    // "emailHash". This will work well if these external identifiers are used
    // only for *identification*. If we need to talk back to the user, we should
    // store plain contact information separately only in a single place.
    // Also, external IDs most likely be spread across multiple services, which
    // increases ricks if we store them in plain form.
    // For simplicity’s sake, in this example app we will store them as is.
}
