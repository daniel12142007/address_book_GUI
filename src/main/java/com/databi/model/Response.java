package com.databi.model;

import com.databi.model.enums.Status;

import java.util.List;

public record Response(
        Status status,
        List<Contact> contacts
) {
}