package fr.esgi.musteat.backend.user.exposition.dto;

import fr.esgi.musteat.backend.location.exposition.dto.AddressCodingDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateUserDTO {

    @NotNull
    @NotBlank
    public String name;
    @NotNull
    @NotBlank
    public String password;
    @NotNull
    public AddressCodingDTO location;
}
