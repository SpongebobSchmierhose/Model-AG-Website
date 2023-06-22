package de.api.backend.ui;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PushTokenDto {
    @NotNull
    @NotEmpty
    private String pushToken;
}
