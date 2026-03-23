package com.school.usercrude.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;
import lombok.Data;
import java.util.List;
import com.school.usercrude.dto.request.PostRequestDTO;

@Data
public class UserRequestDTO {
@NotBlank(message = "Name is required")
private String name;
@NotBlank(message = "Email is required")
@Email(message = "Email should be valid")
@UniqueElements(message = "Email must be unique")
private String email;
@NotBlank(message = "Password is required")
@Size(min = 6, message = "Password must be at least 6 characters")
private String password;

private List<PostRequestDTO> posts;



}
