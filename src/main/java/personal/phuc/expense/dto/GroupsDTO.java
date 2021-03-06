package personal.phuc.expense.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GroupsDTO {

    private Integer id;

    private String name;

    private Integer userId;
}
