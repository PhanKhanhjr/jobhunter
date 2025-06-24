package jobhunter.domain.response;

import jobhunter.util.constant.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
    private Instant createdAt;
    private companyUser company;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class companyUser {
        private Long id;
        private String name;
    }
}
