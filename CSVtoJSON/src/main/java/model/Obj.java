package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Obj {
    private String yamlId;
    private String parentYamlId;
    private String name;
    private Kind kind;
    private String technologyCode;
}
