package com.gmp.hr.mapper;

import com.gmp.hr.dto.DepartmentDTO;
import com.gmp.hr.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 部门实体与DTO映射接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    /**
     * 将部门实体转换为DTO
     * 
     * @param department 部门实体
     * @return 部门DTO
     */
    @Mappings({
            @Mapping(source = "parent.id", target = "parentId"),
            @Mapping(source = "parent.departmentName", target = "parentName"),
            @Mapping(source = "manager.id", target = "managerId"),
            @Mapping(source = "manager.name", target = "managerName")
    })
    DepartmentDTO toDTO(Department department);

    /**
     * 将部门DTO转换为实体
     * 
     * @param dto 部门DTO
     * @return 部门实体
     */
    @Mappings({
            @Mapping(target = "parent", ignore = true),
            @Mapping(target = "manager", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "employees", ignore = true),
            @Mapping(target = "positions", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deleted", ignore = true)
    })
    Department toEntity(DepartmentDTO dto);

    /**
     * 将部门实体列表转换为DTO列表
     * 
     * @param departments 部门实体列表
     * @return 部门DTO列表
     */
    List<DepartmentDTO> toDTOList(List<Department> departments);

    /**
     * 更新部门实体
     * 
     * @param dto    部门DTO
     * @param entity 部门实体
     * @return 更新后的部门实体
     */
    @Mappings({
            @Mapping(source = "dto.id", target = "id"),
            @Mapping(source = "dto.departmentCode", target = "departmentCode"),
            @Mapping(source = "dto.departmentName", target = "departmentName"),
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(target = "parent", ignore = true),
            @Mapping(target = "manager", ignore = true),
            @Mapping(target = "children", ignore = true),
            @Mapping(target = "employees", ignore = true),
            @Mapping(target = "positions", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "deleted", ignore = true)
    })
    Department updateEntityFromDTO(DepartmentDTO dto, Department entity);
}