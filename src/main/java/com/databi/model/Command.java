package com.databi.model;

import com.databi.model.enums.CommandEnums;

public record Command(CommandEnums command,
                      Contact contact) {
}