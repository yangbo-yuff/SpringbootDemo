package ${packageName};

<#list importPackages as pkg>
import ${pkg};
</#list>

@Data
public class ${entity} {
<#list fields as field>

    private ${field};
</#list>

}